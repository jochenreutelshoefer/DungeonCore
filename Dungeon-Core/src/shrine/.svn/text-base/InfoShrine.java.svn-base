/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package shrine;

import java.awt.Color;

import dungeon.*;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;


public class InfoShrine extends Shrine {

	
	String info;

	
	public InfoShrine(String s, Room p) {
		super(p);
		info = s;
	}
public void metaClick(Figure f){
		
	}

	/**
	 * @see Shrine#turn(int)
	 */
	public void turn(int round) {
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_info_name");
	}
	
	public int getShrineIndex() {
		return Shrine.SHRINE_INFO;
	}

	public boolean needsTarget() {
		return false;
	}
	
	public String getText() {
		return info;
	}

	public String getStory() {
		
		String s = JDEnv.getResourceBundle().getString("shrine_info_story");
		s += ": \n"+info;
			return s;
	}
	
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }

	/**
	 * @see Shrine#getStatus()
	 */
	public String getStatus() {
		return null;
	}
	
	public Color getColor() {
		return java.awt.Color.orange;	
	}

	/**
	 * @see Usable#use(fighter)
	 */
	public boolean use(Figure f,Object target,boolean meta) {
		return true;
	}

	/**
	 * @see Usable#usableOnce()
	 */
	public boolean usableOnce() {
		return false;
	}

}
