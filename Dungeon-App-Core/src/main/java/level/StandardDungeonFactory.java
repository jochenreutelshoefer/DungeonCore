package level;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.generate.DungeonGenerationFailedException;
import dungeon.generate.SectorDungeonFiller1;
import game.DungeonGame;
import item.equipment.weapon.Club;
import log.Log;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public class StandardDungeonFactory implements DungeonFactory {

	public static int DungeonSizeX = 30;
	public static int DungeonSizeY = 40;
	private static final JDPoint heroEntryPoint = new JDPoint(18, 39);

	@Override
	public Dungeon createDungeon() {
		return createDefaultDungeon();
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return heroEntryPoint;
	}

	@Override
	public String icon() {
		// TODO:
		return null;
	}

	@Override
	public String getName() {
		return "Standard World";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 10000;
	}

	private Dungeon createDefaultDungeon() {
		Dungeon derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39);
		// TODO: Dungeon should not know DungeonGame
		DungeonGame.getInstance().setDungeon(derDungeon);
		try {
			fillDungeon(derDungeon);
		}
		catch (DungeonGenerationFailedException e) {
			try {
				fillDungeon(derDungeon);
			}
			catch (DungeonGenerationFailedException e1) {
				try {
					fillDungeon(derDungeon);
				}
				catch (DungeonGenerationFailedException e2) {
					Log.severe("Cound not generate Dungeon - check Dungeon Generator!"+e1);
					System.exit(0);
				}
				e1.printStackTrace();
			}

		}
		DungeonGame.getInstance().init(derDungeon);
		derDungeon.getRoom(heroEntryPoint).addItem(new Club(40, false));
		return derDungeon;
	}

	@Deprecated
	private void fillDungeon(Dungeon derDungeon)
			throws DungeonGenerationFailedException {
		SectorDungeonFiller1 filler = new SectorDungeonFiller1(derDungeon,
				SectorDungeonFiller1.getValueForDungeon(1), DungeonGame.getInstance(), 1);
		filler.fillDungeon();
	}
}
