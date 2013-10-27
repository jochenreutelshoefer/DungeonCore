package game;

import item.Item;
import item.ItemInfo;
import item.ItemPool;
import item.interfaces.ItemOwner;
import item.quest.Rune;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import spell.Spell;
import spell.TimedSpellInstance;
import test.TestTracker;
import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.generate.DungeonGenerationFailedException;
import dungeon.generate.SectorDungeonFiller1;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;


/**
 * Die Klasse Game verwaltet den ganzen Spielablauf. Sie verwaltet Dungeon, Held
 * und GUI. Abwechselnd bekommt der Held und dann wieder der Dungeon eine
 * Spielrunde. Sie enthaelt die Methoden, die aus den GUI-Befehlen entsprechende
 * Aktionen (Klasse Action) erstellen und auffuehren.
 * 
 */
public class DungeonGame implements Runnable {

	public boolean started = false;

	int dungeon_nr = 0; // hier geh�rt 0 hin

	private int round = 1;

	private boolean gameOver = false;

	public Dungeon derDungeon;

	private final Map<Figure, JDGUI> guiFigures = new HashMap<Figure, JDGUI>();

	public int DungeonSizeX = 30;

	public int DungeonSizeY = 40;

	private final String[] word = { "JAVA", "CLUB", "BEAR" };

	private Rune[] runen;

	private final boolean visibility = false;

	private boolean imortal = false;

	private final List<Item> uniqueItems = new LinkedList<Item>();

	private final long startTime;

	TestTracker tracker;
	
	private static DungeonGame instance = null;
	
	public static DungeonGame getInstance() {
		if(instance == null) {
			instance = new DungeonGame();
		}
		return instance;
	}

	private DungeonGame() {
		startTime = System.currentTimeMillis();
		tracker = new TestTracker();
	}

	public DungeonVisibilityMap getVisMapFor(FigureInfo f) {
		return getFighter(f.getFighterID()).getRoomVisibility();
	}

	public Dungeon getDungeon() {
		return derDungeon;
	}

	public int getRound() {
		return round;
	}

	public Room getRoom(RoomInfo r) {
		JDPoint p = r.getLocation();
		return derDungeon.getRoom(p);
	}

	public Object unwrappObject(InfoEntity o) {
		if (o instanceof ItemInfo) {
			InfoEntity info = ((ItemInfo) o).getOwner();
			ItemOwner owner = (ItemOwner) unwrappObject(info);
			return owner.getItem((ItemInfo) o);
		}

		if (o instanceof RoomInfo) {
			return getRoom((RoomInfo) o);
		}
		if (o instanceof DoorInfo) {
			return getDoor((DoorInfo) o);
		}
		if (o instanceof FigureInfo) {
			return this.getFighter(((FigureInfo) o).getFighterID());
		}
		if (o instanceof PositionInRoomInfo) {
			int index = ((PositionInRoomInfo)o).getIndex();
			JDPoint roomPoint  = ((PositionInRoomInfo)o).getLocation();
			Room room = this.derDungeon.getRoom(roomPoint);
			return room.getPositions()[index];
		}

		System.out.println("failed unwrappObject!");
		return null;
	}

	public Door getDoor(DoorInfo d) {
		RoomInfo room = d.getRooms()[0];
		Room r = getRoom(room);
		Door[] doors = r.getDoors();
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				DoorInfo info = new DoorInfo(doors[i], null);
				if (info.equals(d)) {
					return doors[i];
				}
			}
		}

		return null;
	}

	public Figure getFighter(int index) {
		return Figure.getFigure(index);
	}

	public void makeUniqueItems() {
		// System.out.println("MAKING UNIQUE ITEMS!");
		uniqueItems.add(ItemPool.ebertsklinge());
		uniqueItems.add(ItemPool.zauberersWeisheit());
		uniqueItems.add(ItemPool.glasSchild());
		uniqueItems.add(ItemPool.atlethenhaut());
	}

	public Item selectItem(int value) {
		LinkedList<Item> possible = new LinkedList<Item>();
		for (int i = 0; i < uniqueItems.size(); i++) {
			int a = (int) (value * 0.8);
			int b = (int) (value * 1.2);
			if ((value > a) && (value < b)) {
				possible.add(uniqueItems.get(i));
			}
		}
		// //System.out.println("selecting unique Item");
		if (possible.size() == 0) {
			if (uniqueItems.size() == 0) {
				// System.out.println("Liste leer !");
				return null;
			}
			int k = (int) (Math.random() * uniqueItems.size());
			Item it = uniqueItems.get(k);
			uniqueItems.remove(it);
			return it;
		} else {

			int k = (int) (Math.random() * possible.size());
			Item it = possible.get(k);
			uniqueItems.remove(it);
			return it;
		}
	}

	private Rune[] runeCreater(int k) {
		runen = new Rune[word[k].length()];
		for (int i = 0; i < word[k].length(); i++) {
			runen[i] = new Rune(word[k].charAt(i));
		}
		return runen;
	}

	private void checkGuiFigures() {
		Collection<Figure> l = guiFigures.keySet();
		List<Figure> toDelete = new LinkedList<Figure>();
		for (Iterator<Figure> iter = l.iterator(); iter.hasNext();) {
			Figure element = iter.next();
			if (element.isDead()) {
				toDelete.add(element);
			}

		}

		for (Iterator<Figure> iter = toDelete.iterator(); iter.hasNext();) {
			Object element = iter.next();
			guiFigures.remove(element);
		}
	}

	private void tickGuis() {
		Collection<Figure> l = guiFigures.keySet();

		for (Iterator<Figure> iter = l.iterator(); iter.hasNext();) {
			Figure element = iter.next();
			JDGUI gui = guiFigures.get(element);
			gui.gameRoundEnded();

		}
	}

	@Override
	public void run() {

		while (gameOver == false) {

			checkGuiFigures();
			if (guiFigures.size() == 0) {
				break;
			}

			worldTurn();
			tickGuis();
		}

	}

	public void controlLeft(Figure f) {
		if (guiFigures.containsKey(f)) {
			guiFigures.remove(f);
			if (guiFigures.size() == 0) {
				System.out.println("Kein Gui mehr aktiv --> spiel beendet");
				gameOver = true;
			}
		}
	}

	private void spellsTurn() {
		List<TimedSpellInstance> spells = Spell.timedSpells;
		for (int i = 0; i < spells.size(); i++) {
			((Turnable) spells.get(i)).turn(this.round);
		}
	}

	private final List<Turnable> turnableItems = new LinkedList<Turnable>();

	public void addTurnableItem(Item i) {
		if (i instanceof Turnable) {
			turnableItems.add((Turnable)i);
		}
	}

	private void itemsTurn() {
		for (Iterator<Turnable> iter = turnableItems.iterator(); iter.hasNext();) {
			Turnable element = iter.next();
			element.turn(round);

		}
	}

	public void worldTurn() {
		derDungeon.turn(round);
		spellsTurn();
		itemsTurn();
		round++;
	}

	public void fillDungeon(Dungeon d) throws DungeonGenerationFailedException {

		if (this.derDungeon == null) {
			derDungeon = d;
		}

		// held.resetMemory(DungeonSizeX, DungeonSizeY);
		makeUniqueItems();
		Rune[] runen = runeCreater(dungeon_nr);
		ItemPool.setGame(this);

		// wegen manchmal Fehler in generierung
		SectorDungeonFiller1 filler = new SectorDungeonFiller1(derDungeon,
				getValueForDungeon(dungeon_nr + 1), this, runen, dungeon_nr + 1);

		filler.fillDungeon();
		Figure.createVisibilityMaps(derDungeon);
		Figure.setMonsterControls();
		dungeon_nr += 1;

	}

	private int getValueForDungeon(int level) {
		if (level == 1) {
			return 40000;
		} else if (level == 2) {
			return 200000;
		} else if (level == 3) {
			return 1000000;
		} else {

			return -1;
		}
	}

	public void setImortal(boolean b) {
		imortal = b;
	}

	private final boolean sendHighscore = true;


	public Map<String,String> getHighScoreString(String playerName, String comment,
			boolean reg, boolean liga, HeroInfo h) {

		if (!sendHighscore || playerName.equals("godmode1")
				|| JDEnv.isBeginnerGame()) {
			return null;
		}

		String codeString = "";

		Map<String, String> map = new HashMap<String, String>();
		Hero held = (Hero) this.getFighter(h.getFighterID());
		// String s = new String();
		// String zeilenTrenn = new String("$$$");
		// String trennZeichen = new String("���");

		map.put("player_name", playerName);
		codeString += playerName;
		// s += "&player_name="+playerName;

		map.put("hero_name", h.getName());
		// s += "&hero_name="+h.getName();
		int points = held.getCharacter().getTotalExp();
		Integer expInt = new Integer(points);
		map.put("exp", Integer.toString(expInt));
		codeString += "" + expInt;
		// s += "&exp="+held.getCharacter().getTotalExp();

		map.put("level", Integer.toString(new Integer(held.getCharacter().getLevel())));
		// s += "&level="+held.getCharacter().getLevel();

		map.put("round", Integer.toString(new Integer(getRound())));
		codeString += "" + getRound();
		// s += "&round="+getRound();

		map.put("kills", Integer.toString(new Integer(held.getKills())));
		// s += "&kills"+held.getKills();

		String type = new String();
		int k = held.getHeroCode();
		if (k == Hero.HEROCODE_DRUID) {
			type = "Druide";
		}
		if (k == Hero.HEROCODE_HUNTER) {
			type = "Dieb";
		}
		if (k == Hero.HEROCODE_MAGE) {
			type = "Magier";
		}
		if (k == Hero.HEROCODE_WARRIOR) {
			type = "Krieger";
		}

		map.put("type", type);
		// s += "&type="+type;

		map.put("comment", comment);
		// s += "&comment="+comment;

		String deadString = "0";
		if (held.isDead()) {
			deadString = "1";
		}
		map.put("dead", deadString);
		codeString += deadString;
		// s += "&dead="+deadString;

		map.put("start_time", Long.toString(startTime));
		codeString += Long.toString(startTime);
		// s += "&start_time="+Long.toString(startTime);
		long endTime = System.currentTimeMillis();
		map.put("end_time", Long.toString(endTime));
		codeString += Long.toString(endTime);
		// s += "&end_time="+Long.toString(System.currentTimeMillis());

		String regString = "0";
		if (reg) {
			regString = "1";
		}
		codeString += regString;
		map.put("reg", regString);
		// s += "&reg="+regString;

		String ligaString = "0";
		if (liga) {
			ligaString = "1";
		}
		codeString += ligaString;
		map.put("liga", ligaString);
		// s += "&liga="+ligaString;

		codeString += "sperrholz";
		byte[] digest = null;
		try {
			byte[] theTextToDigestAsBytes = codeString.getBytes("UTF-8");

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(theTextToDigestAsBytes);
			digest = md.digest();
		} catch (Exception e) {
			System.out.println("encoding highscore-string failed");
			System.out.println(e.toString());
		}
		// String code = new String(digest);

		String hex = "";
		for (int i = 0; i < digest.length; i++) {
			byte b = digest[i];
			hex += Integer.toHexString(b & 0xff);
		}

		map.put("chk", "MD5" + hex);

		return map;
	}

	public boolean getImortal() {
		return imortal;
	}

	public boolean getVisibility() {
		return visibility;
	}

	public TestTracker getTracker() {
		return tracker;
	}

	public void setTestTracker(TestTracker tracker) {
		this.tracker = tracker;
	}

	/**
	 * @return Returns the gameOver.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	public void endGame() {
		gameOver = true;
	}

	public void putGuiFigure(Hero held, JDGUI gui) {
		guiFigures.put(held, gui);

	}

	public void setDungeon(Dungeon d) {
		this.derDungeon = d;

	}
}