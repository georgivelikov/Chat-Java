package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageHelp implements Message {

	HEADER("For more information on a specific command, type HELP <name>"),
	DESCRIPTION("Displays information about all commands."),
	DETAILS("HELP <command>\ncommand - provides information about the coammand with the specified name"),
	UNKNOW_COMMAND("Cannot generate HELP message for uknown command.");

	private final String content;
	private final int code;
	
	private MessageHelp(String content) {
		this.content = content;
		this.code = 1;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return content;
	}
}
