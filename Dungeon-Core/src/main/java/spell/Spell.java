package spell;

import figure.Figure;
import figure.action.result.ActionResult;
import gui.Paragraph;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface Spell {

	boolean isApplicable(Figure mage, Object target);

	void sorcer(Figure mage, Object target);


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

	ActionResult fire(Figure figure, Object target, boolean doIt);

	void resetSpell();
}
