package de.jdungeon.skillselection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import spell.Spell;
import user.DefaultDungeonSession;
import util.JDDimension;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.Constants;
import de.jdungeon.DefaultGuiInputController;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.skillselection.SkillSelectionManager;
import de.jdungeon.app.gui.skillselection.SkillSelectionTile;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.gui.LibgdxGUIElement;
import de.jdungeon.libgdx.LibgdxScreenContext;
import de.jdungeon.util.Pair;
import de.jdungeon.world.PlayerController;
import de.jdungeon.world.WorldRenderer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.20.
 */
public class SkillSelectionScreen extends AbstractGameScreen {


	private final OrthographicCamera camera;
	private final OrthographicCamera cameraGUI;
	protected final List<LibgdxGUIElement> guiElements = new ArrayList<>();
	private SpriteBatch batch;

	public SkillSelectionScreen(LibgdxDungeonMain game) {
		super(game);

		// TODO : refactor towards a generic abstract Screen that will do this kind of stuff in a generic way


		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.setToOrtho(true, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);


		// init gui camera and gui renderer
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);



		Pair<Spell, Spell> options = SkillSelectionManager.getInstance().getOptions(((DefaultDungeonSession)game.getSession()).getCurrentStage());

		if(options != null) {
			this.guiElements.add(addTile(options.getA(), true));
			this.guiElements.add(addTile(options.getB(), false));
		}


		Gdx.input.setInputProcessor(new DefaultGuiInputController(game, this));
	}

	@Override
	public boolean clicked(int screenX, int screenY, int pointer, int button) {
		// TODO : refactor towards a generic abstract Screen that will do this kind of stuff
		/*
		Check for gui element click
		 */
		ListIterator<LibgdxGUIElement> listIterator = guiElements.listIterator(guiElements.size());
		while (listIterator.hasPrevious()) {
			LibgdxGUIElement guiElement = listIterator.previous();
			if (guiElement.hasPoint(new JDPoint(screenX, screenY)) && guiElement.isVisible()) {
				//Log.i("touch event fired", this.getClass().getSimpleName()+": touch event fired");
				Input.TouchEvent touchEvent = new Input.TouchEvent();
				touchEvent.x = screenX;
				touchEvent.y = screenY;
				guiElement.handleClickEvent(screenX, screenY);
				return true;
			}
		}

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

	private LibgdxSkillSelectionTile addTile(Spell option, boolean rightHandSide) {
		int posY = this.game.getScreenHeight() / 4;
		int posX = rightHandSide?this.game.getScreenWidth() / 2 + this.game.getScreenWidth() / 12:this.game.getScreenWidth() / 12;
		int width = this.game.getScreenWidth() /3;
		int height = this.game.getScreenHeight() * 2 / 3;
		return new LibgdxSkillSelectionTile(new JDPoint(posX, posY), new JDDimension(width,  height),  this.game, option);
	}



	@Override
	public OrthographicCamera getCamera(ScreenContext context) {
		if(context == LibgdxScreenContext.Context.GUI) {
			return cameraGUI;
		} else {
			return camera;
		}
	}

	@Override
	public void show() {

		camera.update();
		cameraGUI.update();
	}

	@Override
	public void render(float v) {
		// TODO : refactor towards a generic abstract Screen that will do this kind of stuff in a generic way
		if(batch == null) {
			batch = new SpriteBatch();
		}

		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (LibgdxGUIElement guiElement : this.guiElements) {
			if (guiElement.isVisible()) {
				if (guiElement.needsRepaint()) {
					guiElement.paint(batch);
				}
			}
		}

		batch.end();
	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public void update(float deltaTime) {

	}
}
