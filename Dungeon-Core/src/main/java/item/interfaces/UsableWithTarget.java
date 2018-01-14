package item.interfaces;

import figure.DungeonVisibilityMap;
import spell.TargetScope;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.01.18.
 */
public interface UsableWithTarget extends Usable {

	TargetScope getTargetScope();

}
