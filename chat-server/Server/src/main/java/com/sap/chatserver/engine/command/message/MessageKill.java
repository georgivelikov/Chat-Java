package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageKill implements Message {

	DESCRIPTION("Stops the server."),
	DETAILS("KILL\nno additional parameters needed"),
	NO_PERMISSIONS("You dont have permissions to stop the server!"),
	SERVER_NOT_AVAILABLE("Server is not available."),
	STOP_SERVER_ATTEMPT("Administrator %s trying to stop server."),
	STOP_SERVER_SUCCESS("Administrator %s successfully stopped server."),
	STOP_SERVER_FAILURE("Administrator %s unsuccessfully tried to stop server.");

	private final String content;
	private final int code;
	
	private MessageKill(String content) {
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
