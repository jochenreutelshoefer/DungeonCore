package spell;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dungeon.DoorInfo;
import dungeon.RoomInfo;
import figure.FigureInfo;
import game.InfoEntity;

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
	public List<? extends InfoEntity> getTargetEntitiesInScope(FigureInfo actor) {
		if(targetClass.equals(FigureInfo.class)) {
			RoomInfo roomInfo = actor.getRoomInfo();
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
		return Collections.emptyList();
	}
}
