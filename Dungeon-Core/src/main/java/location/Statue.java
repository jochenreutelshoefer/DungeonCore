package location;

import java.util.Iterator;
import java.util.List;

import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;
import item.interfaces.Usable;

/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Statue extends Location {

	/**
	 * @see Location#turn(int)
	 */

	public Statue(Room r) {
		super(r);
	}

	public void metaClick(Figure f) {

	}

	public Statue() {
		super();
	}

	@Override
	public void turn(int round) {
		//System.out.println(location.toString());
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
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		return false;
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
