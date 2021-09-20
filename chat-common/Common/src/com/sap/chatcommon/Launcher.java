package com.sap.chatcommon;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Launcher {

	public static void main(String[] args) {
		ConversationEngine engine = new ConversationEngine();
		try {
			engine.run(args);
		} catch (RemoteException | NotBoundException | InterruptedException e) {
			e.printStackTrace();
			System.out.println(e);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	} 
}
