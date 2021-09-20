package com.sap.chatserver.launcher;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sap.chatserver.config.guice.ChatServerModule;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.boot.BootServiceDefault;
import com.sap.chatserver.engine.boot.BootServiceNIO;

public class ChatServerLauncher {

	public static void main(String[] args) {
		Injector guice = Guice.createInjector(new ChatServerModule());
		BootService bootService = guice.getInstance(BootServiceNIO.class);
		//BootService bootService = guice.getInstance(BootServiceDefault.class);
		bootService.startServer(args);
	}
}
