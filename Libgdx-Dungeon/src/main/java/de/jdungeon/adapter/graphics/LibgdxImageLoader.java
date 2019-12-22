package de.jdungeon.adapter.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxImageLoader implements AbstractImageLoader<Image> {

	@Override
	public Image loadImage(String filename) {
		if(! filename.startsWith(PREFIX)) {
			filename = PREFIX + filename;
		}
		return new LibgdxImage(new Texture(Gdx.files.internal(filename)));
	}

	@Override
	public boolean fileExists(String filename) {
		if(! filename.startsWith(PREFIX)) {
			filename = PREFIX + filename;
		}
		return Gdx.files.internal(filename).exists();
	}

}
