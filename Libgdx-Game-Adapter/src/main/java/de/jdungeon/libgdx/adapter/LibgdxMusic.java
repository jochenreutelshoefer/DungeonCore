package de.jdungeon.libgdx.adapter;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.Log;

import de.jdungeon.game.Music;

public class LibgdxMusic implements Music {

	private final MediaPlayer mediaPlayer;
	private boolean isPrepared = false;

	public LibgdxMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnSeekCompleteListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnVideoSizeChangedListener(this);

		}
		catch (Exception e) {
			throw new RuntimeException("Couldn't load music: " + e.getMessage());
		}
	}

	@Override
	public void dispose() {

		try {
			if (this.mediaPlayer.isPlaying()) {
				this.mediaPlayer.stop();
			}
		}
		catch (IllegalStateException e) {
			// media player was not initialized yet
		}
		this.mediaPlayer.release();
	}

	@Override
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	@Override
	public boolean isPlaying() {
		return this.mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !isPrepared;
	}

	@Override
	public void pause() {
		if (this.mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	@Override
	public void play() {
		if (this.mediaPlayer.isPlaying()) {
			return;
		}

		try {
			synchronized (this) {
				if (!isPrepared) {
					mediaPlayer.prepare();
				}
				mediaPlayer.start();
			}
		}
		catch (IllegalStateException e) {
			Log.w("Error", "IllegalState playing music: " + e.getMessage());
		}
		catch (IOException e) {
			Log.w("Error", "IOException playing music: " + e.getMessage());
		}
	}

	@Override
	public void setLooping(boolean isLooping) {
		mediaPlayer.setLooping(isLooping);
	}

	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}

	@Override
	public void stop() {

		try {
			this.mediaPlayer.stop();
		} catch (IllegalStateException e) {
			// media play has not been initialized
		}
			this.mediaPlayer.release();
		synchronized (this) {
			isPrepared = false;
		}
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		synchronized (this) {
			this.mediaPlayer.release();
			isPrepared = false;
		}
	}

	@Override
	public void seekBegin() {
		mediaPlayer.seekTo(0);

	}

	@Override
	public void onPrepared(MediaPlayer player) {
		// TODO Auto-generated method stub
		synchronized (this) {
			isPrepared = true;
		}

	}

	@Override
	public void onSeekComplete(MediaPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoSizeChanged(MediaPlayer player, int width, int height) {
		// TODO Auto-generated method stub

	}
}
