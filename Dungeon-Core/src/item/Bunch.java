/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package item;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dungeon.Door;
import figure.attribute.Attribute;
import game.JDEnv;


public class Bunch extends Item {

	
	private final Set<Key> keys = new HashSet<Key>();

	
	public Bunch() {
		super(0,false);	
	}

	/**
	 * @see Item#getHitPoints()
	 */
	public Attribute getHitPoints() {
		return null;
	}

	
	public Collection<Key> getKeys() {
		return keys;
	}

	
	public boolean tryUnlockDoor(Door d,boolean doIt) {
		boolean b = false;
		for (Key k : this.keys) {
			b = d.lock(k,doIt);
			if(b) return true;	
		}
		return false;
	}	
	
	public void addKey(Key k) {
		keys.add(k);
	}
	
	public boolean hasKey(Key k) {
		return keys.contains(k);	
	}
	
	public boolean hasKey(String k) {
		for (Key ke : this.keys) {
			if(ke.getType().equals(k)) {
				return true;
				
			}	
		}
		return false;	
	}
	
	public boolean removeKey(Key k) {
		return keys.remove(k);	
	}
	
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("bunch")+": "+keys.size();	
	}

	/**
	 * @see Item#getText()
	 */
	@Override
	public String getText() {
		String text = new String();
		for (Key k : this.keys) {
			String one = k.toString();
			String cut = cut(one); 
			text += " , "+cut;
		}	
		return text;
	}
	
	private String cut(String s) {
		return s.substring(11);	
	}

}
