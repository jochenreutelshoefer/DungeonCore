package de.jdungeon.androidapp.io;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.16.
 */
public class MusicUtils {


	public static MediaPlayer playMusicFile(String musicFilename, AssetManager assets) {
		AssetFileDescriptor afd = null;
		MediaPlayer mediaPlayer = null;
		try {
			afd = assets.openFd("music/"+musicFilename);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return mediaPlayer;
	}

	void workerThread() {

	}

	public static void fadeOut(final MediaPlayer _player, final int duration) {
		/*
		final float deviceVolume = 1f; // getDeviceVolume();
		final Handler h = new Handler();
		Handler mHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message message) {
				// This is where you do your work in the UI thread.
				// Your worker tells you in the message what to do.
			}
		};

		Message message = mHandler.obtainMessage(0, null);
		message.sendToTarget();


		h.postDelayed(new Runnable() {
			private float time = duration;
			private float volume = 0.0f;

			@Override
			public void run() {
				if (!_player.isPlaying())
					_player.start();
				// can call h again after work!
				time -= 100;
				volume = (deviceVolume * time) / duration;
				_player.setVolume(volume, volume);
				if (time > 0)
					h.postDelayed(this, 100);
				else {
					_player.stop();
					_player.release();
				}
			}
		}, 100); // 1 second delay (takes millis)

	*/
	}

	public static MediaPlayer playMusicFileRandom(AssetManager assets, String... musicFilenames) {
		return playMusicFile(musicFilenames[((int) (Math.random() * musicFilenames.length))], assets);
	}
}
