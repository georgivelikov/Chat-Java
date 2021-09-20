package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageLogger implements Message {
	DESCRIPTION("Changes the logger level."),
	DETAILS("\nLOGGER <level>\nlevel - changes the logger level to the specified value"),
	NO_PERMISSIONS("You dont have permissions to change the log level."),
	UNKNOWN_LOG_LEVEL("Log level '%s' is uknown. Log level remains '%s'."),
	LOG_LEVEL_CHANGE_SUCCESS("Log level successfully changed from '%s' to '%s'.");
	
	private final String content;
	private final int code;
	
	private MessageLogger(String content) {
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
