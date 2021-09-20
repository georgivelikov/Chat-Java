package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageSend implements Message {

	DESCRIPTION("Sends message to the specified user."),
	DETAILS("\nSEND <receiver> [message]\nreceiver - specifies the username of the receiving user. If '*' is provided the message will be send to all logged in users."
			+ "\nmessage - specifies the message to be send"),
	RECEIVER_UNKNOWN("There is no user with that username."),
	RECEIVER_OFFLINE("%s is offline."),
	MESSAGE_CONFIRMED("message send"),
	MESSAGE_FAILED("Failed to start conversation with %s"),
	CONVERSATION_ALREADY_STARTED("Conversation with %s already started.");
	
	private final String content;
	private final int code;
	
	private MessageSend(String content) {
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
