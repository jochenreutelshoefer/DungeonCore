package de.jdungeon.libgdx;

import de.jdungeon.game.Sound;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxSound implements Sound {

	private final com.badlogic.gdx.audio.Sound sound;
	private final String name;

	public LibgdxSound(com.badlogic.gdx.audio.Sound sound, String name) {
		this.sound = sound;
		this.name = name;
	}

	@Override
	public void play(float volume) {
		sound.play(volume);
	}

	@Override
	public void dispose() {
		sound.dispose();
	}

	@Override
	public int getId() {
		return sound.hashCode();
	}

	@Override
	public String getName() {
		return name;
	}
}
