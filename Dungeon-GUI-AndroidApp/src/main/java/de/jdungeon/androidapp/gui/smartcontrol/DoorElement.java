package de.jdungeon.androidapp.gui.smartcontrol;

import android.graphics.Color;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class DoorElement extends SubGUIElement {

	public DoorElement(JDPoint position, JDDimension dimension, GUIElement parent) {
		super(position, dimension, parent);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint posRelative = this.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
		JDDimension dimension = this.getDimension();
		DrawUtils.drawRectangle(g, Color.RED, absolutePosition, dimension);
	}
}
