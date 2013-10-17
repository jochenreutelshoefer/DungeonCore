/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */

// import java.util.*;
package shrine;

import figure.Figure;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.TextPercept;
import figure.percept.UsePercept;
import game.JDEnv;
import item.interfaces.ItemOwner;
import item.interfaces.Usable;
import item.quest.Rune;
import util.JDColor;
import dungeon.JDPoint;
import dungeon.Room;

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

	@Override
	public String getStory() {
		return story;
	}

	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_RUNEFINDER;
	}
	
	public void metaClick(Figure f) {
		
	}
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }

	public Rune getRune() {
		return r;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
	/**
	 * @see Shrine#turn(int)
	 */
	@Override
	public void turn(int round) {
		ItemOwner o = r.getOwner();
		JDPoint p = o.getLocation();
		// System.out.println("Die Rune befindet sich im Moment in Raum:
		// "+p.toString());

	}

	@Override
	public String getText() {
		return toString() + "\n"+JDEnv.getResourceBundle().getString("shrine_runeFinder_text_a")+" "
				+ r.toString() + JDEnv.getResourceBundle().getString("shrine_runeFinder_text_b")+" ";
	}

	@Override
	public JDColor getColor() {
		return JDColor.blue;
	}

	@Override
	public boolean usableOnce() {
		return false;

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_runeFinder_name");
	}

	

	/**
	 * @see Shrine#getStatus()
	 */
	@Override
	public String getStatus() {
		return JDEnv.getResourceBundle().getString("shrine_runeFinder_name")+": " + r.toString();
	}

	/**
	 * @see Usable#use(fighter)
	 */
	@Override
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
