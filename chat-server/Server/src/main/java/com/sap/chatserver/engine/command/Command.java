package com.sap.chatserver.engine.command;

import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.DatabaseConnectionException;

public interface Command {

	public void execute(Connection connection, String[] commandArgs)
			throws ConnectionCommunicationException, ConnectionCloseException, DatabaseConnectionException;
}
