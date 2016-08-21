package user;

import figure.Figure;
import game.JDGUI;
import level.DungeonFactory;
import level.DungeonManager;
import shrine.LevelExit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public interface DungeonSession {

	JDGUI getGUI();

	DungeonManager getDungeonManager();

	int getCurrentStage();

	void notifyExit(LevelExit exit, Figure figure);

	void initDungeon(DungeonFactory dungeon);
}
