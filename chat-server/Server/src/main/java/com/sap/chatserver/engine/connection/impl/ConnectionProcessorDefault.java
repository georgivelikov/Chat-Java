package com.sap.chatserver.engine.connection.impl;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.config.guice.CommandFactory;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.CommandType;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionProcessor;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;
import com.sap.chatserver.log.LogService;

public class ConnectionProcessorDefault implements ConnectionProcessor {

	private Connection connection;

	private CommandFactory commandFactory;

	private LogService logService;

	private BootService bootService;

	private DbService dbService;

	@Inject
	public ConnectionProcessorDefault(@Assisted Connection connection, @Assisted DbService dbService,
			@Assisted BootService bootService, LogService logService, CommandFactory commandFactory) {
		this.connection = connection;
		this.bootService = bootService;
		this.dbService = dbService;
		this.logService = logService;
		this.commandFactory = commandFactory;
	}

	@Override
	public void run() {
		try {
			processConnection();
		} catch (ConnectionCommunicationException communicationExc) {
			logService.logError(communicationExc.getMessage(), communicationExc.getCause());
		} catch (ConnectionCloseException closeExc) {
			logService.logError(closeExc.getMessage(), closeExc.getCause());
		} catch (DatabaseConnectionException dbConnExc) {
			logService.logError(dbConnExc.getMessage(), dbConnExc.getCause());
		}
	}

	@Override
	public void processConnection()
			throws ConnectionCommunicationException, ConnectionCloseException, DatabaseConnectionException {
		connection.writeMessage(MessageCommon.buildWelcomeMessage());

		while (connection != null && connection.isConnected()) {
			String input = connection.readMessage();

			if (StringUtils.isBlank(input)) {
				continue;
			}

			String[] commandArgs = StringUtils.split(input);
			Command command = parseInputToCommand(commandArgs);
			if (command == null) {
				connection.writeMessage(MessageCommon.UNKNOWN_COMMAND.getContent());
			} else {
				command.execute(connection, commandArgs);
			}
		}
	}

	@Override
	public void bindConnectionToProcessor(Connection connection, SelectionKey selectionKey) {
		// TODO Auto-generated method stub

	}

	private Command parseInputToCommand(String[] commandArgs) {
		try {
			CommandType commandType = CommandType.valueOf(commandArgs[0].toUpperCase());
			Command command;
			switch (commandType) {
			case DUMP:
				command = commandFactory.createCommandDump();
				break;
			case HELP:
				command = commandFactory.createCommandHelp();
				break;
			case KICK:
				command = commandFactory.createCommandKick(dbService);
				break;
			case KILL:
				command = commandFactory.createCommandKill(bootService);
				break;
			case LIST:
				command = commandFactory.createCommandList();
				break;
			case LOGIN:
				command = commandFactory.createCommandLogin(dbService);
				break;
			case LOGOUT:
				command = commandFactory.createCommandLogout();
				break;
			case LOGGER:
				command = commandFactory.createCommandLogger();
				break;
			case QUIT:
				command = commandFactory.createCommandQuit();
				break;
			case REGISTER:
				command = commandFactory.createCommandRegister(dbService);
				break;
			case SEND:
				command = commandFactory.createCommandSend(dbService);
				break;
			default:
				command = null;
				break;
			}

			return command;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public Selector getSelector() {
		// TODO Auto-generated method stub
		return null;
	}
}
