package spell;

import figure.Figure;
import game.InfoEntity;

public interface TargetSpell extends Spell {
	
	boolean distanceOkay(Figure mage, Object target);

	Class<? extends InfoEntity> getTargetClass();

	TargetScope getTargetScope();

}
