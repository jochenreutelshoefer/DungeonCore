package de.jdungeon.app.audio;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.game.AbstractAudioSet;
import de.jdungeon.game.Sound;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class AudioSet implements AbstractAudioSet {

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
		int randomSoundIndex = (int) (Math.random() * sounds.size());
		Sound sound = sounds.get(randomSoundIndex);
		sound.play(new Float(1.0).floatValue());
		sound.dispose();

	}

	@Override
	public List<Sound> getAllSounds() {
		return sounds;
	}

}

