package user;

import dungeon.Dungeon;
import figure.Figure;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import game.ControlUnit;
import game.JDGUI;
import level.DungeonFactory;
import level.DungeonManager;
import location.LevelExit;
import spell.Spell;

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
	 * Returns the game round, i. e. the number of the current round,
	 * that is how many turns have been played in the current dungeon.
	 *
	 * @return current round number
	 */
	int getDungeonRound();
}
