package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeon.Dungeon;
import dungeon.util.DungeonUtils;
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
		// for testing at stage 2
		//if(completedDungeons.isEmpty()) return 1;

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
	public void initDungeon(DungeonFactory dungeon) {
		lastSelectedDungeonFactory = dungeon;
		dungeonGame = DungeonGame.getInstance();

		derDungeon = dungeon.createDungeon();
		Hero currentHero = getCurrentHero();
		// we make a copy of this hero for potential restart after death
		this.currentHero.clearVisibilityMaps();
		heroBackup = (Hero) DeepCopyUtil.copy(this.currentHero);

		// we need to clear the keys from the last dungeon (as they would work in the new one also)
		currentHero.getInventory().clearKeys();
		heroInfo = DungeonUtils.enterDungeon(currentHero, derDungeon,
				dungeon.getHeroEntryPoint());

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
			derDungeon = null;
		}
	}

}
