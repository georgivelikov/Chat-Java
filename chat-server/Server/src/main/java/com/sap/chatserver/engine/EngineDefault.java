package com.sap.chatserver.engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.config.guice.ConnectionFactory;
import com.sap.chatserver.config.guice.ConnectionProcessorFactory;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.boot.MessageBoot;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionProcessor;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.engine.connection.MessageConnection;
import com.sap.chatserver.exception.ConnectionInitException;
import com.sap.chatserver.log.LogService;

public class EngineDefault implements Engine {

	private ServerSocket serverSocket;

	private BootService bootService;

	private DbService dbService;

	private LogService logService;

	private ExecutorService executorService;

	private ConnectionService connectionService;

	private ConnectionFactory connectionFactory;

	private ConnectionProcessorFactory connectionProcessorFactory;

	@Inject
	public EngineDefault(@Assisted ServerSocket serverSocket, @Assisted BootService bootService,
			@Assisted DbService dbService, LogService logService, ExecutorService executorService,
			ConnectionService connectionService, ConnectionFactory connectionFactory,
			ConnectionProcessorFactory connectionProcessorFactory) {
		this.serverSocket = serverSocket;
		this.bootService = bootService;
		this.dbService = dbService;
		this.logService = logService;
		this.executorService = executorService;
		this.connectionService = connectionService;
		this.connectionFactory = connectionFactory;
		this.connectionProcessorFactory = connectionProcessorFactory;
	}

	@Override
	public ExecutorService getExecutorService() {
		return executorService;
	}

	@Override
	public void run() {
		logService.logInfo(String.format(MessageBoot.SERVER_RUNNING.getContent(), bootService.getServerPort()));

		while (bootService.isEngineRunning()) {
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				logService.logError(MessageBoot.CLIENT_SOCKET_INITIALIZATION_FAILED, e);
				continue;
			}

			try {
				Connection connection = connectionFactory.getConnectionDefault(clientSocket);
				connectionService.addConnection(connection);
				ConnectionProcessor connectionProcessor = connectionProcessorFactory.getConnectionProcessorDefault(connection, dbService, bootService);
				executorService.execute(connectionProcessor);
				logService.logInfo(
						String.format(MessageConnection.CLIENT_CONN_SUCCESS.getContent(), connection.getAddress()));
			} catch (ConnectionInitException connInitExc) {
				logService.logError(connInitExc.getMessage(), connInitExc.getCause());
			}
		}
	}
}
