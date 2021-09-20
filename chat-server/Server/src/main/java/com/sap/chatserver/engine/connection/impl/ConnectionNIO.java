package com.sap.chatserver.engine.connection.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.db.entity.UserRole;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.ConnectionInitException;
import com.sap.chatserver.log.LogService;

public class ConnectionNIO implements Connection {

	private SocketChannel clientSocket;
	
	private ByteBuffer readBuffer;
	
	private SelectionKey selectionKey;
	
	private LogService logService;
	
	private String address;
	
	private int port;
	
	private User user;
	
	@Inject
	public ConnectionNIO(@Assisted SocketChannel clientSocket, @Assisted SelectionKey selectionKey, LogService logService) throws ConnectionInitException {
		this.clientSocket = clientSocket;
		this.selectionKey = selectionKey;
		this.readBuffer = ByteBuffer.allocate(4096);
		this.logService = logService;
		this.address = String.format("%s:%s", "", "");
	}
	
	@Override
	public int getPort() {
		return port;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;		
	}

	@Override
	public boolean isConnected() {
		if (clientSocket == null) {
			return false;
		}

		return clientSocket.isOpen();
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public boolean isLoggedIn() {
		return !(user == null);
	}

	@Override
	public boolean checkAdminRights() {
		if (user == null) {
			return false;
		}

		return user.getRole().equals(UserRole.ADMINISTRATOR);
	}

	@Override
	public String readMessage() throws ConnectionCommunicationException {
		int amountRead = -1;
		try {
			amountRead = clientSocket.read((ByteBuffer) readBuffer.clear());
		} catch (IOException e) {
			throw new ConnectionCommunicationException("Error reading from channel", e);
		}
		
		if(amountRead <= 0) {
			return null;
		}

		byte[] content = Arrays.copyOf(readBuffer.array(), amountRead);
		String message = new String(content);
		return message.trim();
	}

	@Override
	public void writeMessage(String message) throws ConnectionCommunicationException {
		message = message + ConstantCommon.NEW_LINE;
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
		try {
			clientSocket.write(writeBuffer);
		} catch (IOException e) {
			throw new ConnectionCommunicationException("Error writing to channel", e);
		}
	}

	@Override
	public void close() throws ConnectionCloseException {
		// TODO HANDLE EXCEPTION
		
	}

}
