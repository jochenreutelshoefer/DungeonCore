package de.jdungeon.asset;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import audio.AudioEffectsManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import figure.Figure;
import figure.hero.Druid;
import figure.hero.Hero;
import figure.hero.Mage;
import figure.hero.Thief;
import figure.hero.Warrior;
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
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.audio.DefaultAudioLoader;
import de.jdungeon.game.Audio;
import de.jdungeon.game.AudioLoader;

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
	public static String DRUID_ATLAS = "druid";
	public static String MAGE_ATLAS = "mage";
	public static String THIEF_ATLAS = "thief";
	public static String DARKDWARF_ATLAS = "darkdwarf";
	public static String SKEL_ATLAS = "skel";
	public static String OGRE_ATLAS = "ogre";
	public static String GHUL_ATLAS = "ghul";
	public static String SPIDER_ATLAS = "spider";
	public static String GUI_ATLAS = "gui";

	/*
	Figure classes that we can draw with the assets provided
	 */
	public static Class[] figureClasses = new Class[] { Warrior.class, Orc.class, Wolf.class, Skeleton.class, Ogre.class, Ghul.class, Spider.class, Druid.class, Mage.class, Thief.class /*TODO: DarkDwarf.class*/ };



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
	private TextureAtlas druidAtlas;
	private TextureAtlas mageAtlas;
	private TextureAtlas thiefAtlas;
	private TextureAtlas darkdwarfAtlas;

	private TextureAtlas wolfAtlas;

	private TextureAtlas skelAtlas;

	private TextureAtlas lionessAtlas;

	private TextureAtlas guiAtlas;

	public TextureAtlas getGuiAtlas() {
		return guiAtlas;
	}

	public TextureAtlas getDungeonAtlas() {
		return dungeonAtlas;
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


		// druid
		String druidAtlasPath = PACKS + DRUID_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(druidAtlasPath, TextureAtlas.class);

		// mage
		String mageAtlasPath = PACKS + MAGE_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(mageAtlasPath, TextureAtlas.class);

		// thief
		String thiefAtlasPath = PACKS + THIEF_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(thiefAtlasPath, TextureAtlas.class);

		// darkdwarf
		String darkdwarfAtlasPath = PACKS + DARKDWARF_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(darkdwarfAtlasPath, TextureAtlas.class);

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

		druidAtlas = assetManager.get(druidAtlasPath);
		cacheMap.put(druidAtlas, textureCacheDruid);

		mageAtlas = assetManager.get(mageAtlasPath);
		cacheMap.put(mageAtlas, textureCacheMage);

		thiefAtlas = assetManager.get(thiefAtlasPath);
		cacheMap.put(thiefAtlas, textureCacheThief);

		/*
		darkdwarfAtlas = assetManager.get(darkdwarfAtlasPath);
		cacheMap.put(darkdwarfAtlas, textureCacheDarkdwarf);
		*/

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


		// init audio effects (need to be initialized before ImageManager loads
		// because the sounds will then be needed to setup the animations
		Audio audio = game.getAudio();

		AudioLoader androidLoader = new DefaultAudioLoader(audio, game);
		AudioEffectsManager.init(androidLoader);
		AudioManagerTouchGUI.init(androidLoader);


		// Initialize all game images
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
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheDruid = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheMage = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheThief = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheDarkdwarf = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheGhul = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheWolf = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheSkeleton = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheGUI = new HashMap<>();
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheLioness = new HashMap<>();

	private final Map<TextureAtlas, Map<String, TextureAtlas.AtlasRegion>> cacheMap = new HashMap<>();

	/*
	 * We also maintain an overall cache map from filename to AtlasRegion.
	 * However it should only be used if absolutely necessary as arbitrary use
	 * will lead to a lots of Atlas changes on the openGL rendering process
	 * slowing down the rendering
	 */
	private final Map<String, TextureAtlas.AtlasRegion> overallRegionCacheMap = new HashMap<>();

	/**
	 * Should only be used if absolutely necessary, that is if the caller code
	 * has no chance to know in which atlas the texture is in.
	 *
	 * @param filename filename of image assets (AtlasRegion)
	 * @return the AtlasRegion for this filename
	 */
	public TextureAtlas.AtlasRegion findTexture(String filename) {
		if(overallRegionCacheMap.containsKey(filename)) {
			return overallRegionCacheMap.get(filename);
		}
		for (TextureAtlas textureAtlas : cacheMap.keySet()) {
			TextureAtlas.AtlasRegion region = textureAtlas.findRegion(filename);
			if(region != null) {
				cacheMap.get(textureAtlas).put(filename, region);
				overallRegionCacheMap.put(filename, region);
				return region;
			}
		}

		// not found after extensive search in any atlas
		Gdx.app.error(TAG, "Couldn't find texture in any atlas: " + filename);
		return null;
	}

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

		return getAtlasRegion(image.getFilenameBlank(), atlas);
	}

	String pathSeparator = "/"; // todo: does this work on all platforms???
	public TextureAtlas.AtlasRegion getAtlasRegion(String filename, TextureAtlas atlas) {

		if(atlas == null) return null;
		if(filename == null) return null;

		String blankFilename = filename;
		if (filename.toLowerCase().endsWith(".gif") || filename.toLowerCase().endsWith(".png")) {
			blankFilename = filename.substring(0, filename.length() - 4);
		}

		if (blankFilename.contains(pathSeparator)) {
			blankFilename = blankFilename.substring(filename.lastIndexOf(pathSeparator) + 1);
		}

		if (cacheMap.get(atlas).containsKey(blankFilename)) {
			// if texture already loaded, retrieve from cache
			return cacheMap.get(atlas).get(blankFilename);
		}
		else {
			Map<String, TextureAtlas.AtlasRegion> textureCache = cacheMap.get(atlas);
			// called first time, hence load texture from atlas
			TextureAtlas.AtlasRegion region = atlas.findRegion(blankFilename);
			if (region != null) {
				region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				region.flip(false, true);
				textureCache.put(blankFilename, region);
				overallRegionCacheMap.put(blankFilename, region);
				Gdx.app.log(TAG, "added new AtlasRegion to cache: " + blankFilename);
				return region;
			}
			else {
				textureCache.put(blankFilename, null);
				Gdx.app.error(TAG, "Couldn't find texture: " + blankFilename);
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
		if(Druid.class.equals(figureClass)) {
			return getAtlasRegion(image, druidAtlas);
		}
		if(Mage.class.equals(figureClass)) {
			return getAtlasRegion(image, mageAtlas);
		}
		if(Thief.class.equals(figureClass)) {
			return getAtlasRegion(image, thiefAtlas);
		}
		/* TODO
		if(DarkDwarf.class.equals(figureClass)) {
			return getAtlasRegion(image, darkdwarfAtlas);
		}
		*/
		if(Warrior.class.equals(figureClass)) {
			return getAtlasRegion(image, warriorAtlas);
		}
		return null;
	}

	public Map<Class<? extends Figure>, TextureAtlas> atlasMap;

	public void initAtlasMap() {
		atlasMap = new HashMap<>();
		atlasMap.put(Warrior.class, Assets.instance.warriorAtlas);
		atlasMap.put(Orc.class, Assets.instance.orcAtlas);
		atlasMap.put(Ogre.class, Assets.instance.ogreAtlas);
		atlasMap.put(Ghul.class, Assets.instance.ghulAtlas);
		atlasMap.put(Spider.class, Assets.instance.spiderAtlas);
		atlasMap.put(Wolf.class, Assets.instance.wolfAtlas);
		atlasMap.put(Skeleton.class, Assets.instance.skelAtlas);
		atlasMap.put(Druid.class, Assets.instance.druidAtlas);
		atlasMap.put(Mage.class, Assets.instance.mageAtlas);
		atlasMap.put(Thief.class, Assets.instance.thiefAtlas);
		// atlasMap.put(DarkDwarf.class, Assets.instance.druidAtlas); TODO
	}

}
