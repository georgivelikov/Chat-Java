package com.sap.chatserver.common;

public class ConstantCommon {
	public static final int MIN_PORT_NUMBER = 0;
	public static final int MAX_PORT_NUMBER = (int) Math.pow(2, 16);
	public static final int DEFAULT_SERVER_PORT = 2000;
	public static final int MAX_ALLOWED_THREADS = 2;
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final int ROW_SIZE = 61;
	public static final String POINTER = ">";
}
