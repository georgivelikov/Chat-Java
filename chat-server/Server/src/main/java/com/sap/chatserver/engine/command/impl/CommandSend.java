package com.sap.chatserver.engine.command.impl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatcommon.Conversation;
import com.sap.chatcommon.ConversationDefault;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.config.server.ConstantConfig;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageSend;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;
import com.sap.chatserver.log.LogService;

public class CommandSend implements Command {

	public static final int MIN_ARGS_COUNT = 3;
	public static final String NAME = "SEND";
	public static final String DESCRIPTION = "";
	public static final String SEND_TO_ALL = "*";
	public static final String MESSAGE_FORMAT = "[%s]%s -> %s: %s";
	public static final String MESSAGE_TO_ALL_FORMAT = "[%s]%s -> all: %s";
	public static final String STOP_ONE_ON_ONE = "BREAK";
	public static final String TIMESTAMP_FORMAT = "HH:mm:ss";
	private static Registry registry;

	private DbService dbService;

	private ConnectionService connectionService;

	private LogService logService;

	@Inject
	public CommandSend(@Assisted DbService dbService, ConnectionService connectionService, LogService logService) {
		this.dbService = dbService;
		this.connectionService = connectionService;
		this.logService = logService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs)
			throws ConnectionCommunicationException, DatabaseConnectionException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		String receiverUsername = commandArgs[1];
		String message = String.join(" ", Arrays.copyOfRange(commandArgs, 2, commandArgs.length));

		if (receiverUsername.equals(SEND_TO_ALL)) {
			sendToAll(connection, message);
		} else {
			sentToUser(connection, receiverUsername, message);
		}
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length < MIN_ARGS_COUNT) {
			connection.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		if (!connection.isLoggedIn()) {
			connection.writeMessage(MessageCommon.LOGIN_FIRST.getContent());
			return false;
		}

		return true;
	}

	private void sendToAll(Connection connection, String message) throws ConnectionCommunicationException {
		String fullMessage = String.format(MESSAGE_TO_ALL_FORMAT, getFormattedTimeStamp(),
				connection.getUser().getUsername(), message);
		Iterable<Connection> allLoggedInConnections = connectionService.getLoggedInConnections();

		for (Connection receiver : allLoggedInConnections) {
			receiver.writeMessage(fullMessage);
		}
	}

	private void sentToUser(Connection connection, String receiverUsername, String message)
			throws ConnectionCommunicationException, DatabaseConnectionException {
		User receiver = dbService.findUserByUsername(receiverUsername);
		if (receiver == null) {
			connection.writeMessage(MessageSend.RECEIVER_UNKNOWN.getContent());
			return;
		}

		Connection receiverConnection = connectionService.findConnectionByUsername(receiverUsername);

		try {
			handleOneToOneMessaging(connection, receiverConnection, message);
		} catch (AlreadyBoundException e) {
			connection.writeMessage(String.format(MessageSend.CONVERSATION_ALREADY_STARTED.getContent(), receiverUsername));
			logService.logError(e);
		} catch (RemoteException e) {
			connection.writeMessage(String.format(MessageSend.MESSAGE_FAILED.getContent(), receiverUsername));
			logService.logError(e);
		}
	}

	private String getFormattedTimeStamp() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
		LocalTime time = LocalTime.now();
		return time.format(formatter);
	}

	private void handleOneToOneMessaging(Connection connection, Connection receiverConnection, String initialMessage)
			throws RemoteException, ConnectionCommunicationException, AlreadyBoundException {
		if (registry == null) {
			initRegistry();
		}
		
		String senderUsername = connection.getUser().getUsername();
		String receiverUsername = receiverConnection.getUser().getUsername();
		String objectIdentifier = "--" + connection.getPort() + receiverConnection.getPort();
		
		exportConversationObject(senderUsername, receiverUsername, initialMessage, objectIdentifier);

		connection.writeMessage(objectIdentifier + " " + true + " " + ConstantConfig.REGISTRY_PORT);
		receiverConnection.writeMessage(objectIdentifier + " " + false + " " + ConstantConfig.REGISTRY_PORT);
	}

	private void initRegistry() throws RemoteException {
		registry = LocateRegistry.createRegistry(ConstantConfig.REGISTRY_PORT);
	}

	private void exportConversationObject(String senderUsername, String receiverUsername, String initialMessage,
			String objectIdentifier) throws RemoteException, AlreadyBoundException {
		Conversation conversation = new ConversationDefault(senderUsername, receiverUsername, initialMessage);
		Conversation stub = (Conversation) UnicastRemoteObject.exportObject(conversation, 0);
		String stubName = "Conversation" + objectIdentifier;
		registry.bind(stubName, stub);
	}
}
