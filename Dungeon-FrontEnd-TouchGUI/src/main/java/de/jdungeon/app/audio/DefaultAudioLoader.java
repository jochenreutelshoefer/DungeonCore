package de.jdungeon.app.audio;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import de.jdungeon.app.audio.AudioSet;
import de.jdungeon.game.AbstractAudioSet;
import de.jdungeon.game.Audio;
import de.jdungeon.game.AudioLoader;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Game;
import de.jdungeon.game.Sound;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class DefaultAudioLoader implements AudioLoader {

	private final Audio audio;
	private final Game game;

	public DefaultAudioLoader(Audio audio, Game game) {
		this.audio = audio;
		this.game = game;
	}

	@Override
	public AbstractAudioSet createAudioSet(String[] files) {
		if (files.length == 0) {
			Logger.getLogger(this.getClass()).warn( "Empty list of sound files on audio set initialization!");
		}
		AudioSet set = new AudioSet();
		for (String file : files) {
			String fullFilename = "sounds/" + file;
			if(fileExists(fullFilename)) {
				Sound sound = audio.createSound(fullFilename);
				if (sound != null) {
					set.addSound(sound);
				} else {
					Logger.getLogger(this.getClass()).warn( "Sound could not be created: "+file);
				}

			} else {
				Logger.getLogger(this.getClass()).warn( "Sound file not found: "+fullFilename);
			}
		}
		return set;
	}


	public boolean fileExists(String file) {
		FileIO fileIO = game.getFileIO();
		try {
			InputStream inputStream = fileIO.readFile(file);
			inputStream.close();
		} catch (IOException ex) {
			return false;
		}
		return true;
	}
}

