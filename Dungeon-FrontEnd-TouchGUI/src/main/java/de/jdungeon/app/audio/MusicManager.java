package de.jdungeon.app.audio;

import de.jdungeon.game.Music;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 29.12.17.
 */
public class MusicManager {

	private static MusicManager instance;

	public static MusicManager getInstance() {
		if(instance == null) {
			instance = new MusicManager();
		}
		return instance;
	}

	Music current = null;

	public void playMusic(Music music) {
		if(current != null) {
			current.stop();
			current.dispose();
		}
		music.play();
		current = music;
	}

}
