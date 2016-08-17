package dungeon.generate;

import game.DungeonGame;


public interface DungeonGameFactory {

	// TODO: there should not be a factory for something that is singleton!?
	DungeonGame createDungeon();

}
