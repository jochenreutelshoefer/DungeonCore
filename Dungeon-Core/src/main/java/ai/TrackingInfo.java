package ai;

import dungeon.JDPoint;

public class TrackingInfo {
	
	public static final int LEVEL_SURE = 5;
	
	private Object o;
	private int round;
	private JDPoint location;
	
	public TrackingInfo(Object o, int round, JDPoint location) {
		this. o = 0;
		this.round = round;
		this.location = location;
	}

	public JDPoint getLocation() {
		return location;
	}

	public Object getObject() {
		return o;
	}

	public int getRound() {
		return round;
	}
	
	

}
