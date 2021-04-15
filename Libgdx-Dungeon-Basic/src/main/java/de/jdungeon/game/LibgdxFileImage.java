package de.jdungeon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import de.jdungeon.game.GameEnv;
import de.jdungeon.game.Logger;
import de.jdungeon.log.Log;

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
			String message = "Creating new File Texture: " + filename;
			Logger logger = GameEnv.getInstance().getGame().getLogger();
			logger.warning(this.getClass().getSimpleName(), message);
			Log.warning(message);
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
