package com.sap.chatserver.config.server;

import java.util.Map;

import com.sap.chatserver.exception.ServerConfigurationException;

public interface ServerConfigParser {

	Map<String, String> getProperties(String location) throws ServerConfigurationException;
}
