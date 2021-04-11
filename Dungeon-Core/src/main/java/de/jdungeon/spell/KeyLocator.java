package de.jdungeon.spell;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Lock;
import de.jdungeon.dungeon.LockInfo;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.game.RoomInfoEntity;
import de.jdungeon.item.interfaces.ItemOwner;

/**
 * @author Duke1
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class KeyLocator extends AbstractTargetSpell implements TargetSpell {

	public static int[][] values = {
			{ 3, 4, 5, 10, 1 },
			{ 6, 12, 7, 30, 1 }
	};

	private final boolean isPossibleNormal = true;
	private final boolean isPossibleInFight = false;

	public KeyLocator(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}

	@Override
	public int getType() {
		return AbstractSpell.SPELL_KEYLOCATOR;
	}

	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_keyLocator_text");
	}

	public KeyLocator(int level) {
		super(level, values[level - 1]);
		this.level = level;
	}

	public KeyLocator() {
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if (target instanceof Door || target instanceof Lock) {
			return true;
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {
		if (target instanceof Door) {
			Door d = ((Door) target);
			if (d.hasLock()) {
				d.getLock().setKeyLocatable(mage);
				tellKeyLocation(mage, d, round);
			}
		}
		else if (target instanceof Lock) {
			((Lock) target).setKeyLocatable(mage);
			tellDirection(((Lock) target).getKey().getOwner().getRoomNumber(), mage, round);
		}
		else {
			mage.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("spell_keyLocator_cast_nothing"), round));
		}
	}

	public static void tellKeyLocation(Figure mage, Door d, int round) {
		ItemOwner owner = d.getKey().getOwner();
		JDPoint location = owner.getRoomNumber();
		tellDirection(location, mage, round);
	}

	private static void tellDirection(JDPoint location, Figure f, int round) {
		f.tellPercept(new TextPercept(JDEnv.getResourceBundle()
				.getString("spell_keyLocator_cast_found") + ": " + location, round));
		f.getRoomVisibility().setVisibilityStatus(location, RoomObservationStatus.VISIBILITY_ITEMS);
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return LockInfo.class;
	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_keyLocator_name");
	}

}
