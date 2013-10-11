/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package item;

import java.util.LinkedList;

import dungeon.*;

import figure.attribute.Attribute;
import game.JDEnv;


public class Bunch extends Item {

	
	LinkedList keys = new LinkedList();

	
	public Bunch() {
		super(0,false);	
	}

	/**
	 * @see Item#getHitPoints()
	 */
	public Attribute getHitPoints() {
		return null;
	}

	
	public LinkedList getKeys() {
		return keys;
	}

	
	public boolean tryUnlockDoor(Door d,boolean doIt) {
		boolean b = false;
		for(int i = 0; i < keys.size(); i++) {
			Key k = (Key)keys.get(i);
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
		for(int i= 0; i< keys.size(); i++) {
			Key ke = (Key)keys.get(i);
			if(ke.getType().equals(k)) {
				return true;
				
			}	
		}
		return false;	
	}
	
	public boolean removeKey(Key k) {
		return keys.remove(k);	
	}
	
	public String toString() {
		return JDEnv.getResourceBundle().getString("bunch")+": "+keys.size();	
	}

	/**
	 * @see Item#getText()
	 */
	public String getText() {
		String text = new String();
		for(int i= 0; i < keys.size(); i++) {
			String one = ((Key)keys.get(i)).toString();
			String cut = cut(one); 
			text += " , "+cut;
		}	
		return text;
	}
	
	private String cut(String s) {
		return s.substring(11);	
	}

}
