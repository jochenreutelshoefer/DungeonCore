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
import figure.Figure;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Wolf;
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
	public static String SKEL_ATLAS = "skel";
	public static String GUI_ATLAS = "gui";

	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	private TextureAtlas dungeonAtlas;

	private TextureAtlas warriorAtlas;

	private TextureAtlas orcAtlas;

	private TextureAtlas wolfAtlas;

	private TextureAtlas skelAtlas;

	private TextureAtlas lionessAtlas;

	private TextureAtlas guiAtlas;

	public TextureAtlas getWarriorAtlas() {
		return warriorAtlas;
	}
	public TextureAtlas getOrcAtlas() {
		return orcAtlas;
	}
	public TextureAtlas getDungeonAtlas() {
		return dungeonAtlas;
	}
	public TextureAtlas getLionessAtlas() {
		return lionessAtlas;
	}
	public TextureAtlas getGuiAtlas() {
		return guiAtlas;
	}

	public TextureAtlas getSkelAtlas() {
		return skelAtlas;
	}

	public TextureAtlas getWolfAtlas() {
		return wolfAtlas;
	}

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

		// gui images
		String guiAtlasPath = PACKS + GUI_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(guiAtlasPath, TextureAtlas.class);

		// warrior
		String warriorAtlasPath = PACKS + WARRIOR_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(warriorAtlasPath, TextureAtlas.class);

		// orc
		String orcAtlasPath = PACKS + ORC_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(orcAtlasPath, TextureAtlas.class);

		// wolf
		String wolfAtlasPath = PACKS + WOLF_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(wolfAtlasPath, TextureAtlas.class);

		// skel
		String skelAtlasPath = PACKS + SKEL_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(skelAtlasPath, TextureAtlas.class);

		// lioness
		String lionessAtlasPath = PACKS + LIONESS_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(lionessAtlasPath, TextureAtlas.class);

		assetManager.finishLoading();
		dungeonAtlas = assetManager.get(dungeonAtlasPath);
		cacheMap.put(dungeonAtlas, textureCacheDungeon);

		orcAtlas = assetManager.get(orcAtlasPath);
		cacheMap.put(orcAtlas, textureCacheOrc);

		wolfAtlas = assetManager.get(wolfAtlasPath);
		cacheMap.put(wolfAtlas, textureCacheWolf);

		warriorAtlas = assetManager.get(warriorAtlasPath);
		cacheMap.put(warriorAtlas, textureCacheWarrior);

		lionessAtlas = assetManager.get(lionessAtlasPath);
		cacheMap.put(lionessAtlas, textureCacheLioness);

		skelAtlas = assetManager.get(skelAtlasPath);
		cacheMap.put(skelAtlas, textureCacheSkeleton);

		guiAtlas = assetManager.get(guiAtlasPath);
		cacheMap.put(guiAtlas, textureCacheGUI);

		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		fonts = new AssetFonts();

		// Initialize all game images
		// TODO: do we need to initialize them _all_ here?
		ImageManager.getInstance(game.getFileIO().getImageLoader()).loadImages();
	}

	@Override
	public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load file: " + assetDescriptor);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	private final Map<String, TextureAtlas.AtlasRegion> textureCacheDungeon = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheWarrior = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheOrc = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheWolf = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheSkeleton = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheGUI = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheLioness = new HashMap<>();

	private final Map<TextureAtlas, Map<String, TextureAtlas.AtlasRegion>> cacheMap = new HashMap<>();


	public TextureAtlas.AtlasRegion getGUITexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, guiAtlas);
	}

	public TextureAtlas.AtlasRegion getWarriorTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, warriorAtlas);
	}

	public TextureAtlas.AtlasRegion getWolfTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, wolfAtlas);
	}

	public TextureAtlas.AtlasRegion getOrcTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, orcAtlas);
	}

	public TextureAtlas.AtlasRegion getSkeletonTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, skelAtlas);
	}

	public TextureAtlas.AtlasRegion getDungeonTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, dungeonAtlas);
	}

	public TextureAtlas.AtlasRegion getAtlasRegion(JDImageProxy<?> image, TextureAtlas atlas) {
		if(atlas == null) return null;
		if(image == null) return null;

		Map<String, TextureAtlas.AtlasRegion> textureCache = cacheMap.get(atlas);
		String filename = image.getFilename();
		if (filename.toLowerCase().endsWith(".gif") || filename.toLowerCase().endsWith(".png")) {
			filename = filename.substring(0, filename.length() - 4);
		}
		String pathSeparator = "/"; // todo: does this work on all platforms???
		if (filename.contains(pathSeparator)) {
			filename = filename.substring(filename.lastIndexOf(pathSeparator) + 1);
		}
		if (textureCache.containsKey(filename)) {
			// if texture already loaded, retrieve from cache
			return textureCache.get(filename);
		}
		else {
			// called first time, hence load texture from atlas
			TextureAtlas.AtlasRegion region = atlas.findRegion(filename);
			if (region != null) {
				region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				region.flip(false, true);
				textureCache.put(filename, region);
				return region;
			}
			else {
				textureCache.put(filename, null);
				Gdx.app.error(TAG, "Couldn't find texture: " + filename);
			}
		}
		return null;
	}

	public TextureAtlas.AtlasRegion getFigureTexture(Class<? extends Figure> figureClass, JDImageProxy<?> image) {
		if(Wolf.class.equals(figureClass)) {
			return getWolfTexture(image);
		}
		if(Skeleton.class.equals(figureClass)) {
			return getSkeletonTexture(image);
		}
		if(Orc.class.equals(figureClass)) {
			return getOrcTexture(image);
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
