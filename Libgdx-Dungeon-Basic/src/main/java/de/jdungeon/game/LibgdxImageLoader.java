package de.jdungeon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxImageLoader implements AbstractImageLoader<Image> {

	protected static final String TAG = LibgdxImageLoader.class.getName();


	@Override
	public Image loadImage(String filename) {
		if(! filename.startsWith(AbstractImageLoader.PREFIX)) {
			filename = AbstractImageLoader.PREFIX + filename;
		}
		if(fileExists(filename)) {
			return new LibgdxFileImage(filename);
		} else {
			Gdx.app.error(TAG, "File not found: "+filename);
			return null;
		}
	}

	@Override
	public boolean fileExists(String filename) {


		String atlasKeyFileName = filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf("."));
		TextureAtlas.AtlasRegion texture = Assets.instance.findTexture(atlasKeyFileName, false);
		if(texture != null) {
			return true;
		}

		try {
			Gdx.files.internal(filename).read().close();
			return true;


		} catch (Exception e) {
			if(! filename.startsWith(AbstractImageLoader.PREFIX)) {
				String filenameWithPrefix = AbstractImageLoader.PREFIX + filename;
				return fileExists(filenameWithPrefix);
			}
			return false;
		}
	}

}
