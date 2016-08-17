package figure.hero;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ai.DefaultHeroReflexBehavior;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import fight.Frightening;
import fight.Slap;
import fight.SlapResult;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.Spellbook;
import figure.action.Action;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.monster.Monster;
import figure.percept.DiePercept;
import figure.percept.InfoPercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;
import figure.percept.ShieldBlockPercept;
import figure.percept.TextPercept;
import game.DungeonGame;
import game.InfoEntity;
import game.InfoProvider;
import item.Bunch;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.equipment.Shield;
import item.equipment.weapon.Weapon;
import item.interfaces.ItemOwner;
import item.quest.LuziasBall;
import shrine.Shrine;
import spell.Spell;

/**
 * Der Held des Spiels. Es kann nur einen geben. Besteht hauptsaechlich aus
 * einem Character, einem Inventar mit den Gegenstaenden und einem Spellbook mit
 * den Zauberspruechen die er kann. Gesteuert wird der Held durch ein
 * HeroControl-Objekt.
 * 
 * @see Inventory
 * @see Character
 * @see Spellbook
 */
// a00d058
public class Hero extends Figure implements InfoProvider {

	public boolean agentStarted = false;

	int timeDelay = 2000;
	public static int hero_index_counter = -1;

	public final static int HEROCODE_WARRIOR = 1;

	public final static int HEROCODE_HUNTER = 2;

	public final static int HEROCODE_DRUID = 3;

	public final static int HEROCODE_MAGE = 4;


	private int sanctions = 0;

	protected double fire_resist_rate = 1.0;

	protected double lightning_resist_rate = 1.0;

	protected double magic_resist_rate = 1.0;

	protected double poison_resist_rate = 1.0;

	public void setCharacter(Character c) {
		this.c = c;
	}

	private Character c;

	private final Inventory inv;

	private final Bunch bund = new Bunch();

	private final JDPoint oldLocation = new JDPoint(0, 0);

	private int kills = 0;

	private final int HeroCode;

	private final Zodiac sign;

	private boolean isDead = false;

	boolean paused = false;

	@Override
	public Attribute getDexterity() {
		return c.getDexterity();
	}

	@Deprecated
	public Map<String, String> getHighScoreData(String playerName,
			String comment,
			boolean reg, boolean liga, HeroInfo h) {
		return new HashMap();
	}

	@Override
	public Attribute getStrength() {
		return c.getStrength();
	}

	@Override
	public int filterFrightening(Frightening fr) {
		return fr.getValue();
	}
	
	@Override
	public int getWorth() {
		return 1000 + (500 * level);
	}
	
	@Override
	protected boolean getBlock(int dmg) {
		Shield shield = this.inv.getShield1();
		if(shield != null) {
			int shieldValue = shield.getBlockValue();
			
			int total = (int)((shieldValue + c.getDexterity_Value()/3) * (((double)100+c.getDexterity_Value())/100));
			int random = (int)(Math.random() * 100);
			if(random > 100 - total) {
				
				shield.madeBlock(dmg);
				return true;
			}
				
			
		}
		
		return false;
		
	}

	@Override
	public boolean canTakeItem(Item i) {
		return inv.canTakeItem(i);
	}

	@Override
	public int getLevel() {
		return c.getLevel();
	}

	@Override
	public void setMakingSpecialAttack(boolean b) {
	}

	@Override
	public int getKnowledgeBalance(Figure f) {
		if (f instanceof Monster) {
			return c.getKnowledgeBalance((Monster) f);
		} else {
			return this.getLevel() - f.getLevel();
		}
	}

	private Room[][] memory;

	@Override
	public Item getItemNumber(int i) {
		return inv.getItemNumber(i);
	}

	@Override
	public int getItemIndex(Item it) {
		List<Item> items = inv.getItems();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == it) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void addModification(TimedAttributeModification mod) {
		c.addModification(mod);
	}

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new HeroInfo(this, map);
	}

	public Hero(String name, int heroCode, Zodiac sign, int HealthVal,
			int StrengthVal, int DexterityVal, int PsychoVal, int Axe,
			int Lance, int Sword, int Club, int Wolfknife, int nature,
			int creature, int undead, int scout, int dust, double dustReg,
			int ch) {
		super();
		this.HeroCode = heroCode;
		this.reflexReactionUnit = new DefaultHeroReflexBehavior(this);
		this.sign = sign;
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

	}

	public Hero(int heroCode, Zodiac sign, Character character) {
		super();
		this.HeroCode = heroCode;
		this.reflexReactionUnit = new DefaultHeroReflexBehavior(this);
		this.sign = sign;
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
		c = character;
		this.bravery = new Attribute(Attribute.BRAVE, brave);
		inv = new Inventory(3, 3, 3, 3, this);
		inv.addItem(bund);
	}

	@Override
	public String getMclass() {
		return "Held";
	}

	@Override
	public boolean removeItem(Item i) {
		getInventory().removeItem(i);
		return true;
	}

	@Override
	public Character getCharacter() {
		return c;
	}

	@Override
	public Action getForcedFightAction() {
		return null;
	}

	@Override
	public Action getForcedMovementAction() {
		return null;
	}

	@Override
	public List<Item> getItems() {
		return Collections.unmodifiableList(getInventory().getItems());
	}

	@Override
	public double getFireResistRate() {
		return fire_resist_rate;
	}

	@Override
	public double getLightningResistRate() {
		return lightning_resist_rate;
	}

	@Override
	public double getMagicResistRate() {
		return magic_resist_rate;
	}

	@Override
	public double getPoisonResistRate() {
		return poison_resist_rate;
	}

	public void setMemory() {
		memory = new Room[getRoom().getDungeon().getSize().getX()][getRoom().getDungeon()
				.getSize().getY()];

	}

	@Override
	public boolean giveAwayItem(Item i, ItemOwner o) {
		return getInventory().giveAwayItem(i, o);
	}


	@Override
	public Attribute getHealth() {
		return getCharacter().getHealth();
	}

	public int getKills() {
		return kills;
	}

	public void incKills() {
		kills++;
	}

	public int getHeroCode() {
		return HeroCode;
	}

	@Override
	public Attribute getPsycho() {
		return getCharacter().getPsycho();
	}

	public Zodiac getSign() {
		return sign;
	}

	@Override
	public boolean isAbleToTakeItemInFight(Item it) {
		return this.isThief() && isAbleToTakeItem(it);
	}

	@Override
	public boolean isAbleToUseShrine() {
		return true;
	}

	@Override
	public boolean isAbleToUseChest() {
		return true;
	}

	@Override
	public String getName() {
		return getCharacter().getName();
	}

	@Override
	public int getActualRangeCapability(int range) {
		Weapon w = this.getInventory().getWeapon1();
		if (w != null) {
			return w.getRangeCapability(range);
		}
		return 20;
	}

	@Override
	public int getTypeSkill(Figure m) {
		return c.giveType_skill(m);
	}

	@Override
	public int getAllArmor(Slap s) {
		return inv.getAllArmor(s);
	}

	@Override
	public Attribute getAttribute(int name) {
		return this.c.getAttribute(name);
	}

	@Override
	protected void sanction(int i) {
		for (int j = 0; j < i; j++) {
			getBrave().modValue((-1));
			getCharacter().getPsycho().modValue((-1));
			getCharacter().getDexterity().modValue((-1));
			getCharacter().getStrength().modValue((-1));
			sanctions++;
		}

	}

	@Override
	public void recover() {
		double healing = getActionPoints()
				* getCharacter().getAttribute(Attribute.HEALTHREG).getValue();
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

	@Override
	public void heal(int k) {
		heal((double) k);
	}

	@Override
	public int getAntiTumbleValue() {
		int str = c.getStrength_Value();
		return (str - 2) * 8;

	}

	@Override
	public boolean layDown(Item it) {
		inv.layDown(it);
		return true;
	}

	@Override
	public void heal(double value) {
		Attribute healthAttr = c.getHealth();

		if (healthAttr.getValue() + value <= healthAttr.getBasic()) {

			healthAttr.modValue(value);
		} else {
			healthAttr.setValue((healthAttr.getBasic()));
		}
	}

	@Override
	public boolean takeItem(Item i) {
		return getInventory().takeItem(i, i.getOwner());
	}

	@Override
	public int getTumbleValue(Figure f) {
		if (f instanceof Monster) {
			int i = c.getKnowledgeBalance(((Monster) f));
			Weapon weap1 = inv.getWeapon1();
			double str = c.getStrength().getValue() - 5;
			int wv = 0;
			double strMod = 0.7 + (str / 10);
			if (weap1 != null) {
				wv = weap1.getTumbleBasic();
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

	@Override
	public void recDust(double value) {
		getCharacter().getDust().modValue(value);
		if (getCharacter().getDust().getValue() >= getCharacter().getDust()
				.getBasic())
			getCharacter().getDust().setValue(
					getCharacter().getDust().getBasic());
	}

	@Override
	public boolean isAbleToUseItem() {
		return true;
	}

	@Override
	public boolean isAbleToTakeItem(Item it) {
		if(it instanceof DustItem) {
			if(getDust().getValue() == getDust().getBasic()) {
				return false;
			}
		}
		return true;

	}

	@Override
	public int getElude(Figure m) {
		int k;
		if (getInventory().getShield1() == null) {
			k = 0;
		} else {
			int shield = (getInventory().getShield1().getBlockValue());

			double skillValue = shield
					* (((double) getCharacter()
							.getKnowledgeBalance((Monster) m)) + 1)
					+ (((((double) (getCharacter()
							.getKnowledgeBalance((Monster) m)) + 1) * ((getCharacter()
							.getDexterity().getValue()) - 3)) / 50));
			if (skillValue < 0) {
				skillValue = 0;
			}
			k = (int) Math.round(shield + skillValue);
		}
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

			// 0 soll nur sein wenn er gel�hmt, versteinert etc ist
			// weil bei null immer getroffen
			ret += 2;

			return ret;
		}
	}

	@Override
	public Attribute getDust() {
		return getCharacter().getDust();
	}

	@Override
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

				float k = weap.getChanceToHit() * (1 + ((float) (getCharacter()
						.getDexterity().getValue()
						+ getCharacter().giveType_skill(m) + getCharacter()
						.giveWeapon_skill()) / 40));
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

	@Override
	public List<Item> getAllItems() {
		return getInventory().getAllItems();
	}

	@Override
	public int getSlapStrength(Figure m) {
		int weapon_blank;
		Weapon weap = getInventory().getWeapon1();
		if (weap == null) {
			weapon_blank = 0;
		} else {
			if (getCharacter().giveWeapon_skill() == 0) {
				weapon_blank = getInventory().getWeapon1().getDamage(-1);
			} else {
				weapon_blank = (getInventory().getWeapon1().getDamage(0));
			}
		}
		String s = "Schlagstaerke: Waffe: " + weapon_blank;

		float Strength_Weapon_Type_Modifier = 1 + ((float) (2
				* getCharacter().getStrength().getValue()
				+ (10 * getCharacter().giveType_skill(m)) + (10 * getCharacter()
				.giveWeapon_skill())) / 100);
		s += " - Staerke-Koennen-Wissen-Faktor: "
				+ Strength_Weapon_Type_Modifier;
		float Weapon_on_Type_Modifier = getInventory()
				.give_Weapon_on_Type_Modifier(m);
		s += " - Waffe-Faktor:" + Weapon_on_Type_Modifier;
		float faktor = Strength_Weapon_Type_Modifier * Weapon_on_Type_Modifier;
		s += " - Gesamtfaktor: " + faktor;
		int x = (int) (weapon_blank * faktor);
		s += " - Erfahrungspunkte geben: " + x;

		return x;
	}

	@Override
	public void receiveSlapResult(SlapResult r) {
		getCharacter().giveExp(r);
		if (r.isLethal()) {
			kills++;
		}
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	public JDPoint getOldLocation() {
		return oldLocation;
	}

	protected int calcScout(Room r) {
		int level = 0;
		int handycap = 120;
		for (int i = 0; i < getCharacter().getScout().getValue(); i++) { // scout
			// gibt
			// den
			// Wert
			// wie
			// oft
			// geprüft
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

	protected int calcFleeResult(int difficulty) {

		if (escape > 0) {

			escape = 0;
			return -1;
		}

		int i1 = (int) (Math.random() * difficulty);

		// NEUE FLUCHT - abhängig nur von Geschicklichkeit

		int dex = (int) getCharacter().getDexterity().getValue() - 2;
		int erg = 4;
		if (i1 < dex) {
			erg = 0;
		} else if (i1 < dex + 1) {
			erg = 1;
		}

		return erg;
	}


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
		int k = getCharacter().getKnowledge(m)
				+ (int) getCharacter().getThreat().getValue();
		// game.newStatement("Drohungswert hier: " + k, 4);

		int l = 0;
		for (int i = 0; i < k; i++) {
			int o = threatHelp(handycap);
			if (o > l) {
				l = o;
			}
		}

		if (getCharacter().getKnowledge(m) == 0) {
			l = 0;
		}
		return l;
	}

	public void roundEnd() {
	}

	@Override
	public boolean payDust(int k) {

		if (c.getDust().getValue() >= k) {
			c.getDust().modValue(k * -1);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<TimedAttributeModification> getModificationList() {
		return c.getModifications();
	}

	@Override
	public JDPoint getLocation() {
		return location;
	}

	public LuziasBall getLuziasBall() {
		return inv.getLuziasBall();
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		return inv.getItemInfos(map);
	}

	@Override
	public int getKilled(int damage) {
		if(isBonusLive()) {
			this.setBonusLive(false);
			this.getHealth().setValue(1);
			this.getRoom().figureLeaves(this);
			Room respawnRoom2 = this.getRespawnRoom();
			respawnRoom2.figureEnters(this,0);
			respawnRoom2.distributePercept(new InfoPercept(InfoPercept.RESPAWN));
			return 1;
		}
		
		Percept p = new DiePercept(this, getRoom(), damage);
		getRoom().distributePercept(p);
		
		isDead = true;
		return 0;
	}

	@Override
	public boolean isDead() {
		return isDead;
	}

	public Room[][] getMemory() {
		return memory;
	}

	public RoomInfo getMemory(int i, int j) {
		return RoomInfo.makeRoomInfo(memory[i][j], this.getRoomVisibility());
	}

	public void resetMemory(int x, int y) {
		memory = new Room[x][y];
	}

	@Override
	public Item getItem(ItemInfo it) {
		return inv.getItem(it);
	}

	@Override
	public boolean tryUnlockDoor(Door d, boolean doIt) {
		List<Item> items = this.getItems();
		boolean b = false;
		for (int i = 0; i < items.size(); i++) {
			Item a = (items.get(i));
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

	@Override
	public boolean hasKey(String k) {
		List<Item> items = getItems();
		for (int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
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

	private List threatings = new LinkedList();

	@Override
	protected void lookInRoom() {
		this.getRoom().getDungeon().getRoom(getLocation()).setVisited(DungeonGame.getInstance().getRound());
		if (this.getRoom().getDungeon().getRoom(getLocation()).getShrine() != null) {
			Shrine s = this.getRoom().getDungeon().getRoom(getLocation()).getShrine();
			this.tellPercept(new TextPercept(s.getStory()));

		}
		if (!this.getRoom().getDungeon().getRoomNr(getLocation().getX(),
				getLocation().getY()).getItems().isEmpty()) {
			this.tellPercept(new InfoPercept(InfoPercept.FOUND_ITEM));
		}
	}

	@Override
	protected boolean flee(int dir) {
		RouteInstruction.Direction fleeDirection = null;
		if (dir == RouteInstruction.NORTH) {
			fleeDirection = RouteInstruction.Direction.North;
		} else if (dir == RouteInstruction.SOUTH) {
			fleeDirection = RouteInstruction.Direction.South;
		} else if (dir == RouteInstruction.WEST) {
			fleeDirection = RouteInstruction.Direction.West;
		} else if (dir == RouteInstruction.EAST) {
			fleeDirection = RouteInstruction.Direction.East;
		} else {
			// System.out.println("FLEEDIRECTION ERRRROR!");
		}

		Room toGo = this.getRoom().getDungeon().getRoomAt(getRoom(), RouteInstruction.direction(dir));
		Door d = this.getRoom().getDungeon().getRoom(getLocation()).getConnectionTo(toGo);

		if ((toGo == null) || (d == null) || (!d.isPassable(this))) {

			return false;
		}

		if (true) {

			double diff = getRoom().calcFleeDiff();
			RouteInstruction.Direction back = getLocation().relativeTo(getOldLocation());
			List<RouteInstruction.Direction> axeX = new LinkedList<>();
			axeX.add(RouteInstruction.Direction.East);
			axeX.add(RouteInstruction.Direction.West);
			List axeY = new LinkedList();
			axeY.add(RouteInstruction.Direction.North);
			axeY.add(RouteInstruction.Direction.South);
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

	@Override
	public boolean scout(int dir) {

		Room loc = getRoom();
		Room toScout = loc.getNeighbourRoom(dir);
		scoutedRooms.add(toScout);
		Door d = loc.getConnectionTo(toScout);

		boolean scoutable = ((d != null));
		String s = new String();
		if ((scoutable)) {
			List<Figure> monsters = toScout.getRoomFigures();
			s += ("Du horchst und schaust duch die Ritzen in der Tür -" + "\n");

			decActionPoints(1);
			int scoutlevel = calcScout(toScout);
			if (scoutlevel == 0) {

				if (monsters.isEmpty()) {
					s += "...aber Du kannst leider nichts rauskriegen";
				} else {
					for (int i = 0; i < monsters.size(); i++) {
						s += "...aber Du kannst leider nichts rauskriegen";
						if (Math.random() < 0.4) {
							toScout.distributePercept(new ScoutPercept(this, getRoom(), dir));
						}
					}

				}
			} else if (scoutlevel == 1) {

				if (monsters.isEmpty()) {
					s += "...aber Du kannst leider nichts rauskriegen";
				} else {
					for (int i = 0; i < monsters.size(); i++) {
						if (Math.random() < 0.4) {
							toScout.distributePercept(new ScoutPercept(this, getRoom(), dir));
						}
					}
					s += (" und ...Du wirst entdeckt!");
				}
			} else if (scoutlevel == 2) {

				if (monsters.isEmpty()) {
					s += "Du siehst dort niemanden.";
					roomVisibility.setVisibilityStatus(toScout.getNumber(),
							RoomObservationStatus.VISIBILITY_FIGURES);

				} else {
					toScout.distributePercept(new ScoutPercept(this, getRoom(), dir));
					s += ("Du hast ihn entdeckt..." + "\n");
					s += ("...und er Dich jetzt auch!");
				}
			} else if (scoutlevel == 3) {
				s += ("... aber Du kannst leider nichts rauskriegen");
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_FOUND);

			} else if (scoutlevel == 4) {
				if (monsters.isEmpty()) {
					if (((int) (Math.random() * 100)) < 80) {
						s += ("hmm.. Du glaubst da ist jemand.");
					} else {
						s += ("hmm..Du glaubst da ist niemand");
					}
				} else {
					if (((int) (Math.random() * 100)) < 20) {
						s += ("hmm..Du glaubst da ist jemand.");
					} else {
						s += ("hmm..Du glaubst da ist niemand");
					}
				}
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_SHRINE);

			} else if (scoutlevel == 5) {
				if (toScout.getRoomFigures().isEmpty()) {
					s += ("In diesem Raum ist niemand!");
					roomVisibility.setVisibilityStatus(toScout.getNumber(),
							RoomObservationStatus.VISIBILITY_FIGURES);

				} else {
					s += ("In diesem Raum ist jemand!");
				}
			} else if (scoutlevel >= 6) {
				if (!toScout.getRoomFigures().isEmpty()) {
					s += ("Du kannst den Gegner genau beobachten." + "\n");
					for (int i = 0; i < monsters.size(); i++) {
						s += (monsters.get(i) + "\n");
					}

				} else {
					s += ("Wenn da jemand gewesen wäre hättest Du es raugekriegt.");
				}
				roomVisibility.setVisibilityStatus(toScout.getNumber(),
						RoomObservationStatus.VISIBILITY_FIGURES);

			} else if (scoutlevel >= 8) {
				if (!toScout.getRoomFigures().isEmpty()) {
					s += ("Gegner lokalisiert!\n");
				} else {
					s += "Der Raum ist frei\n";
				}
				if (!toScout.getItems().isEmpty()) {
					s += "...und sieh mal was da rumliegt!\n";
				} else {
					s += "keine Gegenstände dort\n";
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