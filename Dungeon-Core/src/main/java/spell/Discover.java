/*
 * Created on 13.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;

import dungeon.Door;
import dungeon.HiddenSpot;
import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.percept.TextPercept;
import game.JDEnv;

/**
 * @author Jochen
 *         <p>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Discover extends NoTargetSpell {
//	public int [] diff = { 3 , 13 };
//	public int [] diffMin = { 6 , 15};

	public static int[][] values = { { 6, 3, 5, 10, 1 },
			{ 15, 10, 7, 30, 2 }
	};

	private final boolean isPossibleNormal = true;
	private final boolean isPossibleInFight = false;

	public Discover(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);

	}

	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}

	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}


	public Discover(int level) {
		super(level, values[level - 1]);
	}

	public Discover() {
	}

	@Override
	public int getType() {
		return AbstractSpell.SPELL_DISCOVER;
	}

	public boolean fightModus() {
		return isPossibleInFight;
	}

	public boolean normalModus() {
		return isPossibleNormal;
	}

	@Override
	public void sorcer(Figure mage) {

		Room r = mage.getRoom().getDungeon().getRoom(mage.getLocation());
		HiddenSpot spot = r.getSpot();
		boolean foundSomething = false;
		if (spot != null) {
			spot.setFound(true);

			String str = JDEnv.getResourceBundle().getString("spell_discover_spot");
			mage.tellPercept(new TextPercept(str));
			foundSomething = true;
		}
		Door[] doors = r.getDoors();
		for (int i = 0; i < 4; i++) {
			if (doors[i] != null && doors[i].isHidden()) {
				doors[i].setHidden(false);
				String str = JDEnv.getResourceBundle().getString("spell_discover_door");
				mage.tellPercept(new TextPercept(str));
				foundSomething = true;
			}
		}
		if (!foundSomething) {

			String str = JDEnv.getResourceBundle().getString("spell_discover_nothing");
			mage.tellPercept(new TextPercept(str));
		}

	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_discover_name");
	}

	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_discover_text");
	}
}
