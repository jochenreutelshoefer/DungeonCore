package de.jdungeon.user;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.level.DungeonManager;
import de.jdungeon.location.LevelExit;
import de.jdungeon.spell.Spell;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public interface DungeonSession {

	DungeonManager getDungeonManager();

	DungeonFactory getLastCompleted();

	/**
	 * Checks whether some loss criterion is met in the current game state.
	 * If one is met, it is returned.
	 * If none is met, null is returned.
	 *
	 * @return a met loss criteria or null
	 */
	LossCriterion lossCriterionMet();

	List<DungeonFactory> getCompletedDungeonsList();

	int getNumberOfFails();

	Date getSessionStart();

	Map<DungeonFactory, Integer> getCompletedDungeons();

	Dungeon getCurrentDungeon();

	void setDungeonWorldUpdater(DungeonWorldUpdater updater);

	int getCurrentStage();

	void notifyExit(LevelExit exit, Figure figure);

	HeroInfo initDungeon(DungeonFactory dungeon, ControlUnit controlUnit) throws DungeonGenerationException;

	void restoreHero();

	Hero getCurrentHero();

	DungeonCompletionScore getAchievedScoreFor(DungeonFactory dungeonFactory);

	String getPlayerName();

	String getSessionID();

	int getTotalScore();

	void learnSkill(Spell spell);

	/**
	 * Returns the de.jdungeon.game round, i. e. the number of the current round,
	 * that is how many turns have been played in the current dungeon.
	 *
	 * @return current round number
	 */
	int getDungeonRound();
}
