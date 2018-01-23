package dungeon.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dungeon.Chest;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DungeonFiller;
import dungeon.generate.undo.AddDoor;
import dungeon.generate.undo.DungeonChangeAction;
import dungeon.generate.undo.InsertFigure;
import dungeon.generate.undo.RemoveDoor;
import dungeon.generate.undo.SetChestAction;
import dungeon.generate.undo.SetShrineAction;
import dungeon.util.RouteInstruction;
import figure.monster.Monster;
import item.Item;
import item.ItemPool;
import item.Key;
import shrine.Shrine;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class RoomQuest2x2 extends ReversibleRoomQuest {

	private List<Item> items;
	private Shrine shrine ;

	public RoomQuest2x2(JDPoint p, DungeonFiller df, int x, int y) {
		super(p, df, x, y);
	}

	public RoomQuest2x2(JDPoint p, DungeonFiller df, List<Item> items) {
		super(p, df, 2, 2);
		this.items = items;

	}

	public RoomQuest2x2(DungeonFiller df, List<Item> items) {
		super(df, 2, 2);
		this.items = items;

	}

	public RoomQuest2x2(DungeonFiller df, List<Item> items, Shrine shrine) {
		this(df, items);
		this.shrine = shrine;
	}

	public RoomQuest2x2(JDPoint p, DungeonFiller df, List<Item> items, Shrine shrine) {
		this(p, df, items);
		this.shrine = shrine;
	}

	@Override
	public boolean setUp() {
		return insert();
	}

	@Override
	public List<DungeonChangeAction> createActionList() {

		List<DungeonChangeAction> actions = new ArrayList<>();
		Key k = df.getNextKey();

		//rooms[0][0].removeDoor(RouteInstruction.WEST, true);
		actions.add(new RemoveDoor(rooms[0][0],RouteInstruction.Direction.West));

		//rooms[0][0].setDoor(df.getDungeon(), RouteInstruction.NORTH, true);
		actions.add(new AddDoor(rooms[0][0], RouteInstruction.Direction.North ));

		//rooms[0][0].setDoor(df.getDungeon(), RouteInstruction.EAST, true);
		actions.add(new AddDoor(rooms[0][0], RouteInstruction.Direction.East ));

		//rooms[0][1].removeDoor(RouteInstruction.EAST, true);
		actions.add(new RemoveDoor(rooms[0][1],RouteInstruction.Direction.East));


		//rooms[0][1].removeDoor(RouteInstruction.NORTH, true);
		actions.add(new RemoveDoor(rooms[0][1],RouteInstruction.Direction.North));


		//rooms[0][1].setDoor(df.getDungeon(), RouteInstruction.SOUTH, true);
		actions.add(new AddDoor(rooms[0][1], RouteInstruction.Direction.South ));

		//rooms[1][1].removeDoor(RouteInstruction.EAST, true);
		actions.add(new RemoveDoor(rooms[1][1],RouteInstruction.Direction.East));


		//rooms[1][1].removeDoor(RouteInstruction.SOUTH, true);
		actions.add(new RemoveDoor(rooms[1][1],RouteInstruction.Direction.South));

		//rooms[1][1].setDoor(df.getDungeon(), RouteInstruction.WEST, true);
		actions.add(new AddDoor(rooms[1][1], RouteInstruction.Direction.West ));


		//rooms[1][0].removeDoor(RouteInstruction.SOUTH, true);
		Room room10 = rooms[1][0];
		actions.add(new RemoveDoor(room10,RouteInstruction.Direction.South));


		//rooms[1][0].removeDoor(RouteInstruction.WEST, true);
		actions.add(new RemoveDoor(room10,RouteInstruction.Direction.West));


		Door keyDoor = new Door(room10, rooms[0][0], k);
		//rooms[1][0].setDoor(keyDoor, RouteInstruction.NORTH, true);
		actions.add(new AddDoor(room10, keyDoor, RouteInstruction.Direction.North ));


		Chest ch1 = new Chest(k);
		List<Item> itemsL = new LinkedList<Item>();
		if (items.size() >= 2) {
			itemsL.add(items.remove(0));
			itemsL.add(items.remove(0));
		} else {
			Item it1 = ItemPool.getRandomItem(25, 1);
			Item it2 = ItemPool.getRandomItem(30, 1.5);
			if (it1 != null) {
				itemsL.add(it1);
			}
			if (it2 != null) {
				itemsL.add(it2);
			}
		}

		Chest ch2 = new Chest(itemsL, k);

		//rooms[1][1].setChest(ch2);
		actions.add(new SetChestAction(ch2, rooms[1][1]));
		//rooms[1][0].setChest(ch1);
		actions.add(new SetChestAction(ch1, room10));

		Monster smallMonster = df.getSmallMonster(800);
		//rooms[1][0].figureEnters(smallMonster,0);
		actions.add(new InsertFigure(smallMonster, room10));

		if(shrine != null && room10.getShrine() != null) {
			// TODO: find method to handle situation, that there is already a shrine!
			//rooms[1][0].setShrine(shrine);
			actions.add(new SetShrineAction(shrine, room10));
		}
		for (int i = 0; i < 3; i++) {
			Monster bigMonster = df.getBigMonster(1200);
			//rooms[0][1].figureEnters(bigMonster,0);
			actions.add(new InsertFigure(bigMonster, rooms[0][1]));
		}
		return actions;
	}

	@Override
	public Collection<Item> finalizeRoomQuest() {
		setFloorIndex(2);
		return Collections.emptySet();
	}
}
