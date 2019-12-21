package de.jdungeon.libgdx.adapter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import de.jdungeon.game.Audio;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;
import de.jdungeon.user.Session;

public abstract class LibgdxGame implements Game, ApplicationListener {

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

	public LibgdxGame(Session session) {
		this.session = session;

	}

	@Override
	public Session getSession() {
		return session;
	}

	protected Session session;

    @Override
	public void create() {
        super.onCreate(savedInstanceState);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);

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
        graphics = new LibgdxGraphics(getAssets(), frameBuffer);
        fileIO = new LibgdxFileIO(this);
        audio = new LibgdxAudio(this);
        input = new LibgdxInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
		screen.init();
        setContentView(renderView);
       
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
	public void resume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
	public void pause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
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

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

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
