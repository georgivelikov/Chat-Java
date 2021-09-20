package com.sap.chatserver.engine.connection;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;

public interface ConnectionProcessor extends Runnable {
	
	void processConnection() throws ConnectionCommunicationException, ConnectionCloseException, DatabaseConnectionException;
	
	void bindConnectionToProcessor(Connection connection, SelectionKey selectionKey);
	
	Selector getSelector();
}
