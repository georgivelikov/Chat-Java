package com.sap.chatserver.db.core;

import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.db.entity.UserRole;
import com.sap.chatserver.exception.DatabaseConnectionException;

public interface DbService {

	void addUser(String username, String password, UserRole role) throws DatabaseConnectionException;

	User findUserByUsername(String username) throws DatabaseConnectionException;
}
