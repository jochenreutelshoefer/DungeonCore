package de.jdungeon.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import log.Log;

import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxFileImage implements Image {

	private final String filename;
	private Texture texture;

	public LibgdxFileImage(String filename) {
		this.filename = filename;
	}

	public LibgdxFileImage(String filename, Texture texture) {
		this.filename = filename;
		this.texture = texture;
	}

	private void init() {
		if(texture == null) {
			FileHandle handle = Gdx.files.internal(filename);
			Log.warning("Creating new File Texture: "+filename);
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
