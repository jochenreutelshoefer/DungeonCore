package de.jdungeon.location;

import java.util.Iterator;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;
import de.jdungeon.item.interfaces.Usable;

/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Statue extends Location {

	public Statue(Room r) {
		super(r);
	}

	public void metaClick(Figure f) {

	}

	public Statue() {
		super();
	}

	@Override
	public void turn(int round, DungeonWorldUpdater mode) {
		//System.out.println(de.jdungeon.location.toString());
		List<Figure> l = location.getRoomFigures();
		for (Iterator<Figure> iter = l.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			if (element instanceof Hero) {
				element.heal(3, round);
			}
		}
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		return ActionResult.OTHER;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	/**
	 * @see Location#getStory()
	 */
	@Override
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_statue_story");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getText();
	}

	/**
	 * @see Location#getText()
	 */
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("shrine_statue_name");
	}

	/**
	 * @see Location#getStatus()
	 */
	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return false;
	}

	/**
	 * @see Usable#usableOnce()
	 */
	@Override
	public boolean usableOnce() {
		return false;
	}
}
