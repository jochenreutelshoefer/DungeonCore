package de.jdungeon.androidapp.screen;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.graphics.Color;
import android.graphics.Paint;
import dungeon.JDPoint;

import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public abstract class StandardScreen extends Screen {

	public static Paint defaultPaint;
	protected Paint paint;

	{
		defaultPaint = new Paint();
		defaultPaint.setTextSize(25);
		defaultPaint.setTextAlign(Paint.Align.CENTER);
		defaultPaint.setAntiAlias(true);
		defaultPaint.setColor(Color.RED);
	}

	protected final List<GUIElement> guiElements = new LinkedList<GUIElement>();

	public StandardScreen(Game game) {
		super(game);
		paint = defaultPaint;
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

	@Override
	public void update(float deltaTime) {
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
					guiElement.handleTouchEvent(touchDownEvent);
					break;
				}
			}
		}

	}


	@Override
	public void paint(float deltaTime) {
		Graphics gr = game.getGraphics();
		/*
		 * clear background
		 */
		gr.drawRect(0, 0, this.game.getScreenWidth() * 2,
				this.game.getScreenHeight() * 2, Color.BLACK);



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
