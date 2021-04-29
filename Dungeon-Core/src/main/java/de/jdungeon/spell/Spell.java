package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.gui.Paragraphable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface Spell extends Paragraphable {

	ActionResult fire(Figure figure, RoomEntity target, boolean doIt, int round);

	String getText();

	String getHeaderName();

	int getLevel();

	int getType();

	boolean isPossibleFight();

	boolean isPossibleNormal();

	int getCost();

	Paragraph[] getParagraphs();

}
