package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageKick implements Message {

	DESCRIPTION("Kicks specific user."),
	DETAILS("\nKICK <username>\nusername - specifies the username of the user that must be kicked"),
	NO_PERMISSIONS("You dont have permissions to kick users."),
	USER_NOT_LOGGED_IN("There is no logged in user with that username."),
	USER_NOT_REGISTERED("This username is not registered."),
	USER_NOT_PERMITTED("You can't kick this user."),
	FAILED_SENDING_KICKED_USER_MESSAGE("Failed to send notification message to kicked user."),
	FAILED_CLOSING_KICKED_USER_CONNECTION("Failed to close kicked user connection."),
	FAILED_KICK("Kicking user %s was not successful."),
	SUCCESSFUL_KICK("User '%s' was kicked by administrator %s.");

	private final String content;
	private final int code;
	
	private MessageKick(String content) {
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
