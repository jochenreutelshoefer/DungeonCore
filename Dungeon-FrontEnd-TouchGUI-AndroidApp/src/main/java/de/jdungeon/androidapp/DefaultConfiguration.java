package de.jdungeon.androidapp;

public class DefaultConfiguration implements Configuration {


	private static final String AUDIO_ON_DEFAULT = "true";
	
	@Override
	public String getValue(String attribute) {
		if (attribute.equals(AUDIO_ON))
			return AUDIO_ON_DEFAULT;
		
		return null;
	}
	
}
