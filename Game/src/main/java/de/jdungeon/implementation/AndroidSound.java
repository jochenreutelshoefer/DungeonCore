package de.jdungeon.implementation;

import android.media.SoundPool;
import de.jdungeon.game.Sound;


public class AndroidSound implements Sound {
    private final int soundId;
    private final SoundPool soundPool;

	public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }

	@Override
	public int getId() {
		return soundId;
	}
}
