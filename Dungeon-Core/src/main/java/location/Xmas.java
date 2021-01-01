/*
 *
 * Created on 17.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package location;

import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.JDEnv;
import item.Item;
import item.interfaces.Usable;

/**
 * @author Jochen
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Xmas extends Location {

	Item it;

	public Xmas(Item it, Room r) {
		super(r);
		this.it = it;
	}

	public void metaClick(Figure f) {

	}

	/**
	 * @see Location#getStory()
	 */
	@Override
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_xmas_story");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_xmas_name");
	}

	/**
	 * @see Location#getText()
	 */
	@Override
	public String getText() {
		return toString();
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		if (it != null) {
			f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("shrine_xmas_use"), round));
			this.location.addItem(it);
			it = null;
		}
		else {
			// System.out.println("Item ist null!");
		}
		return true;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f instanceof Hero;
	}

	/**
	 * @see Location#getStatus()
	 */
	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public void turn(int k) {

	}

	/**
	 * @see Usable#usableOnce()
	 */
	@Override
	public boolean usableOnce() {
		return false;
	}
}