package com.sap.chatserver.exception;

import java.io.IOException;

public class ConnectionCloseException extends IOException {
	private static final long serialVersionUID = 1L;

	public ConnectionCloseException(String message, Throwable t) {
		super(message, t);
	}
}
