package spell;

import dungeon.RoomEntity;
import figure.Figure;
import game.RoomInfoEntity;

public interface TargetSpell extends Spell {
	
	boolean distanceOkay(Figure mage, RoomEntity target);

	boolean isApplicable(Figure mage, RoomEntity target);

	Class<? extends RoomInfoEntity> getTargetClass();

	TargetScope getTargetScope();

}
