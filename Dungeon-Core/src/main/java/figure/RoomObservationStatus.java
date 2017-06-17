/*
 * Created on 24.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import game.JDEnv;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.JDPoint;
import dungeon.util.RouteInstruction;

public class RoomObservationStatus {
	
	public static final RoomObservationStatus memoryStatus = new RoomObservationStatus(null,null);

	// todo: refactor to ordered enum
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

	public synchronized List<VisibilityModifier> getVisibilityModifier() {
		return Collections.unmodifiableList(visibilityModifier);
	}

	/**
	 * @return Returns the discoverageStatus.
	 */
	public int getDiscoveryStatus() {
		return discoveryStatus;
	}

	@Deprecated // should not be used, use setVisibilityStatus
	public void setDiscoveryStatus(int status) {
		if (status > this.discoveryStatus) {
			this.discoveryStatus = status;
		}
	}

	public synchronized void resetVisibilityStatus() {
		
		/*
		 * determine current maximum visibility level
		 */
		int max = VISIBILITY_FOUND;
		if(discoveryStatus >= VISIBILITY_SHRINE) {
			max = VISIBILITY_SHRINE;
		}
		max = getMaxVisModifierValue(max);

		/*
		 * notify control unit
		 */
		if(max < VISIBILITY_FIGURES) {
			// TODO: shouldn't we compare to the previous vis level for level change notification?
			map.getFigure().getControl().notifyVisibilityStatusDecrease(point);
		}

		/*
		 * set new visibility status
		 */
		visibilityStatus = max;
		map.getDungeon().getRoom(point).setObserverStatus(map.getFigure(), visibilityStatus);
	}

	private synchronized int getMaxVisModifierValue(int max) {
		for (Iterator<VisibilityModifier> iter = visibilityModifier.iterator(); iter
				.hasNext();) {
			VisibilityModifier element = iter.next();
			if(element.getVisibilityStatus() > max) {
				max = element.getVisibilityStatus();
			}
		}
		return max;
	}

	public synchronized int getVisibilityStatus() {
		if(JDEnv.visCheat) {
			return VISIBILITY_SECRET;
		}
		return Math.max(discoveryStatus, getMaxVisModifierValue(VISIBILITY_UNDISCOVERED));
	}

	public  synchronized void setVisibilityStatus(int newVisbility) {
		this.visibilityStatus = newVisbility;
		discoveryStatus = Math.min(VISIBILITY_SHRINE, Math.max(discoveryStatus, newVisbility));
		map.getDungeon().getRoom(point).setObserverStatus(map.getFigure(), newVisbility);
		
	}

	public synchronized void addVisibilityModifier(VisibilityModifier mod) {
		if(!visibilityModifier.contains(mod)) {
			visibilityModifier.add(mod);
			this.setVisibilityStatus(mod.getVisibilityStatus());
		}
	}

	public synchronized boolean removeVisibilityModifier(Object o) {
		map.getDungeon().getRoom(point).setObserverStatus(map.getFigure(), getVisibilityStatus());
		return visibilityModifier.remove(o);
	}

}
