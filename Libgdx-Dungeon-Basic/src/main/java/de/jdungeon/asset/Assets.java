package de.jdungeon.asset;

import java.util.HashMap;
import java.util.Map;

import de.jdungeon.audio.AudioEffectsManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.game.FileIO;
import de.jdungeon.graphics.ImageManager;
import de.jdungeon.graphics.JDImageProxy;

import de.jdungeon.Constants;
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
	/*
	 * Why are all de.jdungeon.figure classes treated here distinctly in different atlases instead of in a generic way?
	 * The reason lies in the optimization of the open gl rendering process.
	 * For performance optimization purposes it is necessary to have a low number of texture sheet (atlas)
	 * changes during the rendering of a frame. Therefore, it is clever to render all figures of same class
	 * to have only one texture sheet change for each class (not multiple when for example 3 wolves are animated).
	 * To support the rendering of all figures of one class together we need to organized the asset information here
	 * distinctly to be able to change the texture sheet (atlas) for each class.
	 */

	public static String ATLAS_FILE_EXTENSION = ".atlas";
	public static String DUNGEON_ATLAS = "dungeon";
	public static String GUI_ATLAS = "gui";

	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public Map<FigurePresentation, TextureAtlas> atlasMap = new HashMap<>();

	private TextureAtlas dungeonAtlas;
	private TextureAtlas guiAtlas;

	public TextureAtlas getGuiAtlas() {
		return guiAtlas;
	}

	public AssetFonts fonts;
	public static final String PACKS = "packs/";

	private Assets() {
	}

	public void init(AssetManager assetManager, Audio audio, FileIO fileIO) {
		this.assetManager = assetManager;

		assetManager.setErrorListener(this);

		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.finishLoading();

		// general dungeon images
		String dungeonAtlasPath = PACKS + DUNGEON_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(dungeonAtlasPath, TextureAtlas.class);

		// de.jdungeon.gui images
		String guiAtlasPath = PACKS + GUI_ATLAS + ATLAS_FILE_EXTENSION;
		assetManager.load(guiAtlasPath, TextureAtlas.class);

		FigurePresentation[] figurePresentations = FigurePresentation.values();
		for (FigurePresentation figurePresentation : figurePresentations) {
			if(figurePresentation == FigurePresentation.Fir) continue; // exception as Fir does not have animations
			String figureAtlasPath = PACKS + figurePresentation.getFilepath() + ATLAS_FILE_EXTENSION;
			assetManager.load(figureAtlasPath, TextureAtlas.class);
		}
		assetManager.finishLoading();

		for (FigurePresentation figurePresentation : figurePresentations) {
			if(figurePresentation == FigurePresentation.Fir) continue; // exception as Fir does not have animations
			String figureAtlasPath = PACKS + figurePresentation.getFilepath() + ATLAS_FILE_EXTENSION;
			TextureAtlas figureAtlas = null;
			try {
				figureAtlas = assetManager.get(figureAtlasPath);
				Map<String, TextureAtlas.AtlasRegion> figureAtlasRegionCache = new HashMap<>();
				atlasMap.put(figurePresentation, figureAtlas);
				figuresCacheMap.put(figureAtlas, figureAtlasRegionCache);
			}
			catch (GdxRuntimeException exception) {
				Gdx.app.error(TAG, "Couldn't find atlas for de.jdungeon.figure: " + figurePresentation);
				exception.printStackTrace();
			}
		}

		dungeonAtlas = assetManager.get(dungeonAtlasPath);
		figuresCacheMap.put(dungeonAtlas, textureCacheDungeon);

		guiAtlas = assetManager.get(guiAtlasPath);
		figuresCacheMap.put(guiAtlas, textureCacheGUI);

		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		fonts = new AssetFonts();

		// init de.jdungeon.audio effects (need to be initialized before ImageManager loads
		// because the sounds will then be needed to setup the animations

		AudioLoader androidLoader = new DefaultAudioLoader(audio, fileIO);
		AudioEffectsManager.init(androidLoader);
		AudioManagerTouchGUI.init(androidLoader);

		// Initialize all de.jdungeon.game images
		ImageManager.getInstance(fileIO.getImageLoader()).loadImages();
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
	private final Map<String, TextureAtlas.AtlasRegion> textureCacheGUI = new HashMap<>();
	private final Map<TextureAtlas, Map<String, TextureAtlas.AtlasRegion>> figuresCacheMap = new HashMap<>();

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
		if (overallRegionCacheMap.containsKey(filename)) {
			return overallRegionCacheMap.get(filename);
		}
		for (TextureAtlas textureAtlas : figuresCacheMap.keySet()) {
			TextureAtlas.AtlasRegion region = textureAtlas.findRegion(filename);
			if (region != null) {
				figuresCacheMap.get(textureAtlas).put(filename, region);
				overallRegionCacheMap.put(filename, region);
				return region;
			}
		}

		// not found after extensive search in any atlas
		Gdx.app.error(TAG, "Couldn't find texture in any atlas: " + filename);
		return null;
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
		if (atlas == null) return null;
		if (image == null) return null;

		return getAtlasRegion(image.getFilenameBlank(), atlas);
	}

	String pathSeparator = "/"; // todo: does this work on all platforms???

	public TextureAtlas.AtlasRegion getAtlasRegion(String filename, TextureAtlas atlas) {

		if (atlas == null) return null;
		if (filename == null) return null;

		String blankFilename = filename;
		if (filename.toLowerCase().endsWith(".gif") || filename.toLowerCase().endsWith(".png")) {
			blankFilename = filename.substring(0, filename.length() - 4);
		}

		if (blankFilename.contains(pathSeparator)) {
			blankFilename = blankFilename.substring(filename.lastIndexOf(pathSeparator) + 1);
		}

		if (figuresCacheMap.get(atlas).containsKey(blankFilename)) {
			// if texture already loaded, retrieve from cache
			return figuresCacheMap.get(atlas).get(blankFilename);
		}
		else {
			Map<String, TextureAtlas.AtlasRegion> textureCache = figuresCacheMap.get(atlas);
			// called first time, hence load texture from atlas
			TextureAtlas.AtlasRegion region = atlas.findRegion(blankFilename);
			if (region != null) {
				region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				region.flip(false, true);
				textureCache.put(blankFilename, region);
				overallRegionCacheMap.put(blankFilename, region);
				//Gdx.app.de.jdungeon.log(TAG, "added new AtlasRegion to cache: " + blankFilename);
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
	public TextureAtlas.AtlasRegion getFigureTexture(FigurePresentation figureClass, JDImageProxy<?> image) {
		TextureAtlas textureAtlas = this.atlasMap.get(figureClass);
		if (textureAtlas != null) {
			return getAtlasRegion(image, textureAtlas);
		}
		else {
			Gdx.app.error(TAG, "No atlas texture found for de.jdungeon.figure presentation: " + figureClass);
		}
		return null;
	}
}
