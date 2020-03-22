package dungeon.generate;

import game.DungeonGameLoop;


public interface DungeonGameFactory {

	// TODO: there should not be a factory for something that is singleton!?
	DungeonGameLoop createDungeon();

}
