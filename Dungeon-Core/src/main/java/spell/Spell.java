package spell;

import figure.Figure;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface Spell {

	boolean isApplicable(Figure mage, Object target);

	void sorcer(Figure mage, Object target);


	String getName();
}
