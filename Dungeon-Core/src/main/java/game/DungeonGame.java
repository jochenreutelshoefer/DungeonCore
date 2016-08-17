package game;

import dungeon.util.InfoUnitUnwrapper;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.ExitUsedEvent;
import event.GameOverEvent;
import item.Item;
import item.ItemInfo;
import item.ItemPool;
import item.interfaces.ItemOwner;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import shrine.LevelExit;
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
public class DungeonGame implements Runnable, EventListener {

	private boolean started = false;

	private int round = 1;

	private boolean gameOver = false;
	private boolean heroLeft = false;

	private Dungeon derDungeon;

	private final Map<Figure, JDGUI> guiFigures = new HashMap<Figure, JDGUI>();

	@Deprecated
	public int DungeonSizeX = 30;

	@Deprecated
	public int DungeonSizeY = 40;

	private boolean imortal = false;

	private final long startTime;

	private TestTracker tracker;

	/*
	public DungeonSession getSession() {
		return session;
	}
*/
	/*
	public void setSession(DungeonSession session) {
		this.session = session;
	}
	*/

	//private DungeonSession session;
	
	private static DungeonGame instance = null;
	
	public static DungeonGame getInstance() {
		if(instance == null) {
			instance = new DungeonGame();
		}
		return instance;
	}

	public static DungeonGame createNewInstance() {
		instance = new DungeonGame();
		return instance;
	}



	private DungeonGame() {
		startTime = System.currentTimeMillis();
		tracker = new TestTracker();
	}

	public DungeonVisibilityMap getVisMapFor(FigureInfo f) {
		return InfoUnitUnwrapper.getFighter(f.getFighterID()).getRoomVisibility();
	}

	public Dungeon getDungeon() {
		return derDungeon;
	}

	public int getRound() {
		return round;
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
		while (gameOver == false && heroLeft == false) {
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
				EventManager.getInstance().fireEvent(new GameOverEvent());
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



	public void init(Dungeon d) {
		ItemPool.setGame(this);


		Figure.createVisibilityMaps(derDungeon);
		Figure.setMonsterControls();

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
		Hero held = (Hero) InfoUnitUnwrapper.getFighter(h.getFighterID());
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

	public TestTracker getTracker() {
		return tracker;
	}

	public void setTestTracker(TestTracker tracker) {
		this.tracker = tracker;
	}


	public void putGuiFigure(Hero held, JDGUI gui) {
		guiFigures.put(held, gui);

	}

	public void setDungeon(Dungeon d) {
		this.derDungeon = d;

	}

	public void setStarted(boolean startet) {
		this.started = startet;
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		List<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>();
		events.add(ExitUsedEvent.class);
		events.add(GameOverEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if(event instanceof ExitUsedEvent) {
			this.heroLeft = true;
		}
		if(event instanceof GameOverEvent) {
			this.gameOver = true;
		}
	}
}