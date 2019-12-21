package de.jdungeon.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import de.jdungeon.game.Audio;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;
import de.jdungeon.user.Session;

public abstract class AndroidGame extends Activity implements Game {

	protected AndroidFastRenderView renderView;
	private Graphics graphics;
	private Audio audio;
	private Input input;
	private FileIO fileIO;
	private Screen screen;
	private WakeLock wakeLock;
	//public static final int SCREEN_WIDTH = 1920;
	public static final int SCREEN_WIDTH = 1000;
	//public static final int SCREEN_HEIGHT = 1104;
	public static final int SCREEN_HEIGHT = 550;

	public AndroidGame() {

	}

	public void init(Session session) {
		this.session = session;
	}

	@Override
	public Session getSession() {
		return session;
	}

	protected Session session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		// 1920 * 1104
		// 1824 * 1200
		//int frameBufferWidth = isPortrait ? 480: 800;
		int frameBufferWidth = isPortrait ? SCREEN_HEIGHT : SCREEN_WIDTH;
		int frameBufferHeight = isPortrait ? SCREEN_WIDTH : SCREEN_HEIGHT;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		float scaleX = (float) frameBufferWidth
				/ getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight
				/ getWindowManager().getDefaultDisplay().getHeight();

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getInitScreen();
		screen.init();
		setContentView(renderView);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");

		onCreateHook();
	}

	protected abstract void onCreateHook();

	@Override
	public int getScreenWidth() {
		return SCREEN_WIDTH;
		// TODO: implement properly
		//return 800;
		// 1920 * 1104
		// 1824 * 1200
	}

	@Override
	public int getScreenHeight() {
		return SCREEN_HEIGHT;
		/*
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
 		int width = displaymetrics.widthPixels;
		*/
		// TODO: implement properly
		//return 400;
	}

	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();

		if (isFinishing()) {
			screen.dispose();
		}
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null) {
			throw new IllegalArgumentException("Screen must not be null");
		}

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		//screen.update(0);
		this.screen = screen;
		screen.init();
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}
}
