package item;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;
import figure.attribute.Attribute;
import game.JDEnv;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Key extends Item implements Locatable{

	
	String type;

	
	ItemOwner owner;

	
	public Key(String type) {
		super(100, false);
		this.type = type;	
	}


	public ItemOwner getOwner() {
		return owner;
	}

	
	public void setOwner(ItemOwner o) {
		owner = o;
	}

	
	public void getsRemoved() {
		owner = null;
	}

	/**
	 * Returns the type.
	 * @return String
	 * 
	 */
	public String getType() {
		return type;
	}

	
	public String toString() {
		return JDEnv.getResourceBundle().getString("key")+": "+type;	
	}
	
	public Attribute getHitPoints() {
		return null;	
	}
	
	public String getText() {
		return JDEnv.getResourceBundle().getString("key")+": "+type;	
	}

}
