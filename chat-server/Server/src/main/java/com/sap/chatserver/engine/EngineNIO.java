package com.sap.chatserver.engine;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.config.guice.ConnectionFactory;
import com.sap.chatserver.config.guice.ConnectionProcessorFactory;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.boot.MessageBoot;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionProcessor;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.engine.connection.MessageConnection;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.ConnectionInitException;
import com.sap.chatserver.exception.ServerSocketInitException;
import com.sap.chatserver.log.LogService;

public class EngineNIO implements Engine {
	
	private ServerSocketChannel serverSocket;
	
	private List<ConnectionProcessor> connectionProcessorHolder;
	
	private BootService bootService;

	private DbService dbService;

	private LogService logService;

	private ExecutorService executorService;

	private ConnectionService connectionService;

	private ConnectionFactory connectionFactory;

	private ConnectionProcessorFactory connectionProcessorFactory;

	@Inject
	public EngineNIO(@Assisted ServerSocketChannel serverSocket, @Assisted BootService bootService,
			@Assisted DbService dbService, LogService logService, ExecutorService executorService,
			ConnectionService connectionService, ConnectionFactory connectionFactory,
			ConnectionProcessorFactory connectionProcessorFactory) {
		this.serverSocket = serverSocket;
		this.connectionProcessorHolder = new ArrayList<>();
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
		try {
			initConnectionProcessors();
		} catch (ServerSocketInitException e1) {
			//TODO HANDLE EXCEPTION
		}
		
		Random rand = new Random();
		while (bootService.isEngineRunning()) {
			SocketChannel clientSocket;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				logService.logError(MessageBoot.CLIENT_SOCKET_INITIALIZATION_FAILED, e);
				continue;
			}

			try {
				ConnectionProcessor connectionProcessor = connectionProcessorHolder.get(rand.nextInt(connectionProcessorHolder.size()));
				try {
					clientSocket.configureBlocking(false);
				} catch (IOException ioExc) {
					throw new ConnectionInitException("Exception setting socket channel blocking to false.", ioExc);
				}
				SelectionKey selectionKey = getSelectionKey(clientSocket, connectionProcessor.getSelector());
				Connection connection = connectionFactory.getConnectionNIO(clientSocket, selectionKey);
				connectionProcessor.bindConnectionToProcessor(connection, selectionKey);
				connectionService.addConnection(connection);
				try {
					connection.writeMessage(MessageCommon.buildWelcomeMessage());
				} catch (ConnectionCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logService.logInfo(
						String.format(MessageConnection.CLIENT_CONN_SUCCESS.getContent(), connection.getAddress()));
			} catch (ConnectionInitException connInitExc) {
				logService.logError(connInitExc.getMessage(), connInitExc.getCause());
			}
		}
	}
	
	private void initConnectionProcessors() throws ServerSocketInitException {
		for (int i = 0; i < ConstantCommon.MAX_ALLOWED_THREADS; i++) {
			Selector selector = openSelector();
			ConnectionProcessor connectionProcessor = connectionProcessorFactory.getConnectionProcessorNIO(selector, dbService, bootService);
			connectionProcessorHolder.add(connectionProcessor);
			executorService.execute(connectionProcessor);
		}
	}
	
	private Selector openSelector() throws ServerSocketInitException {
		try {
			return Selector.open();
		} catch (IOException ioExc) {
			throw new ServerSocketInitException("Unable to open selector.", ioExc);
		}
	}
	
	private SelectionKey getSelectionKey(SocketChannel socketChannel, Selector selector) throws ConnectionInitException {
		try {
			return socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			throw new ConnectionInitException("Close channel", e);
		}
	}
}
