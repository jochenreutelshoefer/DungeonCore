package de.jdungeon.app.gui.smartcontrol;

import de.jdungeon.dungeon.JDPoint;

import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public interface Drawable {

	void paint(Graphics g, JDPoint viewportPosition);
}
