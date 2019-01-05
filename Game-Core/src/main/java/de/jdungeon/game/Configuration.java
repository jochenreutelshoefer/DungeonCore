package de.jdungeon.game;

public interface Configuration {
	
	String AUDIO_ON = "audio_on";
	String GUI_STYLE = "gui_style";

	String getValue(String attribute);


	GUIStyle getGUIStyle();

	enum GUIStyle {
		retro,
		simple;
	}

}
