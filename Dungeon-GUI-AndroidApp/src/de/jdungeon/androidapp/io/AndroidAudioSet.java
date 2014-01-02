package de.jdungeon.androidapp.io;

import java.util.ArrayList;
import java.util.List;

import audio.AbstractAudioSet;
import de.jdungeon.game.Sound;

public class AndroidAudioSet implements AbstractAudioSet {

	private final List<Sound> sounds = new ArrayList<Sound>();

	public void addSound(Sound s) {
		sounds.add(s);
	}

	@Override
	public void playRandomSound() {
		System.out.println("playing sound!");
		Sound sound = sounds.get((int) (Math.random() * sounds.size()));
		sound.play(new Float(1.0).floatValue());

	}

}
