/**
 * 
 * Abstrakte Superklasse fuer ein Quest
 */

package dungeon.quest;
import java.util.*;

public abstract class Quest {
	
	LinkedList rooms;
	
	public Quest () {
		
	}
	
	public abstract void turn();
	
	public abstract void action();
	
	
	

}
