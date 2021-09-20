package com.sap.chatserver.engine.connection;

import com.sap.chatserver.exception.ConnectionCloseException;

public interface ConnectionService {

	void addConnection(Connection connection);

	void removeConnection(Connection connection) throws ConnectionCloseException;

	void removeAllConnections();

	Connection findConnectionByUsername(String username);

	Iterable<Connection> getConnections();

	Iterable<Connection> getLoggedInConnections();
}
