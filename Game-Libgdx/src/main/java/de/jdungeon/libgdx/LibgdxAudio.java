package de.jdungeon.libgdx;

import com.badlogic.gdx.Gdx;

import de.jdungeon.game.Audio;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Music;
import de.jdungeon.game.Sound;

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
