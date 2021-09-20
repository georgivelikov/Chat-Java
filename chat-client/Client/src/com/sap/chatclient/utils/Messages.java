package com.sap.chatclient.utils;

import java.util.Arrays;

public class Messages {

	public static final char STAR_SYMBOL = '*';
	public static final char DASH_SYMBOL = '-';
	public static final char EMPTY_SYMBOL = ' ';

	public static final String WELCOME_MESSAGE = " CHAT CLIENT ";
	public static final String ENTER_HOST = "Enter 'connect <hostname> <port>' to connect to specific server.";
	public static final String ENTER_HOST_DEFAULT = "Enter 'connect' to connect to default server.";
	public static final String EXIT_MESSAGE = "Enter 'exit' to close the application.";
	public static final String INFO_MESSAGE = "Enter 'help' to see available commands.";
	public static final String UKNOWN_COMMAND = "Unknown command. " + INFO_MESSAGE;
	public static final String IVALID_ARGS_FOR_CONNECT_COMMAND = "Invalid arguments count for 'connect' command.";
	public static final String INVALID_PORT_FORMAT = "Port is not in valid format.";
	public static final String SERVER_NOT_AVAILABLE = "Server is not available!";
	public static final String TRYING_TO_RECONNECT = "Trying to reconnect after %s seconds.";
	public static final String RECONNECT_FAILED = "Reconnect failed after %s tries. Try to connect later.";
	public static final String RECONNECT_SUCCESSFUL = "Reconnect successful. You may continue.";
	public static final String CONNECTION_SUCCESSFUL = "Connection successful!";
	public static final String UNKNOWN_HOST = "Unknown host!";
	public static final String LOGIN_SUCCESSFUL = "Login successful!";
	public static final String LOGOUT_SUCCESSFUL = "Logout successful!";
	public static final String INVALID_INPUT = "Invalid input!";
	public static final String MESSAGE_RECEIVED = "Message received.";
	public static final String MESSAGE_NOT_RECEIVED = "Message not received.";
	public static final String USER_OFFLINE = "User is not online.";

	public static String buildWelcomeMessage() {
		int length = (Constants.ROW_SIZE - WELCOME_MESSAGE.length()) / 2;
		String starsString = buildSpecialString(STAR_SYMBOL, length);
		StringBuilder sb = new StringBuilder();
		return sb.append(starsString).append(WELCOME_MESSAGE)
				.append(starsString).append(Constants.NEW_LINE)
				.append(INFO_MESSAGE).append(Constants.NEW_LINE)
				.toString();
	}

	public static String buildInfoMessage() {
		StringBuilder sb = new StringBuilder();
		return sb.append(ENTER_HOST).append(Constants.NEW_LINE)
				.append(ENTER_HOST_DEFAULT).append(Constants.NEW_LINE)
				.append(EXIT_MESSAGE).append(Constants.NEW_LINE).toString();
	}

	private static String buildSpecialString(char symbol, int length) {
		char[] starsArray = new char[length];
		Arrays.fill(starsArray, symbol);
		return new String(starsArray);
	}
}
