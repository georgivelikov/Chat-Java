package com.sap.chatserver.db.file.impl;

import com.sap.chatserver.common.Message;

public enum MessageDbFile implements Message {

	DB_FILE_NOT_IN_PLACE("Database file source not found on the specified location '%s'."),
	LOADING_USERS_FROM_DB_FILE_FAILED("Failed to read user data from database file."),
	REGISTER_USERDATA_ON_FILE_FAILED("Writing user data on file failed."),
	CORRUPTED_USER_DATA("Corrupted user data on line %s in database file.");
	
	private final String content;
	private final int code;
	
	private MessageDbFile(String content) {
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
