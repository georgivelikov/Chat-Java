package com.sap.chatserver.engine.command.impl;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.inject.Inject;
import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.common.MessageCommon;
import com.sap.chatserver.engine.command.Command;
import com.sap.chatserver.engine.command.message.MessageDump;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.log.LogService;

public class CommandDump implements Command {

	public static final int ARGS_COUNT = 1;
	public static final String NAME = "DUMP";
	public static final int MAX_DEPTH = 100;
	public static final String THREAD_DUMP_OUTPUT_DIRECTORY = "traces";
	public static final String THREAD_DUMP_OUTPUT_FILE_NAME = "trace.txt";
	public static final String THREAD_DUMP_OUTPUT = String.format("%s%s%s", THREAD_DUMP_OUTPUT_DIRECTORY,
			ConstantCommon.FILE_SEPARATOR, THREAD_DUMP_OUTPUT_FILE_NAME);
	public static final String THREAD_DUMP_HEADER_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	private LogService logService;

	@Inject
	public CommandDump(LogService logService) {
		this.logService = logService;
	}

	@Override
	public void execute(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (!validate(connection, commandArgs)) {
			return;
		}

		String threadDump = generateThreadDump();
		saveThreadDumpOnFileSystem(connection, threadDump);
	}

	private boolean validate(Connection connection, String[] commandArgs) throws ConnectionCommunicationException {
		if (commandArgs.length != ARGS_COUNT) {
			connection
					.writeMessage(String.format(MessageCommon.INVALID_ARGUMENTS_COUNT.getContent(), NAME));
			return false;
		}

		if (!connection.isLoggedIn()) {
			connection.writeMessage(MessageCommon.LOGIN_FIRST.getContent());
			return false;
		}

		if (!connection.checkAdminRights()) {
			connection.writeMessage(MessageDump.NO_PERMISSIONS.getContent());
			return false;
		}

		return true;
	}

	private String generateThreadDump() {
		StringBuilder dump = new StringBuilder();
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), MAX_DEPTH);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(THREAD_DUMP_HEADER_TIMESTAMP_FORMAT);
		LocalDateTime dateTime = LocalDateTime.now();
		String dateTimeString = dateTime.format(formatter);

		dump.append(
				String.format(MessageDump.HEADER_FORMAT.getContent(), dateTimeString) + ConstantCommon.NEW_LINE);
		for (ThreadInfo threadInfo : threadInfos) {
			dump.append('"').append(threadInfo.getThreadName()).append("\" ");
			Thread.State state = threadInfo.getThreadState();
			dump.append("\n		at java.lang.Thread.State: ");
			dump.append(state);
			StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
			for (StackTraceElement stackTraceElement : stackTraceElements) {
				dump.append("\n		at ");
				dump.append(stackTraceElement);
			}

			dump.append("\n");
		}

		return dump.toString();
	}

	private void saveThreadDumpOnFileSystem(Connection connection, String threadDump)
			throws ConnectionCommunicationException {
		Path path = Paths.get(THREAD_DUMP_OUTPUT);
		Path parentDir = path.getParent();

		if (Files.notExists(parentDir)) {
			try {
				Files.createDirectories(parentDir);
			} catch (IOException e) {
				logService.logError(String.format(MessageDump.PATH_GENERATION_FAILED.getContent(),
						THREAD_DUMP_OUTPUT_DIRECTORY));
				connection.writeMessage(MessageDump.FAILED_TO_GENERATE.getContent());
				return;
			}
		}

		if (Files.notExists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				logService.logError(String.format(MessageDump.PATH_GENERATION_FAILED.getContent(),
						THREAD_DUMP_OUTPUT_FILE_NAME));
				connection.writeMessage(MessageDump.FAILED_TO_GENERATE.getContent());
				return;
			}
		}

		try {
			Files.write(path, threadDump.getBytes(), StandardOpenOption.APPEND);
			connection.writeMessage(String.format(MessageDump.GENERATION_SUCCESSFUL.getContent(),
					THREAD_DUMP_OUTPUT_DIRECTORY, THREAD_DUMP_OUTPUT_FILE_NAME));
			logService.logInfo(String.format(MessageDump.GENERATION_SUCCESSFUL_LOGGING.getContent(),
					connection.getUser().getUsername()));
		} catch (IOException e) {
			logService.logError(
					String.format(MessageDump.PATH_GENERATION_FAILED.getContent(), THREAD_DUMP_OUTPUT_DIRECTORY));
			connection.writeMessage(MessageDump.FAILED_TO_GENERATE.getContent());
		}
	}

}
