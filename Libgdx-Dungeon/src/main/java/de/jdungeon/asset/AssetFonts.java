package de.jdungeon.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.19.
 */
public class AssetFonts {

	public static final float FONT_SIZE_BIG = 2f;
	public static final String FONT_FILE = "font/arial-15.fnt";
	public static final float FONT_SIZE_NORMAL = 1f;
	public static final float FONT_SIZE_SMALL = 0.75f;
	public final BitmapFont defaultSmallFlipped;
	public final BitmapFont defaultNormalFlipped;
	public final BitmapFont defaultBigFlipped;

	public final BitmapFont defaultSmall;
	public final BitmapFont defaultNormal;
	public final BitmapFont defaultBig;

	public static AssetFonts instance = new AssetFonts();


	public AssetFonts() {
		defaultBigFlipped = new BitmapFont(Gdx.files.internal(FONT_FILE), true);
		defaultBigFlipped.getData().setScale(FONT_SIZE_BIG);
		setFilter(defaultBigFlipped);

		defaultBig = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
		defaultBig.getData().setScale(FONT_SIZE_BIG);
		setFilter(defaultBig);

		defaultNormalFlipped = new BitmapFont(Gdx.files.internal(FONT_FILE), true);
		defaultNormalFlipped.getData().setScale(FONT_SIZE_NORMAL);
		setFilter(defaultNormalFlipped);

		defaultNormal = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
		defaultNormal.getData().setScale(FONT_SIZE_NORMAL);
		setFilter(defaultNormal);

		defaultSmallFlipped = new BitmapFont(Gdx.files.internal(FONT_FILE), true);
		defaultSmallFlipped.getData().setScale(FONT_SIZE_SMALL);
		setFilter(defaultSmallFlipped);

		defaultSmall = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
		defaultSmall.getData().setScale(FONT_SIZE_SMALL);
		setFilter(defaultSmall);
	}

	private void setFilter(BitmapFont defaultBigFlipped) {
		defaultBigFlipped.getRegion()
				.getTexture()
				.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}
}
