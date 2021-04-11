package de.jdungeon.dungeon.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DungeonFillUtils;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.generate.undo.AddDoor;
import de.jdungeon.dungeon.generate.undo.DungeonChangeAction;
import de.jdungeon.dungeon.generate.undo.InsertFigure;
import de.jdungeon.dungeon.generate.undo.RemoveDoor;
import de.jdungeon.dungeon.generate.undo.SetChestAction;
import de.jdungeon.dungeon.generate.undo.SetItem;
import de.jdungeon.dungeon.generate.undo.SetShrineAction;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemPool;
import de.jdungeon.item.Key;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class RoomQuest1x1 extends ReversibleRoomQuest {

	private boolean locked = false;
	private Location s;
	private Key key;
	private boolean finalized = false;

	public RoomQuest1x1(JDPoint p, DungeonFiller df) {
		super(p, df, 1, 1);
	}

	public RoomQuest1x1(JDPoint p, DungeonFiller df, boolean locked, Location s) {
		super(p, df, 1, 1);
		this.s = s;
		this.locked = locked;

	}

	public RoomQuest1x1(DungeonFiller df, boolean locked, Location s) {
		super(df, 1, 1);
		this.s = s;
		this.locked = locked;
	}

	@Override
	List<DungeonChangeAction> createActionList() {
		List<DungeonChangeAction> actions = new ArrayList<>();

		int dir = (int) (Math.random() * 4) + 1;
		if (!accessible(rooms[0][0], dir)) {
			return null;
		}

		for (int i = 1; i < 5; i++) {
			if (i != dir) {
				actions.add(new RemoveDoor(rooms[0][0], RouteInstruction.Direction.fromInteger(i)));
			}
			else {
				// TODO: key back to de.jdungeon.game !?!?
				key = df.getNextKey();
				if(key == null) {
					Log.severe("No next key availabe!");
					continue;
				}
				actions.add(new SetItem(DungeonFillUtils.getRandomMonster(df.getDungeon(), new HashSet<>()), key));
				Room entrance = df.getDungeon().getRoomAt(rooms[0][0],
						RouteInstruction.direction(dir));
				Door d = null;
				if (locked) {
					d = new Door(rooms[0][0], entrance, key);
					df.itemToDistribute(key);
				}
				else {
					d = new Door(rooms[0][0], entrance);
				}
				//rooms[0][0].setDoor(d, dir,true);
				actions.add(new AddDoor(rooms[0][0], d, RouteInstruction.Direction.fromInteger(dir)));
			}
		}

		List<Item> itemsL = new LinkedList<Item>();
		Item it1 = ItemPool.getRandomItem(25, 1);
		Item it2 = ItemPool.getRandomItem(30, 1.5);
		if (it1 != null) {
			itemsL.add(it1);
		}
		if (it2 != null) {
			itemsL.add(it2);
		}
		Chest ch2 = new Chest(itemsL);
		if (s != null) {
			//rooms[0][0].setShrine(s,true);
			actions.add(new SetShrineAction(s, rooms[0][0]));
		}
		//rooms[0][0].setChest(ch2);
		actions.add(new SetChestAction(ch2, rooms[0][0]));
		Monster m = df.getBigMonster(1000);
		df.equipMonster(m, 1);
		//rooms[0][0].figureEnters(m,0);
		actions.add(new InsertFigure(m, rooms[0][0]));

		return actions;
	}

	@Override
	public Collection<Item> finalizeRoomQuest() {
		setFloorIndex(3);
		Set<Item> result = new HashSet<>();
		result.add(key);
		finalized = true;
		return result;
	}



	public boolean isFinalized() {
		return finalized;
	}
}
