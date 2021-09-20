package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageLogin implements Message {

	DESCRIPTION("Logs in an existing user."),
	DETAILS("\nLOGIN <username> <password>\nusername - logs in registered user with the specified username\npassword - logs in register user with the specified password"),
	ALREADY_LOGGED("You are already logged in."),
	USERNAME_DOES_NOT_EXIST("Username '%s' does not exist. Register first."),
	INVALID_PASSWORD("Your password is invalid."),
	SUCCESSFUL_LOGIN("You've successfully logged in."),
	SUCCESSFUL_LOGIN_LOGGING("Client with ip %s successfully logged in as '%s'."),
	FAILED_LOGIN_LOGGING("Client with ip %s unsuccessfully logged in as '%s'.");

	private final String content;
	private final int code;
	
	private MessageLogin(String content) {
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
