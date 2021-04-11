package de.jdungeon.level.stagetwo;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.generate.RoomPositionConstraint;
import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.npc.DefaultNPCFactory;
import de.jdungeon.figure.npc.RescuedNPCAI;
import de.jdungeon.item.Key;
import de.jdungeon.item.VisibilityCheatBall;
import de.jdungeon.item.equipment.weapon.Wolfknife;
import de.jdungeon.level.AbstractDungeonFactory;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.location.LevelExit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.08.16.
 */
public class EscortLevel2A extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 3;
	public static final int DUNGEON_SIZE_X = 3;
	private static final int FLOOR_INDEX_EXIT = 5;

	@Override
	public Dungeon createDungeon() {
		Dungeon dungeon = null;

		List<Key> remainingKeys = new ArrayList<>();

		DungeonFiller filler = null;

		dungeon = new Dungeon(DUNGEON_SIZE_X, DUNGEON_SIZE_Y);
		filler = new SimpleDungeonFiller(dungeon, remainingKeys);
		createAllDoors(dungeon);

		// for testing only
		Room entryRoom = dungeon.getRoom(this.getHeroEntryPoint());
		entryRoom.addItem(new VisibilityCheatBall());


		Room hostageRoom = filler.getUnallocatedRandomRoom();
		Hero npc = DefaultNPCFactory.createDefaultNPC("Willi", Hero.HEROCODE_DRUID);
		npc.takeItem(new Wolfknife(25, false));
		// TODO: use setControl!
		npc.createVisibilityMap(dungeon);
		FigureInfo npcInfo = FigureInfo.makeFigureInfo(npc, npc.getRoomVisibility());
		RescuedNPCAI ai = new RescuedNPCAI();
		ai.setFigure(npcInfo);
		npc.setControl(new FigureControl(npcInfo, ai));
		hostageRoom.figureEnters(npc, 0, -1);


		Room exitRoom = filler.getUnallocatedRandomRoom(new RoomPositionConstraint() {
			@Override
			public boolean isValid(Room candidateRoom) {
				return candidateRoom != entryRoom;
			}
		});
		exitRoom.setShrine(new LevelExit(npc));

		filler.getUnallocatedRandomRoom().figureEnters(filler.getSmallMonster(8000),0, -1);
		filler.getUnallocatedRandomRoom().figureEnters(filler.getSmallMonster(8000),0, -1);
		filler.getUnallocatedRandomRoom().figureEnters(filler.getSmallMonster(8000),0, -1);


		return dungeon;
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return new JDPoint(1, 2);
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Level 2A";
	}

	@Override
	public String getDescription() {
		return "Eskortierung 2A";
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 50;
	}

}
