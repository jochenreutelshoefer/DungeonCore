package spell;

import dungeon.RoomEntity;
import game.RoomInfoEntity;
import item.Item;

import java.util.List;

import figure.Figure;
import figure.FigureInfo;
import figure.percept.TextPercept;
import game.JDEnv;

public class Steal extends AbstractTargetSpell {

	public static int[][] values = { { 6, 4, 8, 10, 1 }, { 15, 13, 12, 25, 2 } };

	private final boolean isPossibleNormal = false;
	private final boolean isPossibleInFight = true;

	public Steal(int level, int diffMin, int diff, int cost, int strength,
			int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
	}

	public Steal() {
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if (target instanceof Figure) {
			return true;
		}
		return false;
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
		return AbstractSpell.SPELL_STEAL;
	}

	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_steal_text");
		return s;
	}

	public Steal(int level) {

		super(level, values[level - 1]);
		this.level = level;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {

		if (target instanceof Figure) {
			Figure m = (Figure) target;

			List<Item> list = m.getItems();
			int max = -1;
			Item best = null;
			for (int i = 0; i < list.size(); i++) {
				Item it = list.get(i);
				int k = it.getWorth();
				if (k > max) {
					max = k;
					best = it;
				}
			}
			if (!list.isEmpty()) {
				m.removeItem(best);
				mage.takeItem(best);
				String str = (m.getName()
						+ JDEnv.getResourceBundle().getString(
								"spell_steal_cast") + ":" + best);
				mage.tellPercept(new TextPercept(str, round));
			}

		}

	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	/**
	 * @see AbstractSpell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_steal_name");
	}

}
