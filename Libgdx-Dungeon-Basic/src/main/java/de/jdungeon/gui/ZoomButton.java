package de.jdungeon.gui;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.world.GameScreenInputProcessor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.01.20.
 */
public class ZoomButton extends ImageLibgdxGUIElement {

	private final GameScreenInputProcessor inputController;
	private final boolean plus;

	public ZoomButton(JDPoint position, JDDimension dimension, GameScreenInputProcessor inputController, String image, boolean plus) {
		super(position, dimension, image);
		this.inputController = inputController;
		this.plus = plus;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		doZoom();
		return true;
	}

	private void doZoom() {
		if (plus) {
			inputController.zoomIn();
		}
		else {
			inputController.zoomOut();
		}
	}
}
