package de.jdungeon.world;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dungeon.JDPoint;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.percept.Percept;
import graphics.GraphicObjectRenderer;
import text.Statement;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.CameraHelper;
import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.movieSequence.CameraFlightSequence;
import de.jdungeon.app.movieSequence.DefaultMovieSequence;
import de.jdungeon.app.movieSequence.StraightLineScroller;
import de.jdungeon.app.movieSequence.TrivialScaleSequence;
import de.jdungeon.app.movieSequence.ZoomSequence;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScreenContext;
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

	private GameScreenInputController inputController;
	private ViewModel viewModel;
	private WorldRenderer worldRenderer;
	private GUIRenderer guiRenderer;
	private LibgdxCameraFlightSequenceManager movieSequenceManager;

	private boolean paused;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private final CameraHelper cameraHelper = new CameraHelper();
	;

	private GameScreenPerceptHandler perceptHandler;
	private FigureInfo figure;
	private final int dungeonSizeX;

	private final int dungeonSizeY;
	private GLProfiler glProfiler;

	public GameScreen(LibgdxDungeonMain game, PlayerController playerController, JDPoint dungeonSize) {
		super(game);
		this.playerController = playerController;
		playerController.setGameScreen(this); // todo: untangle bidirectional dependency here

		this.dungeonSizeX = dungeonSize.getX();
		this.dungeonSizeY = dungeonSize.getY();
	}

	@Override
	public void show() {

		if (OPENGL_PROFILING_ON) {
			glProfiler = new GLProfiler(Gdx.graphics);
			glProfiler.enable();
		}

		inputController = new GameScreenInputController(game, playerController, this);
		Gdx.input.setInputProcessor(inputController);

		figure = playerController.getFigure();
		perceptHandler = new GameScreenPerceptHandler(this, figure);
		viewModel = new ViewModel(figure, dungeonSizeX, dungeonSizeY);
		playerController.setViewModel(viewModel);
		movieSequenceManager = new LibgdxCameraFlightSequenceManager(cameraHelper); // todo: access should not be static

		// init world camera and world renderer
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.setToOrtho(true, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		JDPoint number = figure.getRoomInfo().getNumber();
		camera.position.set(number.getX() * ROOM_SIZE, number.getY() * ROOM_SIZE, 0);
		camera.update();

		// init gui camera and gui renderer
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();

		guiRenderer = new GUIRenderer(inputController, cameraGUI, this.game, (HeroInfo) figure);
		worldRenderer = new WorldRenderer(new GraphicObjectRenderer(ROOM_SIZE, playerController), viewModel, camera, cameraHelper, guiRenderer
				.getFocusManager());

		scrollToScale(figure.getRoomNumber(), 1.4f, 0.6f, CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER);
	}

	public FocusManager getFocusManager() {
		return guiRenderer.getFocusManager();
	}

	@Override
	public void render(float deltaTime) {

		if (!paused) {
			// update gui and everything
			update(deltaTime);

			Gdx.gl.glClearColor(0, 0, 0, 0xff / 255.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			worldRenderer.render();
			guiRenderer.render();

			// for profiling only
			if (OPENGL_PROFILING_ON) {
				Gdx.app.error(TAG, "Open GL calls: " + glProfiler.getCalls());
				Gdx.app.error(TAG, "Open GL draw calls: " + glProfiler.getDrawCalls());
				Gdx.app.error(TAG, "Open GL texture bindings: " + glProfiler.getTextureBindings());
				glProfiler.reset();
			}
		}
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

		movieSequenceManager.update(deltaTime);
		inputController.update(deltaTime);
		guiRenderer.update(deltaTime);
		worldRenderer.update(deltaTime);

		Set<JDPoint> visibilityIncreasedRooms = playerController.getVisibilityIncreasedRooms();
		this.showVisibilityIncrease(visibilityIncreasedRooms);

		// TODO: fetch and show visibility increased rooms from PlayerController/JDGUI
		List<Percept> percepts = playerController.getPercepts();
		for (Percept percept : percepts) {
			this.perceptHandler.tellPercept(percept);
		}
		// TODO: handle and display percepts
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
		// entered current room, no need to do animation
		points.remove(heroRoom);

		final Iterator<JDPoint> pointIterator = points.iterator();
		while (pointIterator.hasNext()) {
			final JDPoint point = pointIterator.next();
			if (heroRoom.isNeighbour(point)) {
				pointIterator.remove();
			}
		}

		if (points.isEmpty()) {
			return;
		}

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
		if(now - lastClickTime < 10) {
			// we do not allow clicks faster than 10ms one after another to filter duplicates
			return false;
		}

		lastClickTime = now;

		/*
		If the player has died, after the next click we show the game over popup
		 */
		Boolean dead = figure.isDead();
		if (dead != null && dead) {
			guiRenderer.getGameOverView().setShow(true);
		}


		/*
		Check for gui element click
		 */
		Vector3 guiPosUnprojected = cameraGUI.unproject(new Vector3(screenX, screenY, 0));
		int guiXunprojected = Math.round(guiPosUnprojected.x);
		int guiYunprojected = Math.round(guiPosUnprojected.y);

		List<GUIElement> guiElements = guiRenderer.guiElements;
		ListIterator<GUIElement> listIterator = guiElements.listIterator(guiElements.size());
		while (listIterator.hasPrevious()) {
			GUIElement guiElement = listIterator.previous();
			if (guiElement.hasPoint(new JDPoint(guiXunprojected, guiYunprojected)) && guiElement.isVisible()) {
				Input.TouchEvent touchEvent = new Input.TouchEvent();
				touchEvent.x = screenX;
				touchEvent.y = screenY;
				guiElement.handleTouchEvent(touchEvent);
				return true;
			}
		}

		/*
		Check for dungeon click
		 */
		boolean handled = worldRenderer.checkWorldClick(screenX, screenY, pointer, button, playerController);
		if(handled) return true;


		return false;
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
		double lookAheadMargin = ROOM_SIZE * 2.5 * cameraHelper.getZoom();
		if (Math.abs(cameraPosScreenCoord.x - playerWorldScreenCoord.x) + lookAheadMargin > screenWidth / 2
				|| Math.abs(cameraPosScreenCoord.y - playerWorldScreenCoord.y) + lookAheadMargin > screenHeight / 2) {
			// neighbour room not visible on screen, hence we need camera re-positioning
			scollToPlayerPosition();
		}
	}

	public GUIRenderer getGuiRenderer() {
		return guiRenderer;
	}
}
