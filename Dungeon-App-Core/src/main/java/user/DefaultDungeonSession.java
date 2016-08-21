package user;

import java.util.ArrayList;
import java.util.List;

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
	private User user;
	private int heroType;
	private DungeonGame dungeonGame;
	private Dungeon derDungeon;
	private HeroInfo heroInfo;

	private DungeonManager manager;

	private JDGUI gui;
	private List<Dungeon> completedDungeons = new ArrayList<Dungeon>();
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

	public DungeonManager getDungeonManager() {
		return manager;
	}

	@Override
	public int getCurrentStage() {
		return completedDungeons.size();
	}

	@Override
	public User getUser() {
		return user;
	}

	/**
	 * Starts the game world's thread triggering the game rounds
	 *
	 * @param gui
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
	public Hero getCurrentHero() {
		if(currentHero == null) {
			currentHero = HeroUtil.getBasicHero(heroType, "Gisbert", Zodiac.Aquarius,
					Profession.Lumberjack);
		}
		return currentHero;
	}

	/**
	 * Tells the players decision for the type of hero to be played
	 *
	 *
	 * @param heroType
	 */
	public void setSelectedHeroType(int heroType) {
		this.heroType = heroType;
	}

	public void initDungeon(DungeonFactory dungeon) {
		dungeonGame = DungeonGame.getInstance();

		derDungeon = dungeon.createDungeon();
		heroInfo = DungeonUtils.enterDungeon(getCurrentHero(), derDungeon,
				dungeon.getHeroEntryPoint());
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
			if(! completedDungeons.contains(currentDungeon) && currentDungeon != null) {
				completedDungeons.add(currentDungeon);
			}
			derDungeon = null;
		}
	}

	public JDGUI getGui() {
		return gui;
	}
}
