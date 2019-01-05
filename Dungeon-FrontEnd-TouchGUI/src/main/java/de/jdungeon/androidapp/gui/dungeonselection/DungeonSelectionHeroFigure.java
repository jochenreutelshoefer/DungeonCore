package de.jdungeon.androidapp.gui.dungeonselection;

import animation.DefaultAnimationSet;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.AnimationGUIElement;
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
