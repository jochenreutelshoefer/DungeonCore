package ai;

import item.ItemInfo;
import item.quest.LuziaAmulett;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import dungeon.DoorInfo;
import dungeon.Room;
import dungeon.RoomInfo;
import figure.RoomObservationStatus;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.action.UseItemAction;
import figure.monster.MonsterInfo;

public class LuziaRunner {

	private static Vector actionQueue = new Vector();

	public static Action getAction(MonsterInfo m) {
		if (actionQueue.size() > 0) {
			return (Action) actionQueue.remove(0);
		}

		List neededRoomInfos = getUnvisibleNeigbourRooms(m);

		if (neededRoomInfos.size() > 0) {
			//System.out.println("Immernoch unsichtbar!");
			return getUseAmulettAction(m);
		} else {
			//System.out.println("Jetzt alle Sichtbar!");
			List rooms = getTheRooms(m);
			RoomInfo heroRoom = null;
			for (Iterator iter = rooms.iterator(); iter.hasNext();) {
				RoomInfo element = (RoomInfo) iter.next();
				Boolean hero = element.hasHero();
				
				if (hero != null) {
					if (hero.booleanValue()) {
						//System.out.println("hero entdeckt: "+element.getNumber().toString());
						//System.out.println("selbst :"+m.getRoomInfo().getNumber().toString());
						heroRoom = element;
					}
				}
			}
			if (heroRoom != null) {
				int dir = m.getRoomInfo().getDirectionTo(heroRoom);
				int otherDir = getOtherDir(dir, m);
				if (otherDir != -1) {
					//System.out.println("will nach: "+otherDir);
					int pos = 1; // TODO: refactor
					int monsterPos = m.getPositionInRoomIndex();
					if(pos != monsterPos) {
					Action a = new MoveAction(otherDir);
					actionQueue.add(a);
					return new StepAction(pos);
					} else {
						return new MoveAction(otherDir);
					}
				}
			}
		}

		return new EndRoundAction();
	}

	private static int getOtherDir(int dir, MonsterInfo m) {
		RoomInfo roomInfo = m.getRoomInfo();
		DoorInfo[] doorInfos = roomInfo.getDoors();
		List dirs = new LinkedList();
		for (int i = 0; i < doorInfos.length; i++) {
			if (doorInfos[i] != null
					&& doorInfos[i].isPassable().booleanValue()) {
				int newDir = i + 1;
				if (newDir!= dir) {
					int posIndex = 0; // TODO: refactor
					if (!roomInfo.getPositionInRoom(posIndex).isOccupied() || roomInfo.getPositionInRoom(posIndex).getFigure().equals(m)) {
						dirs.add(new Integer(newDir));
					}
				}
			}
		}

		if (dirs.size() > 0) {
			Integer i = (Integer) dirs.get(((int) Math.random() * dirs.size()));
			return i.intValue();
		}
		return -1;
	}

	public static List getTheRooms(MonsterInfo m) {
		RoomInfo roomInfo = m.getRoomInfo();
		DoorInfo[] doorInfos = roomInfo.getDoors();

		List neededRoomInfos = new LinkedList();
		for (int i = 0; i < doorInfos.length; i++) {
			if (doorInfos[i] != null
					&& doorInfos[i].isPassable().booleanValue()) {
				RoomInfo other = doorInfos[i].getOtherRoom(roomInfo);
				int visStatus = other.getVisibilityStatus();
				if (visStatus >= RoomObservationStatus.VISIBILITY_FIGURES) {
					neededRoomInfos.add(other);
				} else {
					//System.out.println("getTheRooms immernoch unsichtbar!");
				}
			}
		}
		return neededRoomInfos;
	}

	public static List getUnvisibleNeigbourRooms(MonsterInfo m) {
		RoomInfo roomInfo = m.getRoomInfo();
		DoorInfo[] doorInfos = roomInfo.getDoors();

		List neededRoomInfos = new LinkedList();
		for (int i = 0; i < doorInfos.length; i++) {
			if (doorInfos[i] != null
					&& doorInfos[i].isPassable().booleanValue()) {
				RoomInfo other = doorInfos[i].getOtherRoom(roomInfo);
				int visStatus = other.getVisibilityStatus();
				if (visStatus < RoomObservationStatus.VISIBILITY_FIGURES) {
					neededRoomInfos.add(other);
				}
			}
		}
		return neededRoomInfos;
	}

	public static Action getUseAmulettAction(MonsterInfo m) {
		List items = m.getAllItems();
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			ItemInfo element = (ItemInfo) iter.next();
			if (element.getItemClass() == LuziaAmulett.class) {
				return new UseItemAction(element, null, false);
			}
		}
		return null;
	}
}
