package com.sap.chatserver.engine;

import java.util.concurrent.ExecutorService;

public interface Engine {

	void run();
	
	ExecutorService getExecutorService();
}
