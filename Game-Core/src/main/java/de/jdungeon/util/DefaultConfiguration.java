package de.jdungeon.util;

import de.jdungeon.game.Configuration;

public class DefaultConfiguration implements Configuration {

	private static final String AUDIO_ON_DEFAULT = "true";
	
	public String getValue(String attribute) {
		if (attribute.equals(AUDIO_ON))
			return AUDIO_ON_DEFAULT;
		
		return null;
	}

	public GUIStyle getGUIStyle() {
		return GUIStyle.simple;
	}
}
