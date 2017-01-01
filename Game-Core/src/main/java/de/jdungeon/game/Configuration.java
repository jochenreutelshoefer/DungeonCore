package de.jdungeon.game;

public interface Configuration {
	
	static final String AUDIO_ON = "audio_on";

	String getValue(String attribute);

}
