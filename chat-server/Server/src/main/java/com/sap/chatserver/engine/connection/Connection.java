package com.sap.chatserver.engine.connection;

import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;

public interface Connection {

	public int getPort();
	
	public User getUser();

	public void setUser(User user);

	public boolean isConnected();

	public String getAddress();
	
	public boolean isLoggedIn();
	
	public boolean checkAdminRights();

	public String readMessage() throws ConnectionCommunicationException;

	public void writeMessage(String message) throws ConnectionCommunicationException;

	public void close() throws ConnectionCloseException;
}
