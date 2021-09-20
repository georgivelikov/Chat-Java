package com.sap.chatserver.exception;

import java.io.IOException;

public class ConnectionCommunicationException extends IOException {

	private static final long serialVersionUID = 1L;

	public ConnectionCommunicationException(String message, Throwable t) {
		super(message, t);
	}
}
