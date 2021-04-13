/*
 * Created on 17.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.location;

import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.figure.percept.UsePercept;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;
import de.jdungeon.item.Item;

public class Corpse extends Location {
	private final int type;
	private List<Item> items;

	private final int type_code;

	public Corpse(List<Item> it, Room r, int type) {
		super(r);
		this.items = it;
		type_code = type;
		this.type = type;
	}

	@Override
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_corpse_story");
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	public void metaClick(Figure f) {

	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_corpse_name");
	}

	@Override
	public String getText() {
		String s = "";
		if (this.type_code == 0) {
			s = JDEnv.getResourceBundle().getString("shrine_corpse_dwarf");
		}
		if (this.type_code == 1) {
			s = JDEnv.getResourceBundle().getString("shrine_corpse_warrior");
		}
		if (this.type_code == 2) {
			s = JDEnv.getResourceBundle().getString("shrine_corpse_thief");
			;
		}
		if (this.type_code == 3) {
			s = JDEnv.getResourceBundle().getString("shrine_corpse_druid");
			;
		}
		if (this.type_code == 4) {
			s = JDEnv.getResourceBundle().getString("shrine_corpse_mage");
			;
		}
		return s;
	}

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
		if(doIt) {
			if (items != null) {
				String s = JDEnv.getResourceBundle().getString("shrine_corpse_find");
				f.tellPercept(new TextPercept(s, round));
				Percept p = new UsePercept(f, this, round);
				this.location.addItems(items, null);
				f.getRoom().distributePercept(p);
				items = null;
			}
			return ActionResult.DONE;
		} else {
			return ActionResult.POSSIBLE;
		}
	}

	@Override
	public void turn(int k, GameLoopMode mode) {

	}

	@Override
	public boolean usableOnce() {
		return false;
	}
}
