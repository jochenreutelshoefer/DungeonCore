/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;

import figure.DungeonVisibilityMap;
import figure.RoomObservationStatus;
import figure.memory.MemoryObject;
import figure.memory.MemoryProvider;
import gui.Paragraph;
import gui.Paragraphable;

public abstract class InfoEntity implements Paragraphable,MemoryProvider {
	
	protected DungeonVisibilityMap map;
	
	protected InfoEntity(DungeonVisibilityMap m) {
		map = m;
	}
	
	@Override
	public abstract Paragraph[] getParagraphs();
	
}
