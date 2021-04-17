package de.jdungeon.item.quest;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.item.interfaces.Usable;

import java.util.HashMap;
import java.util.Map;

public class Rune extends Item<Rune> implements Usable, Locatable {

	
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
	public Rune copy() {
		return new Rune(getChar());
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
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt){
		// todo: re-implement entirely
		return ActionResult.DONE;
    }
	    
	
}
