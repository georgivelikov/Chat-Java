package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageLogger;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.log.LogLevel;
import com.sap.chatserver.log.LogService;

public class CommandLogger implements Command {
	public static final int ARGS_COUNT = 2;
	public static final String NAME = "LOGGER";
	public static final String DESCRIPTION = "";

	private LogService logService;

	@Inject
	public CommandLogger(LogService logService) {
		this.logService = logService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		try {
			LogLevel currentLogLevel = logService.getLogLevel();
			LogLevel logLevel = LogLevel.valueOf(commandArgs[1].toUpperCase());
			logService.setLogLevel(logLevel);
			String successMessage = String.format(MessageLogger.LOG_LEVEL_CHANGE_SUCCESS.getContent(),
					currentLogLevel.toString(), logLevel.toString());
			logService.logInfo(successMessage);
			connection.writeMessage(successMessage);
		} catch (IllegalArgumentException e) {
			String failMessage = String.format(MessageLogger.UNKNOWN_LOG_LEVEL.getContent(), commandArgs[1]);
			logService.logWarn(failMessage);
			connection.writeMessage(failMessage);
		}
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

		if (!connection.checkAdminRights()) {
			connection.writeMessage(MessageLogger.NO_PERMISSIONS.getContent());
			return false;
		}

		return true;
	}
}
