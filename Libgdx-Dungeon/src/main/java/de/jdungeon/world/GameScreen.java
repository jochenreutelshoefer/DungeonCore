package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import figure.percept.Percept;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.game.ScreenContext;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class GameScreen extends AbstractGameScreen {

	private final static String TAG = GameScreen.class.getName();
	private final PlayerController playerController;

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private GUIRenderer guiRenderer;

	private boolean paused;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private final GameScreenPerceptHandler perceptHandler;

	public GameScreen(LibgdxDungeonMain game, PlayerController playerController) {
		super(game);
		this.playerController = playerController;
		perceptHandler = new GameScreenPerceptHandler(this);
	}

	@Override
	public void show() {
		worldController = new WorldController(game);


		// init world camera and world renderer
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		worldRenderer = new WorldRenderer(worldController, camera);

		// init gui camera and gui renderer
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();
		guiRenderer = new GUIRenderer(worldController, cameraGUI);
	}

	@Override
	public void render(float deltaTime) {
		if(!paused) {
			worldController.update(deltaTime);
		}

		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
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
}
