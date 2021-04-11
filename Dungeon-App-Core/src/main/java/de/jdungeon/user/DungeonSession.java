package de.jdungeon.user;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.game.ControlUnit;
import de.jdungeon.game.JDGUI;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonManager;
import de.jdungeon.location.LevelExit;
import de.jdungeon.spell.Spell;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public interface DungeonSession {

	JDGUI getGUI();

	DungeonManager getDungeonManager();

	DungeonFactory getLastCompleted();

	Dungeon getCurrentDungeon();

	int getCurrentStage();

	void notifyExit(LevelExit exit, Figure figure);

	HeroInfo initDungeon(DungeonFactory dungeon, ControlUnit controlUnit);

	void revertHero();

	Hero getCurrentHero();

	DungeonCompletionScore getAchievedScoreFor(DungeonFactory dungeonFactory);

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
