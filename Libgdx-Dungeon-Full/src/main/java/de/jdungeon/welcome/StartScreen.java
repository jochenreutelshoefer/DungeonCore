package de.jdungeon.welcome;

import com.badlogic.gdx.Application;
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
import de.jdungeon.asset.Assets;
import de.jdungeon.event.EventManager;

import de.jdungeon.game.*;
import de.jdungeon.app.audio.MusicManager;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.Constants;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class StartScreen extends AbstractScreen {

	private static final String TAG = StartScreen.class.getName();

	SpriteBatch batch = new SpriteBatch();
	Texture bgImageTx;


	private Stage stage;

	private final float DEBUG_REBUILD_INTERVALL = 5;
	private final boolean debugEnabled = false;
	private float debugRebuildStage;

	public StartScreen(Game game) {
		super(game);
		init();
	}

	@Override
	public boolean clicked(int screenX, int screenY, int pointer, int button) {
		// uses Scene2d voodoo
		return false;
	}

	@Override
	public boolean pan(float x, float y, float dx, float dy) {
		return false;
	}

	@Override
	public boolean zoom(float v1, float v2) {
		return false;
	}

	@Override
	public void init() {

		String backgroundFileName = "haunted-castle.jpg";
		if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			backgroundFileName = "assets/"+backgroundFileName;
		}
		bgImageTx = new Texture(Gdx.files.internal(backgroundFileName));
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
		Audio audio = getGame().getAudio();
		Music music = audio.createMusic( "Exciting_Trailer.mp3");
		MusicManager.getInstance().playMusic(music);
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isTouched()) {
			EventManager.getInstance().fireEvent(new StartNewGameEvent());
			// we should stop rendering _this_ screen now
			return;
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
		String gameTitle = Assets.instance.getTextBundle().get("game_title");
		float fontSizeBig = AssetFonts.FONT_SIZE_BIG;
		BitmapFont defaultTitleFont = AssetFonts.instance.defaultBig;
		defaultTitleFont.draw(batch, gameTitle, Gdx.app.getGraphics().getWidth()/2 - 100,Gdx.app.getGraphics().getHeight()*2/3);

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
