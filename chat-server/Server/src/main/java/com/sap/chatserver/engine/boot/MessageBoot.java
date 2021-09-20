package com.sap.chatserver.engine.boot;

import com.sap.chatserver.common.Message;

public enum MessageBoot implements Message {
	
	SOCKET_INITIALIZATION_FAILED("Server did not start successfully. Server socket initialization failed on port %s."),
	SERVER_RUNNING("Server running on port %s!"),
	RUNNING("Server is running on port %s!"),
	CLIENT_SOCKET_INITIALIZATION_FAILED("Client socket initialization failed."),
	SOCKET_CLOSE_FAILED("Server socket on port %s failed to close."),
	STOP_SUCCESS("Server successfully stopped!"),
	STOP_FAILED("Server stop failed!");
	
	private final String content;
	private final int code;
	
	private MessageBoot(String content) {
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
