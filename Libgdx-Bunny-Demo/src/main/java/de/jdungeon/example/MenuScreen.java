package de.jdungeon.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class MenuScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();

	SpriteBatch batch = new SpriteBatch();
	Texture bgImageTx;
	Texture buttImageTx;


	private Stage stage;
	private Skin skinCanyonBunny;

	private Image imgBackground;
	private Image imgLogo;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	private Button btnMenuQuit;


	private Window winOptions;
	private TextButton btnWinOptionSave;
	private TextButton btnWinOptionCancel;
	private CheckBox checkSound;

	private final float DEBUG_REBUILD_INTERVALL = 5;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public MenuScreen(Game game) {
		super(game);
		init();
	}

	public void init() {
		bgImageTx = new Texture(Gdx.files.internal("data/haunted-castle.jpg"));
		buttImageTx = new Texture(Gdx.files.internal("data/vector-button.png"));

	}

	private void rebuildStage() {
		//skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

		Table layerBackground  = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerLogos = buildLogoLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();

		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerLogos);
		stack.add(layerControls);
		stack.add(layerOptionsWindow);
	}

	private Table buildOptionsWindowLayer() {
		Table layer = new Table();
		return layer;
	}

	private Table buildControlsLayer() {
		Table layer = new Table();
		//layer.center().top();
		btnMenuPlay = new Button(new TextureRegionDrawable(buttImageTx));
		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				onPlayClicked();
			}
		});
		btnMenuPlay.setPosition(200, 300);
		btnMenuPlay.setSize(10, 4);
		layer.add(btnMenuPlay);

		//layer.row();
		btnMenuOptions = new Button(new TextureRegionDrawable(buttImageTx));
		btnMenuOptions.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				optionsClicked();
			}
		});
		btnMenuOptions.setPosition(200, 200);
		btnMenuOptions.setSize(10, 4);
		layer.add(btnMenuOptions);

		/*
		//layer.row();
		btnMenuQuit = new Button(new TextureRegionDrawable(buttImageTx));
		btnMenuQuit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				quitClicked();
			}
		});
		btnMenuOptions.setPosition(400, 100);
		layer.addActor(btnMenuQuit);
		*/

		return layer;
	}

	private void quitClicked() {
		game.dispose();
	}

	private void optionsClicked() {
		// todo:
	}

	private void onPlayClicked() {
		game.setScreen(new GameScreen(game));
	}

	private Table buildLogoLayer() {
		Table layer = new Table();
		return layer;
	}

	private Table buildObjectsLayer() {
		Table layer = new Table();
		return layer;
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
		//Table.drawDebug(stage);

		/*
		if(Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
		}

		batch.begin();
		batch.draw(bgImageTx, 0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight(), 0 , 0, bgImageTx.getWidth(), bgImageTx.getHeight(), false, false); //,  0,0, bgImageTx.getWidth() ,bgImageTx.getHeight()
		batch.end();
		*/
	}

	@Override
	public void resize(int i, int i1) {
	 // todo: how to set Viewport to stage?
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
