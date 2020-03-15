package figure.monster;

import ai.AI;
import figure.HealthLevel;
import figure.action.ScoutAction;
import figure.action.ScoutResult;
import item.Item;
import item.ItemInfo;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;
import item.interfaces.LocatableItem;
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
import log.Log;

/**
 * Superklasse fuer alle moeglichen Monster, abgeleitet von Fighter.
 * Implementiert bis auf Monstertypspezifische Sachen alle abstrakten Methoden
 * von Fighter. Wird gesteuert von einem MonsterControl-Objekt.
 * 
 */
public abstract class Monster extends Figure implements Paragraphable,
		InfoProvider {



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

	protected int specialAttackCounter = 0;


	protected String[] lvl_names;


	@Override
	public int filterFrightening(Frightening f) {
		return f.getValue();
	}
	
	@Override
	protected boolean tryUnlockDoor(Door d, boolean doIt) {
		return false;
	}

	protected List<TimedAttributeModification> modifications = new LinkedList<>();

	protected Attribute strength ;
	protected Attribute dexterity = new Attribute(Attribute.DEXTERITY,7);
	

	@Override
	public Attribute getStrength() {
		if (strength == null) {
			return new Attribute(Attribute.STRENGTH, 3);
		}
		return strength;
	}

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
		lookDir = ((int) (Math.random() * 4)) + 1;
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

	protected int healthRecover = 1;

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

	@Override
	public int getTumbleValue(Figure f) {
		return tumbleValue;
	}

	@Deprecated
	public static Monster createMonster(Class<? extends Monster> type, int value, DungeonGame game) {
		if (type == Wolf.class) {
			return new Wolf(value);
		}
		if (type == Orc.class) {
			return new Orc(value);
		}
		if (type == Skeleton.class) {
			return new Skeleton(value);
		}
		if (type == Ogre.class) {
			return new Ogre(value);
		}
		if (type == Ghul.class) {
			return new Ghul(value);
		}
		if (type == Spider.class) {
			return new Spider(value);
		} else
			return null;
	}


	@Override
	public int getAntiTumbleValue() {
		return antiTumbleValue;
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

	protected abstract double getAntiFleeFactor();

	@Override
	public double getAntiFleeValue() {
		if (this.getActionPoints() <= 0) {
			return 0;
		} else {

			int healthLevel = getHealthLevel().getValue();
			double erg = 6 * healthLevel * getAntiFleeFactor();
			int k = 1;
			double mult = 0.6;
			// TODO: redesign and revive ant flee calculation
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
		return new ScoutResult(this, 0);
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(items.get(i),map);
		}
		return array;
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

	}

	@Override
	public void addModification(TimedAttributeModification mod) {
		modifications.add(mod);
	}


	@Override
	public int getWorth() {
		return worth;
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public Attribute getHealth() {
		return health;
	}


	@Override
	public Attribute getPsycho() {
		return psycho;
	}

	public int getChance_to_hit() {
		return (int) chanceToHit.getValue();
	}



	@Override
	protected int getAllArmor(Slap s) {
		return 0;
	}

	@Override
	public float getActualChanceToHit(Figure m) {

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
		int h = this.getHealthLevel().getValue();
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
	
	@Override
	public int getLevel() {
		int potenz = 2;
		int unit = 2000;

		int i = 1;
		while (true) {
			if (worth >= pot(i, potenz) * unit) {
			} else {
				return i;
			}
			i++;
		}

	}

	public static int pot(int base, int pot) {
		int erg = 1;
		for (int i = 0; i < pot; i++) {
			erg = erg * base;
		}
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
	public HealthLevel getHealthLevel() {
		int i = health.perCent();
		return HealthLevel.fromPercent(i);
	}

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
		if (i != null) {
			i.getsRemoved();
		}
		return items.remove(i);
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

	@Override
	public Attribute getBrave() {
		return bravery;
	}

	@Override
	protected void lookInRoom() {

	}

	@Override
	public String toString() {
		return (this.getClass().getSimpleName() + " " + this.getName() + " "+pos);
	}

	@Override
	public void receiveSlapResult(SlapResult r) {
		// macht noch nichts
	}

	@Override
	public int getKnowledgeBalance(Figure f) {
		return level - f.getLevel();
	}

	protected abstract boolean makeSpecialAttack(Figure target);

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
		int k = getHealthLevel().getValue();
		int l = k + 2;
		return ((double) l) / 10;
	}

	@Override
	protected boolean flee(RouteInstruction.Direction dir) {
		Room from = getRoom();
		if (Math.random() < calcFleeChance()) {
			Position oldPos = this.getPos();
			boolean done = walk(dir);
			if (done) {
				// [TODO] SUCCESSFULL?
				FleePercept p = new FleePercept(this, oldPos, dir.getValue(), false);
				from.distributePercept(p);
			}
			return done;

		}
		return false;

	}

	public RouteInstruction.Direction getFleeDirection() {
		return RouteInstruction.Direction.fromInteger(getFleeDir());
	}

	public int getFleeDir() {
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
			if (!goWest()) {
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
		return 0;
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


		if (!items.isEmpty()) {
			getRoom().distributePercept(new ItemDroppedPercept(items, this));
			getRoom().addItems(items, null);
		}

		dieAndLeave();

		if (health.getValue() > 0) {
			return (int) health.getValue();
		} else
			return 0;
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

}
