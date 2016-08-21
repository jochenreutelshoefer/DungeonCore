package de.jdungeon.androidapp.io;

import java.io.IOException;

import android.content.res.AssetManager;
import audio.AbstractAudioSet;
import audio.AudioLoader;

import de.jdungeon.androidapp.JDungeonApp;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Sound;

public class AndroidAudioLoader implements AudioLoader {

	private final Audio audio;
	private JDungeonApp game;

	public AndroidAudioLoader(Audio audio, JDungeonApp game) {
		this.audio = audio;
		this.game = game;
	}

	@Override
	public AbstractAudioSet createAudioSet(String[] files) {
		AndroidAudioSet set = new AndroidAudioSet();
		for (String file : files) {
			String fullFilename = "sounds/" + file;
			if(fileExists(fullFilename)) {
				Sound sound = audio.createSound(fullFilename);
				if (sound != null) {
					set.addSound(sound);
				}
			}
		}
		return set;
	}


	public boolean fileExists(String file) {
		AssetManager mg = game.getResources().getAssets();
		try {
			mg.open(file);

		} catch (IOException ex) {
			return false;
		}
		return true;
	}
}
