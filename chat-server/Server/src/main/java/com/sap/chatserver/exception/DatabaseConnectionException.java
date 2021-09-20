package com.sap.chatserver.exception;

public class DatabaseConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionException(String message) {
		super(message);
	}

	public DatabaseConnectionException(String message, Throwable t) {
		super(message, t);
	}
}
