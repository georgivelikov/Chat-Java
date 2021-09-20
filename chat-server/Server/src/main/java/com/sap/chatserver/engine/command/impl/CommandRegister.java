package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.db.entity.UserRole;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageRegister;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;
import com.sap.chatserver.log.LogService;

public class CommandRegister implements Command {

	public static final int ARGS_COUNT = 3;
	public static final String NAME = "REGISTER";
	public static final String DESCRIPTION = "";
	public static final int MIN_USERNAME_LENGTH = 3;
	public static final int MIN_PASSWORD_LENGTH = 3;
	public static final int MAX_USERNAME_LENGTH = 20;
	public static final int MAX_PASSWORD_LENGTH = 20;

	private DbService dbService;

	private LogService logService;

	@Inject
	public CommandRegister(@Assisted DbService dbService, LogService logService) {
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

		if (!validateUsernameExistence(username)) {
			connection.writeMessage(MessageRegister.USERNAME_TAKEN.getContent());
			return;
		}

		if (!validateUsernameLength(username)) {
			connection.writeMessage(String.format(MessageRegister.INVALID_USERNAME_LENGTH.getContent(),
					MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH));
			return;
		}

		if (!validatePasswordLength(password)) {
			connection.writeMessage(String.format(MessageRegister.INVALID_PASSWORD_LENGTH.getContent(),
					MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
			return;
		}

		try {
			dbService.addUser(username, password, UserRole.REGULAR);
			connection.writeMessage(MessageRegister.SUCCESSFUL_REGISTRATION.getContent());
			logService.logInfo(String.format(MessageRegister.SUCCESSFUL_REGISTRATION_LOGGING.getContent(),
					connection.getAddress(), username));
		} catch (DatabaseConnectionException dbExc) {
			connection.writeMessage(MessageRegister.FAILED_REGISTRATION.getContent());
			logService.logInfo(String.format(MessageRegister.FAILED_REGISTRATION_LOGGING.getContent(), username,
					connection.getAddress()));

			throw dbExc;
		}
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length != ARGS_COUNT) {
			connection
					.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		if (connection.isLoggedIn()) {
			connection.writeMessage(MessageRegister.LOGOUT_FIRST.getContent());
			return false;
		}

		return true;
	}

	private boolean validateUsernameExistence(String username) throws DatabaseConnectionException {
		return dbService.findUserByUsername(username) == null;
	}

	private boolean validateUsernameLength(String username) {
		return username.length() >= MIN_USERNAME_LENGTH && username.length() <= MAX_USERNAME_LENGTH;
	}

	private boolean validatePasswordLength(String password) {
		return password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH;
	}
}
