package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageLogout implements Message {

	DESCRIPTION("Logs out the user."),
	DETAILS("\nLOGOUT\nno addtional parameters needed."),
	LOGOUT_SUCCESS("You've successfully logged out!"),
	LOGOUT_SUCCESS_LOGGING("Client with ip %s and username '%s' successfully logged out.");

	private final String content;
	private final int code;
	
	private MessageLogout(String content) {
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
