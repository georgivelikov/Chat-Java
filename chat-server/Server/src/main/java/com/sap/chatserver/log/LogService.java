package com.sap.chatserver.log;

import com.sap.chatserver.common.Message;

public interface LogService {
	
	void setLogLevel(LogLevel log);
	
	LogLevel getLogLevel();
	
	void logInfo(String message);
	
	void logInfo(String message, Exception e);
	
	void logInfo(Throwable t);
	
	void logInfo(String message, Throwable t);

	void logInfo(Message message, Throwable t);
	
	void logInfo(Message message);

	void logError(String message);
	
	void logError(String message, Exception e);

	void logError(String message, Throwable t);
	
	void logError(Throwable t);
	
	void logError(Message message, Throwable t);
	
	void logError(Message message);
			
	void logWarn(String message);
	
	void logWarn(String message, Exception e);

	void logWarn(String message, Throwable t);
	
	void logWarn(Throwable t);
	
	void logWarn(Message message, Throwable t);
	
	void logWarn(Message message);
	
	void logDebug(String message);
	
	void logDebug(String message, Exception e);

	void logDebug(String message, Throwable t);
	
	void logDebug(Throwable t);
	
	void logDebug(Message message, Throwable t);
	
	void logDebug(Message message);

	void logFatal(String message);
	
	void logFatal(String message, Exception e);

	void logFatal(String message, Throwable t);
	
	void logFatal(Throwable t);
	
	void logFatal(Message message, Throwable t);
	
	void logFatal(Message message);
	
	void logAll(String message);
	
	void logAll(String message, Exception e);

	void logAll(String message, Throwable t);
	
	void logAll(Throwable t);
	
	void logAll(Message message, Throwable t);
	
	void logAll(Message message);

	void logTrace(String message);
	
	void logTrace(String message, Exception e);

	void logTrace(String message, Throwable t);
	
	void logTrace(Throwable t);
	
	void logTrace(Message message, Throwable t);
	
	void logTrace(Message message);
}
