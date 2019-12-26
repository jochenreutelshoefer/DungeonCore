package de.jdungeon.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.19.
 */
public class AssetFonts {

	public final BitmapFont defaultSmall;
	public final BitmapFont defaultNormal;
	public final BitmapFont defaultBig;

	public AssetFonts() {
		defaultBig = new BitmapFont(Gdx.files.internal("font/arial-15.fnt"), true);
		defaultBig.getData().setScale(0.75f);
		defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

		defaultNormal = new BitmapFont(Gdx.files.internal("font/arial-15.fnt"), true);
		defaultNormal.getData().setScale(1f);
		defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

		defaultSmall = new BitmapFont(Gdx.files.internal("font/arial-15.fnt"), true);
		defaultSmall.getData().setScale(2f);
		defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
	}
}
