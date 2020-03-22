package spell;

import dungeon.RoomEntity;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.action.result.ActionResult;
import game.InfoEntity;
import game.RoomInfoEntity;
import gui.Paragraph;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface Spell {


	void sorcer(Figure mage, RoomEntity target, int round);

	ActionResult fire(Figure figure, RoomEntity target, boolean doIt, int round);

	String getName();

	int getLevel();

	int getType();

	@Deprecated
	int getLernCost();

	boolean isPossibleFight();

	boolean isPossibleNormal();

	String getText();

	int getDifficulty();

	int getCost();

	Paragraph[] getParagraphs();

	void resetSpell();

	InfoEntity getInfoObject(DungeonVisibilityMap map);
}
