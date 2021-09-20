package com.sap.chatserver.config.guice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.config.server.ServerConfigParser;
import com.sap.chatserver.config.server.ServerConfigParserXml;
import com.sap.chatserver.db.core.DbConnector;
import com.sap.chatserver.db.core.DbConnectorDefault;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.db.file.impl.DbServiceFile;
import com.sap.chatserver.engine.Engine;
import com.sap.chatserver.engine.EngineDefault;
import com.sap.chatserver.engine.EngineNIO;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.boot.BootServiceDefault;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.impl.CommandDump;
import com.sap.chatserver.engine.command.impl.CommandHelp;
import com.sap.chatserver.engine.command.impl.CommandKick;
import com.sap.chatserver.engine.command.impl.CommandKill;
import com.sap.chatserver.engine.command.impl.CommandList;
import com.sap.chatserver.engine.command.impl.CommandLogger;
import com.sap.chatserver.engine.command.impl.CommandLogin;
import com.sap.chatserver.engine.command.impl.CommandLogout;
import com.sap.chatserver.engine.command.impl.CommandQuit;
import com.sap.chatserver.engine.command.impl.CommandRegister;
import com.sap.chatserver.engine.command.impl.CommandSend;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.ConnectionProcessor;
import com.sap.chatserver.engine.connection.ConnectionService;
import com.sap.chatserver.engine.connection.impl.ConnectionDefault;
import com.sap.chatserver.engine.connection.impl.ConnectionNIO;
import com.sap.chatserver.engine.connection.impl.ConnectionProcessorDefault;
import com.sap.chatserver.engine.connection.impl.ConnectionProcessorNIO;
import com.sap.chatserver.engine.connection.impl.ConnectionServiceDefault;
import com.sap.chatserver.log.LogService;
import com.sap.chatserver.log.LogServiceLog4J;

public class ChatServerModule extends AbstractModule {

	@Override
	protected void configure() {
		ExecutorService executor = Executors.newFixedThreadPool(ConstantCommon.MAX_ALLOWED_THREADS);
		bind(ExecutorService.class).toInstance(executor);
		bind(BootService.class).to(BootServiceDefault.class).in(Scopes.SINGLETON);
		bind(LogService.class).to(LogServiceLog4J.class).in(Scopes.SINGLETON);
		bind(ConnectionService.class).to(ConnectionServiceDefault.class).in(Scopes.SINGLETON);
		bind(ServerConfigParser.class).to(ServerConfigParserXml.class).in(Scopes.SINGLETON);
		bind(DbConnector.class).to(DbConnectorDefault.class).in(Scopes.SINGLETON);

		install(new FactoryModuleBuilder()
				.implement(Key.get(Engine.class, Names.named("Default")), EngineDefault.class)
				.implement(Key.get(Engine.class, Names.named("NIO")), EngineNIO.class)
				.build(EngineFactory.class));

		install(new FactoryModuleBuilder()
				.implement(Key.get(DbService.class, Names.named("DbServiceFile")), DbServiceFile.class)
				.build(DbServiceFactory.class));

		install(new FactoryModuleBuilder()
				.implement(Key.get(Connection.class, Names.named("Default")), ConnectionDefault.class)
				.implement(Key.get(Connection.class, Names.named("NIO")), ConnectionNIO.class)
				.build(ConnectionFactory.class));

		install(new FactoryModuleBuilder()
				.implement(Key.get(ConnectionProcessor.class, Names.named("Default")), ConnectionProcessorDefault.class)
				.implement(Key.get(ConnectionProcessor.class, Names.named("NIO")), ConnectionProcessorNIO.class)
				.build(ConnectionProcessorFactory.class));

		configureCommandsBinding();
	}

	private void configureCommandsBinding() {
		install(new FactoryModuleBuilder()
				.implement(Key.get(Command.class, Names.named("Dump")), CommandDump.class)
				.implement(Key.get(Command.class, Names.named("Help")), CommandHelp.class)
				.implement(Key.get(Command.class, Names.named("Kick")), CommandKick.class)
				.implement(Key.get(Command.class, Names.named("Kill")), CommandKill.class)
				.implement(Key.get(Command.class, Names.named("List")), CommandList.class)
				.implement(Key.get(Command.class, Names.named("Login")), CommandLogin.class)
				.implement(Key.get(Command.class, Names.named("Logout")), CommandLogout.class)
				.implement(Key.get(Command.class, Names.named("Quit")), CommandQuit.class)
				.implement(Key.get(Command.class, Names.named("Register")), CommandRegister.class)
				.implement(Key.get(Command.class, Names.named("Send")), CommandSend.class)
				.implement(Key.get(Command.class, Names.named("Logger")), CommandLogger.class)
				.build(CommandFactory.class));
	}
}
