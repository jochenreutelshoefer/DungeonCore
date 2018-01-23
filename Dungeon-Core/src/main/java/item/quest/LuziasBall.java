package item.quest;
import game.RoomInfoEntity;
import item.Item;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;
import item.interfaces.LocatableItem;
import item.interfaces.Usable;
import dungeon.*;
import shrine.Luzia;
import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.JDEnv;

import java.util.LinkedList;
//import figure.hero.Hero;
//import figure.hero.Inventory;
import java.util.List;

/*
 * Created on 07.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LuziasBall extends Item implements Usable{

	/**
	 * @param value
	 * @param m
	 */
	int dir;
	boolean questSolved = false;


	ItemOwner owner;

	
	Luzia luzia;

	
	public LuziasBall(int value, boolean m ) {
		super(value, m);
		
		// TODO Auto-generated constructor stub
	}
	
	public void getsRemoved() {
		
		}

	public boolean canBeUsedBy(Figure f) {
		return f instanceof Hero;
	}
	
	
	public void setLuzia(Luzia luzia) {
		this.luzia = luzia;
	}

	public boolean needsTarget() {
		return false;
	}

	public Attribute getHitPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void setOwner(ItemOwner o) {
		owner = o;
	}

	
	@Override
	public ItemOwner getOwner() {
		return owner;

	}

	
	public String toString() {
		return JDEnv.getResourceBundle().getString("palantir");
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	public boolean use(Figure f, RoomEntity target, boolean meta) {
		if(questSolved) {
			//[TODO] leuchtet....
			//show();
		}
		else {
			Room pos = ((Thing)luzia.getRequestedItem()).getOwner().getRoom();
			Room here = owner.getRoom();
			String dir = here.getDirectionString(pos);
			String s = JDEnv.getResourceBundle().getString("luzia_amulett_is")+" "+dir+" "+JDEnv.getResourceBundle().getString("from_here");
			f.tellPercept(new TextPercept(s));
			
		}
		return true;
	}
	
	@Override
	public boolean usableOnce() {
		return false;
	}
	
	
	

	public boolean seesEnemy() {
		List<Room> rooms = owner.getRoom().getScoutableNeighbours();
		List<Figure> alleMonster = new LinkedList<Figure>();
		for(int i = 0; i < rooms.size(); i++) {
			alleMonster.addAll(((Room)(rooms.get(i))).getRoomFigures());
		}
		if(!alleMonster.isEmpty()) {
			return true;
			
		}
		return false;
	}
	
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return JDEnv.getResourceBundle().getString("luzias_palantir");
	}
	
	public void solved() {
		questSolved = true;
	}

}
