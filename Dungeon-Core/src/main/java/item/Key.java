package item;
import figure.attribute.Attribute;
import game.JDEnv;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Key extends Item implements Locatable{

	public static String[] keyStrings = { "Kupfer", "Eisen", "Silber",
			"Gold", "Platin", "Bronze", "Blech", "Stahl", "Piponium" };
	
	private final String type;

	
	private ItemOwner owner;

	
	public static List<Key> generateKeylist() {
		List<Key> result = new ArrayList<Key>();
		for (int i = 0; i < Key.keyStrings.length; i++) {
			result.add(new Key(Key.keyStrings[i] + " groß"));
			result.add(new Key(Key.keyStrings[i] + " klein"));
		}
		return result;
	}

	public Key(String type) {
		super(100, false);
		this.type = type;	
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Key key = (Key) o;

		return type.equals(key.type);

	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public ItemOwner getOwner() {
		return owner;
	}

	
	@Override
	public void setOwner(ItemOwner o) {
		owner = o;
	}

	
	@Override
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

	
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("key")+": "+type;	
	}
	
	public Attribute getHitPoints() {
		return null;	
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("key")+": "+type;	
	}

}
