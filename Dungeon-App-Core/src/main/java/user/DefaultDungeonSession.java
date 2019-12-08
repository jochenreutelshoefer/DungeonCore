package user;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.util.DungeonUtils;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.hero.HeroUtil;
import figure.hero.Profession;
import figure.hero.Zodiac;
import game.DungeonGame;
import game.JDEnv;
import game.JDGUI;
import level.DefaultDungeonManager;
import level.DungeonFactory;
import level.DungeonManager;
import shrine.LevelExit;
import spell.Spell;
import util.DeepCopyUtil;

import de.jdungeon.user.Session;
import de.jdungeon.user.User;

/**
 * A Session contains the data of the state of the player,
 * that is which hero he is playing, and which dungeons
 * he has already solved.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.03.16.
 */
public class DefaultDungeonSession implements Session, DungeonSession {

	private Hero currentHero;

	private Hero heroBackup;
	private final User user;
	private int heroType;
	private DungeonGame dungeonGame;
	private DungeonFactory lastCompletedDungeonFactory;
	private DungeonFactory lastSelectedDungeonFactory;
	private Dungeon derDungeon;
	private HeroInfo heroInfo;

	private final DungeonManager manager;

	private JDGUI gui;
	private final Map<DungeonFactory, Integer> completedDungeons = new HashMap<>();
	private Thread dungeonThread;

	public DefaultDungeonSession(User user) {
		this.user = user;
		manager = new DefaultDungeonManager();
		JDEnv.init();
	}

	@Override
	public JDGUI getGUI() {
		return this.gui;
	}

	@Override
	public DungeonManager getDungeonManager() {
		return manager;
	}

	@Override
	public int getCurrentStage() {
		/*
		if(completedDungeons.isEmpty()) {
			DungeonFactory level0 = getDungeonManager().getDungeonOptions(0).get(0);
			DungeonFactory level1 = getDungeonManager().getDungeonOptions(1).get(0);
			lastCompletedDungeonFactory = level1;
			lastSelectedDungeonFactory = level1;
			this.completedDungeons.put(level0, 1);
			this.completedDungeons.put(level1, 2);
			return 2;
		}
		*/

		return completedDungeons.size();
	}

	@Override
	public DungeonFactory getLastCompleted() {
		return lastCompletedDungeonFactory;
	}

	@Override
	public User getUser() {
		return user;
	}

	/**
	 * Starts the game world's thread triggering the game rounds
	 *
	 * @param gui graphical user interface controlling this session
	 */
	public void startGame(JDGUI gui) {
		this.gui = gui;
		dungeonGame.putGuiFigure(currentHero, gui);
		dungeonThread = new Thread(dungeonGame);
		dungeonThread.start();
	}



	/**
	 * Returns the hero object that is currently played by this session
	 * @return
	 */
	@Override
	public Hero getCurrentHero() {
		if(currentHero == null) {
			currentHero = HeroUtil.getBasicHero(heroType, "Gisbert", Zodiac.Aquarius,
					Profession.Lumberjack);
		}
		return currentHero;
	}

	@Override
	public DungeonCompletionScore getAchievedScoreFor(DungeonFactory factory) {
		if(completedDungeons.containsKey(factory)) {
			Integer rounds = completedDungeons.get(factory);
			return new DungeonCompletionScore(rounds, calcScore(factory, rounds));
		}
		return null;
	}

	private int calcScore(DungeonFactory dungeonFactory, int rounds) {
		if(dungeonFactory.getRoundScoringBaseValue() > rounds) {
			return dungeonFactory.getRoundScoringBaseValue() - rounds;
		}
		return 0;
	}

	@Override
	public int getTotalScore() {
		Set<DungeonFactory> dungeons = completedDungeons.keySet();
		int score = 0;
		for (DungeonFactory dungeon : dungeons) {
			score += calcScore(dungeon, completedDungeons.get(dungeon));
		}
		return score;
	}

	@Override
	public void learnSkill(Spell spell) {
		currentHero.getSpellbook().addSpell(spell);
		if(heroBackup != null) {
			heroBackup.getSpellbook().addSpell(spell);
		}
	}

	/**
	 * Tells the players decision for the type of hero to be played
	 *
	 *
	 * @param heroType category of hero that has been selected
	 */
	public void setSelectedHeroType(int heroType) {
		this.heroType = heroType;
	}

	@Override
	public void initDungeon(DungeonFactory dungeonFactory) {
		lastSelectedDungeonFactory = dungeonFactory;
		dungeonGame = DungeonGame.getInstance();

		derDungeon = dungeonFactory.createDungeon();
		Hero currentHero = getCurrentHero();

		// we need to clear the keys from the last dungeonFactory (as they would work in the new one also)
		currentHero.getInventory().clearKeys();

		// we make a copy of this hero for potential restart after death
		this.currentHero.clearVisibilityMaps();
		//makeHeroBackup();


		heroInfo = enterDungeon(currentHero, derDungeon,
				dungeonFactory.getHeroEntryPoint());

	}

	/**
	 * The hero enters the dungeon at the specified point. Creates a FigureInfo
	 * object with a corresponding VisibilityMap.
	 *
	 * @param h
	 * @param derDungeon
	 * @return
	 */
	public static HeroInfo enterDungeon(Hero h, Dungeon derDungeon, JDPoint p) {
		DungeonGame dungeonGame = DungeonGame.getInstance();

		Figure.addFigure(h);
		dungeonGame.setDungeon(derDungeon);
		h.setActualDungeon(derDungeon);
		derDungeon.getRoomNr(p.getX(), p.getY()).figureEnters(h, 0);
		DungeonVisibilityMap heroVisMap = h.getRoomVisibility();

		return new HeroInfo(h, heroVisMap);
	}

	@Override
	public void revertHero() {
		currentHero = (Hero) DeepCopyUtil.copy(heroBackup);
	}

	public HeroInfo getHeroInfo() {
		return heroInfo;
	}

	/**
	 * Returns the dungeon currently played by this session
	 *
	 * @return
	 */
	public Dungeon getCurrentDungeon() {
		return derDungeon;
	}

	@Override
	public void notifyExit(LevelExit exit, Figure figure) {
		if(figure.equals(currentHero)) {
			Dungeon currentDungeon = getCurrentDungeon();
			if(! completedDungeons.containsKey(lastSelectedDungeonFactory) && currentDungeon != null) {
				completedDungeons.put(lastSelectedDungeonFactory, dungeonGame.getRound());
				lastCompletedDungeonFactory = lastSelectedDungeonFactory;
			}
			makeHeroBackup();
			derDungeon = null;
		}
	}

	private void makeHeroBackup() {
		// we need to clear all references to dungeon objects to allow a deep copy of the hero object via serialization
		Figure f = getCurrentHero();
		f.setLocation((Room)null);
		f.setPos(null);
		f.setActualDungeon(null);
		f.clearVisibilityMaps();
		f.setRespawnRoom(null);
		f.setControl(null);
		heroBackup = (Hero) DeepCopyUtil.copy(this.currentHero);
	}
}
