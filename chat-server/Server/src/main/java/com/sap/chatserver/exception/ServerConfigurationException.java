package com.sap.chatserver.exception;

public class ServerConfigurationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServerConfigurationException(String message) {
		super(message);
	}
	
	public ServerConfigurationException(String message, Throwable t) {
		super(message, t);
	}
}
