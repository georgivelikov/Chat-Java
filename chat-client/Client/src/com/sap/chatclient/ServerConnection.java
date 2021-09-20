package com.sap.chatclient;

import java.io.IOException;

import com.sap.chatclient.utils.Constants;
import com.sap.chatclient.utils.Messages;

public class ServerConnection extends Thread {

	private final Client client;

	public ServerConnection(Client client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String serverMessage = client.readMessageFromServer();
				if(serverMessage == null) {
					break;
				}
				
				if(serverMessage.startsWith("--")) {
					handlePrivateChat(serverMessage);
					continue;
				}

				System.out.println(serverMessage);
				System.out.print(Constants.POINTER);
			}
		} catch (ConnectionException e) {
			System.out.println(Messages.SERVER_NOT_AVAILABLE);
		}
	}

	private void handlePrivateChat(String message) {
		try {
			String[] args = message.split(" ");
			String objectIdentifier = args[0];
			String isInitial = args[1];
			String registryIp = client.getServerAddress();
			String registryPort = args[2];
			String jarStartCommand = "start java -jar common.jar";

			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", String.format("%s %s %s %s %s", jarStartCommand, objectIdentifier, isInitial, registryIp, registryPort));
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
