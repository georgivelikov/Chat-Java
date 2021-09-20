package com.sap.chatserver.common;

import java.util.Arrays;

public enum MessageCommon implements Message {

	HELP_INFO("Type 'help' to display information about all available commands."),
	LOGIN_FIRST("You must log in first!"),
	UNKNOWN_COMMAND("Unknown command! " + HELP_INFO),
	INVALID_ARGUMENTS_COUNT("Invalid arguments count for %s command!");

	private static final String WELCOME_MESSAGE = " W E L C O M E ";
	private static final char STAR_SYMBOL = '*';
	private final String content;
	private int code;
	
	private MessageCommon(String content) {
		this.content = content;
		this.code = 1;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return content;
	}
	
	
	public static String buildWelcomeMessage() {
		int headerStarsLength = (ConstantCommon.ROW_SIZE - WELCOME_MESSAGE.length()) / 2;
		String headerStarsString = buildSpecialString(STAR_SYMBOL, headerStarsLength);
		StringBuilder sb = new StringBuilder();
		return sb.append(headerStarsString).append(WELCOME_MESSAGE).append(headerStarsString).append(ConstantCommon.NEW_LINE)
				.append(HELP_INFO).append(ConstantCommon.NEW_LINE).toString();
	}

	private static String buildSpecialString(char symbol, int length) {
		char[] starsArray = new char[length];
		Arrays.fill(starsArray, symbol);
		return new String(starsArray);
	}
}
