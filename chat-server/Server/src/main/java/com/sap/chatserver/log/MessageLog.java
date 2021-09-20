package com.sap.chatserver.log;

import com.sap.chatserver.common.Message;

public enum MessageLog implements Message {
	INVALID_LOG_LEVEL("Invalid log level."),
	LOG_LEVEL_CHANGE_SUCCESS("Log level change successfully."),
	LOG_LEVEL_CHANGE_FAILED("Log level change failed."),
	LOG_LEVEL_CHANGE_FAILED_FROM_CONFIG("Log level change failed. See configuration file any erros and check log files.");
	
	private final String content;
	private final int code;
	
	private MessageLog(String content) {
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
