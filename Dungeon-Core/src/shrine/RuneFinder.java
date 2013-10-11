/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */

// import java.util.*;
package shrine;

import item.interfaces.ItemOwner;
import item.interfaces.Usable;
import item.quest.Rune;

import java.awt.*;

import dungeon.*;

import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.TextPercept;
import figure.percept.UsePercept;
import game.JDEnv;
import gui.Texts;

public class RuneFinder extends Shrine {

	Rune r;

	public RuneFinder(Rune r, Room p) {
		super(p);
		this.r = r;
		story = JDEnv.getResourceBundle().getString("see_rune_finder_shrine");

	}

	public RuneFinder(Rune r) {
		super();
		this.r = r;
		story = JDEnv.getResourceBundle().getString("see_rune_finder_shrine");
		type = Rune.getRuneIndex(r.getChar());
	}

	public String getStory() {
		return story;
	}

	public int getShrineIndex() {
		return Shrine.SHRINE_RUNEFINDER;
	}
	
	public void metaClick(Figure f) {
		
	}
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }

	public Rune getRune() {
		return r;
	}

	public boolean needsTarget() {
		return false;
	}
	/**
	 * @see Shrine#turn(int)
	 */
	public void turn(int round) {
		ItemOwner o = r.getOwner();
		JDPoint p = o.getLocation();
		// System.out.println("Die Rune befindet sich im Moment in Raum:
		// "+p.toString());

	}

	public String getText() {
		return toString() + "\n"+JDEnv.getResourceBundle().getString("shrine_runeFinder_text_a")+" "
				+ r.toString() + JDEnv.getResourceBundle().getString("shrine_runeFinder_text_b")+" ";
	}

	public Color getColor() {
		return java.awt.Color.cyan;
	}

	public boolean usableOnce() {
		return false;

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_runeFinder_name");
	}

	

	/**
	 * @see Shrine#getStatus()
	 */
	public String getStatus() {
		return JDEnv.getResourceBundle().getString("shrine_runeFinder_name")+": " + r.toString();
	}

	/**
	 * @see Usable#use(fighter)
	 */
	public boolean use(Figure f,Object target,boolean meta) {
		Percept p = new UsePercept(f, this);
		f.getRoom().distributePercept(p);
		// game.getGui().figureUsingAnimation(FigureInfo.makeFigureInfo(f,game.getGui().getFigure().getVisMap()));
		ItemOwner o = r.getOwner();
		JDPoint point = o.getLocation();
		
		String s = "Die Rune befindet sich im Moment in Raum: " + point.toString();
		 f.tellPercept(new TextPercept(s));
		return true;
	}

}
