package de.jdungeon.implementation;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import org.apache.log4j.Logger;

import de.jdungeon.game.Audio;
import de.jdungeon.game.Music;
import de.jdungeon.game.Sound;

public class AndroidAudio implements Audio {
    private AssetManager assets;

	public SoundPool getSoundPool() {
		return soundPool;
	}

	private SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
		SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
		soundPoolBuilder.setMaxStreams(20);
		AudioAttributes attributes = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_GAME)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build();

		soundPoolBuilder.setAudioAttributes(attributes);

		this.soundPool = soundPoolBuilder.build();
		//this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music createMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
			Logger.getLogger(this.getClass()).warn("Couldn't load music '" + filename + "'", e);
        }
		String defaultFileName = "music/Eyes_Gone_Wrong.mp3";
		try {
			assets.openFd(defaultFileName);
			return createMusic(defaultFileName);
		}
		catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(this.getClass()).warn("Couldn't load music '" + defaultFileName + "'", e);
			return null;
		}
    }

    @Override
    public Sound createSound(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			//InputStream inputStream = assets.open(filename);
			//int soundId = soundPool.load(filename, 1);
			int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);

		} catch (IOException e) {
			System.out.println("Couldn't load sound '" + filename + "'");
			System.out.println(e.getMessage());
			// throw new RuntimeException("Couldn't load sound '" + filename +
			// "'");
			return null;
        }

    }
}
