/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package de.jdungeon.location;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;
@Deprecated
public class InfoShrine extends Location {

	String info;

	public InfoShrine(String s, Room p) {
		super(p);
		info = s;
	}

	public void metaClick(Figure f) {

	}

	@Override
	public void turn(int round, DungeonWorldUpdater mode) {
	}

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
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		// todo: reimplement entirely: what should happen on use?
		return ActionResult.DONE;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

}
