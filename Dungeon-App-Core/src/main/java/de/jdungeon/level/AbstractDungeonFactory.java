package de.jdungeon.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.DungeonFactory;
import de.jdungeon.dungeon.generate.DungeonFillUtils;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.generate.ReachabilityChecker;
import de.jdungeon.dungeon.generate.RectArea;
import de.jdungeon.dungeon.quest.ReversibleRoomQuest;
import de.jdungeon.dungeon.quest.RoomQuestWall;
import de.jdungeon.level.generation.SimpleDungeonFiller;
//import org.apache.log4j.Logger;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public abstract class AbstractDungeonFactory implements DungeonFactory {

	protected JDPoint entryPoint;
	protected SimpleDungeonFiller filler;

	@Override
	public JDPoint getHeroEntryPoint() {
		if (entryPoint == null) {

			if (filler != null) {
				entryPoint = filler.getUnallocatedRimRoom(false).getPoint();
			}
			else {
				entryPoint = new JDPoint(0, 4);
			}
		}
		return entryPoint;
	}

	protected void createAllDoors(Dungeon dungeon) {
		DungeonFillUtils.createAllDoors(dungeon);
	}

	/**
	 * Tries to setup the list of given RoomQuests in a way that full reachability
	 * is retained, considering also the distribution of keys.
	 *
	 * @param dungeon
	 * @param filler
	 * @param entryRoom
	 * @param entryPoint
	 * @param roomQuests
	 */
	protected void setupRoomQuests(Dungeon dungeon, DungeonFiller filler, Room entryRoom, JDPoint entryPoint, List<ReversibleRoomQuest> roomQuests) {
		ReachabilityChecker reachabilityChecker = new ReachabilityChecker(dungeon, entryPoint);
		for (ReversibleRoomQuest roomQuest : roomQuests) {
			int iteration = 0;
			final int maxIterations = 10;
			boolean success = false;
			while (iteration < maxIterations) {
				RectArea unallocatedSpace = filler.getValidArea(entryRoom, roomQuest.getSizeX(), roomQuest.getSizeY());
				if (unallocatedSpace != null) {
					roomQuest.setLocation(unallocatedSpace.getPosition());
					boolean possible = roomQuest.setUp();
					if(possible) {
						boolean valid = reachabilityChecker.check();
						if(roomQuest instanceof RoomQuestWall) {
							// the rooms of a RoomQuestWall should not be reachable, that is ok
							valid = reachabilityChecker.checkIgnoreRooms(toList(roomQuest.getRooms()));
						}
						if (valid) {
							filler.itemsToDistribute(roomQuest.finalizeRoomQuest());
							filler.addAllocatedRooms(unallocatedSpace.getRooms());
							// successful so break iteration loop
							success = true;
							break;
						} else {
							roomQuest.undo();
						}
					}
					iteration++;
				}
			}
			if(!success) {
				// TODO: find proper way of logging to android logger in non-android projects
				System.out.println("Could not create RoomQuest: "+roomQuest.toString());
			}
		}
	}

	private Collection<Room> toList(Room[][] rooms) {
		List<Room> result = new ArrayList<>();
		int sizeX = rooms[0].length;
		int sizeY = rooms.length;
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				result.add(rooms[j][i]);
			}
		}
		return result;
	}
}
