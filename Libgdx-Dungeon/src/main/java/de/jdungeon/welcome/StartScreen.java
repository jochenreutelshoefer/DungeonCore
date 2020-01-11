package de.jdungeon.welcome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import event.EventManager;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.Constants;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.world.GameScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class StartScreen extends AbstractGameScreen {

	private static final String TAG = StartScreen.class.getName();

	SpriteBatch batch = new SpriteBatch();
	Texture bgImageTx;


	private Stage stage;

	private final float DEBUG_REBUILD_INTERVALL = 5;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public StartScreen(LibgdxDungeonMain game) {
		super(game);
		init();
	}

	@Override
	public void init() {
		bgImageTx = new Texture(Gdx.files.internal("haunted-castle.jpg"));

	}

	@Override
	public void backButton() {

	}

	private void rebuildStage() {
		//skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

		Table layerBackground  = buildBackgroundLayer();

		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
	}

	private Table buildBackgroundLayer() {
		Table layer = new Table();
		Image bgImage = new Image(bgImageTx);
		layer.add(bgImage);
		return layer;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isTouched()) {
			EventManager.getInstance().fireEvent(new StartNewGameEvent());
			//game.setScreen(new GameScreen(game));
		}

		if(debugEnabled) {
			debugRebuildStage -= deltaTime;
			if(debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVALL;
				rebuildStage();
			}
		}

		stage.act(deltaTime);
		stage.draw();
		stage.setDebugAll(true);

		BitmapFont fpsFont = AssetFonts.instance.defaultNormal;
		fpsFont.setColor(1,1,1,1); //white
		batch.begin();
		fpsFont.draw(batch, "Taste drücken zum Start ", Gdx.app.getGraphics().getWidth()/2 - 100,Gdx.app.getGraphics().getHeight()/2);
		batch.end();

	}

	@Override
	public void resize(int i, int i1) {
	 // todo: how to set Viewport to stage?
	}

	@Override
	public void update(float deltaTime) {
		int foo = 0;
	}

	@Override
	public void paint(float deltaTime) {
		render(deltaTime);
	}

	@Override
	public OrthographicCamera getCamera(ScreenContext context) {
		// currently we have not camera here as we use Scene2D for rendering
		return null;
	}

	@Override
	public void pause() {

	}

	@Override
	public void hide() {
		super.hide();
		stage.dispose();
		//skinCanyonBunny.dispose();
	}
}
