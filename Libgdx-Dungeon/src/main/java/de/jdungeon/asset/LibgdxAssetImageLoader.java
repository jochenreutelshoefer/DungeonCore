package de.jdungeon.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.jdungeon.graphics.JDImageProxy;

import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Image;
import de.jdungeon.libgdx.LibgdxAssetImage;
import de.jdungeon.libgdx.LibgdxFileImage;
import de.jdungeon.libgdx.LibgdxImageLoader;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.01.20.
 */
public class LibgdxAssetImageLoader extends LibgdxImageLoader {

	@Override
	public Image loadImage(String filename) {
		if (!filename.startsWith(AbstractImageLoader.PREFIX)) {
			filename = AbstractImageLoader.PREFIX + filename;
		}
		if (filename.contains("de/jdungeon/gui")) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(new JDImageProxy<>(filename, this), Assets.instance.getGuiAtlas());
			return new LibgdxAssetImage(filename, atlasRegion);
		}
		TextureAtlas.AtlasRegion region = Assets.instance.getDungeonTexture(new JDImageProxy<>(filename, this));
		if(region != null) {
			return new LibgdxAssetImage(filename, region);
		}

		if (fileExists(filename)) {
			return new LibgdxFileImage(filename);
		}
		else {
			Gdx.app.error(TAG, "File not found: " + filename);
			return null;
		}
	}
}
