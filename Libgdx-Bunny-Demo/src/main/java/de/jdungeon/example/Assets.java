package de.jdungeon.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.19.
 */
public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public AssetBucket bucket;
	public AssetDrop drop;
	public AssetFonts fonts;
	private Assets() {
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;

		assetManager.setErrorListener(this);

		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.finishLoading();

		Gdx.app.debug(TAG, "# of assets loaded: "+assetManager.getAssetNames().size);
		for(String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: "+a);
		}

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		for(Texture texture : atlas.getTextures()) {
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}

		bucket = new AssetBucket(atlas);
		drop = new AssetDrop(atlas);
		fonts = new AssetFonts();

	}

	@Override
	public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load file: "+assetDescriptor);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	public static class AssetBucket {
		public final TextureAtlas.AtlasRegion bucket;

		public AssetBucket(TextureAtlas atlas) {
			this.bucket = atlas.findRegion("bucket");
		}
	}

	public static class AssetDrop {
		public final TextureAtlas.AtlasRegion drop;

		public AssetDrop(TextureAtlas atlas) {
			this.drop = atlas.findRegion("droplet");
		}
	}

	static class AssetFonts {

		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
			defaultBig = new BitmapFont(Gdx.files.internal("font/arial-15.fnt"), true);
			defaultBig.getData().setScale(2f);
			defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

			defaultNormal = new BitmapFont(Gdx.files.internal("font/arial-15.fnt"), true);
			defaultNormal.getData().setScale(1f);
			defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

			defaultSmall = new BitmapFont(Gdx.files.internal("font/arial-15.fnt"), true);
			defaultSmall.getData().setScale(0.75f);
			defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
		}
	}
}
