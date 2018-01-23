package item.quest;
import dungeon.RoomEntity;
import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.Hero;
import game.RoomInfoEntity;
import item.Item;
import item.interfaces.Locatable;
import item.interfaces.Usable;

import java.util.HashMap;
import java.util.Map;

import shrine.RuneShrine;
import shrine.Shrine;



public class Rune extends Item implements Usable, Locatable {

	
	private final char c;

	private static int index = 0;
	
	private boolean solved = false;

	private static Map<Character, Integer> runeMap = new HashMap<Character, Integer>();
	

    
    public Rune(char s){
    	super(1000,false);
    	
    	
		c = s;
		if(!runeMap.containsKey(new Character(c))) {
			index++;
			runeMap.put(new Character(c), new Integer(index));
		}
	
    }

    public static int getRuneIndex(char c) {
    	return runeMap.get(new Character(c)).intValue();
    }
	
	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public void getsRemoved() {
		
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
	
	@Override
	public boolean usableOnce() {
		return false;
	}

	
	public char getChar() {
		return c;
	}

    @Override
	public String toString(){
	return ("Rune: "+c);
    }

    @Override
	public String getText(){
	return toString();
    }

    @Override
	public boolean canBeUsedBy(Figure f) {
    	return f instanceof Hero;
    }

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta){
		Shrine s = ((Hero)f).getRoom().getShrine(); 
		if(s instanceof RuneShrine){
			if (((RuneShrine) s).takeItem(this)) {
	    	 ((Hero)f).getInventory().removeItem(this);
	    	}
		}
		return true;
    }
	    
	
}
