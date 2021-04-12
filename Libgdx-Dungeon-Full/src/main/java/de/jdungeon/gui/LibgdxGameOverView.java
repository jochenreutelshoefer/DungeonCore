package de.jdungeon.gui;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.PlayerDiedEvent;
import de.jdungeon.util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.02.20.
 */
public class LibgdxGameOverView extends LibgdxPopup {

	public LibgdxGameOverView(JDPoint position, JDDimension dimension) {
		super(position, dimension, "Verloren!", new PlayerDiedEvent());

	}
}
