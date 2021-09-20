package com.sap.chatserver.config.guice;

import com.google.inject.name.Named;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.engine.boot.BootService;
import com.sap.chatserver.engine.command.Command;

public interface CommandFactory {

	@Named("Dump")
	Command createCommandDump();

	@Named("Help")
	Command createCommandHelp();

	@Named("Kick")
	Command createCommandKick(DbService dbService);

	@Named("Kill")
	Command createCommandKill(BootService bootService);

	@Named("List")
	Command createCommandList();

	@Named("Login")
	Command createCommandLogin(DbService dbService);

	@Named("Logout")
	Command createCommandLogout();

	@Named("Quit")
	Command createCommandQuit();

	@Named("Register")
	Command createCommandRegister(DbService dbService);

	@Named("Send")
	Command createCommandSend(DbService dbService);
	
	@Named("Logger")
	Command createCommandLogger();
}
