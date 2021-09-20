package com.sap.chatserver.exception;

import java.io.IOException;

public class ServerSocketCloseException extends IOException {
	private static final long serialVersionUID = 1L;

	public ServerSocketCloseException(String message, Throwable t) {
		super(message, t);
	}
}