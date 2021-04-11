/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.game;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.DungeonVisibilityMap;

public interface InfoProvider {
	
	InfoEntity makeInfoObject(DungeonVisibilityMap map);

	JDPoint getRoomNumber();

}
