package de.jdungeon.game;

import java.util.HashMap;
import java.util.Map;

import de.jdungeon.game.Configuration;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxConfiguration implements Configuration {

	private static Map<String, String> staticValues;

	static {
		staticValues = new HashMap<>();
		staticValues.put(Configuration.AUDIO_ON, "true");
	}

	private Map<String, String> values = new HashMap<>();

	public LibgdxConfiguration() {
		this.values = new HashMap<>();
		this.values.putAll(staticValues);
	}

	public LibgdxConfiguration(Map<String, String> values) {
		this.values = new HashMap<>(values);
		this.values.putAll(staticValues);

	}

	@Override
	public String getValue(String attribute) {
		return values.get(attribute);
	}

	@Override
	public GUIStyle getGUIStyle() {
		return GUIStyle.simple;
	}
}
