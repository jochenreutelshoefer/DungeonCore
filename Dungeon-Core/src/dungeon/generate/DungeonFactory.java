package dungeon.generate;

import game.DungeonGame;


public interface DungeonFactory {

	// TODO: there should not be a factory for something that is singleton!?
	DungeonGame createDungeon();

}
