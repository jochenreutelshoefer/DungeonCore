package de.jdungeon.figure.monster;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import de.jdungeon.ai.AI;
import de.jdungeon.ai.DefaultMonsterIntelligence;
import de.jdungeon.ai.DefaultMonsterReflexBehavior;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.skill.attack.Slap;
import de.jdungeon.skill.attack.SlapResult;
import de.jdungeon.figure.APAgility;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.HealthLevel;
import de.jdungeon.figure.Spellbook;
import de.jdungeon.figure.action.ScoutAction;
import de.jdungeon.figure.action.ScoutResult;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.attribute.TimedAttributeModification;
import de.jdungeon.figure.percept.FleePercept;
import de.jdungeon.figure.percept.ItemDroppedPercept;
import de.jdungeon.dungeon.InfoEntity;
import de.jdungeon.dungeon.InfoProvider;
import de.jdungeon.gui.Paragraphable;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.interfaces.ItemOwner;

/**
 * Superklasse fuer alle moeglichen Monster, abgeleitet von Fighter.
 * Implementiert bis auf Monstertypspezifische Sachen alle abstrakten Methoden
 * von Fighter. Wird gesteuert von einem MonsterControl-Objekt.
 */
public abstract class Monster extends Figure implements Paragraphable, InfoProvider {

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

	protected String[] lvl_names;

	protected double healthRecover = 0.35;

	protected List<TimedAttributeModification> modifications = new LinkedList<>();

	protected Attribute strength;
	protected Attribute dexterity = new Attribute(Attribute.Type.Dexterity, 7);

	@Override
	protected APAgility createAgility() {
		return new APAgility();
	}

	@Override
	public Attribute getStrength() {
		if (strength == null) {
			return new Attribute(Attribute.Type.Strength, 7);
		}
		return strength;
	}

	protected int lastMove = 0;

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new MonsterInfo(this, map);
	}

	protected abstract int getChangeToHit();

	protected abstract int getDamageVariance();

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
		int chance_to_hit = getChangeToHit();
		int valueLeftAfterChanceToHit = (int) ((((double) value) / chance_to_hit));
		int damageBaseValue = this.getHealthDamageBalance()  + ((int) (Math.random() * 4));

		int HealthI = (int) ((double) valueLeftAfterChanceToHit / damageBaseValue);

		health = new Attribute(Attribute.Type.Health, HealthI);
		psycho = new Attribute(Attribute.Type.Psycho, 10);
		dust = new Attribute(Attribute.Type.Dust, 10);

		int variance = getDamageVariance();
		minDamage = damageBaseValue - variance;
		maxDamage = damageBaseValue + variance;

		chanceToHit = new Attribute(Attribute.Type.OtherDeprecatedAttributeType, chance_to_hit);
	}



	@Override
	public Item unwrapItem(ItemInfo it) {
		for (Iterator<Item> iter = items.iterator(); iter.hasNext(); ) {
			Item element = iter.next();
			if (ItemInfo.makeItemInfo(element, null).equals(it)) {
				return element;
			}
		}
		return null;
	}

	Stack<RouteInstruction> routing = new Stack<RouteInstruction>();

	double fireResistRate = 1.0;

	private double lightningResistRate = 1.0;

	private double magicResistRate = 1.0;

	private double poisonResistRate = 1.0;

	protected String name;

	protected Attribute health;

	protected Attribute psycho;

	int minDamage;

	int maxDamage;

	Attribute chanceToHit;

	protected abstract int getHealthDamageBalance();

	protected List<Item> items = new LinkedList<Item>();

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

	@Deprecated
	public static Monster createMonster(Class<? extends Monster> type, int value) {
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
		}
		else {
			return null;
		}
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
	public Attribute getAttribute(Attribute.Type type) {
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
		}
		else {
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
	public ScoutResult scout(ScoutAction action, int round) {
		// by default monster cannot scout
		return new ScoutResult(this, 0);
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(items.get(i), map);
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

	@Override
	protected int getAllArmor(Slap s) {
		return 0;
	}

	@Override
	public float getActualChanceToHit(Figure m) {
		return (float) chanceToHit.getValue();
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
		int k = (int) (1.0 * (minDamage + ((int) (Math.random() * (maxDamage
				- minDamage + 1)))));
		HealthLevel level = this.getHealthLevel();
		if (level == HealthLevel.Strong || level == HealthLevel.Good) {
			return k;
		}
		if (level == HealthLevel.Ok) {
			return (int) (k * 0.8);
		}
		if (level == HealthLevel.Injured) {
			return (int) (k * 0.6);
		}
		return (int) (k * 0.4);
	}

	protected void setLevel(int value) {
		int potenz = 2;
		int unit = 2000;

		int i = 1;
		while (true) {
			if (value >= pot(i, potenz) * unit) {
			}
			else {
				level = i;
				break;
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
	protected void sanction(int i) {
		for (int j = 0; j < i; j++) {
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
		if (i == null) {
			return false;
		}
		items.add(i);
		ItemOwner before = i.getOwner();
		if (before != null) {
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
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void recover(int round) {

		heal(healthRecover, round);
		dust.addToMax(0.5);

		if (psycho.getValue() < psycho.getBasic()) {
			psycho.modValue(1);
		}
		if (chanceToHit.getValue() + 2 < chanceToHit.getBasic()) {
			chanceToHit.modValue(2);
		}
		else if (chanceToHit.getValue() + 1 < chanceToHit.getBasic()) {
			chanceToHit.modValue(1);
		}
	}

	@Override
	protected void lookInRoom(int round) {

	}

	@Override
	public String toString() {
		return (this.getClass().getSimpleName() + " " + this.getName() + " " + pos);
	}

	@Override
	public void receiveSlapResult(SlapResult r) {
		// macht noch nichts
	}

	@Override
	public int getKnowledgeBalance(Figure f) {
		// concept off
		return 0;
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
		int k = getHealthLevel().getValue();
		int l = k + 2;
		return ((double) l) / 10;
	}

	@Override
	public boolean flee(RouteInstruction.Direction dir, int round) {
		Room from = getRoom();
		if (Math.random() < calcFleeChance()) {
			Position oldPos = this.getPos();
			boolean done = walk(dir, round);
			if (done) {
				// [TODO] SUCCESSFULL?
				FleePercept p = new FleePercept(this, oldPos, dir.getValue(), false, round);
				from.distributePercept(p);
			}
			return done;
		}
		return false;
	}

	public RouteInstruction.Direction getFleeDirection() {
		return RouteInstruction.Direction.fromInteger(getFleeDir());
	}

	private int getFleeDir() {
		if (lastMove == RouteInstruction.WEST) {
			if (this.getRoom().directionPassable(RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			}
		}
		else if (lastMove == RouteInstruction.EAST) {
			if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			}
			else if (this.getRoom().directionPassable(
					RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			}
			else if (this.getRoom().directionPassable(
					RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			}
			else if (this.getRoom().directionPassable(
					RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			}
		}
		else if (lastMove == RouteInstruction.NORTH) {
			if (this.getRoom().directionPassable(RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			}
		}
		else if (lastMove == RouteInstruction.SOUTH || lastMove == 0) {
			if (this.getRoom().directionPassable(RouteInstruction.NORTH)) {
				return RouteInstruction.NORTH;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.WEST)) {
				return RouteInstruction.WEST;
			}
			else if (this.getRoom().directionPassable(RouteInstruction.EAST)) {
				return RouteInstruction.EAST;
			}
			if (this.getRoom().directionPassable(RouteInstruction.SOUTH)) {
				return RouteInstruction.SOUTH;
			}
		}
		return 0;
	}

	@Override
	public boolean layDown(Item i) {
		actualDungeon.getRoom(location).takeItem(i);
		items.remove(i);
		return true;
	}

	@Override
	public int getKilled(int damage) {

		if (!items.isEmpty()) {
			getRoom().distributePercept(new ItemDroppedPercept(items, this, -1));
			getRoom().addItems(items, null);
		}

		dieAndLeave();

		if (health.getValue() > 0) {
			return (int) health.getValue();
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean isAbleToUseShrine() {
		return false;
	}

	@Override
	public boolean isAbleToUseChest() {
		return false;
	}

}
