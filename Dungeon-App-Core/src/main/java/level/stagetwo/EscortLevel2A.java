package level.stagetwo;

import java.util.ArrayList;
import java.util.List;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DungeonFiller;
import dungeon.generate.RoomPositionConstraint;
import figure.hero.Hero;
import figure.npc.DefaultNPCFactory;
import figure.npc.RescuedNPCAI;
import item.Key;
import item.VisibilityCheatBall;
import item.equipment.weapon.Wolfknife;
import level.AbstractDungeonFactory;
import level.generation.SimpleDungeonFiller;
import location.LevelExit;

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
		npc.setAI(new RescuedNPCAI());
		hostageRoom.figureEnters(npc, 0, -1);


		Room exitRoom = filler.getUnallocatedRandomRoom(new RoomPositionConstraint() {
			@Override
			public boolean isValid(Room candidateRoom) {
				return candidateRoom != entryRoom;
			}
		});
		exitRoom.setShrine(new LevelExit(npc));

		filler.getUnallocatedRandomRoom().figureEnters(filler.getSmallMonster(800),0, -1);
		filler.getUnallocatedRandomRoom().figureEnters(filler.getSmallMonster(800),0, -1);
		filler.getUnallocatedRandomRoom().figureEnters(filler.getSmallMonster(800),0, -1);


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
