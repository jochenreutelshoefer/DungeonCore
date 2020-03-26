package item.equipment;

import game.JDEnv;

public class ArmorItem extends EquipmentItem {

	public int armorValue;
	private int damg = 0;
	int dmgPerHP = 35;

	public ArmorItem(int armor, int value, boolean m, int hitPoints) {

		super(value, m);
		armorValue = armor;
	}

	public void hit(int value) {
		// concept off

	}

	public int getArmorValue() {
		return armorValue;
	}

	public void takeRelDamage(double d) {
		// concept off
	}

	@Override
	public String getText() {
		return "\n" + JDEnv.getResourceBundle().getString("gui_armor") + ": " + getArmorValue() + "/" + armorValue + "\n";
	}
}
	
	
