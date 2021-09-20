package com.sap.chatserver.db.core;

import java.util.Map;

import com.sap.chatserver.exception.DatabaseInitException;

public interface DbConnector {

	DbService generateDbServiceFile(Map<String, String> properties) throws DatabaseInitException;

	DbService generateDbServiceHibernate(Map<String, String> properties) throws DatabaseInitException;
}
