package de.jdungeon.world;

import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.percept.Percept;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScreenContext;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class GameScreen extends AbstractGameScreen {

	private final static String TAG = GameScreen.class.getName();
	private final PlayerController playerController;

	private InputController worldController;
	private ViewModel viewModel;
	private WorldRenderer worldRenderer;
	private GUIRenderer guiRenderer;

	private boolean paused;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private GameScreenPerceptHandler perceptHandler;
	private FigureInfo figure;

	private final int dungeonSizeX;
	private final int dungeonSizeY;

	public GameScreen(LibgdxDungeonMain game, PlayerController playerController, JDPoint dungeonSize) {
		super(game);
		this.playerController = playerController;
		this.dungeonSizeX = dungeonSize.getX();
		this.dungeonSizeY = dungeonSize.getY();
	}

	@Override
	public void show() {
		worldController = new InputController(game, playerController, this);
		perceptHandler = new GameScreenPerceptHandler(this);
		figure = playerController.getFigure();
		viewModel = new ViewModel(figure, dungeonSizeX, dungeonSizeY);
		playerController.setViewModel(viewModel);
		// init world camera and world renderer
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.setToOrtho(true, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		JDPoint number = figure.getRoomInfo().getNumber();
		// todo: set camera start position correctly - how?
		//camera.lookAt(0, 0, 0);
		camera.position.set(number.getX() * worldRenderer.roomSize +1000, number.getY() * worldRenderer.roomSize +1000, 0);
		camera.update();
		worldRenderer = new WorldRenderer(worldController, playerController, viewModel, camera);

		// init gui camera and gui renderer
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();
		guiRenderer = new GUIRenderer(worldController, cameraGUI, this.game, (HeroInfo)figure);

	}

	@Override
	public void render(float deltaTime) {
		if(!paused) {
			worldController.update(deltaTime);
		}

		Gdx.gl.glClearColor(0, 0, 0, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();
		guiRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
		guiRenderer.resize(width, height);
	}

	@Override
	public void update(float deltaTime) {
		// TODO: fetch and show visibility increased rooms from PlayerController/JDGUI
		List<Percept> percepts = playerController.getPercepts();
		// TODO: handle and display percepts
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
		if(context == ScreenContext.Context.GUI) {
			return this.cameraGUI;
		}
		if(context == ScreenContext.Context.WORLD) {
			return this.camera;
		}
		// may not happen
		return null;
	}

	public boolean clicked(int screenX, int screenY, int pointer, int button) {

		/*
		Check for gui element click
		 */
		List<GUIElement> guiElements = guiRenderer.guiElements;
		ListIterator<GUIElement> listIterator = guiElements.listIterator(guiElements.size());
		while (listIterator.hasPrevious()) {
			GUIElement guiElement = listIterator.previous();
			if (guiElement.hasPoint(new JDPoint(screenX, screenY)) && guiElement.isVisible()) {
				//Log.i("touch event fired", this.getClass().getSimpleName()+": touch event fired");
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
		if(room == null) return false;

		Object clickedObjectInRoom = room.findClickedObjectInRoom(new JDPoint(worldXunprojected, worldYunprojected), roomX * WorldRenderer.roomSize, roomY * WorldRenderer.roomSize);
		if(clickedObjectInRoom != null) {
			playerController.getActionController().objectClicked(clickedObjectInRoom, false);
		}
		return false;
	}
}
