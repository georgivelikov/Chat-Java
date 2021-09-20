package com.sap.chatserver.exception;

public class DatabaseInitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseInitException(String message) {
		super(message);
	}

	public DatabaseInitException(String message, Throwable t) {
		super(message, t);
	}
}
