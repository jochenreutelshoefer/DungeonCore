package gui.audio;

import audio.AbstractAudioSet;
import audio.AudioLoader;

public class DefaultSwingAudioLoader implements AudioLoader {

	@Override
	public AbstractAudioSet createAudioSet(String[] filenames) {
		AudioSet set = new AudioSet(null);
		for (int i = 0; i < filenames.length; i++) {
			set.addClip(filenames[i]);
		}
		return set;
	}

}
