package com.sap.chatclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sap.chatclient.utils.Constants;
import com.sap.chatclient.utils.Messages;

public class Client {

	private String serverAddress;
	private int serverPort;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private InputStreamReader inputStreamReader;
	private OutputStreamWriter outputStreamWriter;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private ExecutorService executor;
	private Scanner consoleReader;

	public Client() {
		this.consoleReader = new Scanner(System.in);
		this.executor = Executors.newCachedThreadPool();
	}

	public void start() {
		String input = null;

		System.out.println(Messages.buildWelcomeMessage());
		boolean isRunning = true;
		while (isRunning) {
			System.out.print(Constants.POINTER);
			input = consoleReader.nextLine();

			if (input.trim().equals("")) {
				continue;
			}

			String[] commandArgs = input.split(" ");

			String commandType = commandArgs[0].toLowerCase();

			switch (commandType) {
			case "connect":
				handleConnectCommand(commandArgs);
				break;
			case "exit":
				isRunning = false;
				break;
			case "help":
				System.out.println(Messages.buildInfoMessage());
				break;
			default:
				System.out.println(Messages.UKNOWN_COMMAND);
				break;
			}
		}

		this.executor.shutdown();
	}

	public void writeMessageToServer(String message) throws ConnectionException {
		try {
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException ioExc) {
			throw new ConnectionException("Error writing to server", ioExc);
		}
	}

	public String readMessageFromServer() throws ConnectionException {
		try {
			StringBuilder sb = new StringBuilder();
			String line = bufferedReader.readLine();
			if(line == null) {
				return line;
			}
			
			sb.append(line);
			
			while(bufferedReader.ready()) {
				line = Constants.NEW_LINE + bufferedReader.readLine();
				sb.append(line);
			}
			
			return sb.toString();
		} catch (IOException ioExc) {
			throw new ConnectionException("Error reading from server", ioExc);
		}
	}

	public boolean tryReconnect(String serverAddress, int serverPort) {
		for (int i = 0; i < Constants.RECONNECT_TRY_COUNT; i++) {
			System.out.println(String.format(Messages.TRYING_TO_RECONNECT, Constants.RECONNECT_SECONDS));
			for (int j = Constants.RECONNECT_SECONDS; j > 0; j--) {
				System.out.println(j);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (connect(serverAddress, serverPort)) {
				return true;
			}
		}

		return false;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	
	private void handleConnectCommand(String[] commandArgs) {
		if (commandArgs.length == 1) {
			serverAddress = Constants.DEFAULT_SERVER_ADDRESS;
			serverPort = Constants.DEFAULT_SERVER_PORT;
		} else if (commandArgs.length == 3) {
			serverAddress = commandArgs[1];
			try {
				serverPort = Integer.parseInt(commandArgs[2]);
			} catch (NumberFormatException e) {
				System.out.println(Messages.INVALID_PORT_FORMAT);
				return;
			}
		} else {
			System.out.println(Messages.IVALID_ARGS_FOR_CONNECT_COMMAND);
			return;
		}

		boolean isConnected = connect(serverAddress, serverPort);
		if (isConnected) {
			handleConnection(serverAddress, serverPort);
		}
	}

	private boolean connect(String serverAddress, int serverPort) {
		try {
			socket = new Socket(serverAddress, serverPort);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			bufferedReader = new BufferedReader(inputStreamReader);
			bufferedWriter = new BufferedWriter(outputStreamWriter);
		} catch (UnknownHostException e) {
			System.out.println(Messages.UNKNOWN_HOST);
			return false;
		} catch (IOException e) {
			System.out.println(Messages.SERVER_NOT_AVAILABLE);
			return false;
		}

		return true;
	}

	private void handleConnection(String serverAddress, int serverPort) {
		executor.execute(new ServerConnection(this));
		String input;
		while (true) {
			input = consoleReader.nextLine();
			if (input.trim().equals("")) {
				System.out.print(Constants.POINTER);
				continue;
			}

			try {
				writeMessageToServer(input);
			} catch (ConnectionException e) {
				System.out.println(Messages.SERVER_NOT_AVAILABLE);

				boolean reconnectSuccessful = tryReconnect(serverAddress, serverPort);
				if (reconnectSuccessful) {
					System.out.println(Messages.RECONNECT_SUCCESSFUL);
					executor.execute(new ServerConnection(this));
					continue;
				} else {
					System.out.println(String.format(Messages.RECONNECT_FAILED, Constants.RECONNECT_TRY_COUNT));
					break;
				}
			}

			if (input.equalsIgnoreCase(Constants.QUIT_COMMAND)) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			}
		}

		System.out.println(Messages.buildWelcomeMessage());
	}
}
