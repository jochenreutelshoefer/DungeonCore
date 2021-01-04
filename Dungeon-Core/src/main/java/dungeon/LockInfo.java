package dungeon;

import java.util.Collection;
import java.util.HashSet;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.MemoryObject;
import game.RoomInfoEntity;
import gui.Paragraph;
import gui.Paragraphable;
import item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.18.
 */
public class LockInfo extends RoomInfoEntity implements Paragraphable, Locatable {

	private final Lock lock;
	private final DungeonVisibilityMap m;

	public LockInfo(Lock lock, DungeonVisibilityMap m) {
		super(m);
		this.lock = lock;
		this.m = m;
	}

	public RoomInfoEntity getLockedObject() {
		Locatable lockableObject = lock.getLockableObject();
		if(lockableObject instanceof Door) {
			return new DoorInfo(((Door) lockableObject), m);
		}
		if(lockableObject instanceof Chest) {
			return new ChestInfo(((Chest) lockableObject), m);
		}
		return null;
	}

	public String getKeyPhrase() {
		return lock.getKey().getType();
	}

	@Override
	public JDPoint getRoomNumber() {
		return lock.getRoomNumber();
	}

	@Override
	public Paragraph[] getParagraphs() {
		return new Paragraph[0];
	}

	@Override
	public MemoryObject getMemoryObject(FigureInfo fig) {
		return null;
	}

	@Override
	public Collection<PositionInRoomInfo> getInteractionPositions() {
		Locatable lockableObject = lock.getLockableObject();
		Collection<PositionInRoomInfo> result = new HashSet<>();
		if(lockableObject instanceof Door) {
			Door door = (Door) lockableObject;
			Room[] rooms = door.getRooms();
			result.add(new PositionInRoomInfo(door.getPositionAtDoor(rooms[0], true),m));
			result.add(new PositionInRoomInfo(door.getPositionAtDoor(rooms[1], true),m));
		}
		if(lockableObject instanceof Chest) {
			JDPoint location = ((Chest) lockableObject).getRoomNumber();
			Position position = m.getDungeon().getRoom(location).getPositions()[Position.Pos.NW.getValue()];
			result.add(new PositionInRoomInfo(position, m));
		}

		return result;
	}
}
