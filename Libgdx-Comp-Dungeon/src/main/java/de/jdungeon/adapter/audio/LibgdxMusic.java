package de.jdungeon.adapter.audio;

import de.jdungeon.game.Music;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxMusic implements Music {

	private final com.badlogic.gdx.audio.Music music;

	public LibgdxMusic(com.badlogic.gdx.audio.Music music) {
		this.music = music;
	}

	@Override
	public void play() {
		music.play();
	}

	@Override
	public void stop() {
		music.stop();
	}

	@Override
	public void pause() {
		music.pause();
	}

	@Override
	public void setLooping(boolean looping) {
		music.setLooping(looping);
	}

	@Override
	public void setVolume(float volume) {
		music.setVolume(volume);
	}

	@Override
	public boolean isPlaying() {
		return music.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !music.isPlaying();
	}

	@Override
	public boolean isLooping() {
		return music.isLooping();
	}

	@Override
	public void dispose() {
		music.dispose();
	}

	@Override
	public void seekBegin() {
		music.stop();
		music.play();
	}
}
