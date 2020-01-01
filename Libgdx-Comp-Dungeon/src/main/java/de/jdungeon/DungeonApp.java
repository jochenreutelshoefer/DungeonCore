package de.jdungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.ExitUsedEvent;
import event.PlayerDiedEvent;
import figure.hero.Hero;
import game.DungeonGame;
import game.JDEnv;
import graphics.ImageManager;
import level.DungeonStartEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spell.Spell;
import user.DefaultDungeonSession;
import user.DungeonSession;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.QuitGameEvent;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.app.gui.dungeonselection.DungeonSelectionScreen;
import de.jdungeon.app.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.app.gui.skillselection.SkillSelectionScreen;
import de.jdungeon.app.screen.GameScreen;
import de.jdungeon.app.screen.start.HeroCategorySelectedEvent;
import de.jdungeon.app.screen.start.WelcomeScreen;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Screen;
import de.jdungeon.io.ResourceBundleLoader;
import de.jdungeon.libgdx.LibgdxAudio;
import de.jdungeon.libgdx.LibgdxConfiguration;
import de.jdungeon.libgdx.LibgdxFileIO;
import de.jdungeon.libgdx.LibgdxGraphics;
import de.jdungeon.libgdx.LibgdxImage;
import de.jdungeon.libgdx.LibgdxImageLoader;
import de.jdungeon.libgdx.LibgdxInput;
import de.jdungeon.libgdx.MyInputProcessor;
import de.jdungeon.user.Session;
import de.jdungeon.user.User;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class DungeonApp implements ApplicationListener, Game, EventListener {

	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 550;

	private final ResourceBundleLoader resourceBundleLoader;
	private Texture dropImage;
	private Texture bucketImage;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	private Sound dropSound;
	private Music rainMusic;

	private LibgdxGraphics graphics;
	private final LibgdxAudio audio = new LibgdxAudio();
	private LibgdxInput input = new LibgdxInput();
	private Logger log;
	private LibgdxJDGUI gui;
	private final LibgdxFileIO fileIO = new LibgdxFileIO();
	private final LibgdxConfiguration configuration = new LibgdxConfiguration();
	private GameScreen gamescreen;
	private Screen currentScreen;

	private boolean firstTimeCreate = true;
	private boolean firstTimeRender = true;
	private LibgdxInput input1;

	public DungeonApp(ResourceBundleLoader resourceBundleLoader) {
		this.resourceBundleLoader = resourceBundleLoader;
	}

	protected Session session;
	protected DungeonSession dungeonSession;

	@Override
	public void create() {

		initLogging();

		ResourceBundle textsBundle = resourceBundleLoader.getBundle(JDEnv.TEXTS_BUNDLE_BASENAME, Locale.GERMAN, this.getClass().getClassLoader());
		if(textsBundle == null) {
			log.error("Could not load resource bundle for texts");
		}
		JDEnv.init(textsBundle);
		this.session = new DefaultDungeonSession(new User("Hans Meiser"));
		this.dungeonSession = (DungeonSession)session; // Todo: improve


		// TODO: solve this weird bidirectional dependency in a better way..
		gui = LibgdxJDGUI.getInstance(this);
		gamescreen = new GameScreen(this, gui);
		gui.setPerceptHandler(gamescreen);
		EventManager.getInstance().registerListener(this);

		Gdx.input.setInputProcessor(new MyInputProcessor(input));

		currentScreen = getInitScreen();
		currentScreen.init();

		log.info("App on CreateHook done");

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("data/droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("data/bucket.png"));

		Map<String, Long> durations = LibgdxImageLoader.durations;
		Long sum = 0l;
		Long max = -1l;
		Long min = Long.MAX_VALUE;
		int count = 0;
		for (Long duration : durations.values()) {
			count++;
			sum += duration;
			if(duration > max) {
				max = duration;
			}
			if(duration < min) {
				min = duration;
			}
		}
		Logger.getLogger(this.getClass().getName()).info("Number of File-exists operations: "+count);
		Logger.getLogger(this.getClass().getName()).info("Average time for operation: "+sum/count);
		Logger.getLogger(this.getClass().getName()).info("Max: "+max);
		Logger.getLogger(this.getClass().getName()).info("Min: "+min);
		Logger.getLogger(this.getClass().getName()).info("Map: "+durations);


		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

	}

	private void initLogging() {
		LogConfigurator logConfigurator = new LogConfigurator();
		//logConfigurator.setFileName(Environment.getExternalStorageDirectory()
		//		+ File.separator + "MyApp" + File.separator + "logs"
		//		+ File.separator + "log4j.txt");
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.setLevel("org.apache", Level.ERROR);
		//logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
		//logConfigurator.setMaxFileSize(1024 * 1024 * 5);
		logConfigurator.setUseFileAppender(false);//setzt ob das Log in ein File gespeichert werden soll (Ja das geht)
		logConfigurator.setImmediateFlush(true);
		logConfigurator.setUseLogCatAppender(true);
		logConfigurator.configure();
		log = Logger.getLogger(DungeonApp.class);
		log.info("Logging initialized");

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}


	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public void render() {

		if(firstTimeRender) {
			log.info("First render frame");
			firstTimeRender = false;
		}

		if(graphics == null) {
			graphics = new LibgdxGraphics();
		}

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(((LibgdxImage)ImageManager.cristall_blueImage.getImage()).getTexture(), 50, 50);
		batch.end();

		currentScreen.update(-1);
		currentScreen.paint(-1);

		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.

		//renderRainDropGame();
	}

	private void renderRainDropGame() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		// process user input
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		// check if we need to create a new raindrop
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the latter case we play back
		// a sound effect as well.
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		//texture.dispose();
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public de.jdungeon.game.Input getInput() {
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
	public void setScreen(Screen screen) {
		currentScreen.pause();
		currentScreen.dispose();
		screen.resume();
		//screen.update(0);
		currentScreen = screen;
		screen.init();
	}

	@Override
	public Screen getCurrentScreen() {
		return currentScreen;
	}

	@Override
	public Screen getInitScreen() {
		if (firstTimeCreate) {
			Assets.load(this);
			firstTimeCreate = false;
		}
		return new WelcomeScreen(this);
	}

	@Override
	public int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	@Override
	public int getScreenHeight() {
		return SCREEN_HEIGHT;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		List<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>();
		events.add(ExitUsedEvent.class);
		events.add(PlayerDiedEvent.class);
		events.add(StartNewGameEvent.class);
		events.add(QuitGameEvent.class);
		events.add(DungeonStartEvent.class);
		events.add(HeroCategorySelectedEvent.class);
		events.add(SkillSelectedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		if(event instanceof StartNewGameEvent) {
			((DefaultDungeonSession)dungeonSession).setSelectedHeroType(Hero.HeroCategory.Thief.getCode());
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this);
			this.setScreen(screen);
		}
		if(event instanceof QuitGameEvent) {
			this.dispose();
		}
		if(event instanceof ExitUsedEvent) {
			DungeonGame.getInstance().stopRunning();

			// todo : implement libgdx pause
			//this.renderView.pause();

			this.dungeonSession.notifyExit(((ExitUsedEvent)event).getExit(), ((ExitUsedEvent)event).getFigure());
			SkillSelectionScreen screen = new SkillSelectionScreen(this);
			this.setScreen(screen);


			// todo : implement libgdx resume
			//this.renderView.resume();

		}
		if(event instanceof SkillSelectedEvent) {
			Spell spell = ((SkillSelectedEvent) event).getSpell();
			dungeonSession.learnSkill(spell);
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this);
			this.setScreen(screen);
		}
		if(event instanceof DungeonStartEvent) {
			log.info("App: processing DungeonStartEvent");
			// initialize new dungeon
			this.dungeonSession.initDungeon(((DungeonStartEvent)event).getEvent().getDungeon());
			DungeonGame.getInstance().restartRunning();
			setScreen(gamescreen);
		}
		if(event instanceof PlayerDiedEvent) {
			this.dungeonSession.revertHero();
			setScreen(new DungeonSelectionScreen(this));
		}
	}
}
