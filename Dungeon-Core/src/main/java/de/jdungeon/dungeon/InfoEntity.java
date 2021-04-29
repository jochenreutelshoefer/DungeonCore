/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.dungeon;

import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.memory.MemoryProvider;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.gui.Paragraphable;

public abstract class InfoEntity implements Paragraphable, MemoryProvider {

	public DungeonVisibilityMap getVisMap() {
		return map;
	}

	protected DungeonVisibilityMap map;
	
	protected InfoEntity(DungeonVisibilityMap m) {
		map = m;
	}
	
	@Override
	public abstract Paragraph[] getParagraphs();


	
}
