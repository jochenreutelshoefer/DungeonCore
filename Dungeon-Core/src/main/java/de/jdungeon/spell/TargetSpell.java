package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.game.RoomInfoEntity;

public interface TargetSpell extends Spell {
	
	boolean distanceOkay(Figure mage, RoomEntity target);

	boolean isApplicable(Figure mage, RoomEntity target);

	Class<? extends RoomInfoEntity> getTargetClass();

	TargetScope getTargetScope();

}
