package de.jdungeon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import audio.AudioEffectsManager;
import graphics.ImageManager;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.audio.DefaultAudioLoader;
import de.jdungeon.app.gui.dungeonselection.LevelIconImageManager;
import de.jdungeon.game.Audio;
import de.jdungeon.game.AudioLoader;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.Game;

public class Assets {

	private static ImageManager imageManager = null;
	private static boolean audioLoaded = false;

	public static void load(Game game) {

		// TODO: refactor this class and move it to Game-Android

		if (game.getConfiguration().getValue(Configuration.AUDIO_ON)
				.equals("true")) {
			// audio
			Audio audio = game.getAudio();

			AudioLoader androidLoader = new DefaultAudioLoader(audio, game);
			AudioEffectsManager.init(androidLoader);
			AudioManagerTouchGUI.init(androidLoader);

			/*
			SoundPool soundPool = ((AndroidAudio) audio).getSoundPool();
			soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					audioLoaded = true;
				}
			});
			*/
		}

		try {
			String levelIconsPath = "pics/levelIcons";
			List<String> filenames = game.getFileIO().readFileNamesOfFolder(levelIconsPath);
			List<String> fullFilenames = new ArrayList<String>();
			for (String filename : filenames) {
				String fullFileName = levelIconsPath + "/" + filename;
				if (!fileExists(game, fullFileName)) {
					Logger.getLogger(Assets.class.getName()).warning("file not found: " + fullFileName);
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

		// images
		imageManager = ImageManager.getInstance(game.getFileIO().getImageLoader());
		imageManager.loadImages();
	}

	private static boolean fileExists(Game game, String file) {
		Object o = game.getFileIO().getImageLoader().loadImage(file);
		if(null != o) {
			return true;
		}

		try {
			InputStream inputStream = game.getFileIO().readFile(file);
			inputStream.close();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

}
