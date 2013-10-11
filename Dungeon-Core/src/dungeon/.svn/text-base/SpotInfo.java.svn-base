/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.SpotMemory;
import gui.Paragraph;



public class SpotInfo extends game.InfoEntity {

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
