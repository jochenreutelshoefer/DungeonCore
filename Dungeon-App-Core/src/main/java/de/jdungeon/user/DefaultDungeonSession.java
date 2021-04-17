package de.jdungeon.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.Room;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.ExitUsedEvent;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.hero.HeroUtil;
import de.jdungeon.figure.hero.Profession;
import de.jdungeon.figure.hero.Zodiac;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.JDGUI;
import de.jdungeon.level.DefaultDungeonManager;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonManager;
import de.jdungeon.location.LevelExit;
import de.jdungeon.spell.Spell;
import de.jdungeon.util.DeepCopyUtil;

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


    private DungeonWorldUpdater dungeonWorldUpdater;
    private DungeonFactory lastCompletedDungeonFactory;
    private DungeonFactory lastSelectedDungeonFactory;
    private Dungeon derDungeon;

    private final DungeonManager manager;

    private JDGUI gui;
    private final Map<DungeonFactory, Integer> completedDungeons = new HashMap<>();

    public DefaultDungeonSession(User user) {
        this.user = user;
        manager = new DefaultDungeonManager();
    }


    @Override
    public void setDungeonWorldUpdater(DungeonWorldUpdater dungeonGameLoop) {
        this.dungeonWorldUpdater = dungeonGameLoop;
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
			//DungeonFactory level1 = getDungeonManager().getDungeonOptions(1).get(0);
			lastCompletedDungeonFactory = level0;
			lastSelectedDungeonFactory = level0;
			this.completedDungeons.put(level0, 1);
			//this.completedDungeons.put(level1, 2);
			return 1;
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

    @Override
    public int getGameRound() {
        return getDungeonRound();
    }

    public void setGUIController(JDGUI gui) {
        this.gui = gui;
    }


    /**
     * Returns the hero object that is currently played by this session
     *
     * @return current session hero
     */
    @Override
    public Hero getCurrentHero() {
        if (currentHero == null) {
            currentHero = HeroUtil.getBasicHero(heroType, "Gisbert", Zodiac.Aquarius,
                    Profession.Lumberjack);
            // todo: testing only
            //currentHero.getSkillSet().put(HealSkill.class, new HealSkill());
            //currentHero.getSkillSet().put(LionessConjureSkill.class, new LionessConjureSkill());
            //currentHero.getSpellbook().addSpell(new Raid());
        }
        return currentHero;
    }

    @Override
    public DungeonCompletionScore getAchievedScoreFor(DungeonFactory factory) {
        if (completedDungeons.containsKey(factory)) {
            Integer rounds = completedDungeons.get(factory);
            return new DungeonCompletionScore(rounds, calcScore(factory, rounds));
        }
        return null;
    }

    private int calcScore(DungeonFactory dungeonFactory, int rounds) {
        if (dungeonFactory.getRoundScoringBaseValue() > rounds) {
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
        if (heroBackup != null) {
            heroBackup.getSpellbook().addSpell(spell);
        }
    }

    @Override
    public int getDungeonRound() {
        if (dungeonWorldUpdater != null) {
            return dungeonWorldUpdater.getCurrentGameRound();
        }
        return -1;
    }

    /**
     * Tells the players decision for the type of hero to be played
     *
     * @param heroType category of hero that has been selected
     */
    public void setSelectedHeroType(int heroType) {
        this.heroType = heroType;
    }

    @Override
    public HeroInfo initDungeon(DungeonFactory dungeonFactory, ControlUnit control) {
        lastSelectedDungeonFactory = dungeonFactory;

        derDungeon = dungeonFactory.createDungeon();

		/*
		 Prepare hero for new dungeon
		 */
        Hero currentHero = getCurrentHero();

        // we need to clear the keys from the last dungeonFactory (as they would work in the new one also)
        currentHero.getInventory().clearKeys();


        // reset vis map
        this.currentHero.clearVisibilityMaps();

        // fill up bars (health, oxygen, dust)
        Attribute health = this.currentHero.getAttribute(Attribute.Type.Health);
        health.setValue(health.getBasic());
        Attribute dust = this.currentHero.getAttribute(Attribute.Type.Dust);
        dust.setValue(dust.getBasic());
        Attribute oxygen = this.currentHero.getAgility().getOxygen();
        oxygen.setValue(oxygen.getBasic());

        currentHero.resetCompletedGameRoundNumber();
        currentHero.setActualDungeon(derDungeon);
        currentHero.setControl(control);

        DungeonVisibilityMap heroVisMap = currentHero.getRoomVisibility();

        HeroInfo heroInfo = new HeroInfo(currentHero, heroVisMap);

        control.setFigure(heroInfo);

        derDungeon.getRoomNr(dungeonFactory.getHeroEntryPoint().getX(), dungeonFactory.getHeroEntryPoint().getY()).figureEnters(currentHero, 0, -1);
        derDungeon.prepare();
        return heroInfo;

    }

    @Override
    public void revertHero() {
        currentHero = (Hero) DeepCopyUtil.copy(heroBackup);
    }

    /**
     * Returns the dungeon currently played by this session
     *
     * @return the current dungeon
     */
    @Override
    public Dungeon getCurrentDungeon() {
        return derDungeon;
    }

    @Override
    public void notifyExit(LevelExit exit, Figure figure) {
        dungeonWorldUpdater.stopRunning();
        if (figure.equals(currentHero)) {
            Dungeon currentDungeon = getCurrentDungeon();
            if (!completedDungeons.containsKey(lastSelectedDungeonFactory) && currentDungeon != null) {
                completedDungeons.put(lastSelectedDungeonFactory, dungeonWorldUpdater.getCurrentGameRound());
                lastCompletedDungeonFactory = lastSelectedDungeonFactory;
            }
            makeHeroBackup();
            derDungeon = null;
        }
        EventManager.getInstance().fireEvent(new ExitUsedEvent(figure, exit));
    }

    private void makeHeroBackup() {
        // we need to clear all references to dungeon objects to allow a deep copy of the hero object via serialization
        Figure f = getCurrentHero();
        f.setLocation((Room) null);
        f.setPos(null);
        f.setActualDungeon(null);
        f.clearVisibilityMaps();
        f.setRespawnRoom(null);
        f.setControl(null);
        heroBackup = (Hero) DeepCopyUtil.copy(this.currentHero);
    }
}
