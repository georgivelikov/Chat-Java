package com.sap.chatserver.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.sap.chatserver.common.Message;

public class LogServiceLog4J implements LogService {

	Logger logger = LogManager.getRootLogger();

	@Override
	public void setLogLevel(LogLevel level) {
		switch (level) {
		case TRACE:
			Configurator.setRootLevel(Level.TRACE);
			break;
		case DEBUG:
			Configurator.setRootLevel(Level.DEBUG);
			break;
		case INFO:
			Configurator.setRootLevel(Level.INFO);
			break;
		case WARN:
			Configurator.setRootLevel(Level.WARN);
			break;
		case ERROR:
			Configurator.setRootLevel(Level.ERROR);
			break;
		case FATAL:
			Configurator.setRootLevel(Level.FATAL);
			break;
		case ALL:
			Configurator.setRootLevel(Level.ALL);
			break;
		case OFF:
			Configurator.setRootLevel(Level.OFF);
			break;
		default:
			Configurator.setRootLevel(Level.ALL);
			break;
		}
	}

	@Override
	public LogLevel getLogLevel() {
		return LogLevel.valueOf(logger.getLevel().name());
	}

	@Override
	public void logInfo(String message) {
		logger.log(Level.INFO, message);
	}

	@Override
	public void logInfo(String message, Exception e) {
		logger.log(Level.INFO, message, e);
	}

	@Override
	public void logInfo(String message, Throwable t) {
		logger.log(Level.INFO, message, t);
	}

	@Override
	public void logInfo(Throwable t) {
		logger.log(Level.INFO, t);
	}

	@Override
	public void logInfo(Message message, Throwable t) {
		logger.log(Level.INFO, message.toString(), t);
	}

	@Override
	public void logInfo(Message message) {
		logger.log(Level.INFO, message.toString());
	}

	@Override
	public void logError(String message) {
		logger.log(Level.ERROR, message);
	}

	@Override
	public void logError(String message, Exception e) {
		logger.log(Level.ERROR, message, e);
	}

	@Override
	public void logError(String message, Throwable t) {
		logger.log(Level.ERROR, message, t);
	}

	@Override
	public void logError(Throwable t) {
		logger.log(Level.ERROR, t);
	}

	@Override
	public void logError(Message message, Throwable t) {
		logger.log(Level.ERROR, message.toString(), t);
	}

	@Override
	public void logError(Message message) {
		logger.log(Level.ERROR, message.toString());
	}

	@Override
	public void logWarn(String message) {
		logger.log(Level.WARN, message);
	}

	@Override
	public void logWarn(String message, Exception e) {
		logger.log(Level.WARN, message, e);
	}

	@Override
	public void logWarn(String message, Throwable t) {
		logger.log(Level.WARN, message, t);
	}

	@Override
	public void logWarn(Throwable t) {
		logger.log(Level.WARN, t);
	}

	@Override
	public void logWarn(Message message, Throwable t) {
		logger.log(Level.WARN, message.toString(), t);
	}

	@Override
	public void logWarn(Message message) {
		logger.log(Level.WARN, message.toString());
	}

	@Override
	public void logDebug(String message) {
		logger.log(Level.DEBUG, message);
	}

	@Override
	public void logDebug(String message, Exception e) {
		logger.log(Level.DEBUG, message, e);
	}

	@Override
	public void logDebug(String message, Throwable t) {
		logger.log(Level.DEBUG, message, t);
	}

	@Override
	public void logDebug(Throwable t) {
		logger.log(Level.DEBUG, t);
	}

	@Override
	public void logDebug(Message message, Throwable t) {
		logger.log(Level.DEBUG, message.toString(), t);
	}

	@Override
	public void logDebug(Message message) {
		logger.log(Level.DEBUG, message.toString());
	}

	@Override
	public void logFatal(String message) {
		logger.log(Level.FATAL, message);
	}

	@Override
	public void logFatal(String message, Exception e) {
		logger.log(Level.FATAL, message, e);
	}

	@Override
	public void logFatal(String message, Throwable t) {
		logger.log(Level.FATAL, message, t);
	}

	@Override
	public void logFatal(Throwable t) {
		logger.log(Level.FATAL, t);
	}

	@Override
	public void logFatal(Message message, Throwable t) {
		logger.log(Level.FATAL, message.toString(), t);
	}

	@Override
	public void logFatal(Message message) {
		logger.log(Level.FATAL, message.toString());
	}

	@Override
	public void logAll(String message) {
		logger.log(Level.ALL, message);
	}

	@Override
	public void logAll(String message, Exception e) {
		logger.log(Level.ALL, message, e);
	}

	@Override
	public void logAll(String message, Throwable t) {
		logger.log(Level.ALL, message, t);
	}

	@Override
	public void logAll(Throwable t) {
		logger.log(Level.ALL, t);
	}

	@Override
	public void logAll(Message message, Throwable t) {
		logger.log(Level.ALL, message.toString(), t);
	}

	@Override
	public void logAll(Message message) {
		logger.log(Level.ALL, message.toString());
	}

	@Override
	public void logTrace(String message) {
		logger.log(Level.TRACE, message);
	}

	@Override
	public void logTrace(String message, Exception e) {
		logger.log(Level.TRACE, message, e);
	}

	@Override
	public void logTrace(String message, Throwable t) {
		logger.log(Level.TRACE, message, t);
	}

	@Override
	public void logTrace(Throwable t) {
		logger.log(Level.TRACE, t);
	}

	@Override
	public void logTrace(Message message, Throwable t) {
		logger.log(Level.TRACE, message.toString(), t);
	}

	@Override
	public void logTrace(Message message) {
		logger.log(Level.TRACE, message.toString());
	}
}
