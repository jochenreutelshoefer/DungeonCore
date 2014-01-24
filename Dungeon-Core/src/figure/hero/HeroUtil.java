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
import spell.Convince;
import spell.Fireball;
import spell.GoldenHit;
import spell.Raid;
import spell.Spell;

public class HeroUtil {

	// HP, ST,DX,PY,AX, LZ, SW, KN, WM,NN,KN, UN,SC, DU, SP
	public static final double[] warriorBasic = { 42, 8, 6, 5, 10, 10, 20, 10,
			0, 0, 10, 0, 1, 10, 0.2 };
	public static final double[] hunterBasic = { 35, 5, 9, 5, 0, 0, 0, 20, 10,
			0, 0, 10, 3, 13, 0.3 };
	public static final double[] druidBasic = { 39, 6, 6, 7, 0, 0, 0, 10, 20,
			10, 0, 0, 2, 20, 0.6 };
	public static final double[] mageBasic = { 30, 5, 5, 9, 0, 10, 0, 0, 20,
			10, 10, 10, 1, 23, 1.0 };

	public static Hero getBasicHero(int heroCode, String heroName,
			String signString) {
		
		Weapon waffe = null;
		int healthVal = 0;
		int strengthVal = 0;
		int dexterityVal = 0;
		int psychoVal = 0;
		int axe = 0;
		int lance = 0;
		int sword = 0;
		int club = 0;
		int wolfknife = 0;
		int nature = 0;
		int undead = 0;
		int scout = 0;
		int creature = 0;
		int dust = 0;
		double dustReg = 0;
		boolean thief = false;
		Spellbook spells = new Spellbook();

		switch (heroCode) {
		case 1:

			spells.addSpell(new GoldenHit(1));
			waffe = new Sword(25, false);
			healthVal = (int) warriorBasic[0];
			strengthVal = (int) warriorBasic[1];
			dexterityVal = (int) warriorBasic[2];
			psychoVal = (int) warriorBasic[3];
			axe = (int) warriorBasic[4];
			lance = (int) warriorBasic[5];
			sword = (int) warriorBasic[6];
			club = (int) warriorBasic[7];
			wolfknife = (int) warriorBasic[8];
			nature = (int) warriorBasic[9];
			creature = (int) warriorBasic[10];
			undead = (int) warriorBasic[11];
			scout = (int) warriorBasic[12];
			dust = (int) warriorBasic[13];
			dustReg = warriorBasic[14];

			break;

		case 2:
			spells.addSpell(new Raid(1));
			waffe = new Club(25, false);
			thief = true;
			healthVal = (int) hunterBasic[0];
			strengthVal = (int) hunterBasic[1];
			dexterityVal = (int) hunterBasic[2];
			psychoVal = (int) hunterBasic[3];
			axe = (int) hunterBasic[4];
			lance = (int) hunterBasic[5];
			sword = (int) hunterBasic[6];
			club = (int) hunterBasic[7];
			wolfknife = (int) hunterBasic[8];
			nature = (int) hunterBasic[9];
			creature = (int) hunterBasic[10];
			undead = (int) hunterBasic[11];
			scout = (int) hunterBasic[12];
			dust = (int) hunterBasic[13];
			dustReg = hunterBasic[14];
			break;
		case 3:
			spells.addSpell(new Convince(1));
			waffe = new Wolfknife(25, false);
			healthVal = (int) druidBasic[0];
			strengthVal = (int) druidBasic[1];
			dexterityVal = (int) druidBasic[2];
			psychoVal = (int) druidBasic[3];
			axe = (int) druidBasic[4];
			lance = (int) druidBasic[5];
			sword = (int) druidBasic[6];
			club = (int) druidBasic[7];
			wolfknife = (int) druidBasic[8];
			nature = (int) druidBasic[9];
			creature = (int) druidBasic[10];
			undead = (int) druidBasic[11];
			scout = (int) druidBasic[12];
			dust = (int) druidBasic[13];
			dustReg = druidBasic[14];
			break;
		case 4:
			spells.addSpell(new Fireball(1));
			waffe = new Lance(25, false);
			healthVal = (int) mageBasic[0];
			strengthVal = (int) mageBasic[1];
			dexterityVal = (int) mageBasic[2];
			psychoVal = (int) mageBasic[3];
			axe = (int) mageBasic[4];
			lance = (int) mageBasic[5];
			sword = (int) mageBasic[6];
			club = (int) mageBasic[7];
			wolfknife = (int) mageBasic[8];
			nature = (int) mageBasic[9];
			creature = (int) mageBasic[10];
			undead = (int) mageBasic[11];
			scout = (int) mageBasic[12];
			dust = (int) mageBasic[13];
			dustReg = mageBasic[14];
			break;
		default:
			// System.out.println("heroCode Error!");
		}
		
		Hero held =
				new Hero(
					heroName,
					heroCode,
					signString,
					healthVal,
					strengthVal,
					dexterityVal,
					psychoVal,
					axe,
					lance,
					sword,
					club,
					wolfknife,
					nature,
					creature,
					undead,
					scout,
					dust,
					dustReg,
					0);

		//spells.addSpell(new Threat());
		held.setSpellbook(spells);

		held.getCharacter().setSpellPoints(1);
		if (thief) {
			held.setThief(true);
		}

		if (held.getHeroCode() == Hero.HEROCODE_DRUID) {
			Spell s = new Bonebreaker(1);
			Scroll scroll1 = new Scroll(s, 5);
			Scroll scroll2 = new Scroll(s, 5);
			Scroll scroll3 = new Scroll(s, 5);
			held.takeItem(scroll1, null);
			held.takeItem(scroll2, null);
			held.takeItem(scroll3, null);
		}

		held.takeItem(waffe, null);
		held.takeItem(new Armor(10, false), null);


		return held;
	}

}
