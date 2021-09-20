package com.sap.chatserver.db.entity;

public interface User {

	public String getUsername();

	public void setUsername(String username);

	public String getPassword();

	public void setPassword(String password);

	public UserRole getRole();

	public void setRole(UserRole role);
}
