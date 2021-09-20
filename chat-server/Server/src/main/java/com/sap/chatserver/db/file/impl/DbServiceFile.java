package com.sap.chatserver.db.file.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.db.core.DbService;
import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.db.entity.UserRole;
import com.sap.chatserver.exception.DatabaseConnectionException;
import com.sap.chatserver.log.LogService;

public class DbServiceFile implements DbService {

	private Path datasource;

	private HashMap<String, User> userCache;

	private LogService logService;

	@Inject
	public DbServiceFile(@Assisted Path datasource, LogService logService) throws DatabaseConnectionException {
		this.datasource = datasource;
		this.logService = logService;
		initializeUsersFromDatabase();
	}

	@Override
	public synchronized void addUser(String username, String password, UserRole role) throws DatabaseConnectionException {
		String userData = username + " " + password + " " + role.toString();
		try {
			Files.write(datasource, (userData + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
			User newUser = new UserFile(username, password, role);
			userCache.put(username, newUser);
		} catch (IOException e) {
			throw new DatabaseConnectionException(MessageDbFile.REGISTER_USERDATA_ON_FILE_FAILED.getContent(), e);
		}
	}

	@Override
	public synchronized User findUserByUsername(String username) {
		return userCache.get(username);
	}

	private void initializeUsersFromDatabase() throws DatabaseConnectionException {
		userCache = new HashMap<>();
		try {
			List<String> userData = Files.readAllLines(datasource);
			for (int i = 0; i < userData.size(); i++) {
				String[] userArgs = StringUtils.split(userData.get(i));
				String username = userArgs[0];
				String password = userArgs[1];
				UserRole userRole;
				try {
					userRole = UserRole.valueOf(userArgs[2].toUpperCase());
				} catch (IllegalArgumentException e) {
					logService.logWarn(String.format(MessageDbFile.CORRUPTED_USER_DATA.getContent(), i + 1));
					continue;
				}

				User user = new UserFile(username, password, userRole);
				userCache.put(username, user);
			}
		} catch (IOException e) {
			throw new DatabaseConnectionException(MessageDbFile.LOADING_USERS_FROM_DB_FILE_FAILED.getContent(), e);
		}

	}
}
