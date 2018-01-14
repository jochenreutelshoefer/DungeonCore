package item.interfaces;

import figure.Figure;

public interface Usable{
	

    boolean use(Figure f, Object target, boolean meta);
    
    boolean usableOnce();
    
    boolean canBeUsedBy(Figure f);
    
    boolean needsTarget();

}
