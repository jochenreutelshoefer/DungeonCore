package de.jdungeon.app.gui;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public class SimpleImageGUIElement extends ImageGUIElement{

	public SimpleImageGUIElement(JDPoint position, JDDimension dimension, Image im, Game game) {
		super(position, dimension, im, game);
	}
}
