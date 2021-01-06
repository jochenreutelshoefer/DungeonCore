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

@Deprecated
public class Xmas extends Location {

	Item it;

	public Xmas(Item it, Room r) {
		super(r);
		this.it = it;
	}

	public void metaClick(Figure f) {

	}

	@Override
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_xmas_story");
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_xmas_name");
	}

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

	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public void turn(int k) {

	}

	@Override
	public boolean usableOnce() {
		return false;
	}
}
