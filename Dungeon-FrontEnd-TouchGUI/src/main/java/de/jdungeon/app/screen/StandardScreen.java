package de.jdungeon.app.screen;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.game.Colors;
import de.jdungeon.game.MotionEvent;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public abstract class StandardScreen extends Screen {

	protected final List<GUIElement> guiElements = new LinkedList<GUIElement>();
	private final long screenCreatedTime;
	private final GUIImageManager guiImageManager;
	public StandardScreen(Game game) {
		super(game);
		screenCreatedTime = System.currentTimeMillis();
		guiImageManager = new GUIImageManager(game.getFileIO().getImageLoader());
	}


	public GUIImageManager getGuiImageManager() {
		return guiImageManager;
	}

	public JDDimension getScreenSize() {
		return new JDDimension(game.getScreenWidth(), game.getScreenHeight());
	}


	@Override
	public void init() {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

	private long lastTouchEvent = -1;

	@Override
	public synchronized void update(float deltoTime) {
		/*
		 * handle touch events
		 */
		List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
		Input.TouchEvent touchDownEvent = null;
		for (int i = 0; i < touchEvents.size(); i++) {
			Input.TouchEvent touchEvent = touchEvents.get(i);
			boolean isTouchDownEvent = game.getInput().isTouchDown(i);
			if (isTouchDownEvent) {
				touchDownEvent = touchEvent;

			}
		}

		if (touchDownEvent != null) {
			JDPoint coordinates = new JDPoint(touchDownEvent.x,
					touchDownEvent.y);
			ListIterator<GUIElement> listIterator = guiElements
					.listIterator(guiElements.size());
			while (listIterator.hasPrevious()) {
				GUIElement guiElement = listIterator.previous();
				if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
					// assert there are at least some milli seconds between touch events
					long now = System.currentTimeMillis();
					long timeSinceScreenCreated = now - screenCreatedTime;
					if(now - lastTouchEvent > 10
							&& timeSinceScreenCreated > 100) {
						// we assume a user cannot make a serious touch action, seeing
						// a screen for less than 50 milli seconds
						boolean handleEvent = guiElement.handleTouchEvent(touchDownEvent);
						if(handleEvent) {
							lastTouchEvent = System.currentTimeMillis();
							break;
						}
					}
				}
			}
		}

	}

	public JDPoint normalizeRawCoordinates(MotionEvent longPressEvent) {

		/*
		 * for some reason these coordinates need to be normalized from scale
		 * 1915/1100
		 */

		int x = (int) (longPressEvent.getRawX() * (game.getScreenWidth()) / 1915);
		int y = (int) (longPressEvent.getRawY() * (game.getScreenHeight()) / 1000);

		return new JDPoint(x, y);
	}



	@Override
	public void paint(float deltaTime) {
		Graphics gr = game.getGraphics(null);
		/*
		 * clear background
		 */
		gr.fillRect(0, 0, this.game.getScreenWidth() * 2,
				this.game.getScreenHeight() * 2, Colors.BLACK);



		/*
		 * draw gui elements
		 */
		List<GUIElement> elements = this.guiElements;
		for (GUIElement guiElement : elements) {
			if (guiElement.isVisible()) {
				guiElement.paint(gr, null);
			}
		}

	}
}
