package de.jdungeon.androidapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.media.SoundPool;
import android.util.Log;
import audio.AudioEffectsManager;
import graphics.ImageManager;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.dungeonselection.LevelIconImageManager;
import de.jdungeon.game.Audio;
import de.jdungeon.game.AudioLoader;
import de.jdungeon.game.Configuration;
import de.jdungeon.implementation.AndroidAudio;
import de.jdungeon.implementation.AndroidAudioLoader;
import de.jdungeon.implementation.AndroidGame;

public class Assets {

	private static ImageManager imageManager = null;
	private static boolean audioLoaded = false;

	public static void load(AndroidGame game) {

		// TODO: refactor this class and move it to Game-Android

		if (game.getConfiguration().getValue(Configuration.AUDIO_ON)
				.equals("true")) {
			// audio
			Audio audio = game.getAudio();

			AudioLoader androidLoader = new AndroidAudioLoader(audio, game);
			AudioEffectsManager.init(androidLoader);
			AudioManagerTouchGUI.init(androidLoader);

			SoundPool soundPool = ((AndroidAudio) audio).getSoundPool();
			soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					audioLoaded = true;
				}
			});
		}

		try {
			String levelIconsPath = "pics/levelIcons";
			List<String> filenames = game.getFileIO().readFileNamesOfFolder(levelIconsPath);
			List<String> fullFilenames = new ArrayList<String>();
			for (String filename : filenames) {
				String fullFileName = levelIconsPath + "/" + filename;
				if (!fileExists(game, fullFileName)) {
					Log.w("warning", "file not found: " + fullFileName);
				}
				else {
					fullFilenames.add(fullFileName);
				}
			}
			LevelIconImageManager.getInstance().setFilenames(fullFilenames);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// initialize images
		ImageManager imageManager = ImageManager.getInstance(game.getFileIO().getImageLoader());
		imageManager.loadImages();
	}

	public static boolean fileExists(AndroidGame game, String file) {
		AssetManager mg = game.getResources().getAssets();
		try {
			mg.open(file);

		}
		catch (IOException ex) {
			return false;
		}
		return true;
	}

}
