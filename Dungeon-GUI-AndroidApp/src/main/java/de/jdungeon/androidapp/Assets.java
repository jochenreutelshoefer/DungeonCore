package de.jdungeon.androidapp;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.res.AssetManager;
import android.media.SoundPool;
import android.util.Log;
import io.AbstractImageLoader;
import graphics.ImageManager;
import audio.AudioEffectsManager;
import audio.AudioLoader;

import de.jdungeon.androidapp.gui.dungeonselection.LevelIconImageManager;
import de.jdungeon.androidapp.io.AndroidAudioLoader;
import de.jdungeon.androidapp.io.AndroidImageLoader;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Graphics.ImageFormat;
import de.jdungeon.game.Image;
import de.jdungeon.game.Music;
import de.jdungeon.game.Sound;
import de.jdungeon.implementation.AndroidAudio;

public class Assets {
	   
    public static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, characterJump, characterDown;
    public static Image button;
    public static Sound click;
    public static Music theme;
    
    private static ImageManager imageManager = null;
    private static AbstractImageLoader<Image> loader = null;
   	private static boolean audioLoaded = false;

	public static void load(JDungeonApp game) {

		if (game.getConfiguration().getValue(Configuration.AUDIO_ON)
				.equals("true")) {
		// audio
		Audio audio = game.getAudio();
		AudioLoader androidLoader = new AndroidAudioLoader(audio, game);
		AudioEffectsManager.init(androidLoader);
			SoundPool soundPool = ((AndroidAudio) audio).getSoundPool();
			soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
					audioLoaded = true;
				}
			});
		}

		try {
			String levelIconsPath = "pics/levelIcons";
			List<String> filenames = game.getFileIO().readFileNamesOfFolder(levelIconsPath);
			List<String> fullFilenames = new ArrayList<String>();
			for (String filename : filenames) {
				String fullFileName = levelIconsPath+"/"+filename;
				if(! fileExists(game, fullFileName)) {
					Log.w("warning", "file not found: "+fullFileName);
				} else {
					fullFilenames.add(fullFileName);
				}
			}
			LevelIconImageManager.getInstance().setFilenames(fullFilenames);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// images
        Graphics g = game.getGraphics();
        loader = new AndroidImageLoader(game);
        imageManager = ImageManager.getInstance(loader);
        imageManager.loadImages();
    }

	public static boolean fileExists(JDungeonApp game, String file) {
		AssetManager mg = game.getResources().getAssets();
		try {
			mg.open(file);

		} catch (IOException ex) {
			return false;
		}
		return true;
	}
   
	public static ImageManager getImageManager() {
		return imageManager;
	}

	public static AbstractImageLoader<Image> getLoader() {
		return loader;
	}

	public static boolean isAudioLoaded() {
		return audioLoaded;
	}
}
