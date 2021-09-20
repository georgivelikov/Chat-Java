package com.sap.chatserver.config.guice;

import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

import com.google.inject.name.Named;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.Engine;
import com.sap.chatserver.engine.boot.BootService;

public interface EngineFactory {

	@Named("Default")
	Engine getEngineDefault(ServerSocket serverSocket, BootService bootService, DbService dbService);
	
	@Named("NIO")
	Engine getEngineNIO(ServerSocketChannel serverSocket, BootService bootService, DbService dbService);
}
