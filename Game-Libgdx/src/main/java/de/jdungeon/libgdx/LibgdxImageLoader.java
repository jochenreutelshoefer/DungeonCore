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

	public static Map<String, Long> durations = new HashMap<>();

	@Override
	public Image loadImage(String filename) {
		if(! filename.startsWith(AbstractImageLoader.PREFIX)) {
			filename = AbstractImageLoader.PREFIX + filename;
		}
		//if(fileExists(filename)) {
			return new LibgdxImage(filename);
		//} else {
		//	Log.warning("File not found: "+filename);
		//	return null;
		//}
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
		durations.put(filename, System.currentTimeMillis() - before);
		return exists;
	}

}
