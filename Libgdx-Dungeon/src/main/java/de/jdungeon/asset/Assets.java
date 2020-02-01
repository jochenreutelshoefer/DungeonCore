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
import figure.hero.Hero;
import figure.monster.Ghul;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
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
	public static String OGRE_ATLAS = "ogre";
	public static String GHUL_ATLAS = "ghul";
	public static String SPIDER_ATLAS = "spider";
	public static String GUI_ATLAS = "gui";

	/*
	Figure classes that we can draw with the assets provided
	 */
	public static Class[] figureClasses = new Class[] { Hero.class, Orc.class, Wolf.class, Skeleton.class, Ogre.class, Ghul.class, Spider.class };



	/*
	 * Why are all figure classes treated here distinctly instead of in a generic way?
	 * The reason lies in the optimization of the open gl rendering process.
	 * For performance optimization purposes it is necessary to have a low number of texture sheet (atlas)
	 * changes during the rendering of a frame. Therefore, it is clever to render all figures of same class
	 * to have only one texture sheet change for each class (not multiple when for example 3 wolves are animated).
	 * To support the rendering of all figures of one class together we need to organized the asset information here
	 * distinctly to be able to change the texture sheet (atlas) for each class.
	 */


	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	private TextureAtlas dungeonAtlas;

	private TextureAtlas warriorAtlas;

	private TextureAtlas orcAtlas;

	private TextureAtlas ogreAtlas;
	private TextureAtlas ghulAtlas;
	private TextureAtlas spiderAtlas;

	private TextureAtlas wolfAtlas;

	private TextureAtlas skelAtlas;

	private TextureAtlas lionessAtlas;

	private TextureAtlas guiAtlas;

	public TextureAtlas getGuiAtlas() {
		return guiAtlas;
	}

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

		// ogre
		String ogreAtlasPath = PACKS + OGRE_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(ogreAtlasPath, TextureAtlas.class);

		// ghul
		String ghulAtlasPath = PACKS + GHUL_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(ghulAtlasPath, TextureAtlas.class);

		// spider
		String spiderAtlasPath = PACKS + SPIDER_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(spiderAtlasPath, TextureAtlas.class);

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

		ogreAtlas = assetManager.get(ogreAtlasPath);
		cacheMap.put(ogreAtlas, textureCacheOgre);


		ghulAtlas = assetManager.get(ghulAtlasPath);
		cacheMap.put(ghulAtlas, textureCacheGhul);

		spiderAtlas = assetManager.get(spiderAtlasPath);
		cacheMap.put(spiderAtlas, textureCacheSpider);

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
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheOgre = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheSpider = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheGhul = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheWolf = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheSkeleton = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheGUI = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheLioness = new HashMap<>();

	private final Map<TextureAtlas, Map<String, TextureAtlas.AtlasRegion>> cacheMap = new HashMap<>();



	public TextureAtlas.AtlasRegion getWarriorTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, warriorAtlas);
	}

	/*
	 *	RENDER THREAD
	 */
	public TextureAtlas.AtlasRegion getDungeonTexture(JDImageProxy<?> image) {
		return getAtlasRegion(image, dungeonAtlas);
	}

	/*
	 *	RENDER THREAD
	 */
	public TextureAtlas.AtlasRegion getAtlasRegion(JDImageProxy<?> image, TextureAtlas atlas) {
		if(atlas == null) return null;
		if(image == null) return null;


		if (cacheMap.get(atlas).containsKey(image.getFilenameBlank())) {
			// if texture already loaded, retrieve from cache
			return cacheMap.get(atlas).get(image.getFilenameBlank());
		}
		else {
			Map<String, TextureAtlas.AtlasRegion> textureCache = cacheMap.get(atlas);
			String filename = image.getFilenameBlank();
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

	/*
	 *	RENDER THREAD
	 */
	public TextureAtlas.AtlasRegion getFigureTexture(Class<? extends Figure> figureClass, JDImageProxy<?> image) {
		if(Wolf.class.equals(figureClass)) {
			return getAtlasRegion(image, wolfAtlas);
		}
		if(Skeleton.class.equals(figureClass)) {
			return getAtlasRegion(image, skelAtlas);
		}
		if(Orc.class.equals(figureClass)) {
			return getAtlasRegion(image, orcAtlas);
		}
		if(Ogre.class.equals(figureClass)) {
			return getAtlasRegion(image, ogreAtlas);
		}
		if(Ghul.class.equals(figureClass)) {
			return getAtlasRegion(image, ghulAtlas);
		}
		if(Spider.class.equals(figureClass)) {
			return getAtlasRegion(image, spiderAtlas);
		}
		return null;
	}

	public Map<Class<? extends Figure>, TextureAtlas> atlasMap;

	public void initAtlasMap() {
		atlasMap = new HashMap<>();
		atlasMap.put(Hero.class, Assets.instance.warriorAtlas);
		atlasMap.put(Orc.class, Assets.instance.orcAtlas);
		atlasMap.put(Ogre.class, Assets.instance.ogreAtlas);
		atlasMap.put(Ghul.class, Assets.instance.ghulAtlas);
		atlasMap.put(Spider.class, Assets.instance.spiderAtlas);
		atlasMap.put(Wolf.class, Assets.instance.wolfAtlas);
		atlasMap.put(Skeleton.class, Assets.instance.skelAtlas);
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
