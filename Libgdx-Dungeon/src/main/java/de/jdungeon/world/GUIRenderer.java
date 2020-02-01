package de.jdungeon.world;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import dungeon.JDPoint;
import figure.hero.HeroInfo;
import text.Statement;
import util.JDDimension;

import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.GameOverView;
import de.jdungeon.app.gui.HealthBar;
import de.jdungeon.app.gui.HourGlassTimer;
import de.jdungeon.app.gui.ImageGUIElement;
import de.jdungeon.app.gui.InfoPanel;
import de.jdungeon.app.gui.TextPerceptView;
import de.jdungeon.app.gui.smartcontrol.SmartControl;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;
import de.jdungeon.gui.ZoomButton;
import de.jdungeon.libgdx.LibgdxGraphics;

/**
 * Renders the GUI (aka head up display) over the game world.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.19.
 */
public class GUIRenderer implements Disposable {

	private final GameScreenInputProcessor inputController;
	private final OrthographicCamera cameraGUI;
	private final LibgdxDungeonMain game;
	private final HeroInfo figure;
	private SpriteBatch batch;
	private Graphics graphics;
	private SmartControl smartControl;
	private InfoPanel infoPanel;
	private TextPerceptView textView;

	private FocusManager focusManager;

	protected final List<GUIElement> guiElements = new LinkedList<GUIElement>();

	private GUIImageManager guiImageManager;
	private GameOverView gameOverView;

	public GUIRenderer(GameScreenInputProcessor inputController, OrthographicCamera cameraGUI, LibgdxDungeonMain game, HeroInfo figure) {
		this.inputController = inputController;
		this.cameraGUI = cameraGUI;
		this.game = game;
		this.figure = figure;
		init();
	}

	private void init() {
		guiImageManager = new GUIImageManager(game.getFileIO().getImageLoader());
		batch = new SpriteBatch();
		cameraGUI.update();
		graphics = new LibgdxGraphics(cameraGUI, Assets.instance.fonts.defaultSmallFlipped, batch);


		/*
		 * init text messages panel
		 */
		/*
		textView = new TextPerceptView(this.game);
		this.guiElements.add(textView);
		*/

		/*
		 * init info panel
		 */

		int infoPanelWidth = (int) (game.getScreenWidth() * 0.2);
		int infoPanelHeight = (int) (game.getScreenHeight() * 0.4);
		infoPanel = new InfoPanel(new JDPoint(game.getScreenWidth()  - infoPanelWidth, 0),
				new JDDimension(infoPanelWidth, infoPanelHeight), new ScreenAdapter(game), game);
		this.guiElements.add(infoPanel);
		focusManager = new FocusManager(infoPanel, figure);


		/*
		 * init health bars
		 */
		int posX = 22;
		JDPoint healthBarPosition = new JDPoint(posX, 5);
		HealthBar healthView = new HealthBar(healthBarPosition, new JDDimension(160, 20), figure, HealthBar.Kind.health, this.game);
		this.guiElements.add(healthView);
		JDPoint dustBarPosition = new JDPoint(posX, 25);
		HealthBar dustView = new HealthBar(dustBarPosition, new JDDimension(160, 20), figure, HealthBar.Kind.dust, this.game);
		this.guiElements.add(dustView);

		/*
		 * init hour glass
		 */
		HourGlassTimer hourglass = new HourGlassTimer(new JDPoint(30, 50), new JDDimension(36, 60), new ScreenAdapter(game),figure,  this.game);
		this.guiElements.add(hourglass);


		/*
		add +/- magnifier
		 */
		ImageGUIElement magnifier = new ImageGUIElement(new JDPoint(26, 156), new JDDimension(44, 70), getGUIImage(GUIImageManager.LUPE2), game) {

			@Override
			public boolean handleTouchEvent(Input.TouchEvent touch) {
				inputController.scrollToPlayer();
				return true;
			}
		};
		this.guiElements.add(new ZoomButton(new JDPoint(30, 120), new JDDimension(36, 36), inputController, getGUIImage(GUIImageManager.PLUS), true));
		this.guiElements.add(new ZoomButton(new JDPoint(30, 224), new JDDimension(36, 36), inputController, getGUIImage(GUIImageManager.MINUS), false));
		this.guiElements.add(magnifier);

		JDDimension screenSize = new JDDimension(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
		smartControl = new SmartControl(new JDPoint(0, 0), screenSize, new ScreenAdapter(game), game, figure, inputController
				.getPlayerController().getActionAssembler(), focusManager);
		this.guiElements.add(smartControl);


		/*
		 * init game over view
		 */
		int width = game.getScreenWidth();
		int height = game.getScreenHeight();
		int widthFifth = (width / 5);
		int heightFifth = (height / 4);
		gameOverView = new GameOverView(new JDPoint((width / 2) - widthFifth,
				(height / 2) - heightFifth), new JDDimension(2 * widthFifth,
				2 * heightFifth), new ScreenAdapter(game), game);
		this.guiElements.add(gameOverView);

	}

	private Image getGUIImage(String filename) {
		return (Image) GUIImageManager.getImageProxy(filename, game.getFileIO().getImageLoader()).getImage();
	}

	public FocusManager getFocusManager() {
		return focusManager;
	}

	public void render() {



		batch.setProjectionMatrix(cameraGUI.combined);
		//batch.begin();
		renderGUIElements();
		//renderGuiScore();
		renderFPSCounter();
		//renderGrid();
		//batch.end();

	}

	private void updateGUIElements(float deltaTime) {
		for (GUIElement guiElement : this.guiElements) {
			if (guiElement.isVisible()) {
				guiElement.update(deltaTime);
			}
		}
	}

	public GameOverView getGameOverView() {
		return gameOverView;
	}

	private void renderGUIElements() {

		for (GUIElement guiElement : this.guiElements) {
			if (guiElement.isVisible()) {
				if (guiElement.needsRepaint()) {
					guiElement.paint(graphics, new JDPoint(0,0));
				}
			}
		}

	}
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private void renderGrid() {
		batch.setProjectionMatrix(cameraGUI.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(cameraGUI.combined);
		shapeRenderer.setColor(Color.GREEN);
		for(int i = 0; i < 10; i++) {
			shapeRenderer.ellipse((i*100)-2, (i*100)-2, 4, 4);
		}
		shapeRenderer.end();

	}

	private void renderGuiScore() {
		float x = -15;
		float y = -15;
		batch.begin();
		AssetFonts.instance.defaultBigFlipped.draw(batch, "0", x + 75, y + 37);
		batch.end();

	}

	private void renderFPSCounter() {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight -15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = AssetFonts.instance.defaultNormalFlipped;
		if(fps >= 45) {
			fpsFont.setColor(0,1,0,1);
		} else if(fps >= 30) {
			fpsFont.setColor(1,1,0,1);
		} else  {
			fpsFont.setColor(1,0,0,1);
		}
		batch.begin();
		fpsFont.draw(batch, "FPS: "+fps, x, y);
		fpsFont.setColor(1,1,1,1); //white
		batch.end();
	}

	public void resize(int width, int height) {
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth =  (Constants.VIEWPORT_GUI_HEIGHT / height) * width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight/2, 0);
		cameraGUI.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public void update(float deltaTime) {
		updateGUIElements(deltaTime);
	}

	public void newStatement(Statement s) {
		if(textView != null) {
			this.textView.addTextPercept(s);
		}
	}
}
