package de.jdungeon.app.gui.dungeonselection;

import de.jdungeon.animation.DefaultAnimationSet;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.gui.AnimationGUIElement;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class DungeonSelectionHeroFigure extends AnimationGUIElement {

	public DungeonSelectionHeroFigure(JDPoint position, JDDimension dimension, DefaultAnimationSet animation, Game game) {
		super(position, dimension, animation, game);
	}

}
