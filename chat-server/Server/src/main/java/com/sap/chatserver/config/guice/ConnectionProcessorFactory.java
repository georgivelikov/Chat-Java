package com.sap.chatserver.config.guice;

import java.nio.channels.Selector;

import com.google.inject.name.Named;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionProcessor;

public interface ConnectionProcessorFactory {
	
	@Named("Default")
	ConnectionProcessor getConnectionProcessorDefault(Connection connection, DbService dbService, BootService bootService);
	
	@Named("NIO")
	ConnectionProcessor getConnectionProcessorNIO(Selector selector, DbService dbService, BootService bootService);
}
