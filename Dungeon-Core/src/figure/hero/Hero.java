package figure.hero;

import item.Bunch;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.equipment.Shield;
import item.equipment.weapon.Weapon;
import item.interfaces.ItemOwner;
import item.quest.LuziasBall;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import shrine.Shrine;
import spell.Spell;
import ai.DefaultHeroReflexBehavior;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.RouteInstruction;
import fight.Frightening;
import fight.Slap;
import fight.SlapResult;
import fight.Threat;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.Spellbook;
import figure.action.Action;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import figure.percept.DiePercept;
import figure.percept.InfoPercept;
import figure.percept.Percept;
import figure.percept.ShieldBlockPercept;
import figure.percept.TextPercept;
import game.Game;
import game.InfoEntity;
import game.InfoProvider;

/**
 * Der Held des Spiels. Es kann nur einen geben. Besteht hauptsaechlich aus
 * einem Character, einem Inventar mit den Gegenstaenden und einem Spellbook mit
 * den Zauberspruechen die er kann. Gesteuert wird der Held durch ein
 * HeroControl-Objekt.
 * 
 * @see Inventory
 * @see Character
 * @see Spellbook
 * @see HeroControl
 */
// a00d058
public class Hero extends Figure implements InfoProvider {

	// private int actionPoints;
	public boolean agentStarted = false;

	// private int fightAP = 0;
	int timeDelay = 2000;

	public static int hero_index_counter = -1;

	// private static LinkedList allHeros = new LinkedList();

	private int hero_index;

	public final static int HEROCODE_WARRIOR = 1;

	public final static int HEROCODE_HUNTER = 2;

	public final static int HEROCODE_DRUID = 3;

	public final static int HEROCODE_MAGE = 4;

	private int sanctions = 0;

	protected double fire_resist_rate = 1.0;

	protected double lightning_resist_rate = 1.0;

	protected double magic_resist_rate = 1.0;

	protected double poison_resist_rate = 1.0;

	private Character c;

	private Inventory inv;

	// private Game game;
	private Bunch bund = new Bunch();

	// public LinkedList Statements = new LinkedList();

	private JDPoint oldLocation = new JDPoint(0, 0);

	private int kills = 0;

	private int HeroCode;

	private String Sign;

	private boolean isDead = false;

	boolean paused = false;

	/**
	 * @return Returns the paused.
	 */
	public boolean isPaused() {
		return paused;
	}
	
	public Attribute getDexterity() {
		return c.getDexterity();
	}

	public Map getHighScoreData(String playerName, String comment,
			boolean reg, boolean liga, HeroInfo h) {
		return game.getHighScoreString(playerName, comment, reg, liga, h);
	}

	public Attribute getStrength() {
		return c.getStrength();
	}

	public int filterFrightening(Frightening fr) {
		return fr.getValue();
	}
	
	public int getWorth() {
		return 1000 + (500 * level);
	}
	
	protected boolean getBlock(int dmg) {
		//System.out.println("getBlock");
		Shield shield = this.inv.getShield1();
		if(shield != null) {
			int shieldValue = shield.getBlockValue();
			//System.out.println("shield: "+shieldValue);
			
			int total = (int)((shieldValue + c.getDexterity_Value()/3) * (((double)100+c.getDexterity_Value())/100));
			//System.out.println("dex: "+c.getDexterity_Value());
			//System.out.println("block total: "+total);
			int random = (int)(Math.random() * 100);
			//System.out.println("random: "+random);
			if(random > 100 - total) {
				
				shield.madeBlock(dmg);
				return true;
			}
				
			
		}
		
		return false;
		
	}

	public boolean canTakeItem(Item i) {
		return inv.canTakeItem(i);
	}

	/**
	 * @param paused
	 *            The paused to set.
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public int getLevel() {
		return c.getLevel();
	}

	public void setMakingSpecialAttack(boolean b) {
	}

	public int getKnowledgeBalance(Figure f) {
		if (f instanceof Monster) {
			return c.getKnowledgeBalance((Monster) f);
		} else {
			return this.getLevel() - f.getLevel();
		}
	}

	private Room[][] memory;

	public Item getItemNumber(int i) {
		return inv.getItemNumber(i);
	}

	public int getItemIndex(Item it) {
		List items = inv.getItems();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == it) {
				return i;
			}
		}
		return -1;
	}

	public void addModification(TimedAttributeModification mod) {
		// System.out.println("adding mod");
		c.addModification(mod);
	}

	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new HeroInfo(this, map);
	}

	public Hero(String name, int heroCode, String Sign, int HealthVal,
			int StrengthVal, int DexterityVal, int PsychoVal, int Axe,
			int Lance, int Sword, int Club, int Wolfknife, int nature,
			int creature, int undead, int scout, int dust, double dustReg,
			int ch) {
		super();
		this.HeroCode = heroCode;
		this.reflexReactionUnit = new DefaultHeroReflexBehavior(this);
		this.Sign = Sign;
		int brave = 0;
		if (heroCode == 1) {
			brave = 8;
		} else if (heroCode == 2) {
			brave = 4;
		} else if (heroCode == 3) {
			brave = 7;
		} else if (heroCode == 4) {
			brave = 5;
		}

		c = new Character(name, HealthVal, StrengthVal, DexterityVal,
				PsychoVal, Axe, Lance, Sword, Club, Wolfknife, nature,
				creature, undead, scout, brave, dust, dustReg, this);
		
		this.bravery = new Attribute(Attribute.BRAVE, brave);
		inv = new Inventory(3, 3, 3, 3, this);
		inv.addItem(bund);
		this.hero_index = Hero.hero_index_counter;
		Hero.hero_index_counter--;
		// Hero.allHeros.add(this);

	}

	public String getMclass() {
		return "Held";
	}

	public boolean removeItem(Item i) {
		getInventory().removeItem(i);
		return true;
	}

	// public int getActionPoints() {
	// return getCharacter().getActionPoints();
	// }

	// public void decActionPoints(int k) {
	// getCharacter().decActionPoints(k);
	// }

	public Character getCharacter() {
		return c;
	}

	public Action getForcedFightAction() {
		return null;
	}

	public Action getForcedMovementAction() {
		return null;
	}

	public LinkedList getItems() {
		return getInventory().getItems();
	}

	public double getFireResistRate() {
		return fire_resist_rate;
	}

	public double getLightningResistRate() {
		return lightning_resist_rate;
	}

	public double getMagicResistRate() {
		return magic_resist_rate;
	}

	public double getPoisonResistRate() {
		return poison_resist_rate;
	}

	public void setMemory() {
		// game = g;
		memory = new Room[game.getDungeon().getSize().getX()][game.getDungeon()
				.getSize().getY()];

	}

	// public int getLevel() {
	// return Level;
	// }

	public boolean giveAwayItem(Item i, ItemOwner o) {
		return getInventory().giveAwayItem(i, o);
	}

	// public LinkedList getStatements() {
	// LinkedList s = Statements;
	// Statements = new LinkedList();
	// return s;
	// }

	public Attribute getHealth() {
		return getCharacter().getHealth();
	}

	/**
	 * Describe <code>getKills</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Describe <code>incKills</code> method here.
	 * 
	 * @return an <code>void</code> value
	 */
	public void incKills() {
		kills++;
	}

	/**
	 * Describe <code>incFightAP</code> method here.
	 * 
	 * @return an <code>void</code> value
	 */
	// public void incFightAP(int v) {
	// getCharacter().decFightAP((-1) * v);
	// }
	// public void incAP(int v) {
	// incActionPoints(v);
	// }
	/**
	 * Describe <code>decFightAP</code> method here.
	 * 
	 * @return an <code>void</code> value
	 */
	// public void decFightAP(int v) {
	// int fightAP = getCharacter().getFightAP() - v;
	// getCharacter().setFightAP(fightAP);
	// }
	/**
	 * Describe <code>setFightAP</code> method here.
	 * 
	 * @return an <code>void </code> value
	 */
	// public void setFightAP(int v) {
	// getCharacter().setFightAP(v);
	// }
	/**
	 * Describe <code>getFightAP</code> method here.
	 * 
	 * @return an <code>int</code> value
	 */

	/**
	 * Describe <code>getHeroCode</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 */
	public int getHeroCode() {
		return HeroCode;
	}

	public Attribute getPsycho() {
		return getCharacter().getPsycho();
	}

	/**
	 * Describe <code>getSign</code> method here.
	 * 
	 * @return a <code>String</code> value
	 * 
	 */
	public String getSign() {
		return Sign;
	}

	public boolean isAbleToTakeItemInFight(Item it) {
		return this.isThief() && isAbleToTakeItem(it);
	}

	public boolean isAbleToUseShrine() {
		return true;
	}

	public boolean isAbleToUseChest() {
		return true;
	}

	public boolean isAbleToUseShrineInFight() {
		return false;
	}

	/**
	 * Describe <code>getName</code> method here.
	 * 
	 * @return a <code>String</code> value
	 */
	public String getName() {
		return getCharacter().getName();
	}

	/**
	 * Describe <code>getHealth</code> method here.
	 * 
	 * @return an <code>int</code> value
	 */

	/**
	 * Describe <code>getHealth_Value</code> method here.
	 * 
	 * @return an <code>int</code> value
	 */
	public int getHealth_Value() {
		return (int) getCharacter().getHealth().getValue();
	}

	public int getActualRangeCapability(int range) {
		Weapon w = this.getInventory().getWeapon1();
		if (w != null) {
			return w.getRangeCapability(range);
		}
		return 20;
	}

	
	
//	protected ActionResult processSpecialAction(SpecialAction a, boolean doIt) {
//
//		int id1 = a.getIdentifier1();
//		
//
//		if (id1 == SpecialAction.ID1_DOOR_LOCK) {
//		
//		}
//
//		return ActionResult.INVALID;
//	}

	/**
	 * Filtert die eingefangenen Schlaege durch die Ruestung, aber ziemlich
	 * undurchsichtig und zieht dann die health ab
	 * 
	 * @param m
	 *            a <code>monster</code> welches Monster
	 * @param slap
	 *            an <code>int</code> wieviel
	 */

	public int getTypeSkill(Figure m) {
		return c.giveType_skill(m);
	}

	public int getAllArmor(Slap s) {
		return inv.getAllArmor(s);
	}

	public Attribute getAttribute(int name) {
		return this.c.getAttribute(name);
	}

	/**
	 * 
	 * @uml.property name="sanctions"
	 */
	protected void sanction(int i) {
		for (int j = 0; j < i; j++) {
			// //System.out.println("sanctioning!");
			getBrave().modValue((-1));
			getCharacter().getPsycho().modValue((-1));
			getCharacter().getDexterity().modValue((-1));
			getCharacter().getStrength().modValue((-1));
			sanctions++;
		}

	}

	public void recover() {
		// //System.out.println(getCharacter().getActionPoints());
		// System.out.println("recover()");
		// if(hasLuziaBall()) {
		// getLuziasBall().show();
		// }
		double healing = getActionPoints()
				* getCharacter().getAttribute(Attribute.HEALTHREG).getValue();
		// System.out.println("heile: "+healing);
		heal(healing);

		recDust(getCharacter().getDustReg().getValue());

		if (sanctions > 0) {
			getBrave().modValue(1);
			getCharacter().getPsycho().modValue(1);
			getCharacter().getDexterity().modValue(1);
			getCharacter().getStrength().modValue(1);
			sanctions--;
		}
	}

	public void heal(int k) {
		heal((double) k);
	}

	public int getAntiTumbleValue() {
		int str = c.getStrength_Value();
		return (str - 2) * 8;

	}

	public boolean layDown(Item it) {
		inv.layDown(it);
		return true;
	}

	public void heal(double value) {
		// System.out.println("Will heilen :"+value);
		Attribute healthAttr = c.getHealth();

		if (healthAttr.getValue() + value <= healthAttr.getBasic()) {

			healthAttr.modValue(value);
		} else {
			healthAttr.setValue((healthAttr.getBasic()));
		}
	}

	public boolean takeItem(Item i, ItemOwner o) {
		return getInventory().takeItem(i, o);
		// return true; //?

	}

	public int getTumbleValue(Figure f) {
		if (f instanceof Monster) {
			int i = c.getKnowledgeBalance(((Monster) f));
			Weapon weap1 = inv.getWeapon1();
			double str = c.getStrength().getValue() - 5;
			int wv = 0;
			double strMod = 0.7 + (str / 10);
			if (weap1 != null) {
				wv = weap1.getTumbleBasic();
				// System.out.println("wv geholt: "+wv);
			}
			if (i < -1) {
				return 0;
			}
			if (i == -1) {
				return (int) ((wv / 3) * strMod);
			}
			if (i == 0) {
				return (int) (wv * 1.5 * strMod);
			}
			if (i == 1) {
				return (int) (wv * 1.5 * strMod);

			}
			if (i >= 2) {
				return (int) (wv * 2 * strMod);
			} else {
				return 0;
			}

		} else {
			return 0;
		}
	}

	// private boolean hurt(int value) {
	// // game.newStatement("hurt: " + value, 4);
	// if (!game.getImortal()) {
	// getCharacter().getHealth().modValue(value * (-1));
	// return (getCharacter().getHealth().getValue() <= 0);
	// } else {
	// return false;
	// }
	// }

	public void recDust(double value) {
		// System.out.println("Dust erh�hen um: " + value);
		getCharacter().getDust().modValue(value);
		if (getCharacter().getDust().getValue() >= getCharacter().getDust()
				.getBasic())
			getCharacter().getDust().setValue(
					getCharacter().getDust().getBasic());
	}

	public boolean isAbleToUseItem() {
		return true;
	}

	public boolean isAbleToTakeItem(Item it) {
		if(it instanceof DustItem) {
			if(getDust().getValue() == getDust().getBasic()) {
				return false;
			}
		}
		return true;

	}

	/**
	 * blockt vielleicht wenn der Held ein Schild hat
	 * 
	 * @return a <code>boolean</code> value
	 */
	// public boolean block(monster m) {
	// if (getInventory().getShield1().toString().equals("noItem"))
	// return false;
	// else
	// return (
	// ((int) (Math.random() * 100))
	// <= (getInventory().getShield1().getChance_to_block()
	// + ((getCharacter().getDexterity().getValue()) / 2)));
	// }
	public int getElude(Figure m) {
		int k;
		if (getInventory().getShield1() == null) {
			k = 0;
		} else {
			int shield = (int) (getInventory().getShield1().getBlockValue());

			double skillValue = shield
					* (((double) getCharacter()
							.getKnowledgeBalance((Monster) m)) + 1)
					+ (((((double) (getCharacter()
							.getKnowledgeBalance((Monster) m)) + 1) * ((getCharacter()
							.getDexterity().getValue()) - 3)) / 50));
			if (skillValue < 0) {
				skillValue = 0;
			}
			// System.out.println("SkillValue bei blocken: " + skillValue);
			k = (int) Math.round(shield + skillValue);
		}
		// game.newStatement("Chance auf blocken: " + k, 4);
		if (k > (int) (Math.random() * 100)) {
			getInventory().getShield1().madeBlock(10);
			getRoom().distributePercept(new ShieldBlockPercept(this));
			// getGame().newStatement(Texts.blocked(), 1);
			return -1;
		} else {
			float dex = (float) getCharacter().getDexterity().getValue();
			float dex_3 = dex / 3;
			int know = getCharacter().getKnowledge((Monster) m);
			int ret = (int) (dex_3 * know);
			// game.newStatement("Chance auszuweichen: " + ret, 4);

			// 0 soll nur sein wenn er gel�hmt, versteinert etc ist
			// weil bei null immer getroffen
			ret += 2;

			return ret;
		}
	}

	public Attribute getDust() {
		return getCharacter().getDust();
	}

	public float getActualChanceToHit(Figure m) {
		float c;
		Weapon weap = getInventory().getWeapon1();
		if (weap != null) {
			if (getGolden_hit() != 0) {
				int k = getGolden_hit();
				setGolden_hit(0);
				c = 150 + (25 * (k - 1));

			} else if (this.isRaiding()) {
				c = 100;
			} else {

				float k = (float) (weap.getChanceToHit() * (float) (1 + ((float) (getCharacter()
						.getDexterity().getValue()
						+ getCharacter().giveType_skill(m) + getCharacter()
						.giveWeapon_skill()) / 40)));
				c = k;
			}
		} else {
			c = 0;
		}

		return c;

	}

	public boolean hasLuziaBall() {
		return this.inv.hasLuziaBall();
	}

	// /**
	// * Zuschlagen oder daneben...
	// *
	// * @param m a <code>monster</code> value
	// * @return an <code>int</code> value
	// */
	// public int slay(fighter m) {
	// //System.out.println("Hier! hero.slay(m)");
	//	
	// float actual_chance_to_hit = getActual_chance_to_hit(m);
	// if (actual_chance_to_hit >= (float) (Math.random() * 100)) {
	//
	// int x = getSlapStrength(m);
	// return x;
	// } else
	// return 0;
	//
	// }

	public LinkedList getAllItems() {
		return getInventory().getAllItems();
	}

	public int getSlapStrength(Figure m) {
		int weapon_blank;
		Weapon weap = getInventory().getWeapon1();
		if (weap == null) {
			weapon_blank = 0;
		} else {
			if (getCharacter().giveWeapon_skill() == 0) {
				weapon_blank = getInventory().getWeapon1().getDamage(-1);
			} else {
				weapon_blank = (int) (getInventory().getWeapon1().getDamage(0));
			}
		}
		String s = "Schlagst�rke: Waffe: " + weapon_blank;

		float Strength_Weapon_Type_Modifier = (float) (1 + ((float) (2
				* getCharacter().getStrength().getValue()
				+ (10 * getCharacter().giveType_skill(m)) + (10 * getCharacter()
				.giveWeapon_skill())) / 100));
		s += " - St�rke-K�nnen-Wissen-Faktor: " + Strength_Weapon_Type_Modifier;
		float Weapon_on_Type_Modifier = getInventory()
				.give_Weapon_on_Type_Modifier((Monster) m);
		s += " - Waffe-Faktor:" + Weapon_on_Type_Modifier;
		float faktor = Strength_Weapon_Type_Modifier * Weapon_on_Type_Modifier;
		s += " - Gesamtfaktor: " + faktor;
		int x = (int) (float) (weapon_blank * faktor);
		s += " - Erfahrungspunkte geben: " + x;
		// game.newStatement(s, 4);

		// getCharacter().giveExp(x, (monster) m);
		return x;
	}

	public void receiveSlapResult(SlapResult r) {
		getCharacter().giveExp(r);
		if (r.isLethal()) {
			kills++;
		}
	}

	// public void giveExp(int x, monster m) {
	// if (getCharacter().getExpCode() == 0) {
	// giveSelfExp(x);
	// } else if (getCharacter().getExpCode() == 1) {
	// giveWeaponExp(x);
	// } else if (getCharacter().getExpCode() == 2) {
	// giveKnowExp(x, (monster) m);
	// }
	//
	// testLevel();
	//
	// }

	public int testLevel() {
		return this.c.testLevel();
	}

	/**
	 * 
	 * @uml.property name="inv"
	 */
	public Inventory getInventory() {
		return inv;
	}

	// private void doLevelUp() {
	// Level++;
	// game.newStatement("LEVEL UP!: ", 4);
	//
	// int k = 3 + (int) (Math.random() * 5);
	// game.newStatement("HP +" + k, 4);
	//
	// for (int i = 0; i < k; i++) {
	// getCharacter().getHealth().incBasic();
	// }
	// getCharacter().getThreat().incBasic();
	// }

	// private int giveType_skill(monster m) {
	// if (m instanceof nature_monster)
	// return getCharacter().getNature_Knowledge().getValue();
	// if (m instanceof creature_monster)
	// return getCharacter().getCreature_Knowledge().getValue();
	// if (m instanceof undead_monster)
	// return getCharacter().getUndead_Knowledge().getValue();
	// else
	// return 0;
	// }
	//
	// private int giveWeapon_skill() {
	// if (getWeapon1() instanceof sword)
	// return Sword.getValue();
	// if (getWeapon1() instanceof axe)
	// return Axe.getValue();
	// if (getWeapon1() instanceof lance)
	// return Lance.getValue();
	// if (getWeapon1() instanceof club)
	// return Club.getValue();
	// if (getWeapon1() instanceof wolfknife)
	// return Wolfknife.getValue();
	// else
	// return 0;
	// }
	// private float give_Weapon_on_Type_Modifier(monster m) {
	// if (m instanceof wolf)
	// return (float)
	// (1 + ((float) ((float) getWeapon1().getWolf_Modifier() / 100)));
	// if (m instanceof bear)
	// return (float)
	// (1 + ((float) ((float) getWeapon1().getBear_Modifier() / 100)));
	// if (m instanceof skeleton)
	// return (float)
	// (1
	// + ((float) ((float) getWeapon1().getSkeleton_Modifier()
	// / 100)));
	// if (m instanceof ghul)
	// return (float)
	// (1 + ((float) ((float) getWeapon1().getGhul_Modifier() / 100)));
	// if (m instanceof orc)
	// return (float)
	// (1 + ((float) ((float) getWeapon1().getOrc_Modifier() / 100)));
	// if (m instanceof ogre)
	// return (float)
	// (1 + ((float) ((float) getWeapon1().getOgre_Modifier() / 100)));
	// else
	// return 1;
	// }

	/**
	 * Man kann ihm hier die primaerwaffe geben, falls er noch keine hat.
	 * 
	 * @param w
	 *            a <code>weapon</code> value
	 * @return a <code>boolean</code> value
	 * 
	 * @uml.property name="oldLocation"
	 */
	// public boolean take_shield1(shield s) {
	// boolean bool = true;
	// if (shields[0].toString().equals("noItem")) {
	// shields[0] = s;
	// } else if (shields[1].toString().equals("noItem")) {
	// shields[1] = s;
	// } else if (shields[2].toString().equals("noItem")) {
	// shields[2] = s;
	// } else
	// bool = false;
	// return bool;
	// }
	public JDPoint getOldLocation() {
		return oldLocation;
	}

	// public boolean takeItem(item ding) {
	// if (ding instanceof weapon) {
	// return take_weapon1((weapon) ding);
	// } else if (ding instanceof an_armor) {
	// return take_armor1((an_armor) ding);
	// } else if (ding instanceof shield) {
	// return take_shield1((shield) ding);
	// } else if (ding instanceof helmet) {
	// return take_helmet1((helmet) ding);
	// } else {
	// items.add(ding);
	// return true;
	// }
	// }

	// public void removeItem(item i) {
	// items.remove(i);
	// }

	protected int calcScout(Room r) {
		int level = 0;
		int handycap = 120;
		for (int i = 0; i < getCharacter().getScout().getValue(); i++) { // scout
			// gibt
			// den
			// Wert
			// wie
			// oft
			// gepr�ft
			// wird,
			// der
			// beste
			// Wert
			// wird
			// genommen
			int a = scoutHelp(handycap);
			if (a > level)
				level = a;
		}

		return level;
	}

	private int scoutHelp(int handycap) {
		int value = ((int) (Math.random() * handycap));
		if (value < getCharacter().getPsycho().getValue() - 5) {
			return 9;
		} else if (value < getCharacter().getPsycho().getValue() * 1) {
			return 8;
		} else if (value < getCharacter().getPsycho().getValue() * 2) {
			return 7;
		} else if (value < getCharacter().getPsycho().getValue() * 3) {
			return 6;
		} else if (value < getCharacter().getPsycho().getValue() * 4) {
			return 5;
		} else if (value < getCharacter().getPsycho().getValue() * 5) {
			return 4;
		} else if (value < getCharacter().getPsycho().getValue() * 6) {
			return 3;
		} else if (value < getCharacter().getPsycho().getValue() * 7) {
			return 2;
		} else if (value < getCharacter().getPsycho().getValue() * 8) {
			return 1;
		} else
			return 0;
	}

	// public String [] toStatement(){
	// String [] daten = new String[18];

	// daten[0]=("Name: "+this.getName());
	// daten[16]=("Sternzeichen"+Sign);
	// daten[1]=("Health: "+this.getHealth_Value()+"/"+this.getHealth());
	// daten[2]=("Kraft: "+this.getStrength());
	// daten[3]=("Geschicklichkeit: "+this.getDexterity());
	// daten[4]=("Psycho: "+this.getPsycho());
	// daten[5]=("Waffenfertigkeiten:");
	// daten[6]=("Schwert: "+this.getSword());
	// daten[7]=("Axt: "+this.getAxe());
	// daten[8]=("Knueppel: "+this.getClub());
	// daten[9]=("Lanze: "+this.getLance());
	// daten[10]=("Wolfmesser: "+this.getWolfknife());
	// daten[11]=("Wissen:");
	// daten[12]=("Naturkenntniss; "+this.getNature_Knowledge());
	// daten[13]=("Kreaturenkenntniss: "+this.getCreature_Knowledge());
	// daten[14]=("Untotenkenntniss: "+this.getUndead_Knowledge());
	// daten[15]=("Sp�hen: "+this.getScout());
	// if (this.getWeapon1()==null) daten[17]=("Keine Waffe");
	// else{
	// daten[17]=("Waffe: "+this.getWeapon1().toString());}
	// return daten;
	// }

	// private int getAll_armor(monster m) {
	// int a = 0;
	// if (getArmor1() != null) {
	// a += getArmor1().getarmor_value();
	// }
	// if (getHelmet1() != null) {
	// a += getHelmet1().getarmor_value();
	// }
	// return a + giveType_skill(m);
	// }

	// public int fleePanicCalc(int difficulty) {
	// boolean success = false;
	// if (Math.random() > 0.25) {
	// success = true;
	// c.getDust().setValue(0);
	// int h = c.getHealth_Value();
	//
	// c.getHealth().modValue((-1) * h / 3);
	//
	// int dex = (int) getCharacter().getDexterity().getValue() - 2;
	// int i1 = (int) (Math.random() * difficulty);
	//
	// int sanction = 0;
	// if (dex < i1) {
	// sanction = i1 - dex;
	// }
	//
	// double relSanction = ((double) sanction) / (double) i1;
	//
	// this.inv.payRel(relSanction);
	// }
	// if (success) {
	// return 0;
	// } else {
	// return 3;
	// }
	//
	// }

	protected int calcFleeResult(int difficulty) {

		if (escape > 0) {
			// game.newStatement("Flucht mit dem Wind: ", 4);

			escape = 0;
			return -1;
		}

		int i1 = (int) (Math.random() * difficulty);

		// int i2 = (int) (Math.random() * test);

		// NEUE FLUCHT - abh�ngig nur von Geschicklichkeit

		int dex = (int) getCharacter().getDexterity().getValue() - 2;
		// int psy = (int) getCharacter().getPsycho().getValue();
		int erg = 4;
		if (i1 < dex) {
			erg = 0;
		} else if (i1 < dex + 1) {
			erg = 1;
		}

		return erg;
	}

	// public void makeThreating(Fight f) {
	//
	// }

	private int threatHelp(int handycap) {
		int value = ((int) (Math.random() * handycap));
		if (value < getCharacter().getPsycho().getValue() - 5) {
			return 5;
		} else if (value < getCharacter().getPsycho().getValue()) {
			return 4;
		} else if (value < getCharacter().getPsycho().getValue() * 2) {
			return 3;
		} else if (value < getCharacter().getPsycho().getValue() * 3) {
			return 2;
		} else if (value < getCharacter().getPsycho().getValue() * 4) {
			return 1;
		} else
			return 0;
	}

	public int threat(Monster m) {
		int handycap = 90;
		int k = (int) getCharacter().getKnowledge(m)
				+ (int) getCharacter().getThreat().getValue();
		// game.newStatement("Drohungswert hier: " + k, 4);

		int l = 0;
		for (int i = 0; i < k; i++) {
			int o = threatHelp(handycap);
			if (o > l) {
				l = o;
			}
		}

		// //System.out.println("gedroht: "+Integer.toString(l));
		if (getCharacter().getKnowledge(m) == 0) {
			// game.newStatement("Wissen nicht ausreichend ", 4);

			l = 0;
		}
		// game.newStatement("Drohung: " + l, 4);

		return l;
	}

	// public void getThreaten(int value, int count, Fight f) {
	// }

	// public int getKnowledge(monster m) {
	// int k = 0;
	// if (m instanceof nature_monster) {
	// //System.out.print("Es ist ein Naturmonster: ");
	// k = getCharacter().getNature_Knowledge_Value();
	// //System.out.println(k);
	// return k;
	// } else if (m instanceof creature_monster) {
	// //System.out.print("Es ist ein Kreaturenmonster: ");
	// k = this.getCreature_Knowledge_Value();
	// //System.out.println(k);
	// return k;
	// } else if (m instanceof undead_monster) {
	// //System.out.print("Es ist ein Untotenmonster: ");
	// k = getUndead_Knowledge_Value();
	// //System.out.println(k);
	// return k;
	// }
	//
	// return Integer.MIN_VALUE;
	// }

	public void roundEnd() {
		// System.out.println("Rundenende");

	}

	// public void addModification(modification mod) {
	// Modifications.add(mod);
	// }

	// public boolean payActionPoint() {
	// boolean b = false;
	// if (getGame().getFight() == null) {
	// if (getCharacter().getActionPoints() > 0) {
	// getCharacter().decActionPoints(1);
	// b = true;
	// }
	// }
	// if (getGame().getFight() != null) {
	// if (getCharacter().getFightAP() > 0) {
	// getCharacter().decFightAP(1);
	// b = true;
	// }
	// }
	// return b;
	//
	// }

	public boolean payDust(int k) {

		if (c.getDust().getValue() >= k) {
			c.getDust().modValue(k * -1);
			return true;
		} else {
			return false;
		}
	}

	// public boolean payActionPoints(int k) {
	// boolean b = false;
	// if (getGame().getFight() == null) {
	// if (getCharacter().getActionPoints() >= k) {
	// getCharacter().decActionPoints(k);
	// b = true;
	// }
	// }
	// if (getGame().getFight() != null) {
	// if (getCharacter().getFightAP() >= k) {
	// getCharacter().decFightAP(k);
	// b = true;
	// }
	// }
	// return b;
	//
	// }

//	protected void fireModifications() {
//		if (getCharacter().getModifications().size() > 0) {
//
//			TimedModification m = ((TimedModification) getCharacter()
//					.getModifications().get(0));
//			if (m.hasFired()) {
//				getCharacter().getModifications().remove(m);
//			} else {
//				if (m.newRound()) {
//
//				}
//			}
//			ListIterator iter = getCharacter().getModifications().listIterator(
//					0);
//			while (iter.hasNext()) {
//
//				m = (TimedModification) iter.next();
//				if (m.hasFired()) {
//					iter.remove();
//				} else {
//					if (m.newRound()) {
//
//					}
//				}
//
//			}
//
//		}
//	}

	public List getModificationList() {
		return c.getModifications();
	}
	// }

	// public void attack(Figure m) {
	// int dir = PositionInRoom.getDirFromTo(getPositionInRoom(), m
	// .getPositionInRoom());
	// lookDir = dir;
	//
	// Slap hurt = slay(m);
	// // game.getGui().figureSlaysAnimation(new HeroInfo(this));
	// if (hurt.getValue_standard() == 0) {
	// // f.newStatement("Du schl�gst daneben!", 3);
	// } else {
	//
	// tellPercept(new TextPercept("Du triffst mit: "
	// + Integer.toString(hurt.getValue_standard())));
	// }
	// SlapResult res = m.getSlap(hurt);
	// receiveSlapResult(res);
	// }

	public void getThreaten(int i) {
	}

	// public boolean fight(/* Fight f */) {
	// System.out.println("enter Hero.fight()");
	// boolean exitsFight = false;
	// this.sufferPoisoningsFight();
	// incFightAP(1);
	//
	// // System.out.println("inc: Held: fightAP:" + getFightAP());
	// if (justGotSlap) {
	// // //System.out.println("status: ");
	// // game.newStatement(getCharacter().getStatus(), 0);
	// justGotSlap = false;
	// }
	// // game.getGui().updateGUI(MainFrame.UPDATE_DUST,false);
	// // game.getGui().updateGUI(MainFrame.UPDATE_HEALTH,false);
	// // game.getGui().updateGUI(MainFrame.UPDATE_FIGHT,true);
	// // // if(before != null) game.getGui().repaintRoom(before);
	// //control.repaint();
	// if (control != null && !this.isDead) {
	// Action a = retrieveFightActionFromControl();
	// processFightAction(a);
	//			
	// control.repaint();
	//			
	//			
	// //
	// // if (!(a instanceof GUIAction)) {
	// // if (paused) {
	// // try {
	// // Thread.sleep(20 * 1000);
	// // } catch (Exception e) {
	// // }
	// // paused = false;
	// // }
	// // try {
	// // Thread.sleep(timeDelay);
	// // } catch (Exception e) {
	// // }
	// // processFightAction(a);
	// // return true;
	// // }
	//			
	// }
	//
	// return false;
	// }

	public JDPoint getLocation() {
		return location;
	}

	public LuziasBall getLuziasBall() {
		return inv.getLuziasBall();
	}

	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		return inv.getItemInfos(map);
	}

	// public int getTotalExp() {
	// return totalExp;
	// }
	//
	// private void giveSelfExp(int v) {
	// totalExp += v;
	// selfExp += v;
	// levelUpSelf();
	// }
	//
	// private void giveKnowExp(int v, monster m) {
	// totalExp += v;
	// if (m instanceof nature_monster) {
	// knowExp[0] += v;
	// levelUpKnow(0);
	// } else if (m instanceof creature_monster) {
	// knowExp[1] += v;
	// levelUpKnow(1);
	// } else if (m instanceof undead_monster) {
	// knowExp[2] += v;
	// levelUpKnow(2);
	// }
	// }
	//
	// private void giveWeaponExp(int v) {
	// totalExp += v;
	// weapon w = getWeapon1();
	// if (w instanceof axe) {
	// weaponExp[0] += v;
	// levelUpWeapon(0);
	// } else if (w instanceof club) {
	// weaponExp[1] += v;
	// levelUpWeapon(1);
	// } else if (w instanceof lance) {
	// weaponExp[2] += v;
	// levelUpWeapon(2);
	// } else if (w instanceof sword) {
	// weaponExp[3] += v;
	// levelUpWeapon(3);
	// } else if (w instanceof wolfknife) {
	// weaponExp[4] += v;
	// levelUpWeapon(4);
	// }
	// }

	// private void levelUpKnow(int i) {
	// int lerndauer = 150;
	// switch (i) {
	// case 0 :
	// if (knowExp[0]
	// >= (Nature_Knowledge.getBasic() + 1) + lerndauer) {
	// knowExp[0] = 0;
	// Nature_Knowledge.incBasic();
	// Statements.add("Du hast Deine Naturkenntnis verbessert!");
	// }
	// break;
	// case 1 :
	// if (knowExp[1]
	// >= (Creature_Knowledge.getBasic() + 1) + lerndauer) {
	// knowExp[1] = 0;
	// Creature_Knowledge.incBasic();
	// Statements.add(
	// "Du hast Deine Kreaturenkenntnis verbessert!");
	// }
	// break;
	// case 2 :
	// if (knowExp[2]
	// >= (Nature_Knowledge.getBasic() + 1) + lerndauer) {
	// knowExp[2] = 0;
	// Undead_Knowledge.incBasic();
	// Statements.add("Du hast Deine Untotenkenntnis verbessert!");
	// }
	// break;
	// }
	// }
	//
	// private void levelUpSelf() {
	// int lerndauer = 150;
	// //System.out.println("Erfahrungspunkte: " + Integer.toString(selfExp));
	// if (selfExp >= (selfLevel + 1) * lerndauer) {
	// selfExp = 0;
	// doLevelUpSelf();
	// }
	// }
	//
	// private void doLevelUpSelf() {
	// int i = (int) (Math.random() * 4);
	// if (i == 0) {
	// Psycho.incBasic();
	// Statements.add("Du f�hlst Dich pl�tzlich kl�ger und mutiger.");
	// } else if (i == 1) {
	// Strength.incBasic();
	// Statements.add("Du f�hlst neue Kraft in Dir.");
	// } else if (i == 2) {
	// Dexterity.incBasic();
	// Statements.add(
	// "Du merkst wie Deine Bewegungen pr�ziser und geschmeidiger werden.");
	// } else if (i == 3) {
	// Scout.incBasic();
	// Statements.add("Du merkst wie Deine Sinne sch�rfer werden.");
	// }
	// }
	//
	// private void levelUpWeapon(int i) {
	// int lerndauer = 120;
	// switch (i) {
	// case 0 :
	// if (weaponExp[0] >= (Axe.getBasic() + 1) * lerndauer) {
	// weaponExp[0] = 0;
	// Axe.incBasic();
	// Statements.add(
	// "Du hast Deine Fertigkeit mit der Axt verbessert!");
	// }
	// break;
	// case 1 :
	// if (weaponExp[1] >= (Club.getBasic() + 1) * lerndauer) {
	// weaponExp[0] = 0;
	// Club.incBasic();
	// Statements.add(
	// "Du hast Deine Fertigkeit mit dem Kn�ppel verbessert!");
	// }
	// break;
	// case 2 :
	// if (weaponExp[2] >= (Lance.getBasic() + 1) * lerndauer) {
	// weaponExp[0] = 0;
	// Lance.incBasic();
	// Statements.add(
	// "Du hast Deine Fertigkeit mit der Lanze verbessert!");
	// }
	// break;
	// case 3 :
	// if (weaponExp[3] >= (Sword.getBasic() + 1) * lerndauer) {
	// weaponExp[0] = 0;
	// Sword.incBasic();
	// Statements.add(
	// "Du hast Deine Fertigkeit mit dem Schwert verbessert!");
	// }
	// break;
	// case 4 :
	// if (weaponExp[4] >= (Wolfknife.getBasic() + 1) * lerndauer) {
	// weaponExp[0] = 0;
	// Wolfknife.incBasic();
	// Statements.add(
	// "Du hast Deine Fertigkeit mit dem Wolfsmesser verbessert!");
	// }
	// break;
	// }
	// }

	public void killedMonster(Monster m) {
		incKills();

		if (m instanceof Wolf) {
			if (getCharacter().getExpCode() == 1) {
				getCharacter().giveWeaponExp(20);
			} // weil der Wolf so wenig HP hat
		}
		if (m instanceof Spider) {
		}
		if (m instanceof Skeleton) {
			getCharacter().giveWeaponExp(10);
		} // auch, aber irgendwann wird des zu wenig sein
		if (m instanceof Ghul) {
		}
		if (m instanceof Orc) {
			getCharacter().giveWeaponExp(10);
		} // wenn die Monster megagro� werden, dann z�hlt nur noch HP
		if (m instanceof Ogre) {
		}
	}

	public int getKilled() {
		if(isBonusLive()) {
			this.setBonusLive(false);
			this.getHealth().setValue(1);
			this.getRoom().figureLeaves(this);
			Room respawnRoom2 = this.getRespawnRoom();
			respawnRoom2.figureEnters(this,0);
			respawnRoom2.distributePercept(new InfoPercept(InfoPercept.RESPAWN));
			return 1;
		}
		
		Percept p = new DiePercept(this, getRoom());
		getRoom().distributePercept(p);
		
		isDead = true;
		return 0;
		
	}

	public boolean isDead() {
		return isDead;
	}

	// public void setExpCode(int i) {
	// if ((i == 0) || (i == 1) || (i == 2)) {
	// expCode = i;
	// } else
	// //System.out.println("ExpCode Error!");
	// }
	// public attribute getAttribut(String a) {
	// if (a == ("Strength")) {
	// return Strength;
	// } else if (a == ("Dexterity")) {
	// return Dexterity;
	// } else if (a == ("Psycho")) {
	// return Psycho;
	// } else if (a == ("Axe")) {
	// return Axe;
	// } else if (a == ("Club")) {
	// return Club;
	// } else if (a == ("Lance")) {
	// return Lance;
	// } else if (a == ("Sword")) {
	// return Sword;
	// } else if (a == ("Wolfknife")) {
	// return Wolfknife;
	// } else if (a == ("Nature_Knowledge")) {
	// return Nature_Knowledge;
	// } else if (a == ("Creature_Knowledge")) {
	// return Creature_Knowledge;
	// } else if (a == ("Undead_Knowledge")) {
	// return Undead_Knowledge;
	// } else if (a == ("Scout")) {
	// return Scout;
	// } else if (a == ("Threat")) {
	// return Threat;
	// } else if (a == ("Health")) {
	// return Health;
	// } else
	// return (new attribute(("Attribut Fehler"), 0));
	// }
	public Room[][] getMemory() {
		return memory;
	}

	public RoomInfo getMemory(int i, int j) {
		return RoomInfo.makeRoomInfo(memory[i][j], this.getRoomVisibility());
	}

	public void resetMemory(int x, int y) {
		memory = new Room[x][y];
	}

	public Item getItem(ItemInfo it) {
		return inv.getItem(it);
	}

	// public void layDown(item it) {
	// boolean no = (it.toString().equals("noItem"));
	//
	// if (!no) {
	// if (it instanceof weapon) {
	// for (int i = 0; i < 3; i++) {
	// if (getInventory().getWeapon(i) == it) {
	// getRoom().addItem(it);
	// weapons[i] = new noWeapon();
	// }
	// }
	// } else if (it instanceof an_armor) {
	// for (int i = 0; i < 3; i++) {
	// if (getInventory().getArmor(i) == it) {
	// getRoom().addItem(it);
	// armors[i] = new noArmor();
	// }
	// }
	// } else if (it instanceof shield) {
	// for (int i = 0; i < 3; i++) {
	// if (getInventory().getShield(i) == it) {
	// getRoom().addItem(it);
	// shields[i] = new noShield();
	// }
	// }
	// } else if (it instanceof helmet) {
	// for (int i = 0; i < 3; i++) {
	// if (getInventory().getHelmet(i) == it) {
	// getRoom().addItem(it);
	// helmets[i] = new noHelmet();
	// }
	// }
	// } else {
	// LinkedList l = getItems();
	// int k = l.size();
	// for (int i = 0; i < k; i++) {
	// if (it == ((item) l.get(i))) {
	// getRoom().addItem(it);
	// l.remove(it);
	// break;
	// }
	// }
	// }
	// }
	// }

	public Game getGame() {
		return game;
	}

	/**
	 * Method tryUnlockDoor.
	 * 
	 * @param d
	 */
	public boolean tryUnlockDoor(Door d, boolean doIt) {
		LinkedList items = this.getItems();
		boolean b = false;
		for (int i = 0; i < items.size(); i++) {
			Item a = ((Item) items.get(i));
			if (a instanceof Bunch) {
				b = ((Bunch) a).tryUnlockDoor(d, doIt);
			}
			if (a instanceof Key) {
				b = d.lock((Key) a, doIt);

			}
			if (b) {
				break;
			}
		}
		return b;
	}

	public boolean hasKey(String k) {
		LinkedList items = getItems();
		for (int i = 0; i < items.size(); i++) {
			Item it = (Item) items.get(i);
			if (it instanceof Key) {
				if (((Key) it).getType().equals(k)) {
					return true;
				}
			}
			if (it instanceof Bunch) {
				return ((Bunch) it).hasKey(k);
			}
		}
		return false;
	}

	public boolean hasKey(Door d) {
		if (d.hasLock()) {
			return hasKey(d.getKey().getType());
		}
		return false;
	}

	// public void heroThreats(/* Fight2 fight2, */int e) {
	// Monster m = null;
	// LinkedList dieMonster = getRoom().getRoomFigures();
	// if (e < dieMonster.size()) {
	// m = (Monster) dieMonster.get(e);
	// } else {
	// m = (Monster) dieMonster.getFirst();
	// }
	// Threat th = new Threat(m);
	// int x = alreadyThreaten(m);
	// int know = getCharacter().getKnowledge(m);
	// if (know <= 0) {
	// if (know < 0) {
	// // System.out.println("Keine Zuordnung war m�glch");
	// } else {
	// // System.out.println(
	// // m.getClass().toString()
	// // + " : Wissen: "
	// // + h.getCharacter().getKnowledge(m));
	// newStatement(
	// "Du wei�t irgendwie nicht, wie Du das tun sollst bei diesem Gegner.",
	// 0);
	// }
	// } else {
	// newStatement(Texts.threat(this), 0);
	// m.getThreaten(threat(m), x);
	// saveThreating(th);
	// System.out.println("heroThreats");
	// decFightAP(1);
	// // heroParty
	// // game.getFight().MonstersFight();
	// }
	// }

	private int alreadyThreaten(Monster m) {
		int l = threatings.size();
		int x = 0;
		for (int i = 0; i < l; i++) {
			if (((Threat) threatings.get(i)).getMonster() == m) {
				x = ((Threat) threatings.get(i)).getCount();
			}
		}
		return x;
	}

	private void saveThreating(Threat th) {
		int l = threatings.size();
		Monster m = th.getMonster();
		boolean found = false;
		for (int i = 0; i < l; i++) {
			if (((Threat) threatings.get(i)).getMonster() == m) {
				((Threat) threatings.get(i)).inc();
				found = true;
			}
		}
		if (found == false) {
			threatings.add(th);
		}
	}

	LinkedList threatings = new LinkedList();

	protected void lookInRoom() {

		game.derDungeon.getRoom(getLocation()).setVisited(game.getRound());
		if (game.derDungeon.getRoom(getLocation()).getShrine() != null) {
			Shrine s = game.derDungeon.getRoom(getLocation()).getShrine();
			this.tellPercept(new TextPercept(s.getStory()));

		}
		if (game.derDungeon.getRoomNr(getLocation().getX(),
				getLocation().getY()).getItems().size() != 0) {
			this.tellPercept(new InfoPercept(InfoPercept.FOUND_ITEM));
		}

	}

	protected boolean flee(int dir) {
		String fleeDirection = new String();
		if (dir == RouteInstruction.NORTH) {
			fleeDirection = "north";
		} else if (dir == RouteInstruction.SOUTH) {
			fleeDirection = "south";
		} else if (dir == RouteInstruction.WEST) {
			fleeDirection = "west";
		} else if (dir == RouteInstruction.EAST) {
			fleeDirection = "east";
		} else {
			// System.out.println("FLEEDIRECTION ERRRROR!");
		}

		Room toGo = game.getDungeon().getRoomAt(getRoom(), dir);
		Room from = getRoom();
		Door d = game.getDungeon().getRoom(getLocation()).getConnectionTo(toGo);

		if ((toGo == null) || (d == null) || (!d.isPassable(this))) {

			return false;
		}

		if (/* (getHero().getCharacter().getFightAP() > 0) */true) {

			double diff = getRoom().calcFleeDiff();
			String back = getLocation().relativeTo(getOldLocation());
			LinkedList axeX = new LinkedList();
			axeX.add("east");
			axeX.add("west");
			LinkedList axeY = new LinkedList();
			axeY.add("north");
			axeY.add("south");
			if (fleeDirection.equals(back)) {

			} else if ((axeX.contains(back) && axeX.contains(fleeDirection))
					|| (axeY.contains(back) && axeY.contains(fleeDirection))) {
				diff += 6; // entgegengesetzt von wo er herkam
			} else {
				diff += 3; // um 90 grad
			}

			int fleeV = 0;

			fleeV = calcFleeResult((int) diff); // 2 kommt nicht
			if(getRoom().getDoor(dir).hasEscapeRoute(this)) {
				fleeV = 1;
			}

			if (fleeV < 3) {
				if (fleeV == 1) {
					// game.getFight().MonstersFight();

				}
				if (fleeV == 2) { // kommt momentan nicht vor
					// schlag der trifft
					// game.getFight().MonstersFight();

				}

				move(toGo);

				if (fleeV < 0) {
					// bei escape Zauberspruch
					setActionPoints(1);
				} else {
					setActionPoints(0);
				}
				setFightAP(0);

				lookInRoom();
				getRoom().checkFight(this);
				return true;
			} else {

				return false;
			}
		} else {

			return false;
		}
	}

	/**
	 * @return Returns the before.
	 */
	public Room getBefore() {
		return before;
	}

	// class Waiter implements Runnable {
	//
	// boolean ready = false;
	//
	// public void run() {
	// while (game.getActualFightAction() == null) {
	//
	// }
	// ready = true;
	// }
	//
	// public boolean getReady() {
	// return ready;
	// }
	//
	// }
	/**
	 * @param heroCode
	 *            The heroCode to set.
	 */
	public void setHeroCode(int heroCode) {
		HeroCode = heroCode;
	}

	/**
	 * @param sign
	 *            The sign to set.
	 */
	public void setSign(String sign) {
		Sign = sign;
	}

	public boolean scout(int dir) {

		Room loc = getRoom();
		// System.out.println("loc: " + loc);
		Room toScout = loc.getNeighbourRoom(dir);
		scoutedRooms.add(toScout);
		// System.out.println("toScout :" + toScout);
		Door d = loc.getConnectionTo(toScout);

		boolean scoutable = ((d != null) /** && (d.isPassable())* */
		);
		// System.out.println("scoutable: " + scoutable);
		String s = new String();
		if ((scoutable)) {
			List<Figure> monsters = toScout.getRoomFigures();
			s += ("Du horchst und schaust duch die Ritzen in der T�r -" + "\n");

			decActionPoints(1);
			int scoutlevel = calcScout(toScout);
			if (scoutlevel == 0) {

				if (monsters.size() == 0) {
					s += "...aber Du kannst leider nichts rauskriegen";
				} else {
					for (int i = 0; i < monsters.size(); i++) {
						s += "...aber Du kannst leider nichts rauskriegen";
						if (Math.random() < 0.4) {
							RouteInstruction route = new RouteInstruction(
									game.derDungeon.getRoom(getLocation()));
							((Monster) monsters.get(i))
									.addRouteInstruction(route);
						}
					}

				}
			} else if (scoutlevel == 1) {

				if (monsters.size() == 0) {
					s += "...aber Du kannst leider nichts rauskriegen";
				} else {
					for (int i = 0; i < monsters.size(); i++) {
						if (Math.random() < 0.4) {
							((Monster) monsters.get(i))
									.addRouteInstruction(new RouteInstruction(
											game.derDungeon
													.getRoom(getLocation())));
						}
					}
					s += (" und ...Du wirst entdeckt!");
				}
			} else if (scoutlevel == 2) {

				if (monsters.size() == 0) {
					s += "Du siehst dort niemanden.";
					roomVisibility.setVisibilityStatus(toScout.getNumber(),
							RoomObservationStatus.VISIBILITY_FIGURES);

				} else {
					for (int i = 0; i < monsters.size(); i++) {
						((Monster) monsters.get(i))
								.addRouteInstruction(new RouteInstruction(
										game.derDungeon.getRoom(getLocation())));
					}
					s += ("Du hast ihn entdeckt..." + "\n");
					s += ("...und er Dich jetzt auch!");
				}
			} else if (scoutlevel == 3) {
				s += ("... aber Du kannst leider nichts rauskriegen");
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_FOUND);

			} else if (scoutlevel == 4) {
				if (monsters.size() == 0) {
					if (((int) Math.random() * 100) < 80) {
						s += ("hmm.. Du glaubst da ist jemand.");
					} else {
						s += ("hmm..Du glaubst da ist niemand");
					}
				} else {
					if (((int) Math.random() * 100) < 20) {
						s += ("hmm..Du glaubst da ist jemand.");
					} else {
						s += ("hmm..Du glaubst da ist niemand");
					}
				}
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_SHRINE);

			} else if (scoutlevel == 5) {
				if (toScout.getRoomFigures().size() == 0) {
					s += ("In diesem Raum ist niemand!");
					roomVisibility.setVisibilityStatus(toScout.getNumber(),
							RoomObservationStatus.VISIBILITY_FIGURES);

				} else {
					s += ("In diesem Raum ist jemand!");
				}
			} else if (scoutlevel >= 6) {
				if (toScout.getRoomFigures().size() > 0) {
					s += ("Du kannst den Gegner genau beobachten." + "\n");
					for (int i = 0; i < monsters.size(); i++) {
						s += (((Monster) monsters.get(i)).toString() + "\n");
					}

				} else {
					s += ("Wenn da jemand gewesen w�re h�ttest Du es raugekriegt.");
				}
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_FIGURES);

			} else if (scoutlevel >= 8) {
				if (toScout.getRoomFigures().size() > 0) {
					s += ("Gegner lokalisiert!\n");
				} else {
					s += "Der Raum ist frei\n";
				}
				if (toScout.getItems().size() > 0) {
					s += "...und sieh mal was da rumliegt!\n";
				} else {
					s += "keine Gegenst�nde dort\n";
				}
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_ITEMS);
			} else {
				s += ("Scout Level Error: " + scoutlevel);
			}
		} else {
			s += ("Das funktioniert so jetzt gerade nicht...");
		}
		tellPercept(new TextPercept(s));
		return true;
	}

	public boolean learnSpell(Spell s) {
		if (c.getSpellBuffer().contains(s)) {
			int k = s.getLernCost();
			if (c.getSpellPoints() >= k) {
				c.decSpellPoints(k);
				this.getSpellbook().addSpell(s);
				c.getSpellBuffer().remove(s);
				return true;
			}
		}

		return false;

	}
}