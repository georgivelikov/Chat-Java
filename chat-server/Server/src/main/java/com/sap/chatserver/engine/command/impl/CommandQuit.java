package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageQuit;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;

public class CommandQuit implements Command {

	public static final int ARGS_COUNT = 1;
	public static final String NAME = "QUIT";
	public static final String DESCRIPTION = "";
	
	@Inject
	private ConnectionService connectionService;

	@Inject
	public CommandQuit(ConnectionService connectionService) {
		this.connectionService = connectionService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs)
			throws ConnectionCommunicationException, ConnectionCloseException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		String username = connection.getUser() == null ? "" : connection.getUser().getUsername();
		connection.writeMessage(String.format(MessageQuit.GOODBUY_MESSAGE.getContent(), username));
		connectionService.removeConnection(connection);
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length != ARGS_COUNT) {
			connection
					.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		return true;
	}

}
