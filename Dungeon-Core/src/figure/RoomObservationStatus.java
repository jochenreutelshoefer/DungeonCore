/*
 * Created on 24.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import game.JDEnv;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.JDPoint;
import dungeon.RouteInstruction;

public class RoomObservationStatus {
	
	public static final RoomObservationStatus memoryStatus = new RoomObservationStatus(null,null);

	public static final int VISIBILITY_FOUND = 1;

	public static final int VISIBILITY_ITEMS = 4;

	public static final int VISIBILITY_FIGURES = 3;

	public static final int VISIBILITY_SECRET = 5;

	public static final int VISIBILITY_SHRINE = 2;

	public static final int VISIBILITY_UNDISCOVERED = 0;

	private int visibilityStatus = 0;

	private int discoveryStatus = 0;

	private final JDPoint point;
	
	private final DungeonVisibilityMap map;
	
	private final List<VisibilityModifier> visibilityModifier = new LinkedList<VisibilityModifier>();

	public RoomObservationStatus(DungeonVisibilityMap map, JDPoint p) {
		this.map = map;
		this.point = p;
	}

	/**
	 * @return Returns the discoverageStatus.
	 */
	public int getDiscoveryStatus() {
		return discoveryStatus;
	}

	/**
	 * @param discoverageStatus
	 *            The discoverageStatus to set.
	 */
	public void setDiscoveryStatus(int status) {
		if (status > this.discoveryStatus) {
			this.discoveryStatus = status;
		}
	}

	public void resetVisibilityStatus() {
		
		/*
		 * determine current maximum visibility level
		 */
		int max = VISIBILITY_FOUND;
		if(discoveryStatus >= VISIBILITY_SHRINE) {
			max = VISIBILITY_SHRINE;
		}
		for (Iterator<VisibilityModifier> iter = visibilityModifier.iterator(); iter
				.hasNext();) {
			VisibilityModifier element = iter.next();
			if(element.getVisibilityStatus() > max) {
				
				max = element.getVisibilityStatus();
			}
		}

		/*
		 * notify control unit
		 */
		if(max < VISIBILITY_FIGURES) {
			map.getFigure().getControl().notifyVisbilityStatusDecrease(point);
		}

		/*
		 * set new visibility status
		 */
		visibilityStatus = max;
		map.getDungeon().getRoom(point).setObserverStatus(map.getFigure(), visibilityStatus);
	}
	
	public RoomObservationStatus getNeighbour(int dir) {
		if(dir == RouteInstruction.NORTH) {
			if(point.getY() <= 0) {
				return null;
			}
			return map.getStatusObject(JDPoint.getPoint(point.getX(),point.getY()-1));
		}
		if(dir == RouteInstruction.SOUTH) {
			if(point.getY() >= map.getSizeY()-1) {
				return null;
			}
			return map.getStatusObject(JDPoint.getPoint(point.getX(),point.getY()+1));
		}
		if(dir == RouteInstruction.WEST) {
			if(point.getX() <= 0) {
				return null;
			}
			return map.getStatusObject(JDPoint.getPoint(point.getX()-1,point.getY()));
		}
		if(dir == RouteInstruction.EAST) {
			if(point.getX() >= map.getSizeX()-1) {
				return null;
			}
			return map.getStatusObject(JDPoint.getPoint(point.getX()+1,point.getY()));
		}
		return null;
	}

	/**
	 * @return Returns the visibilityStatus.
	 */
	public int getVisibilityStatus() {
		if(JDEnv.visCheat) {
			return VISIBILITY_SECRET;
		}
		return visibilityStatus;
	}

	/**
	 * @param visibilityStatus
	 *            The visibilityStatus to set.
	 */
	public void setVisibilityStatus(int visibilityStatus) {
		this.visibilityStatus = visibilityStatus;
		if(visibilityStatus > discoveryStatus) {
			discoveryStatus = visibilityStatus;
		}
		map.getDungeon().getRoom(point).setObserverStatus(map.getFigure(), visibilityStatus);
		
	}

	public void addVisibilityModifier(VisibilityModifier mod) {
		if(!visibilityModifier.contains(mod)) {
			visibilityModifier.add(mod);
			mod.getVisibilityStatus();
			this.setVisibilityStatus(mod.getVisibilityStatus());
		}
	}

	public boolean removeVisibilityModifier(Object o) {
		
		return visibilityModifier.remove(o);
	}

}
