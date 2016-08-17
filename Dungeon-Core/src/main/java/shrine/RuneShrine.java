package shrine;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.UsePercept;
import game.JDEnv;
import item.Item;
import item.ItemInfo;
import item.interfaces.ItemOwner;
import item.quest.Rune;

import java.util.List;

import util.JDColor;
import dungeon.Room;


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
	
	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_RUNE;
	}
public void metaClick(Figure f) {
		
	}
	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] it = new ItemInfo[1];
		if(r != null) {
			it[0] = ItemInfo.makeItemInfo(r,map);
			return it;
		}
		return null;
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }
	
	@Override
	public Item getItem(ItemInfo it) {
		
			if(ItemInfo.makeItemInfo(r,null).equals(it)) 
				return r;
		
			return null;
	}
	@Override
	public Room getRoom() {
			return this.location; 
		}
		
	@Override
	public boolean removeItem(Item i) {
		if(i == r) {
			r = null;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public String getText() {

		return toString()
			+ JDEnv.getResourceBundle().getString("shrine_rune_text_a")+" "
			+ index
			+ " "+JDEnv.getResourceBundle().getString("shrine_rune_text_b")
			+ "\n"+JDEnv.getResourceBundle().getString("state")+ ": "+getStatus();
	}

	@Override
	public String getStory() {
		return story;
	}
	
	@Override
	public boolean addItems(List<Item> l, ItemOwner o) {
		for(int i = 0; i < l.size();i++) {
			Item it = (l.get(i));
			this.takeItem(it);
		}
		return true;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

	
	public int getIndex() {
		return index;
	}

	
	public char getChar() {
		return c;
	}


	@Override
	public JDColor getColor() {
		return JDColor.red;
	}

	@Override
	public boolean takeItem(Item i) {
		if ((r == null)) {
			r = i;
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
	
	@Override
	public Item getItemNumber(int i) {
		if(i == 0) {
			return r;
		}
		return null;
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_rune_name")+": " + Integer.toString(index);
	}

	@Override
	public boolean use(Figure f,Object target,boolean meta) {
		if(r != null) {
			this.location.addItem(r);
			r = null;
		}
		return true;
	}

	@Override
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

	@Override
	public String getStatus() {
		if (r == null) {
			return JDEnv.getResourceBundle().getString("empty");
		} else {
			return r.toString();
		}

	}
}
