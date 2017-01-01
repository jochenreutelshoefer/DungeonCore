package graphics.util;

public class RoomSize {
	
	public static int by(int divisor, int roomSize) {
		return  Math.round(roomSize / divisor);
	}

}
