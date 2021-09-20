package com.sap.chatserver.engine.boot;

import com.sap.chatserver.exception.ServerSocketCloseException;

public interface BootService {

	void startServer(String[] args);

	void stopServer() throws ServerSocketCloseException;
	
	boolean isEngineRunning();
	
	int getServerPort();
}
