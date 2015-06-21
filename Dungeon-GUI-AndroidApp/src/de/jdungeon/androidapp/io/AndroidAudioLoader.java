package de.jdungeon.androidapp.io;

import audio.AbstractAudioSet;
import audio.AudioLoader;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Sound;

public class AndroidAudioLoader implements AudioLoader {

	private final Audio audio;

	public AndroidAudioLoader(Audio audio) {
		this.audio = audio;
	}

	@Override
	public AbstractAudioSet createAudioSet(String[] files) {

		AndroidAudioSet set = new AndroidAudioSet();
		for (String file : files) {
			String fullFilename = "sounds/" + file;
			Sound sound = audio.createSound(fullFilename);
			if (sound != null) {
				set.addSound(sound);
			}
		}
		return set;
	}

}
