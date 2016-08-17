
package dungeon;
import java.util.Comparator;

/**
 * Vergleicht 2 R�ume bzgl. der Anzahl ihrer Tueren
 *
 */
public class DoorCountRoomComparator implements Comparator<Room> {
	@Override
	public int compare(Room o1, Room o2) {
				Room r1 = (o1);
				int value1 = (r1.getDoorCount());
				Room r2 = (o2);
				int value2 = (r2.getDoorCount());
				if(value1 < value2) {
					return 1;
				}
				else {
					return -1;	
				}
			
		
			}
			

}
