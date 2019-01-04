/*
 * Created on 24.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import figure.action.ScoutResult;
import game.ControlUnit;

public class DungeonVisibilityMap {

	private RoomObservationStatus rooms[][];

	private Figure f;

	private boolean hasVisCheat = false;

	private Dungeon dungeon;

	private static DungeonVisibilityMap allVis;

	private Set<RoomObservationStatus> cache = new HashSet<>();

	public static DungeonVisibilityMap getAllVisMap(Dungeon d) {
		if (allVis == null) {
			allVis = new DungeonVisibilityMap(d);
			RoomObservationStatus[][] stats = d.getNewRoomVisibilityMap(allVis);
			allVis.setMap(stats);
			allVis.setVisCheat();
		}
		return allVis;

	}

	public void setFigure(Figure f) {
		this.f = f;
	}

	public Figure getFigure() {
		return f;
	}

	public DungeonVisibilityMap(Dungeon d) {
		dungeon = d;
	}

	public DungeonVisibilityMap() {

	}

	public void setVisCheat() {
		hasVisCheat = true;
	}

	public void setMap(RoomObservationStatus[][] r) {
		this.rooms = r;
	}

	public int getSizeX() {
		return rooms.length;
	}

	public int getSizeY() {
		return rooms[0].length;
	}

	public RoomObservationStatus getStatusObject(JDPoint p) {
		if (p == null)
			return null;
		int x = p.getX();
		int y = p.getY();
		if(x < 0 || x > rooms.length-1) {
			return null;
		}
		if(y < 0 || y > rooms[0].length-1) {
			return null;
		}
		return rooms[x][y];
	}

	public void setVisibilityStatus(int x, int y, int status) {
		RoomObservationStatus currentStatus = rooms[x][y];
		if(currentStatus.getVisibilityStatus() < status) {
			// notify figure that new information is revealed
			ControlUnit control = f.getControl();
			if(control != null) {
				control.notifyVisibilityStatusIncrease(new JDPoint(x, y));
			}
		}

		rooms[x][y].setVisibilityStatus(status);
		synchronized (cache) {
			cache.add(rooms[x][y]);
		}
	}

	@Deprecated
	public void setDiscoveryStatus(int x, int y, int status) {
		rooms[x][y].setDiscoveryStatus(status);

	}

	public void resetVisibilityStatus(int x, int y) {
		rooms[x][y].resetVisibilityStatus();
	}

	public int getVisibilityStatus(int x, int y) {
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		if (rooms[x][y] != null) {
			return rooms[x][y].getVisibilityStatus();
		} else {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
	}

	public int getDiscoveryStatus(int x, int y) {
		if(x >= rooms.length || y >= rooms[0].length) {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		if (rooms[x][y] != null) {
			return rooms[x][y].getDiscoveryStatus();
		} else {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
	}

	public void resetVisibilityStatus(JDPoint p) {
		resetVisibilityStatus(p.getX(), p.getY());
	}

	public int getVisibilityStatus(JDPoint p) {
		if(p == null) return -1;
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		return getVisibilityStatus(p.getX(), p.getY());
	}


	public int getDiscoveryStatus(JDPoint p) {
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		return getDiscoveryStatus(p.getX(), p.getY());
	}

	@Deprecated
	public void setDiscoveryStatus(JDPoint p, int status) {
		setDiscoveryStatus(p.getX(), p.getY(), status);
	}

	public void setVisibilityStatus(JDPoint p, int status) {
		setVisibilityStatus(p.getX(), p.getY(), status);
	}

	/**
	 * @return Returns the dungeon.
	 */
	public Dungeon getDungeon() {
		return dungeon;
	}

	/**
	 * @param dungeon
	 *            The dungeon to set.
	 */
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public RoomObservationStatus[][] getRooms() {
		return rooms;
	}

	public void resetTemporalVisibilities() {
		synchronized (cache) {
			for (RoomObservationStatus roomObservationStatus : cache) {
				roomObservationStatus.resetVisibilityStatus();
			}
			cache.clear();
		}

	}

	public void removeScoutedVisibility(Position position) {
		RouteInstruction.Direction possibleScoutDirection = position.getPossibleFleeDirection();
		if(possibleScoutDirection != null) {
			Room room = position.getRoom();
			if (room != null) {
				Room neighbourRoom = room.getNeighbourRoom(possibleScoutDirection);
				if (neighbourRoom != null) {
					JDPoint scoutedRoomNumber = neighbourRoom.getLocation();
					if (scoutedRoomNumber != null) {
						RoomObservationStatus visStatus = getStatusObject(scoutedRoomNumber);
						List<VisibilityModifier> visibilityModifier = visStatus.getVisibilityModifier();
						List<VisibilityModifier> toRemove = new ArrayList<>();
						for (VisibilityModifier modifier : visibilityModifier) {
							if (modifier instanceof ScoutResult) {
								ScoutResult scoutResult = ((ScoutResult) modifier);
								if (scoutResult.getScoutingFigure().equals(this.getFigure())
										&& scoutResult.getPosition().equals(position)) {
									// we actually found a scout result which now needs to be removed
									toRemove.add(scoutResult);
								}
							}
						}
						for (VisibilityModifier scoutResultToRemove : toRemove) {
							visStatus.removeVisibilityModifier(scoutResultToRemove);
						}
					}
				}
			}
		}
	}
}
