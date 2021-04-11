package de.jdungeon.game;

import de.jdungeon.user.Session;

public interface Game {

	Audio getAudio();

	Input getInput();

	FileIO getFileIO();

	Graphics getGraphics(ScreenContext context);

	void setCurrentScreen(Screen screen);

	Screen getCurrentScreen();

	Screen getInitScreen();

	int getScreenWidth();

	int getScreenHeight();

	Configuration getConfiguration();

	/**
	 * Returns the current session of the player.
	 *
	 * @return the de.jdungeon.user representing the current player
	 */
	Session getSession();

	Logger getLogger();
}

