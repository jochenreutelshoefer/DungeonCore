package de.jdungeon.androidapp.audio;

import de.jdungeon.game.AbstractAudioSet;
import de.jdungeon.game.AudioLoader;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 02.01.17.
 */
public class AudioManagerTouchGUI {

	private static boolean initialized = false;

	public static AbstractAudioSet TOUCH1;
	public static AbstractAudioSet JAM;

	public static void init(AudioLoader a) {

		if (!initialized) {
			initialized = true;
			//TOUCH1 = a.createAudioSet(new String[] { "smash.wav"});
			TOUCH1 = a.createAudioSet(new String[] { "touchscreen-05.mp3"});
			JAM = a.createAudioSet(new String[] { "jam.mp3"});
		}
	}

	public static void playSound(AbstractAudioSet set) {
		if (set != null) {
			set.playRandomSound();
		}
	}

}
