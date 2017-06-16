package figure.monster;

import ai.AI;
import figure.action.ScoutAction;
import figure.action.ScoutResult;
import item.Item;
import item.ItemInfo;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;
import item.quest.DarkMasterKey;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import ai.DefaultMonsterIntelligence;
import ai.DefaultMonsterReflexBehavior;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.Position;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import fight.Frightening;
import fight.Slap;
import fight.SlapResult;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.Spellbook;
import figure.action.Action;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.percept.DiePercept;
import figure.percept.FleePercept;
import figure.percept.ItemDroppedPercept;
import figure.percept.Percept;
import game.DungeonGame;
import game.InfoEntity;
import game.InfoProvider;
import gui.Paragraphable;

/**
 * Superklasse fuer alle moeglichen Monster, abgeleitet von Fighter.
 * Implementiert bis auf Monstertypspezifische Sachen alle abstrakten Methoden
 * von Fighter. Wird gesteuert von einem MonsterControl-Objekt.
 * 
 */
public abstract class Monster extends Figure implements Paragraphable,
		InfoProvider {


	public final static int DT_STANDARD = 0;

	public final static int DT_FIRE = 1;

	@Deprecated
	public final static int BEAR = 1;

	@Deprecated
	public final static int GHUL = 2;

	@Deprecated
	public final static int OGRE = 3;

	@Deprecated
	public final static int ORC = 4;

	@Deprecated
	public final static int SKELETON = 5;

	@Deprecated
	public final static int WOLF = 6;

	@Deprecated
	public final static int DARKMASTER = 7;

	@Deprecated
	public final static int DWARF = 8;

	@Deprecated
	public final static int FIR = 9;

	@Deprecated
	public final static int LIONESS = 10;

	public final static int BEAR_HUNTING = 1;

	public final static int ORC_HUNTING = 6;

	public final static int OGRE_HUNTING = 0;

	public final static int SKELETON_HUNTING = 6;

	public final static int WOLF_HUNTING = 7;

	public final static int GHUL_HUNTING = 0;

	public final static int MISSION_NONE = 0;

	public final static int MISSION_DARK_MASTER = 1;

	public final static int MISSION_FLEE = 2;

	public final static int MISSION_HUNT = 3;

	public final static int MISSION_ROAM = 4;

	protected boolean firstBlood = false;

	
	protected int specialAttackCounter = 0;

	protected boolean fighted = false;

	protected String[] lvl_names;


	@Override
	public int filterFrightening(Frightening f) {
		return f.getValue();
	}
	
	@Override
	protected boolean tryUnlockDoor(Door d, boolean doIt) {
		return false;
	}

	protected List modifications = new LinkedList();

	protected Attribute strength ;
	protected Attribute dexterity = new Attribute(Attribute.DEXTERITY,7);
	

	@Override
	public Attribute getStrength() {
		if (strength == null) {
			System.out.println("st�rke-attribut von Monster ist null: "
					+ this.toString());
			return new Attribute(Attribute.STRENGTH, 3);
		}
		return strength;
	}

	protected boolean hasJustBeenThreaten = false;

	protected int lastMove = 0;

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new MonsterInfo(this, map);
	}

	protected abstract int getCHANCE_TO_HIT();

	protected abstract int getSCATTER();
	
	protected Attribute dust;
	
	@Override
	protected boolean getBlock(int dmg) {
		return false;
	}

	protected void construcHelp(int value) {
		this.spellbook = new Spellbook();
		this.reflexReactionUnit = new DefaultMonsterReflexBehavior(this);
		lookDir = ((int) Math.random() * 4) + 1;
		worth = value;
		setLevel(value);
		int cth_modifier = (int) (Math.random() * 8) - 4;
		value = (int) ((((double) value) / (getCHANCE_TO_HIT() + cth_modifier)) * 6);
		int k = (int) ((this.getHEALTH_DAMAGE_BALANCE() - 2 + ((int) (Math
				.random() * 4))) * Math.sqrt(this.getLevel()));
		
		int HealthI = (int) ((double) value / k);

		
		health = new Attribute(Attribute.HEALTH, HealthI);
		psycho = new Attribute(Attribute.PSYCHO, 10);
		dust  = new Attribute(Attribute.DUST,10);
		int average = k;

		int scatter = getSCATTER();
		minDamage = average - scatter;
		maxDamage = average + scatter;

		chanceToHit = new Attribute(Attribute.CHANCE_TO_HIT,
				getCHANCE_TO_HIT() + cth_modifier);
	}

	protected boolean hasJustFleen = false;

	protected boolean thundered = false;

	protected boolean justGotSlap = false;

	protected int healthRecover = 1;

	//protected String Mclass;

	@Override
	public Item getItem(ItemInfo it) {
		for (Iterator<Item> iter = items.iterator(); iter.hasNext();) {
			Item element = iter.next();
			if (ItemInfo.makeItemInfo(element,null).equals(it)) {
				return element;
			}

		}
		return null;
	}

	protected Stack<RouteInstruction> routing = new Stack<RouteInstruction>();

	protected boolean makingSpecialAttack = false;

	protected int tumbleValue;

	protected int antiTumbleValue;

	protected double fireResistRate = 1.0;

	protected double lightningResistRate = 1.0;

	protected double magicResistRate = 1.0;

	protected double poisonResistRate = 1.0;

	protected boolean spitted = false;

	protected int firstBloodRounds = 0;

	int convinced = 0;

	public boolean luzia;

	protected String name;

	protected Attribute health;

	protected Attribute psycho;

	protected int minDamage;

	protected int maxDamage;

	protected Attribute chanceToHit;

	protected abstract int getHEALTH_DAMAGE_BALANCE();

	boolean makesgoldenHit = false;

	protected List<Item> items = new LinkedList<Item>();

	protected boolean specialAttacking = true;

	public void addRouteInstruction(RouteInstruction r) {
		routing.push(r);
	}

	public Monster(int value, AI ai) {
		super(ai);
		init(value);
	}

	public Monster(int value) {
		super(new DefaultMonsterIntelligence());
		init(value);

	}

	private void init(int value) {
		construcHelp(value);
	}

	/*

	public Monster(int value) {
		super();
		if(JDEnv.isBeginnerGame()) {
			value *= JDEnv.BEGINNER_RATE;
		}
		construcHelp(value);
	}
*/

	@Override
	public int getTumbleValue(Figure f) {
		return tumbleValue;
	}

	@Deprecated
	public static Monster createMonster(int type, int value, DungeonGame game) {
		if (type == WOLF) {
			return new Wolf(value);
		}
		if (type == ORC) {
			return new Orc(value);
		}
		if (type == SKELETON) {
			return new Skeleton(value);
		}
		if (type == OGRE) {
			return new Ogre(value);
		}
		if (type == GHUL) {
			return new Ghul(value);
		}
		if (type == BEAR) {
			return new Spider(value);
		} else
			return null;
	}

	
	
	
	protected double getFire_resist_rate() {
		return fireResistRate;
	}

	
	protected double getLightning_resist_rate() {
		return lightningResistRate;
	}

	
	@Override
	public int getAntiTumbleValue() {
		return antiTumbleValue;
	}

	
	protected double getMagic_resist_rate() {
		return magicResistRate;
	}

	@Override
	public double getFireResistRate() {
		return fireResistRate;
	}

	@Override
	public double getLightningResistRate() {
		return lightningResistRate;
	}

	@Override
	public double getMagicResistRate() {
		return magicResistRate;
	}

	@Override
	public Attribute getAttribute(int name) {
		return null;
	}
	
	@Override
	public List getModificationList() {
		return this.modifications;
	}

	
	@Override
	public double getPoisonResistRate() {
		return poisonResistRate;
	}

	public abstract int hunting();

	/**
	 * Random-Konstruktor der unterschiedliche Viecher liefert
	 * 
	 * @param value
	 *            an <code>int</code> value
	 * @return a <code>monster</code> value
	 */
	public static Monster newMonster(int value, Dungeon d, int x, int y,
			DungeonGame game) {
		int a = (int) (Math.random() * 100);
		// System.out.println("bis 100: " + a);
		if (a <= 10)
			return new Ogre(value);
		else if (a <= 20)
			return new Spider(value);
		else if (a <= 30)
			return new Ghul(value);
		else if (a <= 50)
			return new Orc(value);
		else if (a <= 70)
			return new Skeleton(value);
		else
			return new Wolf(value);
	}

	protected abstract double getAntiFleeFactor();

	@Override
	public double getAntiFleeValue() {
		if (this.getFightAP() < 0) {
			return 0;
		} else {

			int healthLevel = getHealthLevel();
			double erg = 6 * healthLevel * getAntiFleeFactor();
			int k = 1;
			double mult = 1;
			if (k < -2) {
				mult = 3;
			} else if (k == -2) {
				mult = 2;
			} else if (k == -1) {
				mult = 1.5;
			} else if (k == -0) {
				mult = 1;
			} else if (k == 1) {
				mult = 0.6;
			} else if (k == 2) {
				mult = 0.2;
			} else {
				mult = 0;
			}

			return erg * mult;
		}
	}

	@Override
	public String getMclass() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected ScoutResult scout(ScoutAction action) {
		// by default monster cannot scout
		return new ScoutResult(action, this, 0, "");
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(items.get(i),map);
		}
		return array;
	}

	public boolean specialAttackAvailable() {
		return this.specialAttackCounter == 0;
	}

	@Override
	public List<Item> getAllItems() {
		return items;
	}

	@Override
	public Attribute getDust() {
		return dust;
	}
	
	@Override
	public Attribute getDexterity() {
		return this.dexterity;
	}


	@Override
	public boolean hasItem(Item k) {

		for (int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			if (it == k) {
				return true;
			}
		}
		return false;

	}

	@Override
	public void recDust(double value) {
		// //System.out.println("Dust erh�hen um: "+value);
		// getCharacter().getDust().modValue(value);
		// if (getCharacter().getDust().getValue() >=
		// getCharacter().getDust().getBasic())
		// getCharacter().getDust().setValue(getCharacter().getDust().getBasic());
	}

	@Override
	public void addModification(TimedAttributeModification mod) {
		modifications.add(mod);
	}

	/**
	 * Describe <code>getworth</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 * @uml.property name="worth"
	 */
	@Override
	public int getWorth() {
		return worth;
	}

	/**
	 * Describe <code>getname</code> method here.
	 * 
	 * @return a <code>String</code> value
	 * 
	 * @uml.property name="name"
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getItemIndex(Item it) {

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == it) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Describe <code>getMin_Damage</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 * @uml.property name="min_Damage"
	 */
	public int getMin_Damage() {
		return minDamage;
	}

	/**
	 * Describe <code>getMax_Damage</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 * @uml.property name="max_Damage"
	 */
	public int getMax_Damage() {
		return maxDamage;
	}

	/**
	 * Describe <code>getHealth</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 * @uml.property name="health"
	 */
	@Override
	public Attribute getHealth() {
		return health;
	}

	/**
	 * Describe <code>getHealth_Value</code> method here.
	 * 
	 * @return an <code>int</code> value
	 */
	public int getHealth_Value() {
		return (int) health.getValue();
	}

	/**
	 * Describe <code>getpsycho</code> method here.
	 * 
	 * @return an <code>int</code> value
	 * 
	 * @uml.property name="psycho"
	 */
	@Override
	public Attribute getPsycho() {
		return psycho;
	}

	/**
	 * Describe <code>gerpsycho_Value</code> method here.
	 * 
	 * @return an <code>int</code> value
	 */
	public int getpsycho_Value() {
		return (int) psycho.getValue();
	}

	public int getBrave_Value() {
		return (int) bravery.getValue();
	}

	/**
	 * Describe <code>getChance_to_hit</code> method here.
	 * 
	 * @return an <code>int</code> value
	 */
	public int getChance_to_hit() {
		return (int) chanceToHit.getValue();
	}


	@Override
	protected int getTypeSkill(Figure m) {
		return 1;
	}

	@Override
	protected int getAllArmor(Slap s) {
		return 0;
	}

	@Override
	public Action getForcedFightAction() {
		return null;
	}

	@Override
	public Action getForcedMovementAction() {
		return null;
	}

	public MonsterInfo makeMonsterInfo(DungeonVisibilityMap map) {
		return MonsterInfo.makeMonsterInfo(this, map);
	}

	// /**
	// * Schlaegt zu. Ist ein Faktor 1.5 drin!!
	// *
	// * @return an <code>int</code> value
	// */
	// public int slay (fighterI f) {
	//       
	// if(Chance_to_hit.getValue() >= (Math.random() * 100)) {
	//	    
	// return (int)( 1.5 * (int)(Min_Damage + ((int)(Math.random() * (Max_Damage
	// - Min_Damage+1)))));
	//       
	// }
	// else return 0;
	//
	// }

	@Override
	public float getActualChanceToHit(Figure m) {

		// game.newStatement(
		// this.getName() + " Chance_to_hit:" + Chance_to_hit.getValue(),
		// 4);
		if (this.makesgoldenHit) {
			makesgoldenHit = false;
			return 100;
		}
		float c = (float) chanceToHit.getValue();
		return c;
	}

	@Override
	public int getActualRangeCapability(int range) {
		if (range == Position.DIST_FAR) {
			return 100;
		}
		if (range == Position.DIST_MID) {
			return 100;
		}
		if (range == Position.DIST_NEAR) {
			return 100;
		}

		return 100;

	}

	@Override
	public int getSlapStrength(Figure m) {
		// Faktor 1,0 !!
		int k = (int) (1.0 * (minDamage + ((int) (Math.random() * (maxDamage
				- minDamage + 1)))));
		// game.newStatement(this.getName() + " Schlagst�rke:" + k, 4);
		int h = this.getHealthLevel();
		if (h == 4) {

			return k;
		}
		if (h == 3) {
			return (int) (k * 0.7);
		}
		if (h == 2) {
			return (int) (k * 0.4);
		}

		return (int) (k * 0.2);

	}

	@Override
	public int getElude(Figure m) {
		int k = 5;
		// game.newStatement(this.getName() + " Chance ausweichen:" + k, 4);

		return k;
	}

	protected void setLevel(int value) {
		int potenz = 2;
		int unit = 2000;
		
		int i = 1;
		while (true) {
			if (value >= pot(i, potenz) * unit) {
			} else {
				level = i;
				break;
			}
			i++;
		}
	}
	
	protected String getLvlName(int worth, String[] array) {
		
				if (level > array.length) {
					return "Bestie";
				} else {
					return array[level - 1];
				}

			
	}

	@Override
	public int getLevel() {
		int potenz = 2;
		int unit = 2000;

		int i = 1;
		while (true) {
			if (worth >= pot(i, potenz) * unit) {
			} else {
				return i;
				// //System.out.println("Level: " + i);
			}

			i++;
		}

	}

	public static int pot(int base, int pot) {
		int erg = 1;
		for (int i = 0; i < pot; i++) {
			erg = erg * base;
		}
		// //System.out.println("" + base + " hoch " + pot + " = " + erg);
		return erg;

	}



	@Override
	public void setMakingSpecialAttack(boolean b) {

		this.makingSpecialAttack = b;
	}

	@Override
	protected void sanction(int i) {
		for (int j = 0; j < i; j++) {
			bravery.modValue((-1));
			psycho.modValue((-1));
			chanceToHit.modValue((-4));
		}
	}

	@Override
	public int getHealthLevel() {
		int i = health.perCent();
		if (i > 50)
			return 4;
		else if (i > 30)
			return 3;
		else if (i > 15)
			return 2;
		else if (i > 8)
			return 1;
		else
			return 0;
	}

	/**
	 * 
	 * @uml.property name="items"
	 */
	@Override
	public List<Item> getItems() {
		return items;
	}

	@Override
	public boolean takeItem(Item i) {
		if (i == null)
			return false;
		items.add(i);
		ItemOwner before = i.getOwner();
		if(before != null) {
			before.removeItem(i);
		}
		Item.notifyItem(i, this);
		return true; // ?
	}

	@Override
	public boolean removeItem(Item i) {
		if (i instanceof Locatable) {
			((Locatable) i).getsRemoved();
		}
		return items.remove(i);
	}

	public void heroHasFleen(Room r, int monsterCnt) {
		double d = ((double) 1) / monsterCnt;
		double l = Math.random();
		if (l < d) {
			int hunt = this.hunting();
			int k = (int) Math.random() * hunt;
			if (k >= 5) {
				// sofort Verfolgen
				this.addRouteInstruction(new RouteInstruction(r));

			} else if (k >= 3) {
				// eine Runde sp�ter verfolgen
				this.addRouteInstruction(new RouteInstruction(0));
				this.addRouteInstruction(new RouteInstruction(r));

			} else if (k >= 1) {
				// 3 Runden sp�ter verfolgen
				this.addRouteInstruction(new RouteInstruction(0));
				this.addRouteInstruction(new RouteInstruction(0));
				this.addRouteInstruction(new RouteInstruction(r));
			} else {
				// nicht verfolgen

			}
		} else {
			// bei mehrerern Monstern nicht
		}

	}

	@Override
	public boolean giveAwayItem(Item i, ItemOwner o) {
		if (items.contains(i)) {
			o.takeItem(i);
			items.remove(i);
			return true;
		} else {
			return false;
		}
	}


	public boolean goEast() {
		Room wannaGo = actualDungeon.getRoomNr(location.getX() + 1, location
				.getY());
		Door d = actualDungeon.getRoom(location).getConnectionTo(wannaGo);
		if ((d != null) && (d.isPassable(this)) && (wannaGo != null)
				&& (!wannaGo.hasStatue())) {

			move(wannaGo);
			lastMove = RouteInstruction.EAST;

			return true;
		} else {
			return false;
		}
	}

	public boolean goWest() {
		Room wannaGo = actualDungeon.getRoomNr(location.getX() - 1, location
				.getY());
		Door d = actualDungeon.getRoom(location).getConnectionTo(wannaGo);
		if ((d != null) && (d.isPassable(this)) && (wannaGo != null)
				&& (!wannaGo.hasStatue())) {
			move(wannaGo);
			lastMove = RouteInstruction.WEST;

			return true;
		} else
			return false;
	}

	public boolean goNorth() {
		Room wannaGo = actualDungeon.getRoomNr(location.getX(),
				location.getY() - 1);
		Door d = actualDungeon.getRoom(location).getConnectionTo(wannaGo);
		if ((d != null) && (d.isPassable(this)) && (wannaGo != null)
				&& (!wannaGo.hasStatue())) {

			move(wannaGo);
			lastMove = RouteInstruction.NORTH;

			return true;
		} else
			return false;
	}

	public boolean goSouth() {
		Room wannaGo = actualDungeon.getRoomNr(location.getX(),
				location.getY() + 1);
		Door d = actualDungeon.getRoom(location).getConnectionTo(wannaGo);
		if ((d != null) && (d.isPassable(this)) && (wannaGo != null)
				&& (!wannaGo.hasStatue())) {

			move(wannaGo);
			lastMove = RouteInstruction.SOUTH;

			return true;
		} else
			return false;
	}


	@Override
	public Item getItemNumber(int i) {
		return items.get(i);
	}


	@Override
	public void recover() {

		heal(healthRecover);
		dust.addToMax(0.5);
		
		if (psycho.getValue() < psycho.getBasic()) {
			psycho.modValue(1);
		}
		if (bravery.getValue() < bravery.getBasic()) {
			bravery.modValue(1);
		}
		if (chanceToHit.getValue() + 2 < chanceToHit.getBasic()) {
			chanceToHit.modValue(2);
		} else if (chanceToHit.getValue() + 1 < chanceToHit.getBasic()) {
			chanceToHit.modValue(1);
		}

	}

	public void getThreaten(int k, int count/* , Fight w */) {
		hasJustBeenThreaten = true;
		if (count > 0) {
			if (count == 1) {
				// newStatement("Zweite Drohung:", 4);
				k = k / 2;
			} else if (count == 2) {
				// newStatement("dritte Drohung:", 4);
				k = k / 3;
			} else if (count == 3) {
				// newStatement("vierte Drohung:", 4);
				k = 0;
			} else {
				// newStatement("Xte Drohung: Keine Drohung", 4);
				hasJustBeenThreaten = false;
			}
		}
		int x = k * (-1);
		// getGame().newStatement(" Mut erniedrigt um: " + x, 4);

		bravery.modValue(x);

	}


	@Override
	public Attribute getBrave() {
		return bravery;
	}

	@Override
	protected void lookInRoom() {

	}

	@Override
	public String toString() {
		return (this.getClass().getSimpleName() + " " + this.getName());
	}

	@Override
	public void receiveSlapResult(SlapResult r) {
		// macht noch nichts
	}

	public void incConvinced(int k) {
		convinced += k;
	}

	@Override
	public int getKnowledgeBalance(Figure f) {
		return level - f.getLevel();
	}

	public int calcFearLevel() {
		int level = 0;
		// //System.out.println("Mut: "+Integer.toString(brave.getValue()));
		int handycap = 50;
		int k = (int) bravery.getValue();
		if (k == 0)
			k = 1;
		for (int i = 0; i < k; i++) { // brave gibt den Wert wie oft gepr�ft
										// wird, der beste Wert
			// wird genommen
			int a = fleeHelp(handycap);
			// //System.out.println("Durchlauf: "+Integer.toString(i)+" Wert:
			// "+Integer.toString(a));
			if (a > level)
				level = a;
		}
		if (thundered) {
			level = 0;
			thundered = false;
		}
		return level;

	}

	public boolean hasJustBeenThreaten() {
		return hasJustBeenThreaten;
	}

	protected abstract boolean makeSpecialAttack(Figure target);

	public void break_special_attack() {
		if (this.makingSpecialAttack) {

			this.makingSpecialAttack = false;
			// getGame().newStatement("Spezialangriff unterbrochen", 2);
		}
	}

	@Override
	public boolean isAbleToUseItem() {
		return false;
	}

	@Override
	public boolean isAbleToTakeItem(Item it) {
		return true;

	}

	@Override
	public boolean isAbleToTakeItemInFight(Item it) {
		return false;
	}

	@Override
	public boolean canTakeItem(Item it) {
		return true;
	}


	protected double calcFleeChance() {
		int k = getHealthLevel();
		int l = k + 2;
		return ((double) l) / 10;
	}

	@Override
	protected boolean flee(RouteInstruction.Direction dir) {
		Room from = getRoom();
		if (Math.random() < calcFleeChance()) {
			boolean done = walk(dir);
			if (done) {
				// [TODO] SUCCESSFULL?
				FleePercept p = new FleePercept(this, from, dir.getValue(), false);
				from.distributePercept(p);
			}
			return done;

		}
		return false;

	}

	public RouteInstruction.Direction getFleeDirection() {
		return RouteInstruction.Direction.fromInteger(getFleeDir());
	}

	public int getFleeDir(/* Fight w */) {
		if (lastMove == RouteInstruction.WEST) {
			if (this.getRoom().directionPassable(RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			} else if (this.getRoom().directionPassable(RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			} else if (this.getRoom().directionPassable(RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			} else if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			}

		} else if (lastMove == RouteInstruction.EAST) {
			if (goWest() == false) {
				if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
					return RouteInstruction.WEST;
				} else if (this.getRoom().directionPassable(
						RouteInstruction.NORTH)) {
					return RouteInstruction.NORTH;
				} else if (this.getRoom().directionPassable(
						RouteInstruction.SOUTH)) {
					return RouteInstruction.SOUTH;
				} else if (this.getRoom().directionPassable(
						RouteInstruction.EAST)) {
					return RouteInstruction.EAST;
				}

			}
		} else if (lastMove == RouteInstruction.NORTH) {
			if (this.getRoom().directionPassable(RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			} else if (this.getRoom().directionPassable(RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			} else if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			} else if (this.getRoom().directionPassable(RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			}

		} else if (lastMove == RouteInstruction.SOUTH || lastMove == 0) {
			if (this.getRoom().directionPassable(RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			} else if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			} else if (this.getRoom().directionPassable(RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			}
			if (this.getRoom().directionPassable(RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			}

		}

		// System.out.println("lastMove Error! monster.flee()");
		return 0;
	}

	public void looseItems() {
		Iterator<Item> iterator = items.iterator();
		while(iterator.hasNext()) {
			Item j = iterator.next();
			iterator.remove();
			actualDungeon.getRoom(location).takeItem(j);
		}
		
	}
	@Override
	protected boolean layDown(Item i) {
		actualDungeon.getRoom(location).takeItem(i);
		items.remove(i);
		return true;
	}

	protected int fleeHelp(int handycap) {
		int value = ((int) (Math.random() * handycap));
		// int antiFlee = 10;
		if (value < psycho.getValue() - 3) {
			return 5;
		} else if (value < psycho.getValue() * 2) {
			return 4;
		} else if (value < psycho.getValue() * 3) {
			return 3;
		} else if (value < psycho.getValue() * 4) {
			return 2;
		} else if (value < (handycap - 3)) {
			return 1;
		} else
			return 0;
	}

	@Override
	public int getKilled(int damage) {
		dead = true;

		try {
			Thread.sleep(600);
		} catch (Exception e) {
		}
		if (items.size() > 0) {
			getRoom().distributePercept(new ItemDroppedPercept(items, this));
			getRoom().addItems(items, null);
		}

		pos.figureLeaves();
		Figure.removeFigure(this);
		this.getRoom().figureLeaves(this);

		if (health.getValue() > 0) {
			return (int) health.getValue();
		} else
			return 0;
	}


	/**
	 * Returns the thundered.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isThundered() {
		return thundered;
	}

	/**
	 * Sets the thundered.
	 * 
	 * @param thundered
	 *            The thundered to set
	 * 
	 */
	public void setThundered(boolean thundered) {
		this.thundered = thundered;
	}

	public Object getMasterKey() {
		for (int i = 0; i < this.items.size(); i++) {
			if (items.get(i) instanceof DarkMasterKey) {
				return items.get(i);
			}
		}
		return null;
	}

	@Override
	public boolean isAbleToUseShrine() {
		return false;
	}

	@Override
	public boolean isAbleToUseChest() {
		return false;
	}

	/**
	 * @return
	 * 
	 */
	public boolean isSpitted() {
		return spitted;
	}

	/**
	 * @param b
	 * 
	 */
	public void setSpitted(boolean b) {
		spitted = b;
	}

	/**
	 * @param b
	 * 
	 */
	public void setLuzia(boolean b) {
		luzia = b;
	}

	@Override
	public void fightBegins(List<Figure> l) {

	}

	/**
	 * @return Returns the lastMove.
	 */
	public int getLastMove() {
		return lastMove;
	}

	/**
	 * @param lastMove
	 *            The lastMove to set.
	 */
	public void setLastMove(int lastMove) {
		this.lastMove = lastMove;
	}

	/**
	 * @return Returns the fighted.
	 */
	public boolean hasFighted() {
		return fighted;
	}

	/**
	 * @param fighted
	 *            The fighted to set.
	 */
	public void setFighted(boolean fighted) {
		this.fighted = fighted;
	}

	/**
	 * @return Returns the routing.
	 */
	public Stack<RouteInstruction> getRouting() {
		return routing;
	}

	/**
	 * @param routing
	 *            The routing to set.
	 */
	public void setRouting(Stack<RouteInstruction> routing) {
		this.routing = routing;
	}
}
