package com.sap.chatserver.config.guice;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.google.inject.name.Named;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionInitException;

public interface ConnectionFactory {

	@Named("Default")
	Connection getConnectionDefault(Socket socket) throws ConnectionInitException;
	
	@Named("NIO")
	Connection getConnectionNIO(SocketChannel socket, SelectionKey selectionKey) throws ConnectionInitException;
}
