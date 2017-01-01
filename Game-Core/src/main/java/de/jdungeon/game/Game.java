package de.jdungeon.game;

import de.jdungeon.user.Session;

public interface Game {

		Audio getAudio();

		Input getInput();

	    FileIO getFileIO();

	    Graphics getGraphics();

	    void setScreen(Screen screen);

	    Screen getCurrentScreen();

	    Screen getInitScreen();

		Logger getLogger();

		int getScreenWidth();

		int getScreenHeight();

	Configuration getConfiguration();

	/**
	 * Returns the current session of the player.
	 *
	 * @return the user representing the current player
	 */
		Session getSession();
	}

