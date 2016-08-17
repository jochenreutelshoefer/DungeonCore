package item.interfaces;
import figure.Figure;

public interface Usable{
	
	//public int zahl = 5;

    public boolean use(Figure f, Object target,boolean meta);
    
    public boolean usableOnce();
    
    public boolean canBeUsedBy(Figure f);
    
    public boolean needsTarget();


}
