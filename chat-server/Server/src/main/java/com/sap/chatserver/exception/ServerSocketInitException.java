package com.sap.chatserver.exception;

import java.io.IOException;

public class ServerSocketInitException extends IOException {
	private static final long serialVersionUID = 1L;

	public ServerSocketInitException(String message, Throwable t) {
		super(message, t);
	}
}
