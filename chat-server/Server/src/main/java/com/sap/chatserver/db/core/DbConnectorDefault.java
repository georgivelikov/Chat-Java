package com.sap.chatserver.db.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.google.inject.Inject;
import com.sap.chatserver.config.guice.DbServiceFactory;
import com.sap.chatserver.config.server.ConstantConfig;
import com.sap.chatserver.config.server.MessageConfig;
import com.sap.chatserver.db.file.impl.MessageDbFile;
import com.sap.chatserver.exception.DatabaseInitException;

public class DbConnectorDefault implements DbConnector {

	private DbServiceFactory dbServiceFactory;

	@Inject
	public DbConnectorDefault(DbServiceFactory dbServiceFactory) {
		this.dbServiceFactory = dbServiceFactory;
	}

	@Override
	public DbService generateDbServiceFile(Map<String, String> properties) throws DatabaseInitException {
		String databaseSource = properties.get(ConstantConfig.CONFIG_DB_SOURCE);
		if (databaseSource == null) {
			throw new DatabaseInitException(
					String.format(MessageConfig.PROP_MISSING_EXC.getContent(), ConstantConfig.CONFIG_DB_SOURCE));
		}

		Path path = Paths.get(databaseSource);

		if (!Files.exists(path)) {
			throw new DatabaseInitException(
					String.format(MessageDbFile.DB_FILE_NOT_IN_PLACE.getContent(), databaseSource));
		}

		return dbServiceFactory.createDbServiceFile(path);
	}

	@Override
	public DbService generateDbServiceHibernate(Map<String, String> properties) {
		// TODO Auto-generated method stub
		return null;
	}

}
