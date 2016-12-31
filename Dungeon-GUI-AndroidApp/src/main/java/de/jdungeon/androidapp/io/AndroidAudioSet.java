package de.jdungeon.androidapp.io;

import java.util.ArrayList;
import java.util.List;

import audio.AbstractAudioSet;

import de.jdungeon.androidapp.Assets;
import de.jdungeon.game.Sound;

public class AndroidAudioSet implements AbstractAudioSet {

	private final List<Sound> sounds = new ArrayList<Sound>();

	public void addSound(Sound s) {
		sounds.add(s);
	}

	@Override
	public void playRandomSound() {
		//System.out.println("playing sound!");
		if(sounds.size() == 0) {
			System.out.println("No sounds available for AudioSet");
			return;
		}
		if(!Assets.isAudioLoaded()) {
			System.out.println("Audio not finished loading yet");
		}
		int randomSoundIndex = (int) (Math.random() * sounds.size());
		Sound sound = sounds.get(randomSoundIndex);
		sound.play(new Float(1.0).floatValue());

	}

	@Override
	public List<Sound> getSounds() {
		return sounds;
	}

}
