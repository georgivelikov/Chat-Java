package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageLogout;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.log.LogService;

public class CommandLogout implements Command {

	public static final int ARGS_COUNT = 1;
	public static final String NAME = "LOGOUT";
	public static final String DESCRIPTION = "";
	
	private LogService logService;

	@Inject
	public CommandLogout(LogService logService) {
		this.logService = logService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		logService.logInfo(String.format(MessageLogout.LOGOUT_SUCCESS.getContent(), connection.getAddress(),
				connection.getUser().getUsername()));
		connection.setUser(null);
		connection.writeMessage(MessageLogout.LOGOUT_SUCCESS.getContent());

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
