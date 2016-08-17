/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;

import dungeon.JDPoint;
import figure.DungeonVisibilityMap;
import figure.memory.MemoryObject;
import gui.Paragraphable;

public interface InfoProvider {
	
	public InfoEntity makeInfoObject(DungeonVisibilityMap map);
	
	
	
	public JDPoint getLocation();

}
