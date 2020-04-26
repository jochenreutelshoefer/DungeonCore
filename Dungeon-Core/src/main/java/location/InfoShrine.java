/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package location;

import dungeon.RoomEntity;
import dungeon.Room;
import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;

public class InfoShrine extends Location {

	String info;

	public InfoShrine(String s, Room p) {
		super(p);
		info = s;
	}

	public void metaClick(Figure f) {

	}

	/**
	 * @see Location#turn(int)
	 */
	@Override
	public void turn(int round) {
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_info_name");
	}


	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public String getText() {
		return info;
	}

	@Override
	public String getStory() {

		String s = JDEnv.getResourceBundle().getString("shrine_info_story");
		s += ": \n" + info;
		return s;
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
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		return true;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

}
