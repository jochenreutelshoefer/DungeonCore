package spell;

import dungeon.RoomEntity;
import figure.Figure;
import figure.percept.TextPercept;
import game.JDEnv;
import game.RoomInfoEntity;
import item.Item;
import item.ItemInfo;
import item.equipment.EquipmentItem;

/**
 * @author Duke1
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class Repair extends AbstractTargetSpell implements TargetSpell {

	public static int[][] values = { { 5, 2, 5, 10, 1 },
			{ 10, 4, 7, 30, 2 }
	};

	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if (target instanceof EquipmentItem) {
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

	public Repair(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;

	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public int getType() {
		return AbstractSpell.SPELL_REPAIR;
	}

	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_rapair_text");
		return s;
	}

	public Repair(int level) {

		super(level, values[level - 1]);

		isPossibleNormal = true;
		isPossibleInFight = false;
	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_rapair_name");
	}

	public int getCost(int level) {
		return 5 * level;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {
		//ItemChoiceView v = new ItemChoiceView(mage.getGame().getGui().getMainFrame(),"Gegenstand ausw�hlen",this,mage, false);
		if (target instanceof Item) {
			repairItem((Item) target);
			String str = JDEnv.getResourceBundle().getString("spell_rapair_cast");
			mage.tellPercept(new TextPercept(str, round));
		}

	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return ItemInfo.class;
	}

	/**
	 * @see AbstractSpell#getName()
	 */

	public void repairItem(Item i) {
		if (i instanceof EquipmentItem) {
			System.out.println("reairing: " + i.toString());
			((EquipmentItem) i).repair(level * 5);
		}
	}

}
