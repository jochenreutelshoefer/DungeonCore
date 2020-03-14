package de.jdungeon.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Disposable;
import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import graphics.ImageManager;
import item.Item;
import item.ItemInfo;
import text.Statement;
import util.JDDimension;

import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.TextPerceptView;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.screen.InfoMessagePopupEvent;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.gui.ImageLibgdxGUIElement;
import de.jdungeon.gui.LibgdxFocusManager;
import de.jdungeon.gui.LibgdxGUIElement;
import de.jdungeon.gui.LibgdxGameOverView;
import de.jdungeon.gui.LibgdxHealthBar;
import de.jdungeon.gui.LibgdxHourGlassTimer;
import de.jdungeon.gui.LibgdxInfoPanel;
import de.jdungeon.gui.LibgdxSkillActivityProvider;
import de.jdungeon.gui.LibgdxUseItemActivityProvider;
import de.jdungeon.gui.LibgdxUseSkillPresenter;
import de.jdungeon.gui.ZoomButton;
import de.jdungeon.gui.itemwheel.LibgdxItemWheel;
import de.jdungeon.gui.thumb.SmartControlPanel;

import static de.jdungeon.gui.thumb.SmartControlPanel.SMART_CONTROL_SIZE;

/**
 * Renders the GUI (aka head up display) above the game world.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.19.
 */
public class GUIRenderer implements Disposable {

	private final static String TAG = GUIRenderer.class.getName();

	private final GameScreenInputProcessor inputController;
	private final OrthographicCamera cameraGUI;
	private final HeroInfo figure;
	private final LibgdxDungeonMain game;
	private SpriteBatch batch;
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private SmartControlPanel smartControl;
	private LibgdxInfoPanel infoPanel;
	private TextPerceptView textView;


	private final LibgdxFocusManager focusManager;

	final List<LibgdxGUIElement> libgdxGuiElements = new ArrayList<>();

	private LibgdxGameOverView gameOverView;
	private GLProfiler glProfiler;
	private LibgdxItemWheel itemWheelHeroItems;
	private String message;

	private GUIImageManager guiImageManager;
	private InventoryImageManager inventoryImageManager;

	public GUIRenderer(GameScreenInputProcessor inputController, OrthographicCamera cameraGUI, LibgdxDungeonMain game, HeroInfo figure, LibgdxFocusManager focusManager) {
		this.inputController = inputController;
		this.cameraGUI = cameraGUI;
		cameraGUI.update();
		this.game = game;
		this.figure = figure;
		this.focusManager = focusManager;
		init();
	}

	private void reinit() {
		libgdxGuiElements.clear();
		init();
	}

	private void init() {
		guiImageManager = new GUIImageManager(game.getFileIO().getImageLoader());
		inventoryImageManager = new InventoryImageManager(guiImageManager);

		batch = new SpriteBatch();
		shapeRenderer.setAutoShapeType(true); // TODO: research and refactor w.r.t. ShapeTypes



		int screenWidth = Gdx.app.getGraphics().getWidth();
		int screenHeight = Gdx.app.getGraphics().getHeight();

		Gdx.app.log(TAG, "Initializing GUIRenderer with screen width: "+screenWidth +" x "+screenHeight);

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

		int infoPanelHeight = (int) (screenHeight * 0.4);
		int infoPanelWidth = (int) (infoPanelHeight * 0.8);
		infoPanel = new LibgdxInfoPanel(new JDPoint(10, 40),
				new JDDimension(infoPanelWidth, infoPanelHeight), guiImageManager);
		this.libgdxGuiElements.add(infoPanel);
		focusManager.setInfoPanel(infoPanel);


		/*
		 * init health bars
		 */
		/*
		*/

		int posX = screenWidth - 170;
		JDPoint healthBarPosition = new JDPoint(posX, 5);
		LibgdxHealthBar healthView = new LibgdxHealthBar(healthBarPosition, new JDDimension(160, 20), figure, LibgdxHealthBar.Kind.health);
		this.libgdxGuiElements.add(healthView);
		JDPoint dustBarPosition = new JDPoint(posX, 25);
		LibgdxHealthBar dustView = new LibgdxHealthBar(dustBarPosition, new JDDimension(160, 20), figure, LibgdxHealthBar.Kind.dust);
		this.libgdxGuiElements.add(dustView);

		/*
		 * init hour glass
		 */

		int offsetFromRightBorder = screenWidth - 40;
		LibgdxHourGlassTimer hourglass = new LibgdxHourGlassTimer(new JDPoint(offsetFromRightBorder, 50), new JDDimension(34, 60), figure, this.guiImageManager);
		this.libgdxGuiElements.add(hourglass);


		/*
		add +/- magnifier
		 */
		ImageLibgdxGUIElement magnifier = new ImageLibgdxGUIElement(new JDPoint(offsetFromRightBorder - 4, 156), new JDDimension(44, 70), GUIImageManager.LUPE2) {

			@Override
			public boolean handleClickEvent(int x, int y) {
				inputController.scrollToPlayer();
				return true;
			}
		};
		this.libgdxGuiElements.add(new ZoomButton(new JDPoint(offsetFromRightBorder, 120), new JDDimension(36, 36), inputController, GUIImageManager.PLUS, true));
		this.libgdxGuiElements.add(new ZoomButton(new JDPoint(offsetFromRightBorder, 224), new JDDimension(36, 36), inputController, GUIImageManager.MINUS, false));
		this.libgdxGuiElements.add(magnifier);


		/*
		 * init smart thumb control
		 */
		JDDimension screenSize = new JDDimension(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
		JDPoint smartControlRoomPanelPosition = new JDPoint(screenSize.getWidth() - SMART_CONTROL_SIZE, screenSize.getHeight() / 2 + 50 - SMART_CONTROL_SIZE / 2);
		Gdx.app.log(TAG, "Initializing Smart Control Panel at: "+smartControlRoomPanelPosition.getX() +" / "+smartControlRoomPanelPosition.getY());
		JDDimension smartControlRoomPanelSize = new JDDimension(SMART_CONTROL_SIZE, SMART_CONTROL_SIZE);
		smartControl = new SmartControlPanel( smartControlRoomPanelPosition, smartControlRoomPanelSize, guiImageManager, figure, inputController.getPlayerController().getActionAssembler());
		this.libgdxGuiElements.add(smartControl);



		/*
		 * init hero item wheel
		 */
		int selectedIndexItem = 17;
		int wheelSize = 350;
		JDDimension itemWheelSize = new JDDimension(wheelSize, wheelSize);
		double wheelCenterY = screenHeight + wheelSize / 2 + 120;
		LibgdxUseItemActivityProvider useItemActivityProvider = new LibgdxUseItemActivityProvider(figure, inventoryImageManager, inputController
				.getPlayerController()
				.getActionAssembler(), focusManager);
		itemWheelHeroItems = new LibgdxItemWheel(new JDPoint(50, wheelCenterY),
				itemWheelSize,
				figure,
				useItemActivityProvider,
				selectedIndexItem,
				ImageManager.inventory_box_normal.getFilenameBlank(),
				0.1f
		);
		this.libgdxGuiElements.add(itemWheelHeroItems);



		/*
		 * Init use skill panel below smart control
		 */

		int screenWidthBy2 = (int) (screenWidth / 2.07);
		int itemPresenterHeight = 100;
		LibgdxSkillActivityProvider skillActivityProvider = new LibgdxSkillActivityProvider(inputController.getPlayerController());
		LibgdxUseSkillPresenter skillPresenter = new LibgdxUseSkillPresenter(
				new JDPoint(screenWidth / 2, screenHeight - itemPresenterHeight - 25),
				new JDDimension(screenWidthBy2, itemPresenterHeight),
				skillActivityProvider,
				ImageManager.inventory_box_normal.getFilenameBlank(),
				ImageManager.inventory_box_normalInactive.getFilenameBlank()
		);
		this.libgdxGuiElements.add(skillPresenter);


		/*
		 * init game over view
		 */
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		int widthFifth = (width / 5);
		int heightFifth = (height / 4);

		gameOverView = new LibgdxGameOverView(new JDPoint((width / 2) - widthFifth,
				(height / 2) - heightFifth), new JDDimension(2 * widthFifth,
				2 * heightFifth));
		this.libgdxGuiElements.add(gameOverView);


	}

	public void render() {
		renderGUIElements();
	}

	private void updateGUIElements(float deltaTime) {

		for (LibgdxGUIElement guiElement : this.libgdxGuiElements) {
			if (guiElement.isVisible()) {
				guiElement.update(deltaTime);
			}
		}
	}

	public LibgdxFocusManager getFocusManager() {
		return focusManager;
	}

	public LibgdxGameOverView getGameOverView() {
		return gameOverView;
	}

	private void renderGUIElements() {


		/*
		render shapes  TODO: find solution without using ShapeRenderer (create necessary sprites?)
		 */
		shapeRenderer.setProjectionMatrix(cameraGUI.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		for (LibgdxGUIElement guiElement : this.libgdxGuiElements) {
			if (guiElement.isVisible()) {
				if (guiElement.needsRepaint()) {
					guiElement.paint(shapeRenderer);
				}
			}
		}
		shapeRenderer.end();


		/*
		render sprites
		 */
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		renderFPSCounter();
		for (LibgdxGUIElement guiElement : this.libgdxGuiElements) {
			if (guiElement.isVisible()) {
				if (guiElement.needsRepaint()) {
					guiElement.paint(batch);
				}
			}
		}
		batch.end();
	}

	private void renderFPSCounter() {
		float x = cameraGUI.viewportWidth /2;
		float y = 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = AssetFonts.instance.defaultNormalFlipped;
		if (fps >= 45) {
			fpsFont.setColor(0, 1, 0, 1);
		}
		else if (fps >= 30) {
			fpsFont.setColor(1, 1, 0, 1);
		}
		else {
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); //white

		if (glProfiler != null) {
			fpsFont.draw(batch, "GL draw calls:" + glProfiler.getDrawCalls(), cameraGUI.viewportWidth/2 - 200, y);
		}
	}

	public void resize(int width, int height) {
		Gdx.app.log(TAG, "Resize width: "+width +" ; height: "+height);
		reinit();
		cameraGUI.viewportHeight = height;
		cameraGUI.viewportWidth = width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
		//Gdx.app.log(TAG, "camerGUI viewport width: "+width +" ; height: "+height);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public void update(float deltaTime) {
		updateGUIElements(deltaTime);

		if (getMessage() != null) {
			EventManager.getInstance().fireEvent(new InfoMessagePopupEvent(getMessage()));
			// TODO: test
			// should animate red enemy blobs if there are multiple and no enemy is selected
			smartControl.animateEnemyBlobs();
		}
	}

	public String getMessage() {
		return message;
	}

	private void clearMessage() {
		message = null;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void newStatement(Statement s) {
		if (textView != null) {
			this.textView.addTextPercept(s);
		}
	}

	public void setGLProfiler(GLProfiler glProfiler) {
		this.glProfiler = glProfiler;
	}



	public LibgdxItemWheel getItemWheel() {
		return this.itemWheelHeroItems;
	}


}
