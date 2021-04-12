package de.jdungeon.world;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import de.jdungeon.animation.AnimationManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.game.Game;
import de.jdungeon.game.RoomInfoEntity;
import de.jdungeon.graphics.GraphicObjectRenderer;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.CameraHelper;
import de.jdungeon.Constants;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.audio.MusicManager;
import de.jdungeon.app.movieSequence.CameraFlightSequence;
import de.jdungeon.app.movieSequence.DefaultMovieSequence;
import de.jdungeon.app.movieSequence.StraightLineScroller;
import de.jdungeon.app.movieSequence.TrivialScaleSequence;
import de.jdungeon.app.movieSequence.ZoomSequence;
import de.jdungeon.game.Music;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.gui.LibgdxFocusManager;
import de.jdungeon.gui.LibgdxGUIElement;
import de.jdungeon.util.Pair;

import static de.jdungeon.world.WorldRenderer.ROOM_SIZE;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class GameScreen extends AbstractGameScreen {

	private final static String TAG = GameScreen.class.getName();
	private static final boolean OPENGL_PROFILING_ON = false;

	private final PlayerController playerController;
	private GameScreenInputProcessor inputController;
	private GameScreenPerceptHandler perceptHandler;

	private GraphicObjectRenderer graphicObjectRenderer;
	private ViewModel worldViewModel;
	private WorldRenderer worldRenderer;
	private LibgdxCameraFlightSequenceManager movieSequenceManager;
	private OrthographicCamera camera;
	private final CameraHelper cameraHelper = new CameraHelper();

	private GUIRenderer guiRenderer;
	private OrthographicCamera cameraGUI;
	private LibgdxFocusManager focusManager;

	private FigureInfo figure;
	private final int dungeonSizeX;
	private final int dungeonSizeY;

	private GLProfiler glProfiler;

	public GameScreen(Game game, PlayerController playerController, JDPoint dungeonSize) {
		super(game);
		this.playerController = playerController;
		playerController.setGameScreen(this); // todo: untangle bidirectional dependency here

		this.dungeonSizeX = dungeonSize.getX();
		this.dungeonSizeY = dungeonSize.getY();
	}

	@Override
	public void show() {

		MusicManager.getInstance().stopCurrentMusic();
		Music music = getGame().getAudio().createMusic("music/" + "Eyes_Gone_Wrong_edited.mp3");
		MusicManager.getInstance().playMusic(music);

		//if (OPENGL_PROFILING_ON) {
		glProfiler = new GLProfiler(Gdx.graphics);
		glProfiler.enable();
		//}

		inputController = new GameScreenInputProcessor(game, playerController, this);
		Gdx.input.setInputProcessor(inputController);

		// does not seem to do anything
		//Gdx.gl.glEnable(GL20.GL_BLEND);
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


		Gdx.gl.glClearColor(0, 0, 0, 0xff / 255.0f);

		AnimationManager animationManager = new AnimationManager();

		figure = playerController.getFigure();
		perceptHandler = new GameScreenPerceptHandler(this, figure, animationManager);
		worldViewModel = new ViewModel(figure, dungeonSizeX, dungeonSizeY);
		playerController.setViewModel(worldViewModel);
		movieSequenceManager = new LibgdxCameraFlightSequenceManager(cameraHelper); // todo: access should not be static

		// init world camera and world renderer
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.setToOrtho(true, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		JDPoint number = figure.getRoomInfo().getNumber();
		camera.position.set(number.getX() * ROOM_SIZE, number.getY() * ROOM_SIZE, 0);
		camera.update();

		int screenWidth = Gdx.app.getGraphics().getWidth();
		int screenHeight = Gdx.app.getGraphics().getHeight();

		// init de.jdungeon.gui camera and de.jdungeon.gui renderer
		cameraGUI = new OrthographicCamera(screenWidth, screenHeight);
		cameraGUI.position.set(screenWidth/2, screenHeight/2, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();

		focusManager = new LibgdxFocusManager(figure);

		guiRenderer = new GUIRenderer(inputController, cameraGUI, this.game, (HeroInfo) figure, focusManager);
		guiRenderer.setGLProfiler(glProfiler);
		graphicObjectRenderer = new GraphicObjectRenderer(ROOM_SIZE, playerController);
		worldRenderer = new WorldRenderer(graphicObjectRenderer, worldViewModel, camera, cameraHelper, focusManager, animationManager);

		scrollToScale(figure.getRoomNumber(), 2f, 0.6f, CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER);


	}

	public LibgdxFocusManager getFocusManager() {
		return focusManager;
	}

	private long lastCall;

	@Override
	public void render(float deltaTime) {

		long now = System.currentTimeMillis();
		//Log.info("render call gap: "+ (now - lastCall));
		this.lastCall = now;

		if (!paused) {
			// update de.jdungeon.gui and everything
			update(deltaTime);


			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			worldRenderer.render();
			guiRenderer.render();

			// for profiling only
			if (OPENGL_PROFILING_ON) {
				Gdx.app.error(TAG, "Open GL calls: " + glProfiler.getCalls());
				Gdx.app.error(TAG, "Open GL draw calls: " + glProfiler.getDrawCalls());
				Gdx.app.error(TAG, "Open GL texture bindings: " + glProfiler.getTextureBindings());
				Gdx.app.error(TAG, "Open GL shader switches: " + glProfiler.getShaderSwitches());
				Gdx.app.error(TAG, "Open GL vertex count average: " + glProfiler.getVertexCount().average);
			}
			glProfiler.reset();
		}

		long renderCodeDuration = System.currentTimeMillis() - this.lastCall;
		//Log.info("renderCode duration: "+renderCodeDuration);
	}

	public CameraHelper getCameraHelper() {
		return cameraHelper;
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
		guiRenderer.resize(width, height);
	}

	@Override
	public void update(float deltaTime) {

		if (!paused) {

			movieSequenceManager.update(deltaTime);
			inputController.update(deltaTime);
			guiRenderer.update(deltaTime);
			worldRenderer.update(deltaTime);

			Set<JDPoint> visibilityIncreasedRooms = playerController.getVisibilityIncreasedRooms();
			this.showVisibilityIncrease(visibilityIncreasedRooms);

			List<Percept> percepts = playerController.getPercepts();
			for (Percept percept : percepts) {
				this.perceptHandler.tellPercept(percept);
			}
		}
	}

	public static String CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER = "scroll-to-player";

	private void scollToPlayerPosition() {
		scollToPlayerPosition(0.6F);
	}

	public void scollToPlayerPosition(float duration) {
		if (!movieSequenceManager.containsFlight(CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER)) {
			// check that a scroll-to-player camera flight isn't already in the queue
			scrollToScale(figure.getRoomNumber(), duration, cameraHelper.getUserSelectedZoomLevel(), CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER);
		}
	}

	private void checkCamPosition(float deltaTime) {
		if (movieSequenceManager.getCurrentSequence(deltaTime) == null) {
			// currently no movie running

		}
	}

	/*
	Creates a movie sequence that zooms in/out
 	*/

	private Pair<Float, Float> toPair(Vector2 position) {
		return new Pair<>(position.x, position.y);
	}

	private void scrollFromTo(Pair<Float, Float> start, Pair<Float, Float> target, float duration,
							  float zoom, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new TrivialScaleSequence(zoom),
				new StraightLineScroller(start,
						target, duration), duration, title);
		this.movieSequenceManager.addSequence(sequence);
	}

	public void scrollTo(JDPoint target, float duration, String title) {
		scrollFromToScale(toPair(cameraHelper.getPosition()), floatPairRoomToWorldCoordinates(target), duration, cameraHelper
				.getZoom(), cameraHelper
				.getZoom(), title);
	}

	public void scrollToScale(JDPoint target, float duration,
							  float endScale, String title) {
		scrollFromToScale(toPair(cameraHelper.getPosition()), floatPairRoomToWorldCoordinates(target), duration, cameraHelper
				.getZoom(), endScale, title);
	}

	public void scrollToScale(float duration, float endScale) {
		scrollFromToScale(toPair(cameraHelper.getPosition()), toPair(cameraHelper.getPosition()), duration, cameraHelper
				.getZoom(), endScale, "zoom");
	}

	private void scrollFromToScale(Pair<Float, Float> start, Pair<Float, Float> target, float duration,
								   float startScale, float endScale, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, endScale, duration),
				new StraightLineScroller(start,
						target, duration), duration, title);
		this.movieSequenceManager.addSequence(sequence);
	}

	private Pair<Float, Float> floatPairRoomToWorldCoordinates(JDPoint point) {
		return new Pair<>(
				(float) point.getX() * ROOM_SIZE + ROOM_SIZE / 2,
				(float) point.getY() * ROOM_SIZE + ROOM_SIZE / 2);
	}

	private Pair<Float, Float> floatPair(JDPoint point) {
		return new Pair<>(
				(float) point.getX(), (float) point.getY());
	}

	public void showVisibilityIncrease(Set<JDPoint> points) {
		if (points.isEmpty()) {
			return;
		}

		JDPoint heroRoom = figure.getRoomInfo().getPoint();
		// entered current room, no need to do de.jdungeon.animation
		points.remove(heroRoom);

		final Iterator<JDPoint> pointIterator = points.iterator();
		while (pointIterator.hasNext()) {
			final JDPoint point = pointIterator.next();
			if (heroRoom.isNeighbourIncludeCorners(point)) {
				pointIterator.remove();
			}
		}

		if (points.isEmpty()) {
			return;
		}

		filterEdgeRoomsToShow(points);

		// zoom out
		float flightScale = cameraHelper.getCurrentZoom();
		float stepDuration = 0.7f;
		Pair<Float, Float> last = toPair(cameraHelper.getPosition());
		for (JDPoint p : points) {
			// center on each discovered room
			Pair<Float, Float> next = floatPairRoomToWorldCoordinates(p);
			scrollFromTo(last, next, stepDuration, flightScale, "show visibility increase PART 2 center to each room");
			last = next;
		}

		// scroll back to hero
		scrollFromToScale(last, floatPairRoomToWorldCoordinates(figure.getRoomNumber()), 0.5f, flightScale, cameraHelper
				.getUserSelectedZoomLevel(), "show visibility increase PART 3 zoom and scroll back to hero");
	}

	/**
	 * Filters the given set of points down to a set containing
	 * the most south, the most west, the most north and the most east room.
	 * <p>
	 * For example, if a single point is given, it actually takes all for roles...
	 *
	 * @param points given points
	 */
	private void filterEdgeRoomsToShow(Set<JDPoint> points) {
		Iterator<JDPoint> iterator = points.iterator();
		JDPoint first = iterator.next();
		JDPoint mostEast = first;
		JDPoint mostWest = first;
		JDPoint mostSouth = first;
		JDPoint mostNorth = first;

		for (JDPoint point : points) {
			if (mostEast.getX() < point.getX()) {
				mostEast = point;
			}
			if (mostWest.getX() > point.getX()) {
				mostWest = point;
			}
			if (mostNorth.getY() > point.getY()) {
				mostNorth = point;
			}
			if (mostNorth.getY() < point.getY()) {
				mostSouth = point;
			}
		}

		points.clear();
		points.add(mostEast);
		points.add(mostWest);
		points.add(mostNorth);
		points.add(mostSouth);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		super.resume();
		paused = false;
	}

	@Override
	public OrthographicCamera getCamera(ScreenContext context) {
		if (context == ScreenContext.Context.GUI) {
			return this.cameraGUI;
		}
		if (context == ScreenContext.Context.WORLD) {
			return this.camera;
		}
		// may not happen
		return null;
	}

	private long lastClickTime;

	@Override
	public boolean clicked(int screenX, int screenY, int pointer, int button) {

		long now = System.currentTimeMillis();
		if (now - lastClickTime < 300) {
			// we do not allow clicks faster than 10ms one after another to filter duplicates
			return false;
		}

		lastClickTime = now;



		/*
		Check for de.jdungeon.gui element click
		 */
		Vector3 guiPosUnprojected = cameraGUI.unproject(new Vector3(screenX, screenY, 0));
		int guiXunprojected = Math.round(guiPosUnprojected.x);
		int guiYunprojected = Math.round(guiPosUnprojected.y);

		List<LibgdxGUIElement> guiElements = guiRenderer.libgdxGuiElements;
		ListIterator<LibgdxGUIElement> listIterator = guiElements.listIterator(guiElements.size());
		while (listIterator.hasPrevious()) {
			LibgdxGUIElement guiElement = listIterator.previous();
			if (guiElement.hasPoint(new JDPoint(guiXunprojected, guiYunprojected)) && guiElement.isVisible()) {
				guiElement.handleClickEvent(screenX, screenY);
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				return true;
			}
		}

		/*
		If the player has died, after the next click we show the de.jdungeon.game over popup
		 */
		Boolean dead = figure.isDead();
		if (dead != null && dead) {
			guiRenderer.getGameOverView().setShow(true);
		}

		/*
		Check for dungeon click
		 */
		boolean handled = worldRenderer.checkWorldClick(screenX, screenY, pointer, button, playerController);
		if (handled) return true;

		return false;
	}

	@Override
	public boolean pan(float x, float y, float dx, float dy) {
		boolean processed = false;
		List<LibgdxGUIElement> guiElements = guiRenderer.libgdxGuiElements;
		ListIterator<LibgdxGUIElement> listIterator = guiElements.listIterator(guiElements.size());
		while (listIterator.hasPrevious()) {
			LibgdxGUIElement guiElement = listIterator.previous();
			if (guiElement.hasPoint(new JDPoint(x, y)) && guiElement.isVisible()) {
				guiElement.handlePanEvent(x, y, dx, dy);
				processed = true;
			}
		}

		// if the pan is not for a UI element, then we do a camera move on the dungeon world
		if (!processed) {
			doCameraMove(dx, dy);
		}
		return true;
	}

	private void doCameraMove(float dx, float dy) {
		float moveFactor = 0.4f;
		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			// on desktop we need a faster move factor than on android
			moveFactor = 0.7f;
		}
		if (Gdx.app.getType() == Application.ApplicationType.Android) {
			// on desktop we need a faster move factor than on android
			moveFactor = 0.4f;
		}

		cameraHelper.getPosition().x = cameraHelper.getPosition().x - (dx * moveFactor);
		cameraHelper.getPosition().y = cameraHelper.getPosition().y - (dy * moveFactor);
	}

	@Override
	public boolean zoom(float v1, float v2) {
		cameraHelper.addZoom(v1 + v2);
		return true;
	}

	/**
	 * Checks whether the current camera position is good, i. e. are all neighbour rooms visible?
	 * If not, we scroll the player room to the middle of the screen.
	 */
	public void checkCameraPosition() {
		JDPoint number = figure.getRoomInfo().getNumber();
		Pair<Float, Float> playerWorldPosition = floatPairRoomToWorldCoordinates(number);
		Vector3 playerWorldScreenCoord = camera.project(new Vector3(playerWorldPosition.getA(), playerWorldPosition.getB(), 0));
		int screenWidth = Gdx.app.getGraphics().getWidth();
		int screenHeight = Gdx.app.getGraphics().getHeight();

		// TODO: we need to work with camera project/unproject as roomSize is world coordinates and calculation is in screen coordinates
		Vector2 currentCameraPosition = cameraHelper.getPosition();
		Vector3 cameraPosScreenCoord = camera.project(new Vector3(currentCameraPosition, 0));
		double lookAheadMargin = ROOM_SIZE * 3.5 * cameraHelper.getZoom();
		if (Math.abs(cameraPosScreenCoord.x - playerWorldScreenCoord.x) + lookAheadMargin > screenWidth / 2
				|| Math.abs(cameraPosScreenCoord.y - playerWorldScreenCoord.y) + lookAheadMargin > screenHeight / 2) {
			// neighbour room not visible on screen, hence we need camera re-positioning
			scollToPlayerPosition();
		}
	}

	public GUIRenderer getGuiRenderer() {
		return guiRenderer;
	}

	public GraphicObjectRenderer getGraphicObjectRenderer() {
		return this.graphicObjectRenderer;
	}

	public void invalidateEntityRenderCache(RoomInfoEntity location) {
		worldRenderer.invalidateEntityRenderCache(location);
	}
}