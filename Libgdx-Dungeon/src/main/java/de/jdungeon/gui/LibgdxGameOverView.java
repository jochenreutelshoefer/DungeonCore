package de.jdungeon.gui;

import dungeon.JDPoint;
import event.PlayerDiedEvent;
import util.JDDimension;

import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.02.20.
 */
public class LibgdxGameOverView extends LibgdxPopup {

	public LibgdxGameOverView(JDPoint position, JDDimension dimension) {
		super(position, dimension, "Verloren!", new PlayerDiedEvent());

	}
}
