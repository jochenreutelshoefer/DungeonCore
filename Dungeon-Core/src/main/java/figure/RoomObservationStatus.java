/*
 * Created on 24.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import dungeon.Room;
import game.ControlUnit;
import game.JDEnv;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import dungeon.JDPoint;
import log.Log;

public class RoomObservationStatus {
	
	public static final RoomObservationStatus memoryStatus = new RoomObservationStatus(null,null);

	// todo: refactor to ordered enum
	public static final int VISIBILITY_FOUND = 1;

	public static final int VISIBILITY_ITEMS = 4;
	// todo: visibility degree items and figures is equal
	public static final int VISIBILITY_FIGURES = 3;

	public static final int VISIBILITY_SECRET = 5;
	// todo: visibility degree found and shrine is equal
	public static final int VISIBILITY_SHRINE = 2;

	public static final int VISIBILITY_UNDISCOVERED = 0;

	private int visibilityStatus = 0;

	private int discoveryStatus = 0;

	private final JDPoint point;
	
	private final Figure figure;
	
	private final List<VisibilityModifier> visibilityModifier = new Vector<>();

	public RoomObservationStatus(Figure figure, JDPoint p) {
		this.figure = figure;
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
			final ControlUnit control = figure.getControl();
			if(control != null) {
				control.notifyVisibilityStatusDecrease(point);
			}
		}

		/*
		 * set new visibility status
		 */
		visibilityStatus = max;
		Room room = figure.getActualDungeon().getRoom(point);
		if(room != null) {
			room.setObserverStatus(figure, visibilityStatus);
		} else {
			Log.warning("Could not find room for Point: "+ point);

		}
	}

	private int getMaxVisModifierValue(int max) {
		synchronized (visibilityModifier) {

			for (Iterator<VisibilityModifier> iter = visibilityModifier.iterator(); iter.hasNext();) {
				VisibilityModifier element = iter.next();
				if(!element.stillValid()) {
					iter.remove();
					continue;
				}
				if(element.getVisibilityStatus() > max) {
					max = element.getVisibilityStatus();
				}
			}
			return max;
		}
	}


	public int getVisibilityStatus() {
		return Math.max(discoveryStatus, getMaxVisModifierValue(VISIBILITY_UNDISCOVERED));
	}

	public  synchronized void setVisibilityStatus(int newVisbility) {
		this.visibilityStatus = newVisbility;
		discoveryStatus = Math.min(VISIBILITY_SHRINE, Math.max(discoveryStatus, newVisbility));
		if(figure != null) {
			// if game is not yet running figure is null
			final Room room = figure.getActualDungeon().getRoom(point);
			room.setObserverStatus(figure, newVisbility);
		}

	}

	synchronized void addVisibilityModifier(VisibilityModifier mod) {
		if(!visibilityModifier.contains(mod)) {
			visibilityModifier.add(mod);
			this.setVisibilityStatus(mod.getVisibilityStatus());
		}
	}

	public synchronized boolean removeVisibilityModifier(Object o) {
		boolean remove = visibilityModifier.remove(o);
		figure.getActualDungeon().getRoom(point).setObserverStatus(figure, getVisibilityStatus());
		return remove;
	}

}
