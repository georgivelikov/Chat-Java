package com.sap.chatserver.config.guice;

import java.nio.file.Path;

import com.google.inject.name.Named;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.exception.DatabaseInitException;

public interface DbServiceFactory {

	@Named("DbServiceFile")
	DbService createDbServiceFile(Path path) throws DatabaseInitException;

	// @Named("DbServiceHibernate")
	// DbService createDbServiceHibernate();
}
