package de.jdungeon.adapter.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxImage implements Image {

	private final String filename;
	private Texture texture;

	public LibgdxImage(String filename) {
		this.filename = filename;
	}

	private void init() {
		if(texture == null) {
			FileHandle handle = Gdx.files.internal(filename);
			texture = new Texture(handle);
		}
	}

	@Override
	public int getWidth() {
		init();
		return texture.getWidth();
	}

	@Override
	public int getHeight() {
		init();
		return texture.getHeight();
	}

	@Override
	public Graphics.ImageFormat getFormat() {
		return null;
	}


	public Texture getTexture() {
		init();
		return texture;
	}

	@Override
	public void dispose() {
		if(texture != null) {
			texture.dispose();
		}
	}
}
