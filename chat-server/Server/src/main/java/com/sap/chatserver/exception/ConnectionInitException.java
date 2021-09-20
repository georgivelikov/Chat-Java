package com.sap.chatserver.exception;

import java.io.IOException;

public class ConnectionInitException extends IOException {

	private static final long serialVersionUID = 1L;

	public ConnectionInitException(String message, Throwable t) {
		super(message, t);
	}
}
