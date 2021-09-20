package com.sap.chatserver.engine.connection;

import com.sap.chatserver.common.Message;

public enum MessageConnection implements Message {
	CLIENT_UNKNOWN("<unknown>"),
	CLIENT_CONN_WRITE_EXC("Failed to write message to connection %s."),
	CLIENT_CONN_READ_EXC("Failed to read message from connection %s."),
	CLIENT_CONN_CLOSE_EXC("Connection %s failed to close."),
	CLIENT_OUT_STREAM_EXC_AFTER_SOCKET_CLOSE_EXC("Output stream did not close successfully after failed socket close."),
	CLIENT_IN_STREAM_EXC_AFTER_SOCKET_CLOSE_EXC("Input stream did not close successfully after failed socket close."),
	CLIENT_SOCKET_CLOSE_EXC_AFTER_OUT_STREAM_EXC("Socket did not close successfully after output stream failed to write to client."),
	CLIENT_OUT_STREAM_CLOSE_EXC_AFTER_OUT_STREAM_EXC("Output stream did not close successfully after output stream failed to write to client."),
	CLIENT_IN_STREAM_CLOSE_EXC_AFTER_OUT_STREAM_EXC("Input stream did not close successfully after output stream failed to write to client."),
	CLIENT_SOCKET_CLOSE_EXC_AFTER_IN_STREAM_EXC("Socket did not close successfully after input stream failed to read from client."),
	CLIENT_OUT_STREAM_CLOSE_EXC_AFTER_IN_STREAM_EXC("Output stream did not close successfullyafter input stream failed to read from client."),
	CLIENT_IN_STREAM_CLOSE_EXC_AFTER_IN_STREAM_EXC("Input stream did not close successfullyafter input stream failed to read from client."),
	CLIENT_STREAM_INITIALIZATION_FAILED("Stream initialization failed for client %s."),
	CLIENT_OUTPUT_STREAM_INIT_EXCEPTION("Output stream initialization failed."),
	CLIENT_INPUT_STREAM_INIT_EXCEPTION("Input stream initialization failed."),
	CLIENT_SOCKET_ALREADY_CLOSED("Client socket is already closed."),
	CLIENT_SOCKET_UNSUCCESSFULLY_CLOSED("Socket did not close successfylly."),
	CLIENT_SOCKET_UNSUCCESSFULLY_CLOSED_AFTER_STREAM_INIT_FAILURE("Socket did not close successfully after stream initialization failure."),
	CLIENT_CONN_SUCCESS("Client successfully connected from %s.");
	
	private final String content;
	private final int code;
	
	private MessageConnection(String content) {
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
