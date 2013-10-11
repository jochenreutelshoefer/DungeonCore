package shrine;

import item.*;
import item.interfaces.ItemOwner;
import item.quest.Rune;

import java.awt.Color;
import java.util.*;

import dungeon.*;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.UsePercept;
import game.JDEnv;
import gui.Texts;


public class RuneShrine extends Shrine implements ItemOwner {

	
	int index;

	
	char c;

	
	Item r;

	boolean solved = false;
	//String story;

	public RuneShrine(Room p, int i, char c) {
		super(p);
		index = i;
		this.c = c;
		story = JDEnv.getResourceBundle().getString("see_rune_shrine");
		type = Rune.getRuneIndex(c);

	}

	public RuneShrine(int i, char c) {
		super();
		index = i;
		this.c = c;
		story = JDEnv.getResourceBundle().getString("see_rune_shrine");
		type = Rune.getRuneIndex(c);
	}
	
	public int getShrineIndex() {
		return Shrine.SHRINE_RUNE;
	}
public void metaClick(Figure f) {
		
	}
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] it = new ItemInfo[1];
		if(r != null) {
			it[0] = ItemInfo.makeItemInfo(r,map);
			return it;
		}
		return null;
	}
	
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }
	
	public Item getItem(ItemInfo it) {
		
			if(ItemInfo.makeItemInfo(r,null).equals(it)) 
				return r;
		
			return null;
	}
	public Room getRoom() {
			return this.location; 
		}
		
	public boolean removeItem(Item i) {
		if(i == r) {
			r = null;
			return true;
		}
		return false;
	}
	
	public boolean needsTarget() {
		return false;
	}

	public String getText() {

		return toString()
			+ JDEnv.getResourceBundle().getString("shrine_rune_text_a")+" "
			+ index
			+ " "+JDEnv.getResourceBundle().getString("shrine_rune_text_b")
			+ "\n"+JDEnv.getResourceBundle().getString("state")+ ": "+getStatus();
	}

	public String getStory() {
		return story;
	}
	
	public boolean addItems(List l, ItemOwner o) {
		for(int i = 0; i < l.size();i++) {
			Item it = (Item)(l.get(i));
			this.takeItem(it,o);
		}
		return true;
	}

	public boolean usableOnce() {
		return false;
	}

	
	public int getIndex() {
		return index;
	}

	
	public char getChar() {
		return c;
	}


	public Color getColor() {
		return java.awt.Color.red;
	}

	public boolean takeItem(Item i, ItemOwner o) {
		if ((r == null)) {
			r = (Rune) i;
			if (r instanceof Rune) {
				if (((Rune) r).getChar() == c) {
					solved = true;
					((Rune) r).setSolved(true);
				}
			}
			return true;
		} else
			return false;
	}

	
	public Item getItem() {
		solved = false;
		if (r instanceof Rune)
			((Rune) r).setSolved(false);
		if (r != null)
			return r;
		else
			return null;
	}
	
	public Item getItemNumber(int i) {
		if(i == 0) {
			return r;
		}
		return null;
	}

	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_rune_name")+": " + Integer.toString(index);
	}

	public boolean use(Figure f,Object target,boolean meta) {
		if(r != null) {
			this.location.addItem(r);
			r = null;
		}
		return true;
	}

	public void turn(int k) {
	}

	public void clicked(Figure f, boolean right) {
		//shrineView v = new shrineView(f.getGame().getMain(), "Runenschrein", true,f,this); 
		if (r != null) {
			 //game.getGui().figureUsingAnimation(FigureInfo.makeFigureInfo(f,game.getGui().getFigure().getVisMap()));
			 Percept p = new UsePercept(f,this);
			 f.getRoom().distributePercept(p);
			
			this.location.addItem(r);
			r = null;
		}
	}

	public String getStatus() {
		if (r == null) {
			return JDEnv.getResourceBundle().getString("empty");
		} else {
			return r.toString();
		}

	}
}
