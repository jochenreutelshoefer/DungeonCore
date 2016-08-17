/*
 * 
 * Created on 17.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package shrine;

import item.Item;
import item.interfaces.Usable;
import util.JDColor;
import dungeon.Room;
import figure.Figure;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.JDEnv;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Xmas extends Shrine {

	Item it;

	public Xmas(Item it, Room r) {
		super(r);
		this.it = it;
	}

	public void metaClick(Figure f) {

	}

	@Override
	public JDColor getColor() {
		return JDColor.DARK_GRAY;
	}

	/**
	 * @see Shrine#getStory()
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
	 * @see Shrine#getText()
	 */
	@Override
	public String getText() {
		return toString();
	}

	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_XMAS;
	}

	/**
	 * @see Shrine#clicked(fighter)
	 */
	@Override
	public boolean use(Figure f, Object target, boolean meta) {
		if (it != null) {
			f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString(
					"shrine_xmas_use")));
			this.location.addItem(it);
			it = null;
		} else {
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
	 * @see Shrine#getStatus()
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
