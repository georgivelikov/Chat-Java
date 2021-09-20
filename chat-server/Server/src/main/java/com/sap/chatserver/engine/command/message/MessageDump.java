package com.sap.chatserver.engine.command.message;

import com.sap.chatserver.common.Message;

public enum MessageDump implements Message {

	DESCRIPTION("Generate JVM thread dump."),
	DETAILS("\nDUMP\nno additional parameters needed"),
	NO_PERMISSIONS("You dont have permissions to generate thread dumps."),
	FAILED_TO_GENERATE("Thread dump failed to generate. Check log files for more information."),
	GENERATION_SUCCESSFUL("Thread dump generated successfully. Check {homeDirectory}/%s for %s."),
	GENERATION_SUCCESSFUL_LOGGING("Thread dump generated successfully by %s."),
	PATH_GENERATION_FAILED("Failed to generate '%s' in the file system."),
	HEADER_FORMAT("CREATED AT %s");

	private final String content;
	private final int code;

	private MessageDump(String content) {
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
