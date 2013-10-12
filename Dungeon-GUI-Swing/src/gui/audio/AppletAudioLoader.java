package gui.audio;

import java.applet.Applet;

import audio.AbstractAudioSet;
import audio.AudioLoader;

public class AppletAudioLoader implements AudioLoader {

	private Applet applet;
	
	public AppletAudioLoader(Applet applet) {
		this.applet = applet;
	}
	
	@Override
	public AbstractAudioSet createAudioSet(String[] filenames) {
		AudioSet set = new AudioSet(applet);
		for (int i = 0; i < filenames.length; i++) {
			set.addClip(filenames[i]);
		}
		return set;
	}

}
