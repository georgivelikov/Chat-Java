package com.sap.chatserver.engine.command.impl;

import org.apache.commons.lang3.StringUtils;

import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.CommandType;
import com.sap.chatserver.engine.command.message.MessageDump;
import com.sap.chatserver.engine.command.message.MessageHelp;
import com.sap.chatserver.engine.command.message.MessageKick;
import com.sap.chatserver.engine.command.message.MessageKill;
import com.sap.chatserver.engine.command.message.MessageList;
import com.sap.chatserver.engine.command.message.MessageLogger;
import com.sap.chatserver.engine.command.message.MessageLogin;
import com.sap.chatserver.engine.command.message.MessageLogout;
import com.sap.chatserver.engine.command.message.MessageQuit;
import com.sap.chatserver.engine.command.message.MessageRegister;
import com.sap.chatserver.engine.command.message.MessageSend;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCommunicationException;

public class CommandHelp implements Command {

	public static final int MAX_ARGS_COUNT = 2;
	public static final String NAME = "HELP";
	public static final int PADDING = 10;

	@Override
	public void execute(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		if (commandArgs.length == 1) {
			generateGeneralHelpMessagel(connection);
		} else {
			generateSpecificHelpMessage(connection, commandArgs);
		}
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length > MAX_ARGS_COUNT) {
			connection.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		return true;
	}

	private void generateGeneralHelpMessagel(Connection connection) throws ConnectionCommunicationException {
		StringBuilder helpMessage = new StringBuilder();

		String dumpDescription = StringUtils.rightPad(CommandDump.NAME, PADDING)
				+ MessageDump.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String helpDescription = StringUtils.rightPad(NAME, PADDING) + MessageHelp.DESCRIPTION.getContent()
				+ ConstantCommon.NEW_LINE;
		String kickDescription = StringUtils.rightPad(CommandKick.NAME, PADDING)
				+ MessageKick.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String killDescription = StringUtils.rightPad(CommandKill.NAME, PADDING)
				+ MessageKill.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String listDescription = StringUtils.rightPad(CommandList.NAME, PADDING)
				+ MessageList.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String loggerDescription = StringUtils.rightPad(CommandLogger.NAME, PADDING)
				+ MessageLogger.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String loginDescription = StringUtils.rightPad(CommandLogin.NAME, PADDING)
				+ MessageLogin.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String logoutDescription = StringUtils.rightPad(CommandLogout.NAME, PADDING)
				+ MessageLogout.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String quitDescription = StringUtils.rightPad(CommandQuit.NAME, PADDING)
				+ MessageQuit.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String registerDescription = StringUtils.rightPad(CommandRegister.NAME, PADDING)
				+ MessageRegister.DESCRIPTION.getContent() + ConstantCommon.NEW_LINE;
		String sendDescription = StringUtils.rightPad(CommandSend.NAME, PADDING)
				+ MessageSend.DESCRIPTION.getContent();

		helpMessage.append(ConstantCommon.NEW_LINE).append(MessageHelp.HEADER).append(ConstantCommon.NEW_LINE)
				.append(dumpDescription).append(helpDescription).append(kickDescription).append(killDescription)
				.append(listDescription).append(loggerDescription).append(loginDescription).append(logoutDescription)
				.append(quitDescription).append(registerDescription).append(sendDescription).append(ConstantCommon.NEW_LINE);

		connection.writeMessage(helpMessage.toString());
	}

	private void generateSpecificHelpMessage(Connection connection, String[] commandArgs)
			throws ConnectionCommunicationException {
		String message = "";
		try {
			CommandType commandType = CommandType.valueOf(commandArgs[1].toUpperCase());
			switch (commandType) {
			case DUMP:
				message = MessageDump.DETAILS.getContent();
				break;
			case HELP:
				message = MessageHelp.DETAILS.getContent();
				break;
			case KICK:
				message = MessageKick.DETAILS.getContent();
				break;
			case KILL:
				message = MessageKill.DETAILS.getContent();
				break;
			case LIST:
				message = MessageList.DETAILS.getContent();
				break;
			case LOGGER:
				message = MessageLogger.DETAILS.getContent();
				break;
			case LOGIN:
				message = MessageLogin.DETAILS.getContent();
				break;
			case LOGOUT:
				message = MessageLogout.DETAILS.getContent();
				break;
			case QUIT:
				message = MessageQuit.DETAILS.getContent();
				break;
			case REGISTER:
				message = MessageRegister.DETAILS.getContent();
				break;
			case SEND:
				message = MessageSend.DETAILS.getContent();
				break;
			default:
				message = MessageHelp.UNKNOW_COMMAND.getContent();
				break;
			}
		} catch (IllegalArgumentException e) {
			message = MessageHelp.UNKNOW_COMMAND.getContent();
		}

		connection.writeMessage(message + ConstantCommon.NEW_LINE);
	}
}
