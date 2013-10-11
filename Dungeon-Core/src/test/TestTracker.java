/*
 * Created on 02.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package test;
import java.util.*;

import dungeon.*;

public class TestTracker {

	/**
	 * 
	 * @uml.property name="location"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	Room location;

	/**
	 * 
	 * @uml.property name="way"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="dungeon.Room"
	 */
	LinkedList way = new LinkedList();

	public TestTracker(Room r) {
		location = r;
		// TODO Auto-generated constructor stub
	}
	
	public TestTracker() {
		
	}

	/**
	 * @return
	 * 
	 * @uml.property name="location"
	 */
	public Room getLocation() {
		return location;
	}

	/**
	 * @param room
	 * 
	 * @uml.property name="location"
	 */
	public void setLocation(Room room) {
		way.add(location);
		location = room;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="way"
	 */
	public LinkedList getWay() {
		return way;
	}

}
