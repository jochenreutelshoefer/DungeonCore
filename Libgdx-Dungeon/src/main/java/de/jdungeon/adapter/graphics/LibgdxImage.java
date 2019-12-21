package de.jdungeon.adapter.graphics;

import com.badlogic.gdx.graphics.Texture;

import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxImage implements Image {


	private final Texture texture;

	public LibgdxImage(Texture texture) {
		this.texture = texture;
	}

	@Override
	public int getWidth() {
		return texture.getWidth();
	}

	@Override
	public int getHeight() {
		return texture.getHeight();
	}

	@Override
	public Graphics.ImageFormat getFormat() {
		return null;
	}


	public Texture getTexture() {
		return texture;
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
