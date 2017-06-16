package figure.hero;

import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Weapon;
import item.equipment.weapon.Wolfknife;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import spell.Bonebreaker;
import spell.Convince;
import spell.Escape;
import spell.EscapeRoute;
import spell.Fireball;
import spell.GoldenHit;
import spell.GoldenThrow;
import spell.Heal;
import spell.Isolation;
import spell.KeyLocator;
import spell.Light;
import spell.Raid;
import spell.Repair;
import spell.Search;
import spell.AbstractSpell;
import spell.Spy;
import spell.Steal;
import spell.Thunderstorm;
import spell.conjuration.FirConjuration;
import fight.SlapResult;
import figure.Figure;
import figure.attribute.Attribute;
import figure.attribute.DerivedAttribute;
import figure.attribute.ItemModification;
import figure.attribute.TimedAttributeModification;
import figure.monster.CreatureMonster;
import figure.monster.Monster;
import figure.monster.NatureMonster;
import figure.monster.UndeadMonster;
import figure.percept.InfoPercept;
import figure.percept.TextPercept;

/**
 * Diese Klasse verwaltet alle Charakterwerte (Attribute) eines Helden
 */
public class Character implements Serializable {

	private final List<TimedAttributeModification> modifications = new LinkedList<TimedAttributeModification>();

	private final String name;

	private int level;

	private int points;

	private final Attribute health;

	private final Attribute strength;

	private final Attribute dexterity;

	private Attribute psycho;

	private final Attribute axe;

	private final Attribute lance;

	private final Attribute sword;

	private final Attribute club;

	private final Attribute wolfknife;

	private final Attribute scout;

	private final Attribute threat;

	private final Attribute dust;

	private final Attribute dustReg;

	private final Attribute healthReg;

	private final Attribute natureKnowledge;

	private final Attribute creatureKnowledge;

	private final Attribute undeadKnowledge;
	
	private final Map<Integer, Attribute> attributes = new HashMap<Integer, Attribute>();

	private final int lerndauer = 250;

	private final int lerndauerKnow = 20;

	private int skillPoints = 0;

	private int spellPoints = 1;

	private int expCode;

	private final int[] weaponExp = new int[5];

	private final int[] knowExp = new int[3];

	private int selfExp;

	private int totalExp;

	private final int selfLevel;

	private final Figure owner;

	private final List<AbstractSpell> spellBuffer = new LinkedList<AbstractSpell>();

	private final int weaponLearning[][] = { { 15, 20, 14, 12, 19 }, { 17, 11, 15, 16, 13 },
			{ 14, 15, 15, 16, 11 }, { 16, 14, 12, 17, 14 } };

	public Character(String name, double [] values, Figure f) {

		this(name, (int) values[0],
		(int) values[1],
		(int) values[2],
		(int) values[3],
		(int) values[4],
		(int) values[5],
		(int) values[6],
		(int) values[7],
		(int) values[8],
		(int) values[9],
		(int) values[10],
		(int) values[11],
		(int) values[12],
				0,
		(int) values[13],
		 values[14], f);
		if(values.length != 15) {
			throw new IllegalArgumentException("Needs to have 15 attribute values");
		}
	}


	public Character(String name, int HealthVal, int StrengthVal,
			int DexterityVal, int PsychoVal, int Axe, int Lance, int Sword,
			int Club, int Wolfknife, int nature, int creature, int undead,
			int scout, int braveI, int dust, double dustReg, Figure f) {

		this.name = name;
		owner = f;
		getSpellForLevelUp(0);

		this.health = new Attribute((Attribute.HEALTH), HealthVal);
		attributes.put(Attribute.HEALTH,health);

		this.strength = new Attribute((Attribute.STRENGTH), StrengthVal);
		attributes.put(Attribute.STRENGTH,strength);
		
		this.healthReg = new DerivedAttribute(Attribute.HEALTHREG, strength,
				DerivedAttribute.FORMULAR_KEY_8TH);
		attributes.put(Attribute.HEALTHREG,healthReg);

		this.dexterity = new Attribute(Attribute.DEXTERITY, DexterityVal);
		attributes.put(Attribute.DEXTERITY,dexterity);

		this.psycho = new Attribute(Attribute.PSYCHO, PsychoVal);
		attributes.put(Attribute.PSYCHO,psycho);

		this.axe = new Attribute(Attribute.AXE, Axe);
		attributes.put(Attribute.AXE,axe);

		this.lance = new Attribute(Attribute.LANCE, Lance);
		attributes.put(Attribute.LANCE,lance);

		this.sword = new Attribute(Attribute.SWORD, Sword);
		attributes.put(Attribute.SWORD,sword);

		this.club = new Attribute(Attribute.CLUB, Club);
		attributes.put(Attribute.CLUB,club);

		this.wolfknife = new Attribute(Attribute.WOLFKNIFE, Wolfknife);
		attributes.put(Attribute.WOLFKNIFE,wolfknife);

		this.natureKnowledge = new Attribute(Attribute.NATURE_KNOWLEDGE,
				nature);
		attributes.put(Attribute.NATURE_KNOWLEDGE,natureKnowledge);

		this.creatureKnowledge = new Attribute(Attribute.CREATURE_KNOWLEDGE,
				creature);
		attributes.put(Attribute.CREATURE_KNOWLEDGE,creatureKnowledge);

		this.undeadKnowledge = new Attribute((Attribute.UNDEAD_KNOWLEDGE),
				undead);
		attributes.put(Attribute.UNDEAD_KNOWLEDGE,undeadKnowledge);

		this.scout = new Attribute(Attribute.SCOUT, scout);
		attributes.put(Attribute.SCOUT,this.scout);
		

		this.dust = new Attribute(Attribute.DUST, dust);
		attributes.put(Attribute.DUST,this.dust);
		
		this.dustReg = new Attribute(Attribute.DUSTREG, dustReg);
		attributes.put(Attribute.DUSTREG,this.dustReg);

		this.threat = new Attribute(Attribute.THREAT,
				(strength.getBasic() + psycho.getBasic()) / 5);
		attributes.put(Attribute.THREAT,threat);

		owner.setActionPoints(3);

		weaponExp[0] = 0;
		weaponExp[1] = 0;
		weaponExp[2] = 0;
		weaponExp[3] = 0;
		weaponExp[4] = 0;

		selfExp = 0;
		selfLevel = 0;

		knowExp[0] = 0;
		knowExp[1] = 0;
		knowExp[2] = 0;

		totalExp = 0;

		level = 0;

	}

	public void decSkillPoints() {
		if (skillPoints > 0) {
			skillPoints -= 1;
		}
	}

	public boolean hasSkillPoints() {
		return (skillPoints > 0);
	}

	public int getKnowledge(Monster m) {
		int k = 0;
		if (m instanceof NatureMonster) {
			// System.out.print("Es ist ein Naturmonster: ");
			k = getNature_Knowledge_Value();
			// System.out.println(k);

		} else if (m instanceof CreatureMonster) {
			// System.out.print("Es ist ein Kreaturenmonster: ");
			k = this.getCreature_Knowledge_Value();
			// System.out.println(k);

		} else if (m instanceof UndeadMonster) {
			// System.out.print("Es ist ein Untotenmonster: ");
			k = getUndead_Knowledge_Value();
			// System.out.println(k);

		}
		return k / 10;

		// return Integer.MIN_VALUE;
	}

	public int giveWeapon_skill() {
		Weapon w = owner.getInventory().getWeapon1();
		if (w instanceof Sword)
			return (int) (sword.getValue() / 10);
		else if (w instanceof Axe)
			return (int) (axe.getValue() / 10);
		else if (w instanceof Lance)
			return (int) (lance.getValue() / 10);
		else if (w instanceof Club)
			return (int) (club.getValue() / 10);
		else if (w instanceof Wolfknife)
			return (int) (wolfknife.getValue() / 10);
		else
			return 0;
	}

	
	public List<AbstractSpell> getSpellBuffer() {
		return this.spellBuffer;
	}

	
	public int getSpellPoints() {
		return this.spellPoints;
	}

	public void decSpellPoints(int k) {
		spellPoints -= k;

	}

	public int giveType_skill(Figure m) {
		if (m instanceof Monster) {
			return this.getKnowledge((Monster) m);
		}
		return 1;
	}

	public void makeModifications(List mods) {
		int l = mods.size();

		for (int i = 0; i < l; i++) {
			ItemModification m = (ItemModification) (mods.get(i));
			int s = m.getAttribute();
			double v = m.getValue();
			if (s != Attribute.HEALTH && s != Attribute.DUST) {
				getAttribute(s).modValue(v);
			} else {
				getAttribute(s).modBasic(v);
			}
		}
	}

	public void giveExp(SlapResult r) {
		if (expCode == 0) {
			giveSelfExp(r.getExp());
		} else if (expCode == 1) {
			giveWeaponExp(r.getExp());
		} else if (expCode == 2) {
			giveKnowExp(r.getExp(), (Monster) r.getVictim());
		}

		testLevel();

	}

	public void giveExp(int r, Figure m) {
		if (expCode == 0) {
			giveSelfExp(r);
		} else if (expCode == 1) {
			giveWeaponExp(r);
		} else if (expCode == 2) {
			giveKnowExp(r, (Monster) m);
		}

		testLevel();

	}

	public int testLevel() {
		
		int nextLevel = getNextLevelPoints(level);
		if (totalExp >= nextLevel) {

			doLevelUp();
		}
		return nextLevel;
	}
	
	public int getNextLevelPoints(int lvl) {
		return ((lvl * lvl) + 1) * lerndauer;
	}

	public int[] getExpInfo(int expCode, int type) {
		int res[] = new int[2];
		if (expCode == 0) {
			int a = totalExp;
			int x = getNextLevelPoints(level-1);
			if (a - x > 0) {
				a = totalExp - x;
			}

			int b = getNextLevelPoints(level);
			if (b - x > 0) {
				b = b - getNextLevelPoints(level-1);
			}
			res[0] = a;
			res[1] = b;
		}
		if (expCode == 1) {
			res[0] = selfExp;
			res[1] = (selfLevel + 1) * 2 * lerndauer;
		}
		if (expCode == 2) {

			if (type == 0) {
				int value = (int) natureKnowledge.getBasic();
				int rest = value % 10;
				res[0] = (rest
						* (quadrieren(((int) natureKnowledge.getBasic()) / 10) + 1) * lerndauerKnow)
						+ knowExp[0];
				res[1] = 10
						* (quadrieren(((int) natureKnowledge.getBasic()) / 10) + 1)
						* lerndauerKnow;
			}
			if (type == 1) {
				int value = (int) creatureKnowledge.getBasic();
				int rest = value % 10;
				res[0] = (rest
						* (quadrieren(((int) creatureKnowledge
								.getBasic()) / 10) + 1) * lerndauerKnow)
						+ knowExp[1];
				res[1] = 10
						* (quadrieren(((int) creatureKnowledge
								.getBasic()) / 10) + 1) * lerndauerKnow;
			}
			if (type == 2) {
				int value = (int) undeadKnowledge.getBasic();
				int rest = value % 10;
				res[0] = (rest
						* (quadrieren(((int) undeadKnowledge.getBasic()) / 10) + 1) * lerndauerKnow)
						+ knowExp[2];
				res[1] = 10
						* (quadrieren(((int) undeadKnowledge.getBasic()) / 10) + 1)
						* lerndauerKnow;
			}
		}
		if (expCode == 3) {

			if (type == 0) {
				int value = (int) axe.getBasic();
				int rest = value % 10;
				res[0] = (rest * (int) (quadrieren(axe.getBasic() / 10) + 1) * weaponLearning[((Hero) owner)
						.getHeroCode() - 1][0])
						+ weaponExp[0];
				res[1] = 10 * (int) (quadrieren(axe.getBasic() / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][0];
			}
			if (type == 1) {
				int value = (int) club.getBasic();
				int rest = value % 10;
				res[0] = (rest * (int) (quadrieren(club.getBasic() / 10) + 1) * weaponLearning[((Hero) owner)
						.getHeroCode() - 1][1])
						+ weaponExp[1];
				res[1] = 10 * (int) (quadrieren(club.getBasic() / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][1];
			}
			if (type == 2) {
				int value = (int) lance.getBasic();
				int rest = value % 10;
				res[0] = (rest * (int) (quadrieren(lance.getBasic() / 10) + 1) * weaponLearning[((Hero) owner)
						.getHeroCode() - 1][2])
						+ weaponExp[2];
				res[1] = 10 * (int) (quadrieren(lance.getBasic() / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][2];
			}
			if (type == 3) {
				int value = (int) sword.getBasic();
				int rest = value % 10;
				res[0] = (rest * (int) (quadrieren(sword.getBasic() / 10) + 1) * weaponLearning[((Hero) owner)
						.getHeroCode() - 1][3])
						+ weaponExp[3];
				res[1] = 10 * (int) (quadrieren(sword.getBasic() / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][3];
			}
			if (type == 4) {
				int value = (int) wolfknife.getBasic();
				int rest = value % 10;
				res[0] = (rest
						* (int) (quadrieren(wolfknife.getBasic() / 10) + 1) * weaponLearning[((Hero) owner)
						.getHeroCode() - 1][4])
						+ weaponExp[4];
				res[1] = 10 * (int) (quadrieren(wolfknife.getBasic() / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][4];
			}

		}

		return res;
	}

	public void addSpell(AbstractSpell s) {
		owner.getSpellbook().addSpell(s);
	}

	private void doLevelUp() {
		level++;
		int k = 2 + (int) (Math.random() * 3);
		for (int i = 0; i < k; i++) {
			health.incBasic();
		}
		int j = (int) (1 + (int) (Math.random() * 3) * dustReg.getBasic());
		for (int i = 0; i < j; i++) {
			dust.incBasic();
		}
		owner.tellPercept(new InfoPercept(InfoPercept.LEVEL_UP));

		spellPoints++;
		AbstractSpell s = getSpellForLevelUp(level);
		threat.incBasic();
	}

	public void addModification(TimedAttributeModification mod) {
		modifications.add(mod);
	}

	public void giveSelfExp(int v) {
		totalExp += v;
		selfExp += v;
		levelUpSelf();
	}

	private void giveKnowExp(int v, Monster m) {
		totalExp += v;
		if (m instanceof NatureMonster) {
			knowExp[0] += v;
			levelUpKnow(0);
		} else if (m instanceof CreatureMonster) {
			knowExp[1] += v;
			levelUpKnow(1);
		} else if (m instanceof UndeadMonster) {
			knowExp[2] += v;
			levelUpKnow(2);
		}
	}

	public void giveWeaponExp(int v) {
		totalExp += v;
		Weapon w = owner.getInventory().getWeapon1();
		if (w instanceof Axe) {
			weaponExp[0] += v;
			levelUpWeapon(0);
		} else if (w instanceof Club) {
			weaponExp[1] += v;
			levelUpWeapon(1);
		} else if (w instanceof Lance) {
			weaponExp[2] += v;
			levelUpWeapon(2);
		} else if (w instanceof Sword) {
			// System.out.println("v: "+v);
			// System.out.println("vorher: "+weaponExp[3]);
			weaponExp[3] += v;
			// System.out.println("nachher: "+weaponExp[3]);
			levelUpWeapon(3);
		} else if (w instanceof Wolfknife) {
			weaponExp[4] += v;
			levelUpWeapon(4);
		}
	}

	public boolean wantLevelUpStrength() {
		if (skillPoints > 0) {
			skillPoints--;
			this.strength.incBasic();
			return true;
		}

		return false;
	}

	public boolean wantLevelUpDexterity() {
		if (skillPoints > 0) {
			skillPoints--;
			this.dexterity.incBasic();
			return true;
		}

		return false;
	}

	public boolean wantLevelUpPsycho() {
		if (skillPoints > 0) {
			skillPoints--;
			this.psycho.incBasic();
			return true;
		}

		return false;
	}

	private void levelUpKnow(int i) {
		// int lerndauer = 150;
		switch (i) {
		case 0:
			if (knowExp[0] >= (quadrieren(((int) natureKnowledge.getBasic()) / 10) + 1)
					* lerndauerKnow) {
				knowExp[0] = 0;
				natureKnowledge.incBasic();
				// Statements.add("Du hast Deine Naturkenntnis verbessert!");
			}
			break;
		case 1:
			if (knowExp[1] >= (quadrieren(((int) creatureKnowledge.getBasic()) / 10) + 1)
					* lerndauerKnow) {
				knowExp[1] = 0;
				creatureKnowledge.incBasic();
				// Statements.add(
				// "Du hast Deine Kreaturenkenntnis verbessert!");
			}
			break;
		case 2:
			if (knowExp[2] >= (quadrieren(((int) undeadKnowledge.getBasic()) / 10) + 1)
					* lerndauerKnow) {
				knowExp[2] = 0;
				undeadKnowledge.incBasic();
				// Statements.add("Du hast Deine Untotenkenntnis verbessert!");
			}
			break;
		}
	}
	
	private int getNextSelfLevelPoints(int lvl) {
		return ((lvl + 1) * 2) * lerndauer;
	}

	private void levelUpSelf() {

		// //System.out.println("Erfahrungspunkte: " +
		// Integer.toString(selfExp));
		if (selfExp >= getNextSelfLevelPoints(this.selfLevel)) {
			selfExp = 0;
			doLevelUpSelf();
		}
	}

	private void doLevelUpSelf() {
		skillPoints++;
	}

	private AbstractSpell getSpellForLevelUp(int level) {
		if (level > 4) {
			return null;
		}

		int code = ((Hero) owner).getHeroCode();
		if (code == Hero.HEROCODE_WARRIOR) {
			if (level == 0) {
				
				spellBuffer.add(new GoldenHit(1));
				spellBuffer.add(new GoldenThrow(1));
				spellBuffer.add(new Repair(1));

			}
			if (level == 2) {
				spellBuffer.add(new Isolation(1));
			}
			
		}
	
		if (code == Hero.HEROCODE_HUNTER) {
			if (level == 0) {
				spellBuffer.add(new Search(1));
				
				spellBuffer.add(new Steal(1));
				spellBuffer.add(new Raid(1));
			}
			if (level == 1) {
				spellBuffer.add(new KeyLocator(1));
			}
			if(level == 2){
				spellBuffer.add(new EscapeRoute(1));
			}

		}
		if (code == Hero.HEROCODE_DRUID) {
			if (level == 0) {
				spellBuffer.add(new Heal(1, 8, 4, 6, 35,1));
				spellBuffer.add(new Convince(1));
				spellBuffer.add(new Escape(1, 2, 2, 5, 10,1));

			}
			if (level == 1) {
				spellBuffer.add(new Spy(1));
				spellBuffer.add(new FirConjuration(1));
			}
			if (level == 2) {
				spellBuffer.add(new Bonebreaker(1, 10, 8, 10, 10,1));
			}
		}
		if (code == Hero.HEROCODE_MAGE) {
			if (level == 0) {
				spellBuffer.add(new Fireball(1));
				spellBuffer.add(new Light(1));
				spellBuffer.add(new Thunderstorm(1));
			}
			if (level == 1) {
				spellBuffer.add(new Isolation(1));
				
			}
			if (level == 3) {
				spellBuffer.add(new Fireball(2, 11, 8, 9, 26,1));
			}

		}

		return null;
	}

	
	public void setSpellPoints(int k) {
		this.spellPoints = k;
	}

	private int quadrieren(int k) {

		return k * k;
	}

	private double quadrieren(double k) {

		return k * k;
	}

	private void levelUpWeapon(int i) {
		// int lerndauer = 120;
		switch (i) {
		case 0:
			if (weaponExp[0] >= (quadrieren(((int) axe.getBasic()) / 10) + 1)
					* weaponLearning[((Hero) owner).getHeroCode() - 1][0]) {
				weaponExp[0] -= (quadrieren(((int) axe.getBasic()) / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][0];
				int before = (int) axe.getBasic() / 10;
				axe.incBasic();
				int after = (int) axe.getBasic() / 10;
				if (after > before) {
					owner
							.tellPercept(new TextPercept(
									"Du hast Deine Fertigkeit mit der Axt verbessert!"));
				}
			}
			break;
		case 1:
			if (weaponExp[1] >= (quadrieren(((int) club.getBasic()) / 10) + 1)
					* weaponLearning[((Hero) owner).getHeroCode() - 1][1]) {
				weaponExp[1] -= (quadrieren(((int) club.getBasic()) / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][1];
				int before = (int) club.getBasic() / 10;
				club.incBasic();
				int after = (int) club.getBasic() / 10;
				if (after > before) {
					owner
							.tellPercept(new TextPercept(
									"Du hast Deine Fertigkeit mit dem Kn�ppel verbessert!"));
				}
			}
			break;
		case 2:
			if (weaponExp[2] >= (quadrieren(((int) lance.getBasic()) / 10) + 1)
					* weaponLearning[((Hero) owner).getHeroCode() - 1][2]) {
				weaponExp[2] -= (quadrieren(((int) lance.getBasic()) / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][2];
				int before = (int) lance.getBasic() / 10;
				lance.incBasic();
				int after = (int) lance.getBasic() / 10;
				if (after > before) {
					owner
							.tellPercept(new TextPercept(
									"Du hast Deine Fertigkeit mit der Lanze verbessert!"));
				}
			}
			break;
		case 3:
			if (weaponExp[3] >= (quadrieren(sword.getBasic() / 10) + 1)
					* weaponLearning[((Hero) owner).getHeroCode() - 1][3]) {
				weaponExp[3] -= (quadrieren(sword.getBasic() / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][3];
				int before = (int) sword.getBasic() / 10;
				sword.incBasic();
				int after = (int) sword.getBasic() / 10;
				if (after > before) {
					owner
							.tellPercept(new TextPercept(
									"Du hast Deine Fertigkeit mit dem Schwert verbessert!"));
				}
			}
			break;
		case 4:
			if (weaponExp[4] >= (quadrieren(((int) wolfknife.getBasic()) / 10) + 1)
					* weaponLearning[((Hero) owner).getHeroCode() - 1][4]) {
				weaponExp[4] -= (quadrieren(((int) wolfknife.getBasic()) / 10) + 1)
						* weaponLearning[((Hero) owner).getHeroCode() - 1][4];
				int before = (int) wolfknife.getBasic() / 10;
				wolfknife.incBasic();
				int after = (int) wolfknife.getBasic() / 10;
				if (after > before) {
					owner
							.tellPercept(new TextPercept(
									"Du hast Deine Fertigkeit mit dem Wolfsmesser verbessert!"));
				}
			}
			break;
		}
	}

	public Attribute getAttribute(int a) {
		Attribute attr = attributes.get(a);
		if(attr != null) {
			return attr;
		} else {
			return null;
		}

	}

	public int getHealth_Value() {
		return (int) health.getValue();
	}

	public int getStrength_Value() {
		return (int) strength.getValue();
	}

	public int getDexterity_Value() {
		return (int) dexterity.getValue();
	}

	public int getPsycho_Value() {
		return (int) psycho.getValue();
	}

	public int getNature_Knowledge_Value() {
		return (int) natureKnowledge.getValue();
	}

	public int getCreature_Knowledge_Value() {
		return (int) creatureKnowledge.getValue();
	}

	public int getUndead_Knowledge_Value() {
		return (int) undeadKnowledge.getValue();
	}

	public int getAxeValue() {
		return (int) axe.getValue();
	}

	public int getClubValue() {
		return (int) club.getValue();
	}

	public int getLanceValue() {
		return (int) lance.getValue();
	}

	public int getSwordValue() {
		return (int) sword.getValue();
	}

	public int getWolfknifeValue() {
		return (int) wolfknife.getValue();
	}

	public int getScoutValue() {
		return (int) scout.getValue();
	}

	public Attribute getAxe() {
		return axe;
	}

	public Attribute getClub() {
		return club;
	}

	public Attribute getCreature_Knowledge() {
		return creatureKnowledge;
	}

	public Attribute getDexterity() {
		return dexterity;
	}

	public int getExpCode() {
		return expCode;
	}

	public Attribute getHealth() {
		return health;
	}

	public int[] getKnowExp() {
		return knowExp;
	}

	public Attribute getLance() {
		return lance;
	}

	public int getLevel() {
		return level;
	}

	public List<TimedAttributeModification> getModifications() {
		return modifications;
	}

	public String getName() {
		return name;
	}

	public Attribute getNature_Knowledge() {
		return natureKnowledge;
	}

	public int getPoints() {
		return points;
	}

	public Attribute getPsycho() {
		return psycho;
	}

	public Attribute getScout() {
		return scout;
	}

	public int getSelfExp() {
		return selfExp;
	}

	public int getSelfLevel() {
		return selfLevel;
	}

	public int getKnowledgeBalance(Monster m) {
		int level = m.getLevel();
		int know = getKnowledge(m);
		return know - level;
	}

	public Attribute getStrength() {
		return strength;
	}

	public Attribute getSword() {
		return sword;
	}

	public Attribute getThreat() {
		return threat;
	}

	public int getTotalExp() {
		return totalExp;
	}

	public Attribute getUndead_Knowledge() {
		return undeadKnowledge;
	}

	public int[] getWeaponExp() {
		return weaponExp;
	}

	public Attribute getWolfknife() {
		return wolfknife;
	}

	public void setExpCode(int expCode) {
		this.expCode = expCode;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	protected String getStatus() {
		int i = owner.getHealthLevel();
		String status = "invalid status";
		if (i == 4)
			status = (" Du f�hlst Dich noch immer stark.");
		else if (i == 3)
			status = (" Du bist angeschlagen.");
		else if (i == 2)
			status = (" Du bist verletzt.");
		else if (i == 1)
			status = (" Du f�hlst Dich sehr schwach.");
		else if (i == 0)
			status = (" Du wirst gerade Ohnm�chtig...");
		else
			status = "DAS SOLL SO NICHT SEIN HERO 651!";

		return status;
	}

	public void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	public Attribute getDust() {
		return dust;
	}

	public Attribute getDustReg() {
		return dustReg;
	}

	

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setPsycho(Attribute attribute) {
		psycho = attribute;
	}

	public static int getRandomAttr() {
		// System.out.println("getRandomAttr");
		int k = (int) (Math.random() * 100);
		// System.out.println(Integer.toString(k));
		int s;

		if (k < 12) {
			s = Attribute.PSYCHO;
		} else if (k < 24) {
			s = Attribute.STRENGTH;
		} else if (k < 36) {
			s = Attribute.DEXTERITY;
		} else if (k < 42) {
			s = Attribute.AXE;
		} else if (k < 48) {
			s = Attribute.CLUB;
		} else if (k < 54) {
			s = Attribute.LANCE;
		} else if (k < 60) {
			s = Attribute.SWORD;
		} else if (k < 66) {
			s = Attribute.WOLFKNIFE;
		} else if (k < 73) {
			s = Attribute.NATURE_KNOWLEDGE;
		} else if (k < 80) {
			s = Attribute.CREATURE_KNOWLEDGE;
		} else if (k < 87) {
			s = Attribute.UNDEAD_KNOWLEDGE;
		} else if (k < 95) {
			s = Attribute.SCOUT;
		} else if (k < 100) {
			s = Attribute.THREAT;
		} else
			s = -1;
		return s;
	}

}
