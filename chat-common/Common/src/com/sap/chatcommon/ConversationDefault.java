package com.sap.chatcommon;

import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ConversationDefault implements Conversation {

	private String firstUsername;
	private String secondUsername;
	private Queue<String> firstMessageQueue;
	private Queue<String> secondMessageQueue;
	private String firstLock;
	private String secondLock;
	private String initialMessage;
	
	public ConversationDefault(String firstUsername, String secondUsername, String initialMessage) {
		this.firstUsername = firstUsername;
		this.secondUsername = secondUsername;
		this.firstMessageQueue = new ArrayDeque<>();
		this.secondMessageQueue = new ArrayDeque<>();
		this.firstLock = "lock1";
		this.secondLock = "lock2";
		this.initialMessage = initialMessage;
	}
	
	@Override
	public String getFirstUsername() throws RemoteException {
		return firstUsername;
	}

	@Override
	public String getSecondUsername() throws RemoteException {
		return secondUsername;
	}

	@Override
	public String getInitialMessage() throws RemoteException {
		return initialMessage;
	}

	@Override
	public void sendMessageToFirst(String value) throws RemoteException, InterruptedException {
		synchronized (firstLock) {
			this.firstMessageQueue.add(value);
			this.firstLock.notify();
		}
		
		Thread.sleep(300);
	}

	@Override
	public void sendMessageToSecond(String value) throws RemoteException, InterruptedException {
		synchronized (secondLock) {
			this.secondMessageQueue.add(value);
			this.secondLock.notify();
		}
		
		Thread.sleep(300);
	}

	@Override
	public String receiveMessageFirst() throws RemoteException {
		synchronized (firstLock) {
			while(firstMessageQueue.isEmpty()) {
				try {
					firstLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return this.firstMessageQueue.poll();
		}
	}

	@Override
	public String receiveMessageSecond() throws RemoteException {
		synchronized (secondLock) {
			while(secondMessageQueue.isEmpty()) {
				try {
					secondLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return this.secondMessageQueue.poll();
		}
	}


	
}
