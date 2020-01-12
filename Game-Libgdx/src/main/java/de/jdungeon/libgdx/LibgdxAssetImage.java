package de.jdungeon.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.01.20.
 */
public class LibgdxAssetImage implements Image {

	private final String filename;
	private final TextureAtlas.AtlasRegion atlasRegion;

	public LibgdxAssetImage(String filename, TextureAtlas.AtlasRegion atlasRegion) {
		if(atlasRegion == null) {
			throw new IllegalArgumentException("AtlasRegion was null: "+filename);
		}
		this.filename = filename;
		this.atlasRegion = atlasRegion;
	}

	@Override
	public String toString() {
		return "Bild: "+filename;
	}

	@Override
	public int getWidth() {
		return atlasRegion.getRegionWidth();
	}

	@Override
	public int getHeight() {
		return atlasRegion.getRegionHeight();
	}

	@Override
	public Graphics.ImageFormat getFormat() {
		return null;
	}


	public TextureAtlas.AtlasRegion getAtlasRegion() {
		return atlasRegion;
	}

	@Override
	public void dispose() {
		if(atlasRegion != null) {
			//atlasRegion.dispose();
		}
	}
}
