package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageList implements Message {

	DESCRIPTION("Lists all logged in users. If user has administartor rights, all connections will be listed."),
	DETAILS("LIST\nno additional parameters needed");
	
	private final String content;
	private final int code;
	
	private MessageList(String content) {
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

