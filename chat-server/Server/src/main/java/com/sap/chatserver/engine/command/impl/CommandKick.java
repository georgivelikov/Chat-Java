package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageKick;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.engine.connection.MessageConnection;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;
import com.sap.chatserver.log.LogService;

public class CommandKick implements Command {

	public static final int ARGS_COUNT = 2;
	public static final String NAME = "KICK";
	public static final String DESCRIPTION = "";

	private ConnectionService connectionService;

	private DbService dbService;

	private LogService logService;

	@Inject
	public CommandKick(@Assisted DbService dbService, LogService logService, ConnectionService connectionService) {
		this.dbService = dbService;
		this.logService = logService;
		this.connectionService = connectionService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs)
			throws DatabaseConnectionException, ConnectionCommunicationException, ConnectionCommunicationException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		String kickedUsername = commandArgs[1];
		User kickedUser = dbService.findUserByUsername(kickedUsername);
		if (kickedUser == null) {
			connection.writeMessage(MessageKick.USER_NOT_REGISTERED.getContent());
			return;
		}

		Connection kickedUserConnection = connectionService.findConnectionByUsername(kickedUsername);
		if (kickedUserConnection == null || !kickedUserConnection.isConnected()) {
			connection.writeMessage(MessageKick.USER_NOT_LOGGED_IN.getContent());
			return;
		}

		if (kickedUserConnection.checkAdminRights()) {
			connection.writeMessage(MessageKick.USER_NOT_PERMITTED.getContent());
			return;
		}

		boolean isSuccessful = true;
		
		try {
			connectionService.removeConnection(kickedUserConnection);
		} catch (ConnectionCloseException connCloseExc) {
			logService.logWarn(String.format(MessageConnection.CLIENT_CONN_CLOSE_EXC.getContent(), kickedUserConnection.getAddress()), connCloseExc);
			isSuccessful = false;
		}

		if (!isSuccessful) {
			connection.writeMessage(String.format(MessageKick.FAILED_KICK.getContent(), kickedUsername));
			logService.logInfo(String.format(MessageKick.FAILED_KICK.getContent(), kickedUsername,
					connection.getUser().getUsername()));
			return;
		}

		connection.writeMessage(String.format(MessageKick.SUCCESSFUL_KICK.getContent(), kickedUsername,
				connection.getUser().getUsername()));
		logService.logInfo(String.format(MessageKick.SUCCESSFUL_KICK.getContent(), kickedUsername,
				connection.getUser().getUsername()));

	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length != ARGS_COUNT) {
			connection.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		if (!connection.isLoggedIn()) {
			connection.writeMessage(MessageCommon.LOGIN_FIRST.getContent());
			return false;
		}

		if (!connection.checkAdminRights()) {
			connection.writeMessage(MessageKick.NO_PERMISSIONS.getContent());
			return false;
		}

		return true;
	}
}
