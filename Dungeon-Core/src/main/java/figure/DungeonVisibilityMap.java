/*
 * Created on 24.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Position;
import figure.action.ScoutResult;
import game.ControlUnit;
import log.Log;

public class DungeonVisibilityMap {

	private RoomObservationStatus rooms[][];

	private Figure f;

	private final Map<Position, Set<JDPoint>> scoutCache = new HashMap<>();

	private Dungeon dungeon;

	private static DungeonVisibilityMap allVis;

	private final Set<RoomObservationStatus> cache = new HashSet<>();

	public static DungeonVisibilityMap getAllVisMap(Dungeon d) {
		if (allVis == null) {
			allVis = new DungeonVisibilityMap(d);
			RoomObservationStatus[][] stats = allVis.getNewRoomVisibilityMap();
			for (RoomObservationStatus[] stat : stats) {
				for (RoomObservationStatus roomObservationStatus : stat) {
					roomObservationStatus.setVisibilityStatus(RoomObservationStatus.VISIBILITY_SHRINE);
				}
			}
			allVis.setMap(stats);
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
		this(null, d);
	}

	public RoomObservationStatus[][] getNewRoomVisibilityMap() {
		RoomObservationStatus[][] stats = new RoomObservationStatus[dungeon.getSize().getX()][dungeon.getSize().getY()];
		for (int i = 0; i < stats.length; i++) {
			for (int j = 0; j < stats[0].length; j++) {
				stats[i][j] = new RoomObservationStatus(getFigure(), dungeon.getPoint(i, j));
			}
		}
		return stats;
	}

	public DungeonVisibilityMap(Figure figure, Dungeon d) {
		dungeon = d;
		setFigure(figure);
		RoomObservationStatus[][] stats = getNewRoomVisibilityMap();
		setMap(stats);
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
		if (p == null) {
			return null;
		}
		int x = p.getX();
		int y = p.getY();
		if (x < 0 || x > rooms.length - 1) {
			return null;
		}
		if (y < 0 || y > rooms[0].length - 1) {
			return null;
		}
		return rooms[x][y];
	}

	public synchronized void addVisibilityModifier(JDPoint p, VisibilityModifier mod) {
		final RoomObservationStatus statusObject = getStatusObject(p);

		// fill scout cache for faster removal of scout-based vis-modifiers
		if (mod instanceof ScoutResult) {
			ScoutResult scoutResult = ((ScoutResult) mod);
			final Position pos = scoutResult.getPosition();
			if (scoutCache.containsKey(pos) && scoutCache.get(pos) != null) {
				scoutCache.get(pos).add(p);
			}
			else {
				Set<JDPoint> set = new HashSet<>();
				set.add(p);
				scoutCache.put(pos, set);
			}
		}

		// finally plug modifier
		statusObject.addVisibilityModifier(mod);
		ControlUnit control = f.getControl();
		if (control != null) {
			control.notifyVisibilityStatusIncrease(p);
		}
	}

	public void setVisibilityStatus(int x, int y, int status) {
		RoomObservationStatus currentStatus = rooms[x][y];
		if (currentStatus.getVisibilityStatus() < status) {
			// notify figure that new information is revealed
			ControlUnit control = f.getControl();
			if (control != null) {
				control.notifyVisibilityStatusIncrease(new JDPoint(x, y));
			}
		}

		rooms[x][y].setVisibilityStatus(status);
		synchronized (cache) {
			cache.add(rooms[x][y]);
		}
	}

	public void resetTemporalVisibilities() {
		synchronized (cache) {
			for (RoomObservationStatus roomObservationStatus : cache) {
				roomObservationStatus.resetVisibilityStatus();
			}
			cache.clear();
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
		if (x >= rooms.length || y >= rooms[0].length) {
			Log.warning("invalid room coordinates for getVisibilityStatus: x: " + x + " y: " + y);
			return -1;
		}
		if (rooms[x][y] != null) {
			return rooms[x][y].getVisibilityStatus();
		}
		else {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
	}

	public int getDiscoveryStatus(int x, int y) {
		if (x >= rooms.length || y >= rooms[0].length) {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
		if (rooms[x][y] != null) {
			return rooms[x][y].getDiscoveryStatus();
		}
		else {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
	}

	public void resetVisibilityStatus(JDPoint p) {
		resetVisibilityStatus(p.getX(), p.getY());
	}

	public int getVisibilityStatus(JDPoint p) {
		if (p == null) return -1;
		return getVisibilityStatus(p.getX(), p.getY());
	}

	public int getDiscoveryStatus(JDPoint p) {
		return getDiscoveryStatus(p.getX(), p.getY());
	}

	@Deprecated
	public void setDiscoveryStatus(JDPoint p, int status) {
		setDiscoveryStatus(p.getX(), p.getY(), status);
	}

	public void setVisibilityStatus(JDPoint p, int status) {
		if (dungeon.getRooms()[p.getX()][p.getY()].isWall()) {
			Log.warning("Setting room visibility for wall room");
		}
		setVisibilityStatus(p.getX(), p.getY(), status);
	}

	/**
	 * @return Returns the dungeon.
	 */
	public Dungeon getDungeon() {
		return dungeon;
	}

	/**
	 * @param dungeon The dungeon to set.
	 */
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public RoomObservationStatus[][] getRooms() {
		return rooms;
	}

	public void removeScoutedVisibility(Position position) {
		final Set<JDPoint> scoutedRoomsFromThisPosition = scoutCache.get(position);
		if (scoutedRoomsFromThisPosition != null) {
			for (JDPoint scoutedRoomNumber : scoutedRoomsFromThisPosition) {
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
				visStatus.resetVisibilityStatus();
			}
			// finally also clear cache
			scoutCache.remove(position);
		}
	}
}
