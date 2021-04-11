package de.jdungeon.item.interfaces;

import de.jdungeon.spell.TargetScope;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.01.18.
 */
public interface UsableWithTarget extends Usable {

	TargetScope getTargetScope();

}
