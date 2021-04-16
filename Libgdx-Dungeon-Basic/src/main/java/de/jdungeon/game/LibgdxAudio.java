package de.jdungeon.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxAudio implements Audio {


	public static final String MUSIC_ASSETS_PATH = "music/";
	public static final String SOUND_ASSETS_PATH = "sounds/";
	private static final String ASSETS_PATH = "assets/";


	@Override
	public Music createMusic(String file) {
		//"drop.wav"
		return new LibgdxMusic(Gdx.audio.newMusic(Gdx.files.internal(prefix(MUSIC_ASSETS_PATH + file))));
	}

	private static String prefix(String path) {
		if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			path = ASSETS_PATH + path;
		}
		return path;
	}

	@Override
	public Sound createSound(String file) {
		//"drop.wav"
		return new LibgdxSound(Gdx.audio.newSound(Gdx.files.internal(prefix(SOUND_ASSETS_PATH+file))), file);
	}
}
