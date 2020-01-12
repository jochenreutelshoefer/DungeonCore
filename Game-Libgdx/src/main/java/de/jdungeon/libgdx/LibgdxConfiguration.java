package de.jdungeon.libgdx;

import java.util.HashMap;
import java.util.Map;

import de.jdungeon.game.Configuration;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxConfiguration implements Configuration {

	private static Map<String, String> values;

	static {
		values = new HashMap<>();
		values.put(Configuration.AUDIO_ON, "true");
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
