/**
 * 
 * Abstrakte Superklasse fuer ein Quest
 */

package de.jdungeon.dungeon.quest;
import java.util.*;

public abstract class Quest {
	
	private List rooms;
	
	public Quest () {
		
	}
	
	public abstract void turn();
	
	public abstract void action();
	
	
	

}
