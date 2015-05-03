package gui.bot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ai.AI;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.util.DungeonUtils;
import dungeon.util.RouteInstruction;
import dungeon.util.RouteInstruction.Direction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import figure.percept.Percept;

public class MyDemoBot extends AI {

	// private static final int _300 = 300;
	private HeroInfo hero;

	private final Set<RoomInfo> knownRooms = new HashSet<RoomInfo>();
	private RoomInfo currentTarget = null;

	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			this.hero = ((HeroInfo) f);
		}

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return f instanceof MonsterInfo;
	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}


	@Override
	public Action chooseFightAction() {
		List<MonsterInfo> monsterInfos = hero.getRoomInfo().getMonsterInfos();
		return new AttackAction(monsterInfos.get(0).getFighterID());
	}



	@Override
	public Action chooseMovementAction() {
		/*
		 * store current room
		 */
		RoomInfo currentRoom = hero.getRoomInfo();
		knownRooms.add(currentRoom);

		if (currentTarget == null || currentTarget.equals(hero.getRoomInfo())) {
			currentTarget = calcTargetRoom();
		}

		List<JDPoint> wayFromTo = hero.getShortestWayFromTo(
				currentRoom.getPoint(), currentTarget.getNumber());
		if(wayFromTo != null) {
			JDPoint roomTo = wayFromTo.get(1);
			Direction movementDirection = DungeonUtils
					.getNeighbourDirectionFromTo(
					currentRoom.getPoint(), roomTo);
			Action moveAction = tryMoveTowards(movementDirection);
			if (moveAction != null) {
				return moveAction;
			} else {
				return new EndRoundAction();
			}
		}

		/*
		 * kind of random movement
		 */

		int[] roomDoors = hero.getRoomDoors();
		int dir = 0;
		for (int i : roomDoors) {
			if (i == 2 | i == 1) {
				break;
			}
			dir++;
		}
		Action a = tryMoveTowards(RouteInstruction.direction(dir));
		if (a == null)
			return new EndRoundAction();

		return a;

	}

	private RoomInfo calcTargetRoom() {
		/*
		 * look for doors to 'unknown' rooms
		 */
		Set<RoomInfo> targets = new HashSet<RoomInfo>();
		for (RoomInfo room : knownRooms) {

			DoorInfo[] doors = room.getDoors();
			for (DoorInfo doorInfo : doors) {
				if (doorInfo != null && doorInfo.isPassable()) {

				RoomInfo potentialTargetRoom = doorInfo.getOtherRoom(room);
				if (!knownRooms.contains(potentialTargetRoom)) {
					targets.add(potentialTargetRoom);
				}
				}
			}
		}

		/*
		 * select the nearest target
		 */
		int minPathLenght = Integer.MAX_VALUE;
		RoomInfo nearestTarget = null;
		for (RoomInfo roomInfo : targets) {
			List<JDPoint> shortestWayFromTo = hero.getShortestWayFromTo(
					hero.getRoomNumber(), roomInfo.getNumber());
			if (shortestWayFromTo.size() < minPathLenght) {
				nearestTarget = roomInfo;
				minPathLenght = shortestWayFromTo.size();
			}

		}
		return nearestTarget;
	}

	private Action tryMoveTowards(RouteInstruction.Direction dir) {
		int dirPos = Figure.getDirPos(dir.getValue());
		if (hero.getPositionInRoomIndex() == dirPos) {
			return new MoveAction(dir.getValue());

		} else {
			if (dirPos != -1) {
				return new StepAction(dirPos);

			} else {
				return null;
			}
		}
	}

	@Override
	protected void processPercept(Percept p) {
		// TODO Auto-generated method stub

	}


}
