package item.quest;
import java.util.HashMap;

import shrine.RuneShrine;
import shrine.Shrine;
import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.Hero;
import item.interfaces.*;
import item.Item;



public class Rune extends Item implements Usable,Locatable{

	
	private char c;

	private static int index = 0;
	
	boolean solved = false;

	private static HashMap runeMap = new HashMap();
	
	ItemOwner owner = null;

    
    public Rune(char s){
    	super(1000,false);
    	
    	
		c = s;
		if(!runeMap.containsKey(new Character(c))) {
			index++;
			runeMap.put(new Character(c), new Integer(index));
		}
	
    }

    public static int getRuneIndex(char c) {
    	return ((Integer)runeMap.get(new Character(c))).intValue();
    }
	
	public ItemOwner getOwner() {
		return owner;
	}
	
	public boolean needsTarget() {
		return false;
	}

    
	public void getsRemoved() {
		
		}

	
	public void setOwner(ItemOwner o) {
		owner = o;
	}

	
	public boolean getSolved() {
		return solved;
	}

	
	public void setSolved(boolean b) {
		solved = b;
	}

    
    public Attribute getHitPoints() {
		return null;
	}
	
	public boolean usableOnce() {
		return false;
	}

	
	public char getChar() {
		return c;
	}

    public String toString(){
	return ("Rune: "+c);
    }

    public String getText(){
	return toString();
    }

    public boolean canBeUsedBy(Figure f) {
    	return f instanceof Hero;
    }
    public boolean use(Figure f,Object target,boolean meta){
		Shrine s = ((Hero)f).getRoom().getShrine(); 
		if(s instanceof RuneShrine){
	    	if (((RuneShrine)s).takeItem((Item)this,f)) {
	    	 ((Hero)f).getInventory().removeItem(this);
	    	}
		}
		return true;
    }
	    
	
}
