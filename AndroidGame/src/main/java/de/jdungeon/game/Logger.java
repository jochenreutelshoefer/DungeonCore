package de.jdungeon.game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.08.16.
 */
public interface Logger {

	void info(String message);

	void warning(String message);

	void error(String message);

	void severe(String message);

	void warning(String message, Throwable e);

	void error(String message, Throwable e);

	void severe(String message, Throwable e);



}
