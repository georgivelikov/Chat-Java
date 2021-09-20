package com.sap.chatcommon;

import java.rmi.RemoteException;

public class WriterWorker extends Thread {

	Conversation conversation;
	boolean isInitial;
	
	WriterWorker(Conversation conversation, boolean isInitial) {
		this.conversation = conversation;
		this.isInitial = isInitial;
	}
	
	@Override
	public void run() {
		while(true) {
			String message = null;
			if(isInitial) {
				try {
					message = conversation.receiveMessageFirst();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					message = conversation.receiveMessageSecond();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println(message);
		}
	}
}
