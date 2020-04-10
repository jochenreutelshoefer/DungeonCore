package spell;

import dungeon.RoomEntity;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.action.SpellAction;
import figure.action.result.ActionResult;
import game.InfoEntity;
import game.RoomInfoEntity;
import gui.Paragraph;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface Spell {


	abstract ActionResult fire(Figure figure, RoomEntity target, boolean doIt, int round);

	abstract String getName();

	abstract int getLevel();

	abstract int getType();

	abstract boolean isPossibleFight();

	abstract boolean isPossibleNormal();

	abstract String getText();

	abstract int getDifficulty();

	abstract int getCost();

	abstract Paragraph[] getParagraphs();

}
