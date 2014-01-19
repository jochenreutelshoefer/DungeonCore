package figure.hero;

import figure.Spellbook;
import item.equipment.Armor;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Weapon;
import item.equipment.weapon.Wolfknife;
import item.paper.Scroll;
import spell.Bonebreaker;
import spell.GoldenHit;
import spell.Spell;

public class HeroUtil {

	public static void addHeroStartWeapon(Hero held) {

		Weapon waffe;
		Spellbook spells = new Spellbook();
		held.setSpellbook(spells);
		switch (held.getHeroCode()) {
		case 1:
			waffe = new Sword(25, false);
			spells.addSpell(new GoldenHit(1));
			break;
		case 2:
			waffe = new Club(25, false);
			held.setThief(true);
			break;
		case 3:
			waffe = new Wolfknife(25, false);
			Spell s = new Bonebreaker(1);
			Scroll scroll1 = new Scroll(s, 5);
			Scroll scroll2 = new Scroll(s, 5);
			Scroll scroll3 = new Scroll(s, 5);
			held.takeItem(scroll1, null);
			held.takeItem(scroll2, null);
			held.takeItem(scroll3, null);
			break;
		case 4:
			waffe = new Lance(25, false);
			break;
		default:
			waffe = new Sword(100, false);
			// System.out.println("HeroCode Error!");
		}

		held.takeItem(waffe, null);
		held.takeItem(new Armor(10, false), null);

	}
}
