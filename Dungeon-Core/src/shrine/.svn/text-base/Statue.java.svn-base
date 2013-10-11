package shrine;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import dungeon.*;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;


/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Statue extends Shrine {

	/**
	 * @see Shrine#turn(int)
	 * 
	 * 
	 */
	
	public Statue(Room r) {
		super(r);	
	}
	
public void metaClick(Figure f){
		
	}
	
	
	public Statue() {
		super();
	}
	
	
	public void turn(int round) {
		//System.out.println(location.toString());
		List l = location.getRoomFigures();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Figure element = (Figure) iter.next();
			if(element instanceof Hero) {
				element.heal(3);
			}
			
		}
		
	}

	public boolean use(Figure f,Object target,boolean meta) {
		return false;
	}
	
	/**
	 * @see Shrine#getColor()
	 */
	public Color getColor() {
		return Color.gray;
	}

	public boolean needsTarget() {
		return false;
	}
	/**
	 * @see Shrine#getStory()
	 */
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_statue_story");
	}
	
	public int getShrineIndex() {
		return Shrine.SHRINE_STATUE;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_statue_name");
	}

	/**
	 * @see Shrine#getText()
	 */
	public String getText() {
		return toString();
	}

	
	/**
	 * @see Shrine#getStatus()
	 */
	public String getStatus() {
		return null;
	}

	/**
	 * @see Usable#use(fighter)
	 */
	
	
	public boolean canBeUsedBy(Figure f) {
		return false;
	}

	/**
	 * @see Usable#usableOnce()
	 */
	public boolean usableOnce() {
		return false;
	}

}
