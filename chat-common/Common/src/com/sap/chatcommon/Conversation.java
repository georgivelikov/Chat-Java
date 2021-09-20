package com.sap.chatcommon;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Conversation extends Remote {
	
	String getFirstUsername() throws RemoteException;
	
	String getSecondUsername() throws RemoteException;
	
	void sendMessageToFirst(String value) throws RemoteException, InterruptedException;
	
	void sendMessageToSecond(String value) throws RemoteException, InterruptedException;
	
	String getInitialMessage() throws RemoteException;
	
	String receiveMessageFirst() throws RemoteException;
	
	String receiveMessageSecond() throws RemoteException;
}
