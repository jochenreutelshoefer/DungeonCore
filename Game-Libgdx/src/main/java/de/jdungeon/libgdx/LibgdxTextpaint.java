package de.jdungeon.libgdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.jdungeon.game.TextPaint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.12.19.
 */
public class LibgdxTextpaint implements TextPaint {

	private final BitmapFont font;

	public LibgdxTextpaint(BitmapFont font) {
		this.font = font;
	}

	public BitmapFont getFont() {
		return font;
	}
}
