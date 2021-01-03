package figure.hero;

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
import spell.Heal;
import spell.Isolation;
import spell.KeyLocator;
import spell.Light;
import spell.Raid;
import spell.Search;
import spell.AbstractSpell;
import spell.Spy;
import spell.Steal;
import spell.conjuration.FirConjuration;
import figure.Figure;
import figure.attribute.Attribute;
import figure.attribute.DerivedAttribute;
import figure.attribute.ItemModification;
import figure.attribute.TimedAttributeModification;
import figure.monster.CreatureMonster;
import figure.monster.Monster;
import figure.monster.NatureMonster;
import figure.monster.UndeadMonster;

/**
 * Diese Klasse verwaltet alle Charakterwerte (Attribute) eines Helden
 */
public class Character implements Serializable {

	private final List<TimedAttributeModification> modifications = new LinkedList<TimedAttributeModification>();

	private final String name;

	private int points;

	private final Attribute health;

	private final Attribute strength;

	private final Attribute dexterity;

	private Attribute psycho;

	private final Attribute dust;

	private final Attribute dustReg;

	private final Attribute healthReg;

	private final Map<Attribute.Type, Attribute> attributes = new HashMap<>();

	private final int skillPoints = 0;

	private int spellPoints = 1;

	private final Figure owner;

	private final List<AbstractSpell> spellBuffer = new LinkedList<AbstractSpell>();

	public Character(String name, double [] values, Figure f) {

		this(name,
				(int) values[0],
		(int) values[1],
		(int) values[2],
		(int) values[3],
		(int) values[13],
		 values[14], f);
		if(values.length != 15) {
			throw new IllegalArgumentException("Needs to have 15 attribute values");
		}
	}


	public Character(String name, int HealthVal, int StrengthVal,
			int DexterityVal, int PsychoVal, int dust, double dustReg, Figure f) {

		this.name = name;
		owner = f;
		getSpellForLevelUp(0);

		this.health = new Attribute((Attribute.Type.Health), HealthVal);
		attributes.put(Attribute.Type.Health,health);

		this.strength = new Attribute((Attribute.Type.Strength), StrengthVal);
		attributes.put(Attribute.Type.Strength,strength);
		
		this.healthReg = new DerivedAttribute(Attribute.Type.HealthReg, strength, DerivedAttribute.FORMULAR_KEY_32TH);
		attributes.put(Attribute.Type.HealthReg,healthReg);

		this.dexterity = new Attribute(Attribute.Type.Dexterity, DexterityVal);
		attributes.put(Attribute.Type.Dexterity,dexterity);

		this.psycho = new Attribute(Attribute.Type.Psycho, PsychoVal);
		attributes.put(Attribute.Type.Psycho,psycho);

		this.dust = new Attribute(Attribute.Type.Dust, dust);
		attributes.put(Attribute.Type.Dust,this.dust);
		
		this.dustReg = new Attribute(Attribute.Type.DustReg, dustReg);
		attributes.put(Attribute.Type.DustReg,this.dustReg);

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

	}

	public int giveWeapon_skill() {
		// concept is off
		return 1;
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
			Attribute.Type s = m.getAttribute();
			double v = m.getValue();
			if (s != Attribute.Type.Health && s != Attribute.Type.Dust) {
				getAttribute(s).modValue(v);
			} else {
				getAttribute(s).modBasic(v);
			}
		}
	}

	public void addModification(TimedAttributeModification mod) {
		modifications.add(mod);
	}

	private AbstractSpell getSpellForLevelUp(int level) {
		if (level > 4) {
			return null;
		}

		int code = ((Hero) owner).getHeroCode();
		if (code == Hero.HEROCODE_WARRIOR) {
			if (level == 0) {
				
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

	public Attribute getAttribute(Attribute.Type a) {
		Attribute attr = attributes.get(a);
		if(attr != null) {
			return attr;
		} else {
			return null;
		}

	}

	public int getStrength_Value() {
		return (int) strength.getValue();
	}

	public int getDexterity_Value() {
		return (int) dexterity.getValue();
	}

	public int getNature_Knowledge_Value() {
		// concept is off
		return 1;
	}

	public int getCreature_Knowledge_Value() {
		// concept is off
		return 1;
	}

	public int getUndead_Knowledge_Value() {
		// concept is off
		return 1;
	}

	public int getAxeValue() {
		// concept is off
		return 1;
	}

	public int getClubValue() {
		// concept is off
		return 1;
	}

	public int getLanceValue() {
		// concept is off
		return 1;
	}

	public int getSwordValue() {
		// concept is off
		return 1;
	}

	public int getWolfknifeValue() {
		// concept is off
		return 1;
	}

	public int getScoutValue() {
		// concept is off
		return 1;
	}


	public Attribute getDexterity() {
		return dexterity;
	}

	public Attribute getHealth() {
		return health;
	}

	public List<TimedAttributeModification> getModifications() {
		return modifications;
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public Attribute getPsycho() {
		return psycho;
	}

	public int getKnowledgeBalance(Monster m) {
		int know = getKnowledge(m);
		return know - 1;
	}

	public Attribute getStrength() {
		return strength;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	protected String getStatus() {
		int i = owner.getHealthLevel().getValue();
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

	public static Attribute.Type getRandomAttr() {
		int k = (int) (Math.random() * 36);
		Attribute.Type s;
		if (k < 12) {
			s = Attribute.Type.Psycho;
		} else if (k < 24) {
			s = Attribute.Type.Strength;
		} else if (k < 36) {
			s = Attribute.Type.Dexterity;
		} else {
			s = null;
		}
		return s;
	}

}
