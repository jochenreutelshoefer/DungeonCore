/**
 * Realisierung eines DungeonFillers - Sektorenweise
 * Diese Implementierung generiert den kompletten Dungeon indem am Startpunkt
 * ein Sektor 1 generiert und ein Sektor 2 dahinter geschaltet wird.
 *
 */
package dungeon.generate;

import item.quest.Rune;

import java.util.*;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RouteInstruction;

import figure.Figure;
import figure.monster.Monster;
import game.DungeonGame;
import game.JDEnv;

public class SectorDungeonFiller1 extends DungeonFiller {

	public boolean succes = true;

	// dungeonView view;
	public SectorDungeonFiller1(Dungeon d, int value, DungeonGame game, Rune[] runen,

	int level) {

		super(d, value, game, runen, level);
		// view = new dungeonView(d);

	}

	/**
	 * @see DungeonFiller#getMap()
	 */
	protected int[][] getMap() {
		return null;
	}

	/**
	 * @see DungeonFiller#fillDungeon()
	 */
	public void fillDungeon() throws DungeonGenerationFailedException {

		Sector s1 = new Sector1(d, d.getPoint(18, 39), 1, 800, 24, game, this);

		if (!JDEnv.isBeginnerGame()) {
			Sector s2 = new Sector2(d, (d.getRoomAt(s1.getConnectionRoom(),
					RouteInstruction.NORTH).getNumber()), 2, 1400, 40, game,
					this);
		}

	}

	protected Monster getMonsterIn(int sector, int nope) {
		for (int i = 0; i < 1000; i++) {
			Room r = d.getRoom(d.getRandomPoint());
			// Hier muss was rein, damit nicht Monster innen den Schl�ssel
			// kriegt
			if ((r.getSec() != null) && (r.getSec().number == sector)) {
				List<Figure> monsters = r.getRoomFigures();
				if (monsters.size() > 0) {
					return (Monster) monsters
							.get((int) (Math.random() * monsters.size()));
				}
			}
		}
		return null;

	}

}
