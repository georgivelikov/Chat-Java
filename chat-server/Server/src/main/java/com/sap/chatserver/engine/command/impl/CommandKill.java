package com.sap.chatserver.engine.command.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageKill;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.ServerSocketCloseException;
import com.sap.chatserver.log.LogService;

public class CommandKill implements Command {

	public static final int ARGS_COUNT = 1;
	public static final String NAME = "KILL";
	public static final String DESCRIPTION = "";
	
	private BootService bootService;

	private ConnectionService connectionService;

	private LogService logService;

	@Inject
	public CommandKill(@Assisted BootService bootService, ConnectionService connectionService, LogService logService) {
		this.bootService = bootService;
		this.connectionService = connectionService;
		this.logService = logService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs)
			throws ConnectionCommunicationException, ConnectionCloseException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		String adminUsername = connection.getUser().getUsername();
		logService.logInfo(String.format(MessageKill.STOP_SERVER_ATTEMPT.getContent(), adminUsername));

		connectionService.removeAllConnections();

		try {
			bootService.stopServer();
		} catch (ServerSocketCloseException stopExc) {
			String adminFailMessage = String.format(MessageKill.STOP_SERVER_FAILURE.getContent(), adminUsername);
			String logErrorMessage = String.format("%s %s", adminFailMessage, stopExc.getMessage());
			logService.logError(logErrorMessage, stopExc.getCause());
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
			connection.writeMessage(MessageKill.NO_PERMISSIONS.getContent());
			return false;
		}
		
		return true;
	}
}
