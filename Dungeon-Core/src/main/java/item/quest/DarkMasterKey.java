package item.quest;
import dungeon.*;
import item.Item;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;
import figure.attribute.Attribute;
import game.JDEnv;

/*
 * Created on 29.07.2004
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
public class DarkMasterKey extends Item implements Locatable{

	/**
	 * @param value
	 * @param m
	 * 
	 * @uml.property name="owner"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	ItemOwner owner;

	/**
	 * 
	 * @uml.property name="masterShrinePosition"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Room masterShrinePosition;

	
	public DarkMasterKey(int value, boolean m, Room r) {
		super(value, m);
		masterShrinePosition = r;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @uml.property name="owner"
	 */
	public ItemOwner getOwner() {
		return owner;
	}

	/**
	 * 
	 * @uml.property name="owner"
	 */
	public void setOwner(ItemOwner io) {
		owner = io;
	}

	
	public void getsRemoved() {
		
	}

	/* (non-Javadoc)
	 * @see item#getHitPoints()
	 */
	public Attribute getHitPoints() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		return JDEnv.getResourceBundle().getString("portal_stone");
	}

	/* (non-Javadoc)
	 * @see item#getText()
	 */
	public String getText() {
		// TODO Auto-generated method stub
		return JDEnv.getResourceBundle().getString("portal_stone_text");
	}

	/**
	 * @return
	 * 
	 * @uml.property name="masterShrinePosition"
	 */
	public Room getMasterShrinePosition() {
		return masterShrinePosition;
	}

}
