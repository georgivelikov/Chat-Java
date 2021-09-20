package com.sap.chatserver.engine.connection.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sap.chatserver.common.ConstantCommon;
import com.sap.chatserver.db.entity.User;
import com.sap.chatserver.db.entity.UserRole;
import com.sap.chatserver.engine.connection.Connection;
import com.sap.chatserver.engine.connection.MessageConnection;
import com.sap.chatserver.exception.ConnectionCloseException;
import com.sap.chatserver.exception.ConnectionCommunicationException;
import com.sap.chatserver.exception.ConnectionInitException;
import com.sap.chatserver.log.LogService;

public class ConnectionDefault implements Connection {
	private Socket clientSocket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private OutputStreamWriter outputStreamWriter;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private User user;
	private String address;
	private LogService logService;

	@Inject
	public ConnectionDefault(@Assisted Socket clientSocket, LogService logService)
			throws ConnectionInitException {
		this.clientSocket = clientSocket;
		this.address = String.format("%s:%s", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
		this.logService = logService;
		initializeStreams();
	}

	@Override
	public int getPort() {
		return this.clientSocket.getPort();
	}
	
	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean isConnected() {
		if (clientSocket == null) {
			return false;
		}

		return !clientSocket.isClosed();
	}

	@Override
	public boolean isLoggedIn() {
		return !(user == null);
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public boolean checkAdminRights() {
		if (user == null) {
			return false;
		}

		return user.getRole().equals(UserRole.ADMINISTRATOR);
	}
	
	@Override
	public void close() throws ConnectionCloseException {
		if (clientSocket == null || clientSocket.isClosed()) {
			logService.logInfo(MessageConnection.CLIENT_SOCKET_ALREADY_CLOSED.getContent());
			return;
		}

		try {
			clientSocket.close();
		} catch (IOException ioExc) {
			try {
				if(outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_OUT_STREAM_EXC_AFTER_SOCKET_CLOSE_EXC, e);
			}

			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_IN_STREAM_EXC_AFTER_SOCKET_CLOSE_EXC, e);
			}

			throw new ConnectionCloseException(String.format(MessageConnection.CLIENT_CONN_CLOSE_EXC.getContent(), address),
					ioExc);
		}
	}

	@Override
	public void writeMessage(String message) throws ConnectionCommunicationException {
		try {
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException ioExc) {
			try {
				if(outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_OUT_STREAM_CLOSE_EXC_AFTER_OUT_STREAM_EXC, e);
			}
			
			try {
				if(inputStream != null) {
					inputStream.close();
				}				
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_IN_STREAM_CLOSE_EXC_AFTER_OUT_STREAM_EXC, e);
			}
			
			try {
				if(clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_SOCKET_CLOSE_EXC_AFTER_OUT_STREAM_EXC, e);
			}
			
			throw new ConnectionCommunicationException(
					String.format(MessageConnection.CLIENT_CONN_WRITE_EXC.getContent(), address), ioExc);
		}
	}

	@Override
	public String readMessage() throws ConnectionCommunicationException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(bufferedReader.readLine());
			while(bufferedReader.ready()) {
				sb.append(ConstantCommon.NEW_LINE + bufferedReader.readLine());
			}
			
			return sb.toString();
		} catch (IOException ioExc) {
			try {
				if(outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_OUT_STREAM_CLOSE_EXC_AFTER_IN_STREAM_EXC, e);
			}
			
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_IN_STREAM_CLOSE_EXC_AFTER_IN_STREAM_EXC, e);
			}
			
			try {
				if(clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
				}
			} catch (IOException e) {
				logService.logError(MessageConnection.CLIENT_SOCKET_CLOSE_EXC_AFTER_IN_STREAM_EXC, e);
			}
			
			throw new ConnectionCommunicationException(
					String.format(MessageConnection.CLIENT_CONN_READ_EXC.getContent(), address), ioExc);
		}
	}

	private void initializeStreams() throws ConnectionInitException {
		boolean socketClose = true;
		boolean outputStreamClose = true;
		try {
			try {
				outputStream = clientSocket.getOutputStream();
			} catch (IOException e) {
				throw new ConnectionInitException(MessageConnection.CLIENT_OUTPUT_STREAM_INIT_EXCEPTION.getContent(), e);
			}

			try {
				inputStream = clientSocket.getInputStream();
			} catch (IOException e) {
				throw new ConnectionInitException(MessageConnection.CLIENT_INPUT_STREAM_INIT_EXCEPTION.getContent(), e);
			}
			
			outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			bufferedWriter = new BufferedWriter(outputStreamWriter);
			bufferedReader = new BufferedReader(inputStreamReader);
			socketClose = false;
			outputStreamClose = false;
		} finally {
			if (outputStreamClose) {
				try {
					outputStream.close();
				} catch (IOException ioExc) {
					logService.logError(ioExc);
				}
			}

			if (socketClose) {
				try {
					clientSocket.close();
				} catch (IOException ioExc) {
					logService.logError(ioExc);
				}
			}
		}
	}
}
