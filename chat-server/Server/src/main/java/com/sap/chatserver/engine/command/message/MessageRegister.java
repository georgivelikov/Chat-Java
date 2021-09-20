package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageRegister implements Message {

	DESCRIPTION("Register new user."),
	DETAILS("\nREGISTER <username> <password>\nusername - register user with the specified username\npassword - register user with the specified password"),
	LOGOUT_FIRST("You must logout first to register user!"),
	USERNAME_TAKEN("This username is taken! Try again with different username."),
	INVALID_USERNAME_LENGTH("Username should be at between %s and %s characters long!"),
	INVALID_PASSWORD_LENGTH("Password should be at betwee %s and %s characters long!"),
	FAILED_REGISTRATION("There is problem with your registration! Try again later."),
	FAILED_REGISTRATION_LOGGING("There is problem with registering username '%s' from client with ip %s"),
	SUCCESSFUL_REGISTRATION("Register successful! You may log in now."),
	SUCCESSFUL_REGISTRATION_LOGGING("Client with ip %s successfully registered username '%s'.");

	private final String content;
	private final int code;
	
	private MessageRegister(String content) {
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
