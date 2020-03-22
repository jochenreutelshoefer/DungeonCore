package spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dungeon.DoorInfo;
import dungeon.LockInfo;
import dungeon.RoomInfo;
import figure.FigureInfo;
import game.InfoEntity;
import game.RoomInfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public class DefaultTargetScope implements TargetScope {

	private final Class<? extends InfoEntity> targetClass;

	DefaultTargetScope(Class<? extends InfoEntity> targetClass) {
		this.targetClass = targetClass;
	}

	public Class<? extends InfoEntity> getTargetClass() {
		return targetClass;
	}

	public static TargetScope createDefaultScope(Class<? extends InfoEntity> targetClass) {
		return new DefaultTargetScope(targetClass);
	}

	@Override
	public List<? extends RoomInfoEntity> getTargetEntitiesInScope(FigureInfo actor) {
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
		return Collections.emptyList();
	}
}
