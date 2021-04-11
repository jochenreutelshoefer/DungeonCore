package de.jdungeon.graphics.util;

public class RoomSize {
	
	public static int by(int divisor, int roomSize) {
		return  (int) Math.round(((double)roomSize) / divisor);
	}

}
