package com.sap.chatserver.config.server;

import com.sap.chatserver.common.Message;

public enum MessageConfig implements Message {
	
	CONFIG_PARSE_EXC("Unable to parse configuration file."),
	PROP_MISSING_EXC("Server property '%s' is missing in configuration file."),
	PROP_FORMAT_NOT_CORRECT_EXC("Server property '%s' is not in correct format in configuration file."),
	REGISTRY_INIT_FAILED("Initializing registry on port %s failed.");
	
	private final String content;
	private final int code;
	
	private MessageConfig(String content) {
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
