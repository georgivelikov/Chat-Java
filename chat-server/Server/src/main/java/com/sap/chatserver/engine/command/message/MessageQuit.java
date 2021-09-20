package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageQuit implements Message {

	DESCRIPTION("Quits the server."),
	DETAILS("\nQUIT\nno addtional parameters needed."),
	GOODBUY_MESSAGE("Goodbye %s.");
	
	private final String content;
	private final int code;
	
	private MessageQuit(String content) {
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
