package de.jdungeon.asset;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import graphics.ImageManager;
import graphics.JDImageProxy;

import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.19.
 */
public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();
	public static String ATLAS_FILE_EXTENSION = ".atlas";
	public static String DUNGEON_ATLAS = "dungeon";
	public static String WARRIOR_ATLAS = "warrior";
	public static String LIONESS_ATLAS = "lioness";
	public static String WOLF_ATLAS = "wolf";
	public static String ORC_ATLAS = "orc";

	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	private TextureAtlas dungeonAtlas;
	private TextureAtlas warriorAtlas;
	private TextureAtlas orcAtlas;
	private TextureAtlas wolfAtlas;
	private TextureAtlas lionessAtlas;

	public AssetBucket bucket;
	public AssetDrop drop;
	public AssetFonts fonts;
	public static final String PACKS = "packs/";

	private Assets() {
	}

	public void init(AssetManager assetManager, LibgdxDungeonMain game) {
		this.assetManager = assetManager;

		assetManager.setErrorListener(this);

		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.finishLoading();


		// general dungeon images
		String dungeonAtlasPath = PACKS + DUNGEON_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(dungeonAtlasPath, TextureAtlas.class);

		// warrior
		String warriorAtlasPath = PACKS + WARRIOR_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(warriorAtlasPath, TextureAtlas.class);

		// orc
		String orcAtlasPath = PACKS + ORC_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(orcAtlasPath, TextureAtlas.class);

		// wolf
		String wolfAtlasPath = PACKS + WOLF_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(wolfAtlasPath, TextureAtlas.class);

		// lioness
		String lionessAtlasPath = PACKS + LIONESS_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(lionessAtlasPath, TextureAtlas.class);

		assetManager.finishLoading();
		dungeonAtlas = assetManager.get(dungeonAtlasPath);
		orcAtlas = assetManager.get(orcAtlasPath);
		wolfAtlas = assetManager.get(wolfAtlasPath);
		warriorAtlas = assetManager.get(warriorAtlasPath);
		lionessAtlas = assetManager.get(lionessAtlasPath);

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

		// Initialize all game images
		// TODO: do we need to initialize them _all_ here?
		ImageManager.getInstance(game.getFileIO().getImageLoader()).loadImages();



	}

	@Override
	public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load file: "+assetDescriptor);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	private final Map<String, TextureAtlas.AtlasRegion> textureCache = new HashMap<>();

	/**
	 * note: may return null if texture is not found!
	 *
	 * @param image image to be retrieved
	 * @return AtlasRegion from TextureAtlas assets
	 */
	public TextureAtlas.AtlasRegion getTexture(JDImageProxy<?> image) {
		String filename = image.getFilename();
		if(filename.endsWith(".gif")) {
			filename = filename.substring(0, filename.length()-4);
		}
		if(textureCache.containsKey(filename)) {
			// if texture already loaded, retrieve from cache
			return textureCache.get(filename);
		}
		else {
			// called first time, hence load texture from atlas
			TextureAtlas.AtlasRegion region = dungeonAtlas.findRegion(filename);
			if(region != null) {
				region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				region.flip(false, true);
				textureCache.put(filename, region);
				return region;
			}
			 else {
				textureCache.put(filename, null);
				Gdx.app.error(TAG, "Couldn't find texture: "+filename);
			}
		}
		return null;
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

}
