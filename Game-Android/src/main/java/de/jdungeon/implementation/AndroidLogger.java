package de.jdungeon.implementation;

import android.util.Log;

import de.jdungeon.game.Logger;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.08.16.
 */
public class AndroidLogger implements Logger {

	private static final String dungeonTag = "dungeonTag";

	@Override
	public void info(String message) {
		Log.i(dungeonTag, message);
	}

	@Override
	public void warning(String message) {
		Log.w(dungeonTag, message);
	}

	@Override
	public void error(String message) {
		Log.e(dungeonTag, message);
	}

	@Override
	public void severe(String message) {
		Log.d(dungeonTag, message);
	}

	@Override
	public void warning(String message, Throwable e) {
		Log.w(dungeonTag, message, e);
	}

	@Override
	public void error(String message, Throwable e) {
		Log.e(dungeonTag, message, e);
	}

	@Override
	public void severe(String message, Throwable e) {
		Log.d(dungeonTag, message, e);
	}
}
