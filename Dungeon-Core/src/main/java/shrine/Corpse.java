/*
 * Created on 17.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package shrine;

/**
 * @author Jochen
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.List;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.percept.Percept;
import figure.percept.TextPercept;
import figure.percept.UsePercept;
import game.JDEnv;
import item.Item;
import item.interfaces.Usable;
import util.JDColor;

public class Corpse extends Shrine {
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
	public JDColor getColor() {
		return JDColor.DARK_GRAY;
	}

	/**
	 * @see Shrine#getStory()
	 */
	@Override
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_corpse_story");
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_CORPSE;
	}

	public void metaClick(Figure f) {

	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_corpse_name");
	}

	public static String makeAufzeichnungen(Dungeon d, JDPoint startRoom, Room xroom, Room paxTreasure) {

		String str = JDEnv.getResourceBundle().getString("shrine_corpse_note_begin") + " ";

		int dist = JDPoint.getAbsDist(startRoom, xroom.getNumber());
		if (dist <= 2) {
			str += JDEnv.getResourceBundle().getString("shrine_corpse_note_near") + " ";
		}
		else {
			if (dist <= 5) {
				str += JDEnv.getResourceBundle().getString("shrine_corpse_note_mid") + " ";
			}
			else {
				str += JDEnv.getResourceBundle().getString("shrine_corpse_note_far") + " ";
			}
			str += d.getRoom(startRoom).getDirectionString(xroom);
		}

		str += JDEnv.getResourceBundle().getString("shrine_corpse_note_middlepart") + " ";

		int dist2 = JDPoint.getAbsDist(startRoom, paxTreasure.getNumber());
		if (dist2 <= 2) {
			str += JDEnv.getResourceBundle().getString("shrine_corpse_note_near") + " ";
		}
		else {
			if (dist2 <= 5) {
				str += JDEnv.getResourceBundle().getString("shrine_corpse_note_mid") + " ";
			}
			else {
				str += JDEnv.getResourceBundle().getString("shrine_corpse_note_far") + " ";
			}
			str += d.getRoom(startRoom).getDirectionString(paxTreasure);
		}

		str += JDEnv.getResourceBundle().getString("shrine_corpse_note_end") + " ";
		return str;
	}

	/**
	 * @see Shrine#getText()
	 */
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
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		if (items != null) {
			String s = JDEnv.getResourceBundle().getString("shrine_corpse_find");
			f.tellPercept(new TextPercept(s, round));
			Percept p = new UsePercept(f, this, round);
			this.location.addItems(items, null);
			f.getRoom().distributePercept(p);
			items = null;
		}
		return true;
	}

	@Override
	public void turn(int k) {

	}

	@Override
	public boolean usableOnce() {
		return false;
	}


}
