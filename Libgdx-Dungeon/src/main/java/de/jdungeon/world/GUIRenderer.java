package de.jdungeon.world;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import dungeon.JDPoint;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import util.JDDimension;

import de.jdungeon.Constants;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.HealthBar;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.libgdx.LibgdxGraphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.19.
 */
public class GUIRenderer implements Disposable {

	private final InputController worldController;
	private final OrthographicCamera cameraGUI;
	private final LibgdxDungeonMain game;
	private final HeroInfo figure;
	private SpriteBatch batch;
	private Graphics graphics;

	protected final List<GUIElement> guiElements = new LinkedList<GUIElement>();
	private GUIImageManager guiImageManager;


	public GUIRenderer(InputController worldController, OrthographicCamera cameraGUI, LibgdxDungeonMain game, HeroInfo figure) {
		this.worldController = worldController;
		this.cameraGUI = cameraGUI;
		this.game = game;
		this.figure = figure;
		init();
	}

	private void init() {
		guiImageManager = new GUIImageManager(game.getFileIO().getImageLoader());
		batch = new SpriteBatch();
		cameraGUI.update();
		graphics = new LibgdxGraphics(cameraGUI, batch);

		/*
		 * init health bars
		 */
		int posX = 22;
		JDPoint healthBarPosition = new JDPoint(posX, 5);
		HealthBar healthView = new HealthBar(healthBarPosition,
				new JDDimension(160, 20), figure, HealthBar.Kind.health, this.game);
		this.guiElements.add(healthView);
		JDPoint dustBarPosition = new JDPoint(posX, 25);
		HealthBar dustView = new HealthBar(dustBarPosition,
				new JDDimension(160, 20), figure, HealthBar.Kind.dust, this.game);
		this.guiElements.add(dustView);

	}

	public void render() {
		batch.setProjectionMatrix(cameraGUI.combined);
		//batch.begin();
		renderGUIElements();
		renderGuiScore();
		renderFPSCounter();
		//batch.end();

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
}
