package com.sap.chatcommon;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConversationEngine {

	public static final String MESSAGE_FORMAT = "[%s]%s -> %s: %s";
	public static final String TIMESTAMP_FORMAT = "HH:mm:ss";
	public Scanner sc;
	private DateTimeFormatter formatter;

	public ConversationEngine() {
		this.sc = new Scanner(System.in);
		this.formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
	}

	public void run(String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String objectIdentifier = args[0];
		String registryIp = args[2];
		int port = Integer.parseInt(args[3]);
		Conversation conversation = getConversationObject(objectIdentifier, registryIp, port);

		boolean isInitial = Boolean.parseBoolean(args[1]);
		if (isInitial) {
			startFirstToSecondConversation(conversation);
		} else {
			startSecondToFirstConversation(conversation);
		}
	}

	public Conversation getConversationObject(String objectIdentifier, String registryIp, int port) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(registryIp, port);
		String stubName = "Conversation" + objectIdentifier;
		return (Conversation) registry.lookup(stubName);
	}

	public void startFirstToSecondConversation(Conversation conversation) throws RemoteException, InterruptedException {
		System.out.println(conversation.getFirstUsername().toUpperCase() + " -------> "
				+ conversation.getSecondUsername().toUpperCase());
		String initialMessageFormatted = formatMessage(conversation.getFirstUsername(),
				conversation.getSecondUsername(), conversation.getInitialMessage());
		System.out.println(initialMessageFormatted);

		WriterWorker writer = new WriterWorker(conversation, true);
		writer.start();

		while (true) {
			String message = sc.nextLine();
			String formattedMessage = formatMessage(conversation.getFirstUsername(), conversation.getSecondUsername(), message);
			System.out.println(formattedMessage);
			conversation.sendMessageToSecond(formattedMessage);
		}
	}

	public void startSecondToFirstConversation(Conversation conversation) throws RemoteException, InterruptedException {
		System.out.println(conversation.getSecondUsername().toUpperCase() + " -------> "
				+ conversation.getFirstUsername().toUpperCase());
		String initialMessageFormatted = formatMessage(conversation.getFirstUsername(),
				conversation.getSecondUsername(), conversation.getInitialMessage());
		System.out.println(initialMessageFormatted);

		WriterWorker writer = new WriterWorker(conversation, false);
		writer.start();
		
		while (true) {
			String message = sc.nextLine();
			String formattedMessage = formatMessage(conversation.getSecondUsername(), conversation.getFirstUsername(), message);
			System.out.println(formattedMessage);
			conversation.sendMessageToFirst(formattedMessage);
		}
	}

	private String formatMessage(String senderUsername, String receiverUsername, String message) {
		return String.format(MESSAGE_FORMAT, getFormattedTimeStamp(), senderUsername, receiverUsername, message);
	}

	private String getFormattedTimeStamp() {
		LocalTime time = LocalTime.now();
		return time.format(formatter);
	}
}
