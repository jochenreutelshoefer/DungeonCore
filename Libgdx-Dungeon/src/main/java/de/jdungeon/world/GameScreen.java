package de.jdungeon.world;

import java.util.List;
import java.util.ListIterator;

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

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.movieSequence.DefaultMovieSequence;
import de.jdungeon.app.movieSequence.CameraFlightSequence;
import de.jdungeon.app.movieSequence.StraightLineScroller;
import de.jdungeon.app.movieSequence.TrivialScaleSequence;
import de.jdungeon.app.movieSequence.ZoomSequence;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.util.Pair;

import static de.jdungeon.world.LibgdxCameraFlightSequenceManager.SCALE_COMPATIBILITY_FACTOR;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class GameScreen extends AbstractGameScreen {

	private final static String TAG = GameScreen.class.getName();
	private final PlayerController playerController;

	private GameScreenInputController inputController;
	private ViewModel viewModel;
	private WorldRenderer worldRenderer;
	private GUIRenderer guiRenderer;
	private LibgdxCameraFlightSequenceManager movieSequenceManager;

	private boolean paused;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
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
		glProfiler = new GLProfiler(Gdx.graphics);
		glProfiler.enable();
		inputController = new GameScreenInputController(game, playerController, this);
		perceptHandler = new GameScreenPerceptHandler(this);
		figure = playerController.getFigure();
		viewModel = new ViewModel(figure, dungeonSizeX, dungeonSizeY);
		playerController.setViewModel(viewModel);
		movieSequenceManager = new LibgdxCameraFlightSequenceManager(GameScreenInputController.cameraHelper); // todo: access should not be static

		// init world camera and world renderer
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.setToOrtho(true, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		JDPoint number = figure.getRoomInfo().getNumber();
		camera.position.set(number.getX() * worldRenderer.roomSize, number.getY() * worldRenderer.roomSize, 0);
		camera.update();
		worldRenderer = new WorldRenderer(inputController, playerController, viewModel, camera);

		// init gui camera and gui renderer
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();
		guiRenderer = new GUIRenderer(inputController, cameraGUI, this.game, (HeroInfo) figure);

		scollToPlayerPosition(1.4f);
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
			/*
			Gdx.app.error(TAG, "Open GL calls: " + glProfiler.getCalls());
			Gdx.app.error(TAG, "Open GL draw calls: " + glProfiler.getDrawCalls());
			Gdx.app.error(TAG, "Open GL texture bindings: " + glProfiler.getTextureBindings());
			glProfiler.reset();
			*/
		}
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
		// TODO: fetch and show visibility increased rooms from PlayerController/JDGUI
		List<Percept> percepts = playerController.getPercepts();
		// TODO: handle and display percepts
	}

	public static String CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER ="scroll-to-player";

	private void scollToPlayerPosition() {
		scollToPlayerPosition(0.6F);
	}

	private void scollToPlayerPosition(float duration) {
		if(!movieSequenceManager.containsFlight(CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER)) {
			// check that a scroll-to-player camera flight isn't already in the queue
			scrollToScale(figure.getRoomNumber(), duration, 60,CAMERA_FLIGHT_TAG_SCROLL_TO_PLAYER);
		}
	}

	private void checkCamPosition(float deltaTime) {
		if(movieSequenceManager.getCurrentSequence(deltaTime) == null) {
			// currently no movie running

		}
	}

	/*
	Creates a movie sequence that zooms in/out
 	*/
	private void zoomToSize(int duration, float roomScale, String title) {
		zoomToSize(duration, roomScale, figure.getRoomNumber(), title);
	}

	private void zoomToSize(int duration, float targetScale, JDPoint position, String title) {
		zoomToSize(duration, worldRenderer.roomSize, targetScale, position, title);
	}

	private void zoomToSize(int duration, float startScale, float targetScale, JDPoint position, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, targetScale, duration),
				new StraightLineScroller(WorldRenderer.getPlayerRoomWorldPosition(figure), floatPair(position), duration), duration, title);
		this.movieSequenceManager.addSequence(sequence);
	}

	public void scrollTo(JDPoint number, float duration, String title) {
		scrollTo(number, duration, (int) worldRenderer.roomSize, title);
	}

	public void scrollTo(JDPoint number, float duration, int roomScale, String title) {
		scrollFromTo(toPair(GameScreenInputController.cameraHelper.getPosition()), floatPairRoomToWorldCoordinates(number), duration, roomScale, title);
	}

	private Pair<Float, Float> toPair(Vector2 position) {
		return new Pair<>(position.x, position.y);
	}

	private void scrollFromTo(JDPoint start, JDPoint target, float duration, int roomScale, String title) {
		scrollFromTo(floatPair(start), floatPair(target), duration, roomScale, title);
	}

	private void scrollFromTo(Pair<Float, Float> start, Pair<Float, Float> target, float duration,
							  int roomScale, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new TrivialScaleSequence(roomScale),
				new StraightLineScroller(start,
						target, duration), duration, title);
		this.movieSequenceManager.addSequence(sequence);
	}

	private void scrollToScale(JDPoint target, float duration,
								   int endScale, String title) {
		scrollFromToScale(toPair(GameScreenInputController.cameraHelper.getPosition()), floatPairRoomToWorldCoordinates(target), duration, (int) (GameScreenInputController.cameraHelper.getZoom()*SCALE_COMPATIBILITY_FACTOR), endScale, title);
	}

	private void scrollFromToScale(JDPoint start, JDPoint target, float duration, int startScale,
								   int endScale, String title) {
		scrollFromToScale(floatPair(start), floatPair(target), duration, startScale, endScale, title);
	}

	private void scrollFromToScale(Pair<Float, Float> start, Pair<Float, Float> target, float duration,
								   int startScale, int endScale, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, endScale, duration),
				new StraightLineScroller(start,
						target, duration), duration, title);
		this.movieSequenceManager.addSequence(sequence);
	}

	private Pair<Float, Float> floatPairRoomToWorldCoordinates(JDPoint point) {
		return new Pair<>(
				(float) point.getX() * WorldRenderer.roomSize + WorldRenderer.roomSize/2,
				(float) point.getY() * WorldRenderer.roomSize + WorldRenderer.roomSize/2);
	}

	private Pair<Float, Float> floatPair(JDPoint point) {
		return new Pair<>(
				(float) point.getX(), (float) point.getY());
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

	@Override
	public boolean clicked(int screenX, int screenY, int pointer, int button) {


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
		Vector3 worldPosUnprojected = camera.unproject(new Vector3(screenX, screenY, 0));
		int worldXunprojected = Math.round(worldPosUnprojected.x);
		int worldYunprojected = Math.round(worldPosUnprojected.y);

		int roomX = worldXunprojected / WorldRenderer.roomSize;
		int roomY = worldYunprojected / WorldRenderer.roomSize;

		ViewRoom room = this.viewModel.getRoom(roomX, roomY);
		if (room == null) return false;

		Object clickedObjectInRoom = room.findClickedObjectInRoom(new JDPoint(worldXunprojected, worldYunprojected), roomX * WorldRenderer.roomSize, roomY * WorldRenderer.roomSize);
		if (clickedObjectInRoom != null) {
			playerController.getActionController().objectClicked(clickedObjectInRoom, false);
		}
		return false;
	}

	/**
	 * Checks whether the current camera position is good, i. e. are all neighbour rooms visible?
	 * If not, we scroll the player room to the middle of the screen.
	 */
	public void checkCameraPosition() {
		JDPoint number = figure.getRoomInfo().getNumber();
		Pair<Float, Float> playerWorldPosition = floatPairRoomToWorldCoordinates(number);
		int screenWidth = Gdx.app.getGraphics().getWidth();
		int screenHeight = Gdx.app.getGraphics().getHeight();


		Vector2 currentCameraPosition = GameScreenInputController.cameraHelper.getPosition();
		double lookAheadMarginX = WorldRenderer.roomSize * 3.5;  // for some reason we need more as expected on X to make it work as desired, todo: is weird - clarify
		double lookAheadMarginY = WorldRenderer.roomSize * 2.5;
		if(			Math.abs(currentCameraPosition.x - playerWorldPosition.getA()) + lookAheadMarginX >  screenWidth  / 2
				||	Math.abs(currentCameraPosition.y - playerWorldPosition.getB()) + lookAheadMarginY >  screenHeight / 2) {
			// neighbour room not visible on screen, hence we need camera re-positioning
			scollToPlayerPosition();
		}


	}
}
