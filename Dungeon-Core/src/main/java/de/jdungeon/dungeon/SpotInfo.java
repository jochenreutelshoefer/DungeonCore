/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.dungeon;

import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.memory.SpotMemory;
import de.jdungeon.gui.Paragraph;



public class SpotInfo extends de.jdungeon.game.InfoEntity {

	private HiddenSpot spot;
	
	public SpotInfo(HiddenSpot spot, DungeonVisibilityMap stats) {
		super(stats);
		this.spot = spot;
	}
	
	public Paragraph[] getParagraphs() {
		return null;
	}
	
	public SpotMemory getMemoryObject(FigureInfo info) {
		return (SpotMemory)spot.getMemoryObject(info);
	}
	
}
