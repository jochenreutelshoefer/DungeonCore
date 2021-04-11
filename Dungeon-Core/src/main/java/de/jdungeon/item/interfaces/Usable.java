package de.jdungeon.item.interfaces;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;

public interface Usable{

	int dustCosts();

    ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt);
    
    boolean usableOnce();
    
    boolean canBeUsedBy(Figure f);
    
    boolean needsTarget();

}
