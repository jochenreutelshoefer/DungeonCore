package figure.hero;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ai.DefaultHeroReflexBehavior;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import fight.Slap;
import fight.SlapResult;
import figure.APAgility;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigurePresentation;
import figure.RoomObservationStatus;
import figure.Spellbook;
import figure.action.ScoutAction;
import figure.action.ScoutResult;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.monster.Monster;
import figure.percept.DiePercept;
import figure.percept.InfoPercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.InfoProvider;
import item.Bunch;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.equipment.Shield;
import item.equipment.weapon.Weapon;
import log.Log;
import location.Location;

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
public class Hero extends Figure implements InfoProvider, Serializable {

	public boolean agentStarted = false;

	int timeDelay = 2000;
	public static int hero_index_counter = -1;


	@Deprecated
	public final static int HEROCODE_WARRIOR = 1;

	@Deprecated
	public final static int HEROCODE_HUNTER = 2;

	@Deprecated
	public final static int HEROCODE_DRUID = 3;

	@Deprecated
	public final static int HEROCODE_MAGE = 4;


	public enum HeroCategory {
		Warrior(1),
		Thief(2),
		Druid(3),
		Mage(4);

		public int getCode() {
			return code;
		}

		public static HeroCategory fromValue(int value) {
			if(value == Warrior.code) {
				return Warrior;
			}
			if(value == Thief.code) {
				return Thief;
			}
			if(value == Druid.code) {
				return Druid;
			}
			if(value == Mage.code) {
				return Mage;
			}
			Log.severe("Invalid hero category code: "+value);
			return null;
		}
		private final int code;
		HeroCategory(int value) {
			this.code = value;
		}
	}


	private int sanctions = 0;

	public void setCharacter(Character c) {
		this.c = c;
	}

	private Character c;

	private final Inventory inv;

	private final Bunch bund = new Bunch();

	private final JDPoint oldLocation = new JDPoint(0, 0);

	private final HeroCategory heroCategory;

	private final Zodiac sign;

	boolean paused = false;

	@Override
	public Attribute getDexterity() {
		return c.getDexterity();
	}

	@Deprecated
	public Map<String, String> getHighScoreData(String playerName,
			String comment,
			boolean reg, boolean liga, HeroInfo h) {
		return new HashMap<>();
	}

	@Override
	public Attribute getStrength() {
		return c.getStrength();
	}

	@Override
	public int getWorth() {
		return 1000 + (500 * level);
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		if(this.heroCategory == HeroCategory.Warrior) {
			return FigurePresentation.Warrior;
		}
		if(this.heroCategory == HeroCategory.Druid) {
			return FigurePresentation.Druid;
		}
		if(this.heroCategory == HeroCategory.Thief) {
			return FigurePresentation.Sailor;
		}
		if(this.heroCategory == HeroCategory.Mage) {
			return FigurePresentation.Mage;
		}
		return null;
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
	public void setMakingSpecialAttack(boolean b) {
	}

	@Override
	public int getKnowledgeBalance(Figure f) {
		if (f instanceof Monster) {
			return c.getKnowledgeBalance((Monster) f);
		} else {
			// concept is off
			return 0;
		}
	}

	private Room[][] memory;


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
		this.heroCategory = HeroCategory.fromValue(heroCode);
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
				PsychoVal,  dust, dustReg, this);
		
		inv = new Inventory(3, 3, 3, 3, this);
		bund.setOwner(this);
		inv.addItem(bund);


	}

	public Hero(int heroCode, Zodiac sign, Character character) {
		super();
		this.heroCategory = HeroCategory.fromValue(heroCode);
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
	public List<Item> getItems() {
		return Collections.unmodifiableList(getInventory().getItems());
	}

	@Override
	public double getFireResistRate() {
		return 1;
	}

	@Override
	public double getLightningResistRate() {
		return 1;
	}

	@Override
	public double getMagicResistRate() {
		return 1;
	}

	@Override
	public double getPoisonResistRate() {
		return 1;
	}

	@Override
	public Attribute getHealth() {
		return getCharacter().getHealth();
	}

	public int getHeroCode() {
		return this.heroCategory.getCode();
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
		return isAbleToTakeItem(it);
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
	public int getAllArmor(Slap s) {
		return inv.getAllArmor(s);
	}

	@Override
	public Attribute getAttribute(Attribute.Type name) {
		if(name == Attribute.Type.Oxygen) {
			return this.getAgility().getOxygen();
		}
		return this.c.getAttribute(name);
	}

	@Override
	protected void sanction(int i) {
		for (int j = 0; j < i; j++) {
			getCharacter().getPsycho().modValue((-1));
			getCharacter().getDexterity().modValue((-1));
			getCharacter().getStrength().modValue((-1));
			sanctions++;
		}

	}

	@Override
	public void recover(int round) {
		double healing = (getActionPoints() + 1)
 				* getCharacter().getAttribute(Attribute.Type.HealthReg).getValue();
		heal(healing, round);

		recDust(getCharacter().getDustReg().getValue());

		if (sanctions > 0) {
			getCharacter().getPsycho().modValue(1);
			getCharacter().getDexterity().modValue(1);
			getCharacter().getStrength().modValue(1);
			sanctions--;
		}
	}

	@Override
	public boolean layDown(Item it) {
		inv.layDown(it);
		return true;
	}


	@Override
	public boolean takeItem(Item i) {
		return i != null && getInventory().takeItem(i, i.getOwner());
	}

	@Override
	public boolean hasItem(Item i) {
		return getInventory().hasItem(i);
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
	public boolean isAbleToTakeItem(Item it) {
		if(it instanceof DustItem) {
			if(getDust().getValue() == getDust().getBasic()) {
				return false;
			}
		}
		return true;

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

		float Strength_Weapon_Type_Modifier = 1 + ((float) (2
				* getCharacter().getStrength().getValue()
				+ (10 * getCharacter().giveType_skill(m)) + (10 * getCharacter()
				.giveWeapon_skill())) / 100);
		float Weapon_on_Type_Modifier = getInventory()
				.give_Weapon_on_Type_Modifier(m);
		float faktor = Strength_Weapon_Type_Modifier * Weapon_on_Type_Modifier;

		int x = (int) (weapon_blank * faktor);

		return x;
	}

	@Override
	public void receiveSlapResult(SlapResult r) {
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
		for (int i = 0; i < getCharacter().getPsycho().getValue(); i++) { // scout
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

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		return inv.getItemInfos(map);
	}

	@Override
	public int getKilled(int damage) {
		if(isBonusLive()) {
			this.setBonusLive(false);
			this.getHealth().setValue(1);
			this.getRoom().figureDies(this);
			Room respawnRoom2 = this.getRespawnRoom();
			respawnRoom2.figureEnters(this,0, -1);
			respawnRoom2.distributePercept(new InfoPercept(InfoPercept.RESPAWN, -1));
			return 1;
		}
		else {
			dieAndLeave();
		}
		
		Percept p = new DiePercept(this, getRoom(), damage);
		getRoom().distributePercept(p);
		
		return 0;
	}


	public Room[][] getMemory() {
		return memory;
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

	@Override
	protected void lookInRoom(int round) {
		if (this.getRoom().getDungeon().getRoom(getLocation()).getShrine() != null) {
			Location s = this.getRoom().getDungeon().getRoom(getLocation()).getShrine();
			this.tellPercept(new TextPercept(s.getStory(), round));

		}
		if (!this.getRoom().getDungeon().getRoomNr(getLocation().getX(),
				getLocation().getY()).getItems().isEmpty()) {
			this.tellPercept(new InfoPercept(InfoPercept.FOUND_ITEM, round));
		}
	}

	@Override
	protected APAgility createAgility() {
		return new APAgility();
	}

	@Override
	public boolean flee(RouteInstruction.Direction fleeDirection, int round) {
		Room toGo = this.getRoom().getDungeon().getRoomAt(getRoom(), fleeDirection);
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
			if(getRoom().getDoor(fleeDirection).hasEscapeRoute(this)) {
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

				move(toGo, round);


				lookInRoom(round);
				getRoom().checkFight(this, round);
				return true;
			} else {

				return false;
			}
		} else {

			return false;
		}
	}

	@Override
	public ScoutResult scout(ScoutAction action, int round) {
		int dir = action.getDirection();
		Room loc = getRoom();
		Room toScout = loc.getNeighbourRoom(dir);
		Door d = loc.getConnectionTo(toScout);

		boolean scoutable = ((d != null));
		String s = new String();
		int visStatusResult = -1;
		if ((scoutable)) {
			List<Figure> monsters = toScout.getRoomFigures();
			s += ("Du horchst und schaust duch die Ritzen in der Tür -" + "\n");

			decActionPoints(action, round);
			int scoutlevel = calcScout(toScout);
			if (scoutlevel == 0) {
				if (monsters.isEmpty()) {
					s += "...aber Du kannst leider nichts rauskriegen";
				} else {
					for (int i = 0; i < monsters.size(); i++) {
						s += "...aber Du kannst leider nichts rauskriegen";
						if (Math.random() < 0.4) {
							toScout.distributePercept(new ScoutPercept(this, getRoom(), dir, round));
						}
					}

				}
				visStatusResult = RoomObservationStatus.VISIBILITY_UNDISCOVERED;
			} else if (scoutlevel == 1) {

				if (monsters.isEmpty()) {
					s += "...aber Du kannst leider nichts rauskriegen";
				} else {
					for (int i = 0; i < monsters.size(); i++) {
						if (Math.random() < 0.4) {
							toScout.distributePercept(new ScoutPercept(this, getRoom(), dir, round));
						}
					}
					s += (" und ...Du wirst entdeckt!");
				}
			} else if (scoutlevel == 2 || scoutlevel == 3|| scoutlevel == 4) {
				s += ("... aber Du kannst leider nichts rauskriegen");
				visStatusResult = RoomObservationStatus.VISIBILITY_FOUND;

			} else if (scoutlevel >= 5) {
				if (!toScout.getRoomFigures().isEmpty()) {
					s += ("Du kannst den Gegner genau beobachten." + "\n");
					for (int i = 0; i < monsters.size(); i++) {
						s += (monsters.get(i) + "\n");
					}

				} else {
					s += ("Wenn da jemand gewesen wäre hättest Du es rausgekriegt.");
				}
				visStatusResult = RoomObservationStatus.VISIBILITY_FIGURES;

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
				visStatusResult = RoomObservationStatus.VISIBILITY_ITEMS;

			} else {
				s += ("Scout Level Error: " + scoutlevel);
			}
		} else {
			s += ("Das funktioniert so jetzt gerade nicht...");
		}
		tellPercept(new TextPercept(s, round));
		return new ScoutResult(this, visStatusResult);
	}

}