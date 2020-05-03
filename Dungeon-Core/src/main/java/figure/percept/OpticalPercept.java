/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dungeon.JDPoint;

public abstract class OpticalPercept extends Percept {

	Set<JDPoint> locations = new HashSet<>();

	public OpticalPercept(JDPoint room, int round) {
		super(round);
		locations.add(room);
	}

	public OpticalPercept(JDPoint room1, JDPoint room2, int round) {
		super(round);
		locations.add(room1);
		locations.add(room2);
	}

	public Collection<JDPoint> getLocations() {
		return Collections.unmodifiableCollection(locations);
	}

	public JDPoint getPoint() {
		if(locations.isEmpty()) return null;
		return locations.iterator().next();
	}

	@Override
	public String toString() {
		return super.toString()+ " "+ this.getClass().getName()+ " " + locations;
	}
}
