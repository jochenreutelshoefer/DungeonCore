package de.jdungeon.game;

import com.badlogic.gdx.Gdx;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxAudio implements Audio {


	@Override
	public Music createMusic(String file) {
		//"drop.wav"
		return new LibgdxMusic(Gdx.audio.newMusic(Gdx.files.internal(file)));
	}

	@Override
	public Sound createSound(String file) {
		//"drop.wav"
		return new LibgdxSound(Gdx.audio.newSound(Gdx.files.internal(file)), file);
	}
}
