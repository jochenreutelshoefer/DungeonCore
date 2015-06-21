package gui.bot;

import game.DungeonGame;
import dungeon.Dungeon;
import dungeon.generate.DungeonFactory;
import dungeon.generate.DungeonGenerationFailedException;
import dungeon.generate.SectorDungeonFiller1;

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
		dungeonGame.setDungeon(derDungeon);
		SectorDungeonFiller1 filler = new SectorDungeonFiller1(derDungeon,
				SectorDungeonFiller1.getValueForDungeon(1), dungeonGame, 1);

		try {
			filler.fillDungeon();
			dungeonGame.init(derDungeon);
		} catch (DungeonGenerationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dungeonGame;
	}

}
