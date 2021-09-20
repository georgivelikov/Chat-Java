package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.exception.ConnectionCommunicationException;

public class CommandList implements Command {

	public static final int ARGS_COUNT = 1;
	public static final String NAME = "LIST";
	public static final String DESCRIPTION = "";
	public static final String ADMIN_FORMAT = "%s %s";
	public static final String NOT_LOGGED = "<not logged>";

	private ConnectionService connectionService;

	@Inject
	public CommandList(ConnectionService connectionService) {
		this.connectionService = connectionService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		StringBuilder info = new StringBuilder();
		Iterable<Connection> connections = null;

		if (connection.checkAdminRights()) {
			connections = connectionService.getConnections();

			for (Connection conn : connections) {
				String formattedMessage;
				if (conn.isLoggedIn()) {
					formattedMessage = String.format(ADMIN_FORMAT, conn.getUser().getUsername(), conn.getAddress());
				} else {
					formattedMessage = String.format(ADMIN_FORMAT, NOT_LOGGED, conn.getAddress());
				}

				info.append(formattedMessage).append(ConstantCommon.NEW_LINE);
			}
		} else {
			connections = connectionService.getLoggedInConnections();

			for (Connection conn : connections) {
				info.append(conn.getUser().getUsername()).append(ConstantCommon.NEW_LINE);
			}
		}

		connection.writeMessage(info.toString().trim());
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length != ARGS_COUNT) {
			connection
					.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		if (!connection.isLoggedIn()) {
			connection.writeMessage(MessageCommon.LOGIN_FIRST.getContent());
			return false;
		}
		
		return true;
	}
}
