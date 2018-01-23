package item.interfaces;

import dungeon.RoomEntity;
import figure.Figure;
import game.RoomInfoEntity;

public interface Usable{

	int dustCosts();

    boolean use(Figure f, RoomEntity target, boolean meta);
    
    boolean usableOnce();
    
    boolean canBeUsedBy(Figure f);
    
    boolean needsTarget();

}
