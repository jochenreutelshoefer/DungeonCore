package de.jdungeon.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.ImageGUIElement;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.world.GameScreenInputController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.01.20.
 */
public class ZoomButton extends ImageGUIElement {

	private final GameScreenInputController screen;
	private final boolean plus;

	public ZoomButton(JDPoint position, JDDimension dimension, GameScreenInputController screen, Image image, boolean plus) {
		super(position, dimension, image, screen.getGame());
		this.screen = screen;
		this.plus = plus;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		doZoom();
		return true;
	}

	private void doZoom() {
		if(plus) {
			screen.zoomIn();
		} else {
			screen.zoomOut();
		}
	}

	@Override
	public void handleLongPressEvent(MotionEvent touch) {
		doZoom();
	}
}

