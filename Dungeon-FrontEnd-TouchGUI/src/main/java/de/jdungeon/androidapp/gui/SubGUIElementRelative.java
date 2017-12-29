package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.smartcontrol.SmartControl;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class SubGUIElementRelative extends SubGUIElement {

	private final JDPoint posRelative;

	public SubGUIElementRelative(JDPoint posRelative, JDDimension dimension, SmartControl smartControl) {
		super(posRelative, dimension, smartControl);
		this.posRelative = posRelative;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative
				.getY());
		g.fillRect(absolutePosition.getX(), absolutePosition.getY(), dimension.getWidth(), dimension.getHeight(), Colors.WHITE);
	}

}
