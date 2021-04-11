package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.gui.Paragraph;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface Spell {

	ActionResult fire(Figure figure, RoomEntity target, boolean doIt, int round);

	String getName();

	int getLevel();

	int getType();

	boolean isPossibleFight();

	boolean isPossibleNormal();

	String getText();

	int getCost();

	Paragraph[] getParagraphs();

}
