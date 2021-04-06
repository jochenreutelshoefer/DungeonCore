package item.interfaces;

import dungeon.RoomEntity;
import figure.Figure;
import figure.action.result.ActionResult;
import game.RoomInfoEntity;

public interface Usable{

	int dustCosts();

    ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt);
    
    boolean usableOnce();
    
    boolean canBeUsedBy(Figure f);
    
    boolean needsTarget();

}
