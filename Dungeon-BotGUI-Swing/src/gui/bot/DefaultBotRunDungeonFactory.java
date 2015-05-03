package gui.bot;

import game.DungeonGame;
import dungeon.Dungeon;
import dungeon.generate.DungeonFactory;
import dungeon.generate.DungeonGenerationFailedException;

public class DefaultBotRunDungeonFactory implements DungeonFactory {

	public static int DungeonSizeX = 30;

	public static int DungeonSizeY = 40;

	@Override
	public DungeonGame createDungeon() {
		/*
		 * init game and dungeon
		 */
		DungeonGame dungeonGame = DungeonGame.createNewInstance();

		Dungeon derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39,
				dungeonGame);

		try {
			dungeonGame.fillDungeon(derDungeon);
		} catch (DungeonGenerationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dungeonGame;
	}

}
