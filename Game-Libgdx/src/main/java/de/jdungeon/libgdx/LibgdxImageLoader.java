package de.jdungeon.libgdx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxImageLoader implements AbstractImageLoader<Image> {

	private static final String TAG = LibgdxImageLoader.class.getName();


	@Override
	public Image loadImage(String filename) {
		if(! filename.startsWith(AbstractImageLoader.PREFIX)) {
			filename = AbstractImageLoader.PREFIX + filename;
		}
		if(fileExists(filename)) {
			return new LibgdxImage(filename);
		} else {
			Gdx.app.error(TAG, "File not found: "+filename);
			return null;
		}
	}

	@Override
	public boolean fileExists(String filename) {
		if(! filename.startsWith(AbstractImageLoader.PREFIX)) {
			filename = AbstractImageLoader.PREFIX + filename;
		}
		long before = System.currentTimeMillis();
		boolean exists = true;
		try {
			Gdx.files.internal(filename).read().close();
			exists = true;

		} catch (Exception e) {
			exists = false;
		}
		return exists;
	}

}
