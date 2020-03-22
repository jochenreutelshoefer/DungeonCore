/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;

import java.util.List;

import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.percept.TextPercept;
import game.JDEnv;

public class Spy extends NoTargetSpell {

	public static int[][] values = { { 10, 6, 5, 10, 1 },
			{ 15, 10, 7, 30, 2 }
	};

	private final boolean isPossibleNormal = true;
	private final boolean isPossibleInFight = false;

	public Spy(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
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


	@Override
	public int getType() {
		return AbstractSpell.SPELL_SPY;
	}

	public Spy(int level) {
		super(level, values[level - 1]);
	}

	public Spy() {
	}

	public int getCost(int level) {
		return level * 8;
	}

	@Override
	public void sorcer(Figure mage, int round) {

		List<Room> rooms = mage.getRoom().getNeighboursWithDoor();

		for (int i = 0; i < rooms.size(); i++) {
			Room toView = rooms.get(i);
			mage.getRoomObservationStatus(toView.getLocation())
					.setVisibilityStatus(RoomObservationStatus.VISIBILITY_FIGURES);
		}

		String str = JDEnv.getResourceBundle().getString("spell_spy_cast");
		mage.tellPercept(new TextPercept(str, round));

	}

	/**
	 * @see AbstractSpell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_spy_name");
	}

	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_spy_text");
		return s;
	}

}
