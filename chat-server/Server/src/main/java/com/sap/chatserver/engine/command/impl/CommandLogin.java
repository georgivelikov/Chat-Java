package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageLogin;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;
import com.sap.chatserver.log.LogService;

public class CommandLogin implements Command {

	public static final int ARGS_COUNT = 3;
	public static final String NAME = "LOGIN";
	public static final String DESCRIPTION = "";
	
	private DbService dbService;

	private LogService logService;

	@Inject
	public CommandLogin(@Assisted DbService dbService, LogService logService) {
		this.dbService = dbService;
		this.logService = logService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs)
			throws ConnectionCommunicationException, DatabaseConnectionException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		String username = commandArgs[1];
		String password = commandArgs[2];

		User user = dbService.findUserByUsername(username);

		if (user == null) {
			connection.writeMessage(String.format(MessageLogin.USERNAME_DOES_NOT_EXIST.getContent(), username));
			logService.logInfo(String.format(MessageLogin.FAILED_LOGIN_LOGGING.getContent(),
					connection.getAddress(), username));
			return;
		}

		if (!validatePassword(user, password)) {
			connection.writeMessage(MessageLogin.INVALID_PASSWORD.getContent());
			logService.logInfo(String.format(MessageLogin.FAILED_LOGIN_LOGGING.getContent(),
					connection.getAddress(), username));
			return;
		}

		connection.setUser(user);
		connection.writeMessage(MessageLogin.SUCCESSFUL_LOGIN.getContent());
		logService.logInfo(String.format(MessageLogin.SUCCESSFUL_LOGIN_LOGGING.getContent(),
				connection.getAddress(), username));
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length != ARGS_COUNT) {
			connection
					.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		if (connection.isLoggedIn()) {
			connection.writeMessage(MessageLogin.ALREADY_LOGGED.getContent());
			return false;
		}

		return true;
	}

	private boolean validatePassword(User user, String password) {
		return user.getPassword().equals(password);
	}
}
