package figure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ai.AI;
import ai.AbstractReflexBehavior;
import ai.DefaultMonsterIntelligence;
import ai.AbstractAI;
import dungeon.Chest;
import dungeon.Dir;
import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.DungeonWorldObject;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.Room;
import dungeon.util.DungeonUtils;
import dungeon.util.InfoUnitUnwrapper;
import dungeon.util.RouteInstruction;
import event.EventManager;
import event.WorldChangedEvent;
import fight.Frightening;
import fight.Poisoning;
import fight.Slap;
import fight.SlapResult;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.DoNothingAction;
import figure.action.EndRoundAction;
import figure.action.EquipmentChangeAction;
import figure.action.ExpCodeChangeAction;
import figure.action.FleeAction;
import figure.action.LayDownItemAction;
import figure.action.LearnSpellAction;
import figure.action.LockAction;
import figure.action.MoveAction;
import figure.action.ScoutAction;
import figure.action.ScoutResult;
import figure.action.ShrineAction;
import figure.action.SkillUpAction;
import figure.action.SpellAction;
import figure.action.StepAction;
import figure.action.SuicideAction;
import figure.action.TakeItemAction;
import figure.action.TakeItemFromChestAction;
import figure.action.UseChestAction;
import figure.action.UseItemAction;
import figure.action.result.ActionResult;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.hero.Character;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.memory.FigureMemory;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import figure.other.ConjuredMagicFigure;
import figure.percept.AttackPercept;
import figure.percept.BreakSpellPercept;
import figure.percept.DiePercept;
import figure.percept.DoorSmashPercept;
import figure.percept.FightBeginsPercept;
import figure.percept.FightEndedPercept;
import figure.percept.FleePercept;
import figure.percept.HitPercept;
import figure.percept.InfoPercept;
import figure.percept.MissPercept;
import figure.percept.MovePercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;
import figure.percept.ShieldBlockPercept;
import figure.percept.StepPercept;
import figure.percept.TakePercept;
import figure.percept.TextPercept;
import figure.percept.TumblingPercept;
import figure.percept.UsePercept;
import figure.percept.WaitPercept;
import game.ControlUnit;
import game.InfoEntity;
import game.JDEnv;
import game.JDGUI;
import game.Turnable;
import gui.Paragraph;
import gui.Paragraphable;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.interfaces.ItemOwner;
import item.interfaces.Usable;
import org.apache.log4j.Logger;
import shrine.Shrine;
import spell.AbstractSpell;
import spell.Spell;
import spell.SpellInfo;
import util.JDColor;

/**
 * Oberklasse fuer eine Figur. Eine Figur befindet sich immer in genau einem
 * Raum und kann sich durch Tueren in andere Raeume bewegen. Unterklassen
 * muessen alle abstrakten Methoden fuer den Kampf implementieren. In dieser
 * Klasse werden die Aktionspunkte verwaltet. Jede Runde erhaelt eine Figur
 * Aktionspunkte um Aktionen ausfuehren zu koennen. Die Aktionen werden in
 * processMovementAction() bzw. im Kampf in processFightAction() ausgefuehrt.
 *
 * @see Monster
 * @see Hero
 */

public abstract class Figure extends DungeonWorldObject implements ItemOwner,
		Turnable, VisibilityModifier, Paragraphable, Serializable {

	public static final int STATUS_HEALTHY = 4;

	public static final int STATUS_WEAK = 3;

	public static final int STATUS_INJURED = 2;

	public static final int STATUS_WOUNDED = 1;

	public static final int STATUS_CRITICAL = 0;

	protected Position pos = null;

	protected Room room = null;

	protected int blinded = 0;

	protected int cobwebbed = 0;

	protected ControlUnit control;
	private AI ai;
	protected AbstractReflexBehavior reflexReactionUnit;

	protected int actionPoints = 0;

	protected int fightAP = 0;

	private Spell lastSpell = null;

	protected int figureID = -1;

	protected Spellbook spellbook;

	private static int figureID_counter = 1;

	private static Map<Integer, Figure> allFigures = new HashMap<>();

	protected int level;

	protected int lookDir = 1;

	private int golden_hit = 0;

	public int escape = 0;

	public boolean double_bonus = false;

	public boolean half_bonus = false;

	public boolean raiding = false;

	boolean thief = false;

	private List<Poisoning> poisonings = new LinkedList<Poisoning>();

	protected String status = (" sieht noch recht stark aus.");

	protected String shortStatus = ("stark");

	protected boolean dead = false;

	protected Map<Dungeon, DungeonVisibilityMap> visibilities = new HashMap<>();

	protected JDPoint location;

	public Room before = null;

	private boolean bonusLive = false;

	public boolean isBonusLive() {
		return bonusLive;
	}

	protected Room respawnRoom = null;

	public Room getRespawnRoom() {
		return respawnRoom;
	}

	public void setRespawnRoom(Room respawnRoom) {
		this.respawnRoom = respawnRoom;
	}

	@Override
	public FigureMemory getMemoryObject(FigureInfo info) {
		return new FigureMemory(this, info);
	}

	public void setBonusLive(boolean bonusLive) {
		this.bonusLive = bonusLive;
	}

	public static void addFigure(Figure f) {
		allFigures.put(Integer.valueOf(f.getFighterID()), f);
	}

	// hack to save some memory
	public static void unsetUnnecessaryRoomObStatsObjects(Dungeon d) {
		for (Iterator<Integer> iter = allFigures.keySet().iterator(); iter
				.hasNext(); ) {

			Figure element = allFigures.get(iter.next());
			DungeonVisibilityMap map = element.getRoomVisibility();
			RoomObservationStatus[][] rooms = map.getRooms();
			for (int i = 0; i < rooms.length; i++) {
				for (int j = 0; j < rooms[0].length; j++) {
					if (!d.getRoomNr(i, j).isClaimed()) {
						rooms[i][j] = null;
					}
				}
			}

		}
	}

	public static Figure getFigure(int id) {
		return allFigures.get(Integer.valueOf(id));
	}

	public abstract String getName();

	protected boolean wayPassable(Door d, Room toGo) {

		if ((d != null) && (d.isPassable(this)) && (toGo != null)) {
			return !(toGo.hasStatue() && this instanceof Monster);
		}
		return false;
	}

	@Override
	public int getVisibilityStatus() {
		return 4;
	}

	public boolean hasKey(String k) {
		List<Item> items = getItems();
		for (int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			if (it instanceof Key) {
				if (((Key) it).getType().equals(k)) {
					return true;
				}
			}
		}
		return false;
	}

	public void decActionPoints(int k) {
		actionPoints -= k;
		if (actionPoints < 0) {
			actionPoints = 0;
		}
	}

	public abstract int getLevel();

	public void poison(Poisoning p) {
		poisonings.add(p);
	}

	@Override
	public boolean addItems(List<Item> l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (l.get(i));
			this.takeItem(it);
		}
		return true;
	}

	public void sufferPoisonings() {
		for (int i = 0; i < poisonings.size(); i++) {
			Poisoning p = (poisonings.get(i));
			if (p.getTime() == 0) {
				poisonings.remove(p);
			}
			else {
				p.sufferRound(this);
			}
		}
	}

	private Figure lastFrighener = null;

	public void putFrightening(Frightening fr) {
		int decreasing = filterFrightening(fr);
		lastFrighener = fr.getActor();
		this.getBrave().modValue(decreasing * (-1));
		shock(decreasing);
	}

	private void shock(int value) {
		if (value > 0) {
			this.spellBreak();
		}
		double braveVal = this.getBrave().getValue();
		double braveBase = this.getBrave().getBasic();
		double percent = braveVal / braveBase;
		int shocks = 0;
		int i = 0;
		while (i < value) {
			i++;
			if (Math.random() > percent) {
				shocks++;
			}
		}
		this.reflexReactionUnit.setShock(shocks);
	}

	public Attribute getBrave() {
		return bravery;
	}

	protected abstract int filterFrightening(Frightening fr);

	public void healPoisonings() {
		poisonings = new LinkedList<Poisoning>();
	}

	public abstract Attribute getAttribute(int s);

	public abstract Attribute getPsycho();

	public abstract Attribute getStrength();

	public abstract void recDust(double k);

	@Override
	public abstract boolean removeItem(Item i);

	public abstract void receiveSlapResult(SlapResult r);

	public abstract boolean giveAwayItem(Item i, ItemOwner o);

	public boolean payFightActionPoint() {

		if (getFightAP() > 0) {
			decFightAP(1);
			return true;
		}
		else {
			return false;
		}

	}

	public void tellPercept(Percept p) {
		p.perceivedBy(this);
		if (control != null) {
			control.tellPercept(p);
		}
	}

	public Spell resetLastSpell() {
		Spell s = lastSpell;
		lastSpell = null;
		return s;
	}

	protected abstract List getModificationList();

	public abstract Action getForcedMovementAction();

	public abstract Action getForcedFightAction();

	public boolean payFightActionPoints(int k) {
		boolean b = true;
		for (int i = 0; i < k; i++) {
			b = payFightActionPoint();
		}
		return b;
	}

	public boolean payActionPoint() {
		if (getRoom().fightRunning()) {
			return payFightActionPoint();
		}
		else {
			return payMoveActionPoint();
		}
	}

	public boolean payMoveActionPoint() {

		if (getActionPoints() > 0) {
			decActionPoints(1);

			return true;
		}
		else {
			return false;
		}

	}

	public boolean payDust(double value) {
		if (this.getDust().getValue() >= value) {
			this.getDust().modValue((-1) * value);
			return true;
		}
		return false;
	}

	public boolean payDust(int val) {
		return payDust((double) val);
	}

	public boolean payMoveActionPoints(int k) {
		boolean b = true;
		for (int i = 0; i < k; i++) {
			b = payMoveActionPoint();
		}
		return b;
	}

	public void heal(double value) {
		Attribute healthAttr = getHealth();

		if (healthAttr.getValue() + value <= healthAttr.getBasic()) {

			healthAttr.modValue(value);
		}
		else {
			healthAttr.setValue((healthAttr.getBasic()));
		}

	}

	public void heal(int value) {
		heal((double) value);
	}

	public int getFighterID() {
		if (this.figureID == -1) {
			throw new IllegalStateException("FigureId not set, check correct Figure initialization.");
		}
		return this.figureID;
	}

	public static void createVisibilityMaps(Dungeon d) {
		Set<Integer> s = allFigures.keySet();
		for (Iterator<Integer> iter = s.iterator(); iter.hasNext(); ) {
			Integer element = iter.next();
			Figure f = allFigures.get(element);
			f.createVisibilityMap(d);

		}

	}

	public static void setMonsterControls() {
		Set<Integer> s = allFigures.keySet();
		for (Iterator<Integer> iter = s.iterator(); iter.hasNext(); ) {
			Integer element = iter.next();
			Figure f = allFigures.get(element);
			if (f instanceof Monster) {
				MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(f,
						f.getRoomVisibility());
				AbstractAI ai = new DefaultMonsterIntelligence();
				if (f.getSpecifiedAI() != null) {
					ai = f.getSpecifiedAI();

				}
				ai.setFigure(info);
				ControlUnit control = new FigureControl(info, ai);
				if (f.getControl() == null) {
					f.setControl(control);
				}
			}

		}
	}

	AbstractAI specifiedAI = null;

	public static void removeFigure(Figure f) {
		allFigures.remove(Integer.valueOf(f.getFighterID()));
	}

	protected Attribute bravery = new Attribute(Attribute.BRAVE, 6);

	public static void resetFigureList() {
		allFigures = new HashMap<Integer, Figure>();
	}

	public abstract double getFireResistRate();

	public abstract double getMagicResistRate();

	public abstract double getLightningResistRate();

	public abstract double getPoisonResistRate();

	public abstract int getAntiTumbleValue();

	public void healPerCent(int value) {
		int all = (int) getHealth().getBasic();
		int amount = (all * value) / 100;
		heal(amount);
	}

	public void decFightAP(int v) {
		fightAP -= v;
	}

	public void incFightAP(int v) {
		fightAP += v;
	}

	public abstract boolean isAbleToUseItem();

	public abstract boolean isAbleToTakeItem(Item it);

	public abstract boolean isAbleToTakeItemInFight(Item it);

	public abstract List<Item> getItems();

	private boolean invulnerable = false;

	public boolean isInvulnerable() {
		return invulnerable;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public Room getRoomInfo() {
		return getRoom().getDungeon().getRoom(getLocation());
	}

	private int lastTurn = -1;

	public void timeTick(int round) {

		if (lastTurn < round) {
			fireModifications();

			recover();

			if (cobwebbed > 0) {
				if (cobwebbed > 10) {
					cobwebbed -= 10;
				}
				else {
					cobwebbed = 0;
				}
			}
			this.sufferPoisonings();
		}
	}

	@Override
	public void turn(int i) {

		// TODO: check is this really required?
		if (lastTurn >= i) {
			return;
		}
		else {
			lastTurn = i;
		}

		this.getRoomVisibility().resetTemporalVisibilities();

		setActionPoints(2);

		if (this.getActionPoints() > 0 && !isDead()) {
			doActions(i);
		}

	}

	protected abstract void sanction(int i);

	public boolean hurt(int value) {
		if (invulnerable) {
			return false;
		}
		Attribute h = this.getHealth();

		/*
		 * cheat for debugging
		 */
		if (!Objects.equals(this.getName(), "Terminator")) {
			h.modValue(value * (-1));
		}
		this.setStatus(this.getHealthLevel());
		return (h.getValue() <= 0);
	}

	protected abstract int getTypeSkill(Figure m);

	protected abstract int getAllArmor(Slap s);

	protected abstract void recover();

	protected int tumblings = 0;

	protected int getTumblings() {
		return tumblings;
	}

	protected void incTumblings(int k) {
		tumblings += k;
	}

	protected void decTumblings() {
		if (tumblings > 0) {
			tumblings -= 1;
		}
	}

	public abstract Attribute getDexterity();

	public int calcEludeValue() {
		double dex = this.getDexterity().getValue();
		if (this.tumblings > 0) {
			dex *= 0.2;
		}
		if (this.blinded > 0) {
			dex *= 0.6;
		}
		return (int) dex;
	}

	protected int filterArmor(int allArmor, int value) {
		if (value < allArmor) {
			return value / 2;
		}
		else if (value < allArmor * 2) {
			return (int) (value / 1.5);
		}
		else {

			int v = (int) (value - (1.2 * (allArmor)));
			if (v <= 0) {
				return 1;
			}
			else {
				return v;
			}
		}
	}

	protected abstract boolean getBlock(int dmg);

	protected int hit(Slap s) {

		int slap = s.getValueStandard();
		int slap_fire = s.getValueFire();
		int slap_lightning = s.getValueLightning();
		int slap_magic = s.getValueMagic();
		int slap_poison = s.getValue_poison();

		int all_armor = getAllArmor(s);

		int slapDmg = filterArmor(all_armor, slap);

		int fireDmg = (int) (slap_fire * getFireResistRate());
		int lightningDmg = (int) (slap_lightning * getLightningResistRate());
		int poisonDmg = (int) (slap_poison * getPoisonResistRate());
		int magicDmg = (int) (slap_magic * getMagicResistRate());

		int tumbles = getTumbling(s.getValue_tumble(),
				this.getAntiTumbleValue());
		if (tumbles > 0) {
			incTumblings(tumbles);
			getRoom().distributePercept(new TumblingPercept(this));
			// this.decFightAP(tumbles);
		}

		return slapDmg + fireDmg + lightningDmg + poisonDmg + magicDmg;
	}

	protected void spellBreak() {
		if (lastSpell != null) {
			int cost = lastSpell.getCost();
			int pay = cost / 3;
			this.payDust(pay);
			lastSpell.resetSpell();
			lastSpell = null;

			this.getRoom().distributePercept(new BreakSpellPercept(this));
		}
	}

	public SlapResult getSlap(Slap s) {

		Figure attacker = s.getActor();

		int eludeValue = calcEludeValue();

		int precision = s.getPrecision();

		int chance = (int) (precision - precision
				* ((((double) eludeValue) / 100)));

		boolean dies = false;
		SlapResult res = null;
		Percept p = null;
		int allDmg = 0;

		int random = (int) (Math.random() * 100);

		if (random < chance) {
			// we change look-dir towards opponent if slap does not miss
			lookDir = Position.getDirFromTo(getPositionInRoom(),
					attacker.getPositionInRoom());

			if (getBlock(s.getValueStandard())) {
				p = new ShieldBlockPercept(this);
				res = new SlapResult(allDmg, false, this, allDmg, s);
			}
			else {
				allDmg = hit(s);
				int healthBefore = getHealthLevel();
				if (allDmg < 0) {
					allDmg = 0;
				}

				spellBreak();

				dies = hurt(allDmg);

				int healthAfter = getHealthLevel();

				if (healthBefore != healthAfter) {
					sanction(healthBefore - healthAfter);
				}
				if (dies) {
					getKilled(allDmg);
					gameOver();
				}
				res = new SlapResult(allDmg, dies, this, allDmg, s);
				p = new HitPercept(attacker, this, res);
			}

		}
		else {
			res = new SlapResult(allDmg, false, this, allDmg, s);
			p = new MissPercept(attacker, this);
		}

		this.getRoom().distributePercept(p);
		if (dies) {
			this.getRoom().distributePercept(new DiePercept(this, this.getRoom()));
		}

		return res;
	}

	public void doActions(int i) {

		if (control != null && !this.isDead()) {

			Action a = retrieveMovementActionFromControl();
			int cnt = 0;
			while (!(a instanceof EndRoundAction) && cnt < 8) {

				if (this.getRoom().getDungeon().isGameOver()) {
					break;
				}
				cnt++;

				// TODO: unify action processing in fight and non-fight case
				ActionResult res = processAction(a);
				if (res.equals(ActionResult.DONE)) {
					EventManager.getInstance().fireEvent(new WorldChangedEvent());
				}
				control.actionProcessed(a, res);

				if (getRoom().getFight() != null) {
					break;
				}
				if (isDead()) {
					break;
				}
				a = retrieveMovementActionFromControl();

				if (this.getRoom().getDungeon().isGameOver()) {
					break;
				}
			}

			if (a instanceof EndRoundAction) {
				control.actionProcessed(a, new ActionResult(ActionResult.VALUE_DONE));
				int ap = this.getActionPoints();
				for (int j = 0; j < ap; j++) {
					this.getRoom().distributePercept(new WaitPercept(this));
				}
			}
		}
	}

	private boolean lame() {
		boolean isLamed = false;
		if (blinded > 0) {
			isLamed = true;
			blinded--;
		}
		if (tumblings > 0) {
			isLamed = true;
			tumblings--;
		}
		return isLamed;
	}

	public boolean fight() {

		incFightAP(1);
		boolean isLamed = lame();
		if (isLamed) {
			decFightAP(1);
		}
		if (getFightAP() > 0 && control != null && !this.dead) {
			ActionResult res = null;
			Action a = null;
			int cnt = 0;
			while (res == null || res.getValue() != ActionResult.VALUE_POSSIBLE) {
				cnt++;
				if (cnt > 10) {

					break;
				}
				if (this.getRoom().getDungeon().isGameOver()) {
					return false;
				}
				a = retrieveFightActionFromControl();
				res = processAction(a, false);
				if (res.getValue() == ActionResult.VALUE_IMPOSSIBLE) {
					if (control != null) {
						control.actionProcessed(a, res);
					}
				}

			}

			// TODO: unify action processing in fight and non-fight case
			res = processAction(a);
			if (res.equals(ActionResult.DONE)) {
				EventManager.getInstance().fireEvent(new WorldChangedEvent());
			}
			control.actionProcessed(a, res);

		}
		return false;
	}

	public void attack(Figure op) {
		// new look dir towards opponent
		lookDir = Position.getDirFromTo(getPositionInRoom(),
				op.getPositionInRoom());

		Slap u = slay(op);

		getRoom().distributePercept(new AttackPercept(this, op, u));
		SlapResult res = op.getSlap(u);

		this.receiveSlapResult(res);
	}

	public abstract int getKnowledgeBalance(Figure f);

	protected abstract boolean flee(RouteInstruction.Direction dir);

	public boolean canPayMoveActionPoint() {

		if (getActionPoints() > 0) {

			return true;
		}
		else {
			return false;
		}

	}

	public boolean canPayFightActionPoint() {
		if (getFightAP() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean canPayActionPoints(int k) {
		if (getRoom().fightRunning()) {
			return getFightAP() >= k;
		}
		else {
			return getActionPoints() >= k;
		}
	}

	public boolean isAbleToLockDoor() {
		return true;
	}

	public double getAntiFleeValue() {
		return 0;
	}

	public boolean isDead() {
		return dead;
	}

	private ActionResult handleEquipmentChangeAction(EquipmentChangeAction a,
													 boolean doIt) {
		if (this instanceof Hero) {
			if (doIt) {
				int type = a.getEqupipmentType();
				int index = a.getIndex();

				if (type == EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR) {
					this.getInventory().setArmorIndex(index);
				}
				if (type == EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON) {
					this.getInventory().setWeaponIndex(index);
				}
				if (type == EquipmentChangeAction.EQUIPMENT_TYPE_HELMET) {
					this.getInventory().setHelmetIndex(index);
				}
				if (type == EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD) {
					this.getInventory().setShieldIndex(index);
				}
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
		return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
				ActionResult.IMPOSSIBLE_REASON_OTHER);
	}

	private Item getItemForInfo(ItemInfo item) {
		List<Item> allItems = this.getAllItems();
		for (Item it : allItems) {
			ItemInfo itemInfo = ItemInfo.makeItemInfo(it, getRoomVisibility());
			if (itemInfo.equals(item)) {
				return it;
			}
		}
		return null;
	}

	private ActionResult handleLayDownItemAction(LayDownItemAction a,
												 boolean doIt) {
		if (a.getItem() != null) {
			ItemInfo itemInfo = a.getItem();
			Item item = getItemForInfo(itemInfo);
			if (doIt) {
				this.layDown(item);
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
		boolean equip = a.isEquipment();
		int index = a.getIndex();

		if (equip) {
			if (this instanceof Hero) {
				if (doIt) {
					Inventory inv = ((Hero) this).getInventory();
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR) {
						inv.layDown(inv.getArmor(inv.getArmorIndex()));
					}
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_HELMET) {
						inv.layDown(inv.getHelmet(inv.getHelmetIndex()));
					}
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD) {
						inv.layDown(inv.getShield(inv.getShieldIndex()));
					}
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON) {
						inv.layDown(inv.getWeapon(inv.getWeaponIndex()));
					}
					return ActionResult.DONE;
				}
				return ActionResult.POSSIBLE;
			}
			return ActionResult.OTHER;
		}
		else {
			if (doIt) {
				Item ding = this.getItems().get(index);
				layDown(ding);
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
	}

	protected abstract boolean layDown(Item it);

	protected void gameOver() {
		if (control instanceof JDGUI) {
			((JDGUI) control).gameOver();
		}
	}

	private ActionResult handleAttackAction(AttackAction a, boolean doIt) {
		int targetIndex = a.getTarget();
		Figure target = InfoUnitUnwrapper.getFighter(targetIndex);
		if (target == null) {
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_WRONGTARGET);
		}
		if (getRoom().fightRunning()) {
			if (canPayFightActionPoint()) {
				if (this.getRoom() == target.getRoom()) {
					if (doIt) {

						this.payFightActionPoint();
						attack(target);
						return ActionResult.DONE;
					}
					return ActionResult.POSSIBLE;
				}
			}
			return ActionResult.NOAP;
		}
		return ActionResult.MODE;
	}

	public void setCobwebbed(int k) {
		cobwebbed += k;
	}

	public boolean isCobwebbed() {
		return cobwebbed > 0;
	}

	protected boolean isPinnedToGround() {
		return cobwebbed > 0;
	}

	public ActionResult handleFleeAction(FleeAction a, boolean doIt) {
		if (getRoom().fightRunning()) {

			Room oldRoom = getRoom();
			if (canPayFightActionPoint()) {

				if (this.isPinnedToGround()) {
					return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
							ActionResult.IMPOSSIBLE_REASON_OTHER);
				}

				boolean flees;
				RouteInstruction.Direction dir = pos.getPossibleFleeDirection();
				if (dir != null && getRoom().getDoor(dir) != null
						&& getRoom().getDoor(dir).isPassable(this)) {
					if (doIt) {
						this.lookDir = dir.getValue();
						flees = flee(dir);
						this.payFightActionPoint();
						if (flees) {
							Percept p = new FleePercept(this, oldRoom, dir.getValue(), true);
							oldRoom.distributePercept(p);
							getRoom().distributePercept(p);
						}
						else {
							Percept p = new FleePercept(this, oldRoom, dir.getValue(),
									false);
							oldRoom.distributePercept(p);
						}
						return ActionResult.DONE;
					}
					return ActionResult.POSSIBLE;
				}
				return ActionResult.POSITION;
			}
			return ActionResult.NOAP;
		}
		return ActionResult.MODE;
	}

	protected abstract void setMakingSpecialAttack(boolean b);

	public void fightBegins(List<Figure> figures) {
		tellPercept(new FightBeginsPercept(FigureInfo.makeInfos(figures, this)));
		setFightAP(0);
		setMakingSpecialAttack(false);
	}

	public boolean fightEnded(List<Figure> figures) {
		if (this instanceof ConjuredMagicFigure) {
			((ConjuredMagicFigure) this).disappear();
			return true;
		}
		if (getControl() != null) {
			tellPercept(new FightEndedPercept(FigureInfo.makeInfos(figures, this)));
		}
		setActionPoints(0);
		setFightAP(0);
		if (lastSpell != null) {
			lastSpell.resetSpell();
			lastSpell = null;
		}
		return false;
	}

	protected ActionResult processAction(Action a) {
		return processAction(a, true);
	}

	public double getReadiness() {
		return this.actionPoints;
	}

	public boolean doorSmashes(Door d) {
		Position pos = this.getPos();
		Position posN1 = pos.getLast();
		Position posN2 = pos.getNext();
		if (posN1.getFigure() == null && posN2.getFigure() == null) {
			if (Math.random() > 0.5) {
				doStepTo(posN1.getIndex(), pos.getIndex());
				getDoorSmash(d, false);
				return true;
			}
			else {
				doStepTo(posN2.getIndex(), pos.getIndex());
				getDoorSmash(d, false);
				return true;
			}
		}
		else if (posN1.getFigure() == null) {
			doStepTo(posN1.getIndex(), pos.getIndex());
			getDoorSmash(d, false);
			return true;
		}
		else if (posN2.getFigure() == null) {
			doStepTo(posN2.getIndex(), pos.getIndex());
			getDoorSmash(d, false);
			return true;
		}
		else {
			getDoorSmash(d, true);
			return false;
		}

	}

	public void doorSmashesBack(Door d) {
		getDoorSmash(d, false);
	}

	private void getDoorSmash(Door d, boolean bigSmash) {
		int healthBasic = (int) this.getHealth().getBasic();
		int value = 0;
		if (bigSmash) {
			value = healthBasic / 4;
		}
		else {
			value = healthBasic / 8;
		}
		Percept p = new DoorSmashPercept(this, value);
		d.getRooms()[0].distributePercept(p);
		d.getRooms()[1].distributePercept(p);
		this.hurt(value);
	}

	public ActionResult checkAction(Action a) {
		return processAction(a, false);
	}

	private ActionResult handleExpCodeChangeAction(ExpCodeChangeAction a, boolean doIt) {
		if (this instanceof Hero) {
			if (doIt) {
				int index = a.getIndex();
				this.getCharacter().setExpCode(index);
				return ActionResult.DONE;
			}
			else {
				return ActionResult.POSSIBLE;
			}
		}
		return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
				ActionResult.IMPOSSIBLE_REASON_OTHER);
	}

	public abstract int getItemIndex(Item i);

	public abstract boolean canTakeItem(Item i);

	private ActionResult handleTakeItemFromChestAction(TakeItemFromChestAction a, boolean doIt) {
		boolean fight = this.getRoom().fightRunning();
		if (fight) {
			if (this.canPayFightActionPoint()) {
				ItemInfo info = a.getItem();
				Item item = getRoom().getChest().getItem(info);
				if (this.isAbleToTakeItemInFight(item)) {
					if (this.getRoom().getChest().hasItem(item)) {
						if(! (this.getPos().getIndex() == Position.Pos.NW.getValue())) {
							return ActionResult.POSITION;
						}
						if (this.canTakeItem(item)) {
							if (doIt) {
								this.takeItem(item);
								this.payFightActionPoint();
								return ActionResult.DONE;
							}
							else {
								return ActionResult.POSSIBLE;
							}
						}
					}
				}
				return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
						ActionResult.IMPOSSIBLE_REASON_OTHER);
			}
			else {
				return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
						ActionResult.IMPOSSIBLE_REASON_NOAP);
			}
		}
		else {
			ItemInfo info = a.getItem();
			Item item = getRoom().getChest().getItem(info);
			if (this.isAbleToTakeItem(item)) {
				if (this.getRoom().getChest().hasItem(item)) {
					if (this.canTakeItem(item)) {
						if (doIt) {
							this.takeItem(item);
							return ActionResult.DONE;
						}
						else {
							return ActionResult.POSSIBLE;
						}
					}
				}
			}
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_OTHER);

		}
	}

	private ActionResult handleTakeItemAction(
			TakeItemAction a, boolean doIt) {
		boolean fight = this.getRoom().fightRunning();
		if (fight) {
			if (this.canPayFightActionPoint()) {
				ItemInfo info = a.getItem();
				Item item = getRoom().getItem(info);
				if (this.isAbleToTakeItemInFight(item)) {
					if (this.getRoom().hasItem(item)) {
						if (this.canTakeItem(item)) {
							if (doIt) {
								this.takeItem(item);
								this.payFightActionPoint();
								this.getRoom().distributePercept(new TakePercept(this, item));
								return ActionResult.DONE;
							}
							else {
								return ActionResult.POSSIBLE;
							}
						}
					}
				}
				return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
						ActionResult.IMPOSSIBLE_REASON_OTHER);
			}
			else {
				return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
						ActionResult.IMPOSSIBLE_REASON_NOAP);
			}
		}
		else {
			ItemInfo info = a.getItem();
			Item item = getRoom().getItem(info);
			if (this.isAbleToTakeItem(item)) {
				if (this.getRoom().hasItem(item)) {
					if (this.canTakeItem(item)) {
						if (doIt) {
							this.takeItem(item);
							this.getRoom().distributePercept(new TakePercept(this, item));
							return ActionResult.DONE;
						}
						else {
							return ActionResult.POSSIBLE;
						}
					}
				}
			}
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_OTHER);
		}
	}

	private ActionResult handleMoveAction(MoveAction a, boolean doIt) {

		if (this.getActionPoints() < 1) {
			return ActionResult.NOAP;
		}
		int dir = a.getDirectionIndex();
		if (pos.getIndex() != getDirPos(dir)) {
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_WRONGPOSITION);
		}
		if (this.isPinnedToGround()) {
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_OTHER);
		}
		if (wayPassable(dir)) {
			if (doIt) {
				this.payMoveActionPoints(1);
				walk(a.getDirection());
				return ActionResult.DONE;
			}
			else {
				return ActionResult.POSSIBLE;
			}

		}
		else {
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_WRONGTARGET);
		}
	}

	private ActionResult handleLearnSpellAction(LearnSpellAction a, boolean doIt) {
		if (this instanceof Hero) {
			SpellInfo info = a.getSpell();
			List<AbstractSpell> spells = ((Hero) this).getCharacter().getSpellBuffer();
			for (Iterator<AbstractSpell> iter = spells.iterator(); iter.hasNext(); ) {
				AbstractSpell element = iter.next();
				SpellInfo tmp = new SpellInfo(element, this.getRoomVisibility());
				if (tmp.equals(info)) {
					if (((Hero) this).getCharacter().getSpellPoints() >= element
							.getLernCost()) {
						if (doIt) {
							((Hero) this).learnSpell(element);
							return ActionResult.DONE;
						}
						return ActionResult.POSSIBLE;
					}
				}
			}
		}
		return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
				ActionResult.IMPOSSIBLE_REASON_OTHER);
	}

	private ActionResult processAction(Action a, boolean doIt) {

		if (a == null) {
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_ACTIONNULL);
		}
		if (a instanceof DoNothingAction) {
			return ActionResult.POSSIBLE;
		}
		if (a instanceof EndRoundAction) {
			if (!getRoom().fightRunning()) {
				return ActionResult.POSSIBLE;
			}
		}
		if (!(a instanceof SpellAction)) {
			if (lastSpell != null) {
				lastSpell.resetSpell();
				lastSpell = null;
			}
		}
		if (a instanceof LockAction) {
			return handleLockAction((LockAction) a, doIt);
		}
		if (a instanceof UseChestAction) {
			return handleUseChestAction((UseChestAction) a, doIt);
		}
		if (a instanceof ShrineAction) {
			return handleShrineAction((ShrineAction) a, doIt);
		}
		if (a instanceof SpellAction) {
			return handleSpellAction((SpellAction) a, doIt);
		}
		if (a instanceof EquipmentChangeAction) {
			return handleEquipmentChangeAction((EquipmentChangeAction) a, doIt);
		}
		if (a instanceof AttackAction) {
			return handleAttackAction(((AttackAction) a), doIt);
		}
		if (a instanceof FleeAction) {
			return handleFleeAction((FleeAction) a, doIt);
		}
		if (a instanceof LearnSpellAction) {
			return handleLearnSpellAction((LearnSpellAction) a, doIt);
		}
		if (a instanceof LayDownItemAction) {
			return handleLayDownItemAction(((LayDownItemAction) a), doIt);
		}
		if (a instanceof ExpCodeChangeAction) {
			return handleExpCodeChangeAction((ExpCodeChangeAction) a, doIt);
		}
		if (a instanceof MoveAction) {
			return handleMoveAction((MoveAction) a, doIt);
		}
		if (a instanceof TakeItemAction) {
			return handleTakeItemAction((TakeItemAction) a, doIt);
		}
		if (a instanceof TakeItemFromChestAction) {
			return handleTakeItemFromChestAction((TakeItemFromChestAction) a, doIt);
		}
		if (a instanceof UseItemAction) {
			return handleUseItemAction(((UseItemAction) a), doIt);
		}
		if (a instanceof StepAction) {
			return handleStepAction(((StepAction) a), doIt);
		}

		if (a instanceof ScoutAction) {
			return handleScoutAction(((ScoutAction) a), doIt);
		}

		if (a instanceof SuicideAction) {
			return handleSuicideAction((SuicideAction) a, doIt);
		}

		if (a instanceof SkillUpAction) {
			return handleSkillUpAction((SkillUpAction) a);
		}

		return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
				ActionResult.IMPOSSIBLE_REASON_INVALIDACTION);
	}

	private ActionResult handleSuicideAction(SuicideAction a, boolean doIt) {
		this.getKilled(-1);
		return ActionResult.DONE;
	}

	private ActionResult handleSkillUpAction(SkillUpAction a) {
		if (this instanceof Hero) {
			Character c = this.getCharacter();
			if (c.hasSkillPoints()) {
				int key = a.getKey();
				Attribute att = this.getAttribute(key);
				if (a != null) {
					att.incBasic();

					c.decSkillPoints();
					this.tellPercept(new TextPercept(JDEnv.getResourceBundle()
							.getString("learnSkill")));
					return ActionResult.DONE;
				}
			}
		}
		return ActionResult.OTHER;
	}

	protected abstract boolean isAbleToUseShrine();

	protected abstract boolean isAbleToUseChest();

	private ActionResult handleUseItemAction(UseItemAction a, boolean doIt) {
		// TODO check auf use moeglich
		if (canPayActionPoints(1)) {
			ItemInfo info = a.getItem();
			Item it = this.getItem(info);
			if (it instanceof Usable) {
				Usable usable = (Usable) it;
				Object target = a.getTarget();
				if(((Usable) it).needsTarget() && target == null) {
					return ActionResult.NO_TARGET;
				}
				Object t = null;
				if (target instanceof InfoEntity) {
					t = this.getRoom().getDungeon().getUnwrapper().unwrappObject((InfoEntity) target);
				}
				if (usable.canBeUsedBy(this)) {
					if (doIt) {
						boolean used = ((Usable) it).use(this, t, a.isMeta());
						this.payActionPoint();
						Percept p = new UsePercept(this, (Usable) it);
						getRoom().distributePercept(p);

						if (used && (((Usable) it).usableOnce())) {
							removeItem(it);
						}
						if (used) {

							return ActionResult.DONE;
						}
						else {
							return ActionResult.FAILED;
						}
					}
					return ActionResult.POSSIBLE;
				}
				return ActionResult.OTHER;
			}
			return ActionResult.ITEM;
		}
		return ActionResult.NOAP;
	}

	private void doStepTo(int targetFieldindex, int oldPosIndex) {
		Position newPos = getRoom().getPositions()[targetFieldindex];
		this.lookDir = Position.getDirFromTo(pos.getIndex(), targetFieldindex);
		pos.figureLeaves();
		newPos.setFigure(this);
		Percept p = new StepPercept(this, oldPosIndex, targetFieldindex);
		getRoom().distributePercept(p);
		pos = newPos;
	}

	private ActionResult handleStepAction(StepAction a, boolean doIt) {
		int targetIndex = a.getTargetIndex();
		if (targetIndex == -1) {
			return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
					ActionResult.IMPOSSIBLE_REASON_WRONGTARGET);
		}
		if (getRoom().fightRunning()) {
			if (canPayFightActionPoint()) {

				if (this.isPinnedToGround()) {
					return new ActionResult(ActionResult.VALUE_IMPOSSIBLE,
							ActionResult.IMPOSSIBLE_REASON_OTHER);
				}

				Position newPos = getRoom().getPositions()[targetIndex];
				Figure neighbour;

				int oldPosIndex = pos.getIndex();
				if (pos.getDistanceTo(newPos) == 1) {
					neighbour = newPos.getFigure();
					if (neighbour == null) {
						if (doIt) {

							doStepTo(targetIndex, oldPosIndex);
							this.payFightActionPoint();
							return ActionResult.DONE;

						}
						return ActionResult.POSSIBLE;
					}
					else {
						return ActionResult.WRONG_TARGET;
					}
				}
				return ActionResult.POSITION;
			}
			else {
				return ActionResult.NOAP;
			}

		}
		else {
			if (this.getActionPoints() < 1) {
				return ActionResult.NOAP;
			}

			Position newPos = getRoom().getPositions()[targetIndex];
			if (newPos.getFigure() != null) {
				return ActionResult.WRONG_TARGET;
			}
			if (doIt) {

				this.lookDir = Position.getDirFromTo(pos.getIndex(),
						newPos.getIndex());
				pos.figureLeaves();
				newPos.setFigure(this);

				Percept p = new StepPercept(this, pos.getIndex(),
						newPos.getIndex());
				getRoom().distributePercept(p);
				pos = newPos;
				this.payMoveActionPoint();
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
	}

	private ActionResult handleScoutAction(ScoutAction action, boolean doIt) {
		if (this.getActionPoints() < 1) {
			return ActionResult.NOAP;
		}
		int dir = action.getDirection();
		if (pos.getIndex() != getDirPos(dir)) {
			return ActionResult.POSITION;
		}
		Room toScout = getRoom().getNeighbourRoom(dir);
		if (toScout == null) {
			return ActionResult.INVALID;
		}
		if (doIt) {
			lookDir = dir;
			ScoutResult result = scout(action);

			//getRoomVisibility().setVisibilityStatus(toScout.getNumber(),
			//		result.getVisibilityStatus());
			getRoomVisibility().getStatusObject(toScout.getNumber()).addVisibilityModifier(result);
			Percept p = new ScoutPercept(this, this.getRoom(), dir);
			getRoom().distributePercept(p);
			return ActionResult.DONE;
		}
		return ActionResult.POSSIBLE;
	}

	public static int getDirPos(int dir) {
		if (dir == Dir.NORTH) {
			return 1;
		}
		if (dir == Dir.EAST) {
			return 3;
		}
		if (dir == Dir.SOUTH) {
			return 5;
		}
		if (dir == Dir.WEST) {
			return 7;
		}
		return -1;
	}

	protected abstract ScoutResult scout(ScoutAction action);

	protected abstract int getKilled(int dmg);

	public abstract Attribute getHealth();

	public abstract void addModification(TimedAttributeModification mod);

	protected Dungeon actualDungeon;

	public Figure(Dungeon d) {
		if (d != null) {
			setActualDungeon(d);
		}
		constrHelp();
	}

	public Figure() {
		constrHelp();
	}

	public Figure(ControlUnit control) {
		this.control = control;
		constrHelp();
	}

	public Figure(AI ai) {
		this.ai = ai;
		constrHelp();
	}

	private void constrHelp() {
		this.figureID = figureID_counter;
		status = JDEnv.getResourceBundle().getString("status_strong");
		shortStatus = JDEnv.getResourceBundle()
				.getString("status_short_strong");

		figureID_counter++;
		addFigure(this);
	}

	public void setActualDungeon(Dungeon d) {
		actualDungeon = d;
		createVisibilityMap(d);
		if (ai != null) {
			// TODO: this ai field should nou be used
			FigureInfo info = FigureInfo.makeFigureInfo(this, getRoomVisibility());
			ai.setFigure(info);
			this.control = new FigureControl(info, ai);
		}
	}

	/**
	 * May not be called during game, but only between dungeons
	 * for level management.
	 */
	public void clearVisibilityMaps() {
		visibilities.clear();
	}

	public void createVisibilityMap(Dungeon d) {

		DungeonVisibilityMap roomVisibility = visibilities.get(d);

		if (roomVisibility == null) {
			roomVisibility = new DungeonVisibilityMap(d);
			roomVisibility.setFigure(this);
			RoomObservationStatus[][] stats = d
					.getNewRoomVisibilityMap(roomVisibility);
			roomVisibility.setMap(stats);
			visibilities.put(d, roomVisibility);

		}
	}

	public abstract int getTumbleValue(Figure f);

	public abstract Attribute getDust();

	protected abstract float getActualChanceToHit(Figure m);

	protected abstract int getActualRangeCapability(int range);

	protected float rangeFilter(float c, int dist) {

		if (dist == 1) {
			c = ((c * 100) / 100);
		}
		if (dist == 2) {
			c = ((c * 75) / 100);
		}
		if (dist == 3) {
			c = ((c * 50) / 100);
		}
		if (dist == 4) {
			c = 0;
		}
		return c;
	}

	public int getTumbling(int tumbleValue, int antiTumbleValue) {
		int erg = 0;
		if (tumbleValue >= 1.5 * antiTumbleValue) {
			if (Math.random() < 0.05) {
				return 2;
			}
			if (Math.random() < 0.8) {
				return 1;
			}
		}
		else if (tumbleValue >= 1 * (double) antiTumbleValue) {
			if (Math.random() < 0.5) {
				return 1;
			}
		}
		else if (tumbleValue >= 0.7 * antiTumbleValue) {
			if (Math.random() < 0.3) {
				return 1;
			}
		}
		else if (tumbleValue >= 0.4 * antiTumbleValue) {
			if (Math.random() < 0.2) {
				return 1;
			}
		}
		return erg;
	}

	public RoomObservationStatus getRoomObservationStatus(JDPoint p) {
		return getRoomVisibility().getStatusObject(p);
	}

	protected Slap slay(Figure m) {

		double tumbleFactor = 1;

		assert m != null;
		int dist = Position.getMinDistanceFromTo(this.getPositionInRoom(),
				m.getPositionInRoom());

		float weaponBaseChance = getActualChanceToHit(m);
		float rangedPrecision = rangeFilter(weaponBaseChance, dist);
		float precision = (float) (rangedPrecision * (((double) getActualRangeCapability(dist)) / 100));

		int value = getSlapStrength(m);

		if (this.raiding) {
			raiding = false;
			this.half_bonus = true;
			value += 4;
			tumbleFactor = 4;
			this.tellPercept(new TextPercept(JDEnv.getResourceBundle()
					.getString("raiding_attack")));
		}

		if (half_bonus) {
			half_bonus = false;
			value *= 1.5;
		}
		if (double_bonus) {
			double_bonus = false;
			value *= 2;
		}

		return new Slap(this, value,
				(int) (tumbleFactor * getTumbleValue(m)), (int) precision);
	}

	public abstract int getElude(Figure m);

	public abstract int getSlapStrength(Figure m);

	public Inventory getInventory() {
		return null;
	}

	public Character getCharacter() {
		return null;
	}

	protected int worth;

	@Override
	public Room getRoom() {
		return room;
	}

	public int getGolden_hit() {
		return golden_hit;
	}

	public Spellbook getSpellbook() {
		return spellbook;
	}

	public void setGolden_hit(int golden_hit) {
		this.golden_hit = golden_hit;
	}

	public boolean canPayDust(int amount) {
		return canPayDust((double) amount);
	}

	public boolean canPayDust(double amount) {
		return this.getDust().getValue() >= amount;
	}

	@Override
	public abstract boolean takeItem(Item i);

	public void setSpellbook(Spellbook spellbook) {
		this.spellbook = spellbook;
	}

	public int getEscape() {
		return escape;
	}

	public void setEscape(int escape) {
		this.escape = escape;
	}

	public abstract List<Item> getAllItems();

	public boolean hasItem(Item i) {
		List<Item> items = getAllItems();
		return items.contains(i);
	}

	public boolean isDouble_bonus() {
		return double_bonus;
	}

	public void setDouble_bonus(boolean double_bonus) {
		this.double_bonus = double_bonus;
	}

	public boolean isThief() {
		return thief;
	}

	public void setThief(boolean thief) {
		this.thief = thief;
	}

	public void incAP(int k) {
		incActionPoints(k);
	}

	private void incActionPoints(int k) {
		actionPoints += k;
	}

	public boolean isRaiding() {
		return raiding;
	}

	public void makeRaid(Figure f) {
		raiding = true;
		this.reflexReactionUnit.setRaidAttack(f);
	}

	public void setActionPoints(int actionPoints) {
		this.actionPoints = actionPoints;
	}

	public void setFightAP(int fightAP) {
		this.fightAP = fightAP;
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();

		p[1] = new Paragraph(getMclass());
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();

		p[2] = new Paragraph(getStatus());
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);

		p[3] = new Paragraph(getSpecialsText());
		p[3].setSize(10);
		p[3].setCentered();
		p[3].setColor(JDColor.black);

		return p;
	}

	protected String getSpecialsText() {
		String s = "";
		if (isCobwebbed()) {
			s += JDEnv.getString("spell_net_name");
		}
		return s;
	}

	public abstract String getMclass();

	public int getActionPoints() {
		return actionPoints;
	}

	public int getFightAP() {
		return fightAP;
	}

	public int getHealthLevel() {
		int i = getCharacter().getHealth().perCent();
		if (i > 70) {
			return 4;
		}
		else if (i > 45) {
			return 3;
		}
		else if (i > 25) {
			return 2;
		}
		else if (i > 10) {
			return 1;
		}
		else {
			return 0;
		}
	}

	protected void setStatus(int i) {
		if (i == 4) {
			status = " " + JDEnv.getResourceBundle().getString("status_strong");
			shortStatus = " "
					+ JDEnv.getResourceBundle()
					.getString("status_short_strong");
		}
		else if (i == 3) {
			status = " " + JDEnv.getResourceBundle().getString("status_struck");
			shortStatus = " "
					+ JDEnv.getResourceBundle()
					.getString("status_short_struck");
		}
		else if (i == 2) {
			status = " "
					+ JDEnv.getResourceBundle().getString("status_wounded");
			shortStatus = " "
					+ JDEnv.getResourceBundle().getString(
					"status_short_wounded");
		}
		else if (i == 1) {
			status = " " + JDEnv.getResourceBundle().getString("status_weak");
			shortStatus = " "
					+ JDEnv.getResourceBundle().getString("status_short_weak");
			;
		}
		else if (i == 0) {
			status = " " + JDEnv.getResourceBundle().getString("status_done");
			shortStatus = " "
					+ JDEnv.getResourceBundle().getString("status_short_done");
		}
		if (dead) {
			status = " " + JDEnv.getResourceBundle().getString("status_dead");
			shortStatus = " "
					+ JDEnv.getResourceBundle().getString("status_short_dead");
		}
	}

	public String getStatus() {
		return status;
	}

	public String getShortStatus() {
		return shortStatus;
	}

	public DungeonVisibilityMap getRoomVisibility() {
		return visibilities.get(this.getActualDungeon());
	}

	public int getPositionInRoom() {
		if (pos != null) {
			return pos.getIndex();
		}
		return 0;
	}

	public RouteInstruction.Direction getLookDirection() {

		if (lookDir < 1 || lookDir > 4) {
			return RouteInstruction.Direction.fromInteger(1);
		}
		else {
			return RouteInstruction.Direction.fromInteger(lookDir);
		}
	}

	public void setLookDir(int lookDir) {
		this.lookDir = lookDir;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public void setControl(ControlUnit control) {
		this.control = control;
	}

	@Deprecated
	public void setLocation(JDPoint location) {
		this.location = location;
		if (location == null) {
			room = null;
		}
		else {
			room = getRoom().getDungeon().getRoom(location);
		}
	}

	public void setLocation(Room r) {
		if (r == null) {
			location = null;
			return;
		}
		this.location = r.getLocation();
		if (location == null) {
			room = null;
		}
		else {
			room = r;
		}
	}

	@Override
	public JDPoint getLocation() {
		return location;
	}

	/**
	 * @return Returns the control.
	 */
	public ControlUnit getControl() {
		return control;
	}

	public boolean isInFight() {
		if (getRoom() != null) {
			return getRoom().fightRunning();
		}
		return false;
	}

	protected abstract boolean tryUnlockDoor(Door d, boolean doIt);

	// }

	private ActionResult handleLockAction(LockAction a, boolean doIt) {
		DoorInfo info = a.getDoor();
		if (this.isAbleToLockDoor()) {

			RouteInstruction.Direction direction = info.getDirection(getRoom().getLocation());
			if (direction == null) {
				return ActionResult.WRONG_TARGET;
			}


			Door d = getRoom().getDoor(direction);

			Position positionAtDoor = d.getPositionAtDoor(this.getRoom(), false);
			if(!this.getPos().equals(positionAtDoor)) {
				return ActionResult.POSITION;
			}

			boolean wasLocked = d.getLocked();
			boolean ok = this.tryUnlockDoor(d, doIt);

			if (ok) {

				if (wasLocked) {
					this.tellPercept(new InfoPercept(InfoPercept.UNLOCKED_DOOR));
				}
				else {
					this.tellPercept(new InfoPercept(InfoPercept.LOCKED_DOOR));
				}
				if (doIt) {
					return ActionResult.DONE;
				}
				return ActionResult.POSSIBLE;

			}
			return ActionResult.ITEM;

		}
		return ActionResult.OTHER;
	}

	protected Action retrieveMovementActionFromControl() {
		if (control instanceof JDGUI) {
			((JDGUI) control).onTurn();
		}
		return retrieveAction();
	}

	protected Action retrieveFightActionFromControl() {
		return retrieveAction();
	}

	private Action retrieveAction() {
		Action a;
		/*
		 * Check behavior override (e.g. by magic)
		 */
		Action reflex = reflexReactionUnit.getAction();
		if (reflex != null) {
			a = reflex;
		}
		else {

			a = control.getAction();
			/*
			 * we continuously ask the control for an action until one is
			 * specified
			 */
			// TODO: we need a security mechanism here:
			while (a == null) {
				try {
					Thread.currentThread().sleep(80);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(this.getClass()).error("Waiting for Action was interrupted: ", e);
					e.printStackTrace();
				}
				if (this.getRoom().getDungeon().isGameOver()) {

					break;
				}
				a = control.getAction();
				if (a == null && !(control instanceof JDGUI)) {
					// some messed up AI returning null;
					a = new DoNothingAction();
					break;
				}
			}
		}
		return a;
	}

	protected boolean wayPassable(int dir) {
		Room toGo = getRoom().getDungeon().getRoomAt(
				getRoom().getDungeon().getRoom(getLocation()),
				RouteInstruction.direction(dir));

		Door d = getRoom().getDungeon().getRoom(getLocation())
				.getConnectionTo(toGo);

		boolean passable = wayPassable(d, toGo);
		return passable;
	}

	public void move(Room r) {

		Room toLeave = getRoom();

		if (toLeave != null && toLeave != r) {

			toLeave.figureLeaves(this);
		}

		int dir;
		if (toLeave == null) {
			dir = Dir.NORTH;
		}
		else {
			dir = DungeonUtils.getNeighbourDirectionFromTo(toLeave, r)
					.getValue();
		}

		r.figureEnters(this, dir);

		lookInRoom();
	}

	public boolean walk(RouteInstruction.Direction dir) {
		boolean passable = wayPassable(dir.getValue());

		Room oldRoom = getRoom();
		Room toGo = getRoom().getDungeon().getRoomAt(
				getRoom().getDungeon().getRoom(getLocation()),
				dir);

		if (passable) {
			before = this.getRoom();
			Door door = before.getDoor(dir);
			Position destPos = door.getPositionAtDoor(toGo, true);
			Figure standing = destPos.getFigure();
			if (standing == null) {
				// the position is free so walk right in
				goThroughDoor(oldRoom, toGo);
				return true;

			}
			else {
				if (standing.getControl().isHostileTo(FigureInfo.makeFigureInfo(this, standing.getRoomVisibility()))
						|| this.getControl()
						.isHostileTo(FigureInfo.makeFigureInfo(standing, this.getRoomVisibility()))) {
					boolean raid = this.isRaiding();
					int thisStr = (int) this.getStrength().getValue();
					int otherStr = (int) standing.getStrength().getValue();
					double thisRan = Math.random() * thisStr;
					double otherRan = Math.random() * otherStr;
					if (thisRan > otherRan || raid) {
						// locale figure gets smashed
						boolean moves = standing.doorSmashes(this.getRoom()
								.getDoor(dir));
						if (moves) {
							goThroughDoor(oldRoom, toGo);
							return true;
						}
						else {
							return false;
						}
					}
					else {
						// intruder gets smashed and does not enter
						this.doorSmashesBack(this.getRoom().getDoor(dir));
						return false;
					}
				}
				else {
					// no need for violence, just friends
					goThroughDoor(oldRoom, toGo);
					return true;
				}
			}

		}
		else {
			return false;
		}

	}

	protected abstract void lookInRoom();

	private void goThroughDoor(Room oldRoom, Room toGo) {

		// actual movement
		move(toGo);

		Percept p = new MovePercept(this, oldRoom, toGo);
		toGo.distributePercept(p);
		oldRoom.distributePercept(p);
		lookInRoom();

	}

	public Dungeon getActualDungeon() {
		return actualDungeon;
	}

	public void incBlinded(int k) {
		blinded += k;
	}

	protected void fireModifications() {
		List modifications = getModificationList();
		if (!modifications.isEmpty()) {

			TimedAttributeModification m = null;// ((TimedModification)
			Iterator iter = modifications.iterator();
			while (iter.hasNext()) {
				m = (TimedAttributeModification) iter.next();
				if (m.hasFired()) {
					iter.remove();
				}
				else {
					m.newRound();
				}
			}
		}
	}

	private Spell unWrappSpellInfo(SpellInfo a) {
		if (a == null) {
			return null;
		}
		List<Spell> l = this.getSpellbook().getSpells();
		for (Iterator<Spell> iter = l.iterator(); iter.hasNext(); ) {
			Spell element = iter.next();
			if (element != null
					&& a.equals(new SpellInfo(element, this.getRoomVisibility()))) {
				return element;
			}

		}
		return null;
	}

	private ActionResult handleSpellAction(SpellAction a, boolean doIt) {

		Spell sp = unWrappSpellInfo(a.getSpell());
		if (sp != null) {

			if (lastSpell != null && lastSpell != sp) {
				lastSpell.resetSpell();
				lastSpell = null;
			}

			if (canPayActionPoints(1)) {
				Object target = null;
				if (a.getTarget() != null) {
					target = getRoom().getDungeon().getUnwrapper().unwrappObject((InfoEntity) a.getTarget());
				}
				lastSpell = sp;
				return sp.fire(this, target, doIt);

			}
			return ActionResult.NOAP;
		}
		return ActionResult.INVALID;
	}

	public AbstractReflexBehavior getReflexReactionUnit() {
		return reflexReactionUnit;
	}

	public Figure getLastFrighener() {
		return lastFrighener;
	}

	private ActionResult handleUseChestAction(UseChestAction a, boolean doIt) {
		boolean right = false;
		if (a.isMeta()) {
			right = true;
		}
		if(! (this.getPos().getIndex() == Position.Pos.NW.getValue())) {
			return ActionResult.POSITION;
		}
		Chest s = this.getRoom().getChest();
		if (s != null && this.isAbleToUseChest()) {
			if (doIt) {
				s.clicked(this, right);
				return ActionResult.DONE;
			}
			else {
				return ActionResult.POSSIBLE;
			}
		}
		return ActionResult.OTHER;
	}

	private ActionResult handleShrineAction(ShrineAction a, boolean doIt) {
		Shrine s = this.getRoom().getShrine();
		if (s != null && this.isAbleToUseShrine()) {
			if(! (this.getPos().getIndex() == Position.Pos.NE.getValue())) {
				return ActionResult.POSITION;
			}

			if (doIt) {

				if (s.canBeUsedBy(this)) {
					s.use(this, a.getTarget(), a.isMeta());
					return ActionResult.DONE;
				}
				return ActionResult.OTHER;

			}
			else {
				return ActionResult.POSSIBLE;
			}
		}
		return ActionResult.OTHER;
	}

	public Spell getLastSpell() {
		return lastSpell;
	}

	public AbstractAI getSpecifiedAI() {
		return specifiedAI;
	}

	public void setSpecifiedAI(AbstractAI specifiedAI) {
		this.specifiedAI = specifiedAI;
	}

	public abstract int getWorth();

	public void setAI(AI AI) {
		this.ai = AI;
	}
}