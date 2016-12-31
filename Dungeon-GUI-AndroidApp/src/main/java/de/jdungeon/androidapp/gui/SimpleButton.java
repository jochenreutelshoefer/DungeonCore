package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import event.Event;
import event.EventManager;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScrollMotion;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class SimpleButton extends AbstractGUIElement {

	private final String text;
	private final Event event;

	public SimpleButton(String text, Event event, JDPoint position) {
		super(position, getDefaultDimension());
		this.text = text;
		this.event = event;
	}

	public static JDDimension getDefaultDimension() {
		return new JDDimension(180, 70);
	}

	@Override
	public void handleTouchEvent(Input.TouchEvent touch) {
		EventManager.getInstance().fireEvent(event);
	}



	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		drawBackground(g);

		g.drawString(text, this.getPositionOnScreen().getX()+this.getDimension().getWidth()/2, this.getPositionOnScreen().getY()+44, g.getDefaultPaint());

		drawBorder(g);

	}
}
