package com.sap.chatserver.engine.connection.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.engine.connection.MessageConnection;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.log.LogService;

public class ConnectionServiceDefault implements ConnectionService {

	private List<Connection> connections;

	private LogService logService;

	@Inject
	public ConnectionServiceDefault(LogService logService) {
		this.connections = new ArrayList<>();
		this.logService = logService;
	}

	@Override
	public synchronized void addConnection(Connection connection) {
		if (connections != null) {
			connections.add(connection);
		}
	}

	@Override
	public synchronized void removeConnection(Connection connection) throws ConnectionCloseException {
		if (connections != null) {
			connections.remove(connection);
			connection.setUser(null);
			connection.close();
		}
	}

	@Override
	public synchronized void removeAllConnections() {
		if (connections == null) {
			return;
		}

		Iterator<Connection> i = connections.iterator();
		while (i.hasNext()) {
			Connection connection = i.next();
			connection.setUser(null);
			try {
				connection.close();
			} catch (ConnectionCloseException connCloseExc) {
				logService.logWarn(String.format(MessageConnection.CLIENT_CONN_CLOSE_EXC.getContent(),
						connection.getAddress()), connCloseExc);
			}

			i.remove();
		}

		connections = null;
	}

	@Override
	public synchronized Connection findConnectionByUsername(String username) {
		if (connections == null) {
			return null;
		}

		return connections.stream().filter(c -> c.getUser() != null && c.getUser().getUsername().equals(username))
				.findAny().orElse(null);
	}

	@Override
	public synchronized Iterable<Connection> getConnections() {
		return connections;
	}

	@Override
	public synchronized Iterable<Connection> getLoggedInConnections() {
		return connections.stream().filter(c -> c.getUser() != null).collect(Collectors.toList());
	}
}
