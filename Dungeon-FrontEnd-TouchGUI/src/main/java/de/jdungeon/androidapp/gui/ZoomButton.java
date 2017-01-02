package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 02.01.17.
 */
public class ZoomButton extends ImageGUIElement {

	private final GameScreen screen;
	private final boolean plus;

	public ZoomButton(JDPoint position, JDDimension dimension, GameScreen screen, Image image, boolean plus) {
		super(position, dimension, image);
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
