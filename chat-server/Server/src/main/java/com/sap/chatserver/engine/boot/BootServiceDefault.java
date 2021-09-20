package com.sap.chatserver.engine.boot;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

import com.google.inject.Inject;
import com.sap.chatserver.config.guice.EngineFactory;
import com.sap.chatserver.config.server.ConstantConfig;
import com.sap.chatserver.config.server.MessageConfig;
import com.sap.chatserver.config.server.ServerConfigParser;
import com.sap.chatserver.db.core.DbConnector;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.Engine;
import com.sap.chatserver.exception.DatabaseInitException;
import com.sap.chatserver.exception.ServerConfigurationException;
import com.sap.chatserver.exception.ServerSocketCloseException;
import com.sap.chatserver.exception.ServerSocketInitException;
import com.sap.chatserver.log.LogLevel;
import com.sap.chatserver.log.LogService;
import com.sap.chatserver.log.MessageLog;

public class BootServiceDefault implements BootService {

	private ServerSocket serverSocket;
	
	private int serverPort;
	
	private Engine engine;

	private LogService logService;

	private ServerConfigParser serverConfigParser;

	private DbConnector dbConnector;
	
	private EngineFactory engineFactory;

	@Inject
	public BootServiceDefault(LogService logService, ServerConfigParser serverConfigParser, DbConnector dbConnector, EngineFactory engineFactory) {
		this.logService = logService;
		this.serverConfigParser = serverConfigParser;
		this.dbConnector = dbConnector;
		this.engineFactory = engineFactory;
	}
	
	@Override
	public void startServer(String[] args) {
		try {
			Map<String, String> serverProperties = getServerConfigProperties(args);
			setLoggerLevel(serverProperties);
			serverPort = getServerPort(serverProperties);
			DbService dbService = getDbService(serverProperties);
			serverSocket = initServerSocket(serverPort);
			engine = engineFactory.getEngineDefault(serverSocket, this, dbService);
			engine.run();
		} catch (ServerConfigurationException serverConfigExc) {
			logService.logError(serverConfigExc.getMessage(), serverConfigExc.getCause());
		} catch (ServerSocketInitException serverInitExc) {
			logService.logError(serverInitExc.getMessage(), serverInitExc.getCause());
		} catch (DatabaseInitException dbConnExc) {
			logService.logError(dbConnExc.getMessage(), dbConnExc.getCause());
		}
	}

	@Override
	public synchronized void stopServer() throws ServerSocketCloseException {
		if (serverSocket == null || serverSocket.isClosed()) {
			return;
		}

		engine.getExecutorService().shutdown();

		try {
			serverSocket.close();
		} catch (IOException ioExc) {
			//TO DO
		}

		logService.logInfo(MessageBoot.STOP_SUCCESS);
	}
	

	@Override
	public boolean isEngineRunning() {
		return serverSocket != null && !serverSocket.isClosed();
	}


	@Override
	public int getServerPort() {
		return serverPort;
	}
	
	private Map<String, String> getServerConfigProperties(String[] args) throws ServerConfigurationException {
		String configLocation;
		if (args == null || args.length == 0) {
			configLocation = ConstantConfig.DEFAULT_SERVER_CONFIG_LOCATION;
		} else {
			configLocation = args[0];
		}

		return serverConfigParser.getProperties(configLocation);
	}

	private int getServerPort(Map<String, String> properties) throws ServerConfigurationException {
		String portStr = properties.get(ConstantConfig.CONFIG_PORT);
		if (portStr == null) {
			throw new ServerConfigurationException(
					String.format(MessageConfig.PROP_MISSING_EXC.getContent(), ConstantConfig.CONFIG_PORT));
		}

		try {
			return Integer.parseInt(portStr);
		} catch (NumberFormatException e) {
			throw new ServerConfigurationException(String
					.format(MessageConfig.PROP_FORMAT_NOT_CORRECT_EXC.getContent(), ConstantConfig.CONFIG_PORT));
		}
	}

	private void setLoggerLevel(Map<String, String> serverProperties) {
		String logLevelStr = serverProperties.get(ConstantConfig.CONFIG_LOG_LEVEL);
		if (logLevelStr == null) {
			logService.logWarn(
					String.format(MessageConfig.PROP_MISSING_EXC.getContent(), ConstantConfig.CONFIG_LOG_LEVEL));
			logService.logWarn(MessageLog.LOG_LEVEL_CHANGE_FAILED_FROM_CONFIG);
			return;
		}

		try {
			LogLevel logLevel = LogLevel.valueOf(logLevelStr.toUpperCase());
			logService.setLogLevel(logLevel);
		} catch (IllegalArgumentException e) {
			logService.logWarn(String.format(MessageConfig.PROP_FORMAT_NOT_CORRECT_EXC.getContent(),
					ConstantConfig.CONFIG_LOG_LEVEL));
			logService.logWarn(MessageLog.LOG_LEVEL_CHANGE_FAILED_FROM_CONFIG);
		}
	}

	private ServerSocket initServerSocket(int port) throws ServerSocketInitException {
		try {
			return new ServerSocket(port);
		} catch (IOException ioExc) {
			throw new ServerSocketInitException(
					String.format(MessageBoot.SOCKET_INITIALIZATION_FAILED.getContent(), port), ioExc);
		}
	}

	private DbService getDbService(Map<String, String> serverProperties) throws DatabaseInitException {
		String databaseType = serverProperties.get(ConstantConfig.CONFIG_DB_TYPE);
		if (databaseType == null) {
			throw new DatabaseInitException(
					String.format(MessageConfig.PROP_MISSING_EXC.getContent(), ConstantConfig.CONFIG_DB_TYPE));
		}

		if(databaseType.equals("file")) {
			return dbConnector.generateDbServiceFile(serverProperties);
		} else {
			// TO DO: MySQL database impl
			throw new DatabaseInitException("Unknown database");
		}
	}
}
