package de.jdungeon.spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.LockInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.dungeon.InfoEntity;
import de.jdungeon.dungeon.RoomInfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public abstract class AbstractTargetScope<TARGET> implements TargetScope<TARGET> {


	@Override
	public List<TARGET> getTargetEntitiesInScope(FigureInfo actor, RoomInfoEntity highlightedEntity) {

		if(highlightedEntity != null && getTargetClass().isAssignableFrom(highlightedEntity.getClass())) {
			// the target is actually marked
			return Collections.singletonList((TARGET)highlightedEntity);
		}

		return (List<TARGET>)collectEntitiesOfClass(actor, getTargetClass());
	}

	// todo: move to DefaultTargetScope
	public static TargetScope createDefaultScope(Class<? extends InfoEntity> targetClass) {
		return new DefaultTargetScope(targetClass);
	}



	public List<?> collectEntitiesOfClass(FigureInfo actor, Class<TARGET> targetClass) {
		if(targetClass.equals(RoomInfo.class)) {
			int actorPositionIndex = actor.getPositionInRoomIndex();
			RoomInfo room = actor.getRoomInfo();
			if(room == null) return null; // exit problem
			DoorInfo[] doors = room.getDoors();
			for (DoorInfo door : doors) {
				if(door != null && door.getPositionAtDoor(room).getIndex() == actorPositionIndex) {
					return Collections.singletonList(door.getOtherRoom(room));
				}
			}
		}
		if(targetClass.equals(FigureInfo.class)) {
			RoomInfo roomInfo = actor.getRoomInfo();
			if(roomInfo == null) return Collections.emptyList();
			List<FigureInfo> figureInfos = roomInfo.getFigureInfos();
			figureInfos.remove(actor);
			return figureInfos;
		}
		if(targetClass.equals(DoorInfo.class)) {
			RoomInfo roomInfo = actor.getRoomInfo();
			DoorInfo[] doors = roomInfo.getDoors();
			List<DoorInfo> doorsWithLock = new ArrayList<>();
			for (DoorInfo door : doors) {
				if(door != null && door.hasLock()) {
					doorsWithLock.add(door);
				}
			}
			return doorsWithLock;
		}
		if(targetClass.equals(LockInfo.class)) {
			return getTargetLocks(actor);
		}
		return Collections.emptyList();
	}

	public static List<LockInfo> getTargetLocks(FigureInfo actor) {
		RoomInfo roomInfo = actor.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();
		List<LockInfo> locks = new ArrayList<>();
		for (DoorInfo door : doors) {
			if(door != null && door.hasLock()) {
				locks.add(door.getLock());
			}
		}
		return locks;
	}
}
