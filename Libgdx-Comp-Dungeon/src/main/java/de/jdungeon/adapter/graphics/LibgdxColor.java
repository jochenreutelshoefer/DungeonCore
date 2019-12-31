package de.jdungeon.adapter.graphics;

import de.jdungeon.game.Color;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxColor implements Color {

	private final com.badlogic.gdx.graphics.Color color;

	public LibgdxColor(com.badlogic.gdx.graphics.Color color) {
		this.color = color;
	}

	public com.badlogic.gdx.graphics.Color getColor() {
		return color;
	}
}
