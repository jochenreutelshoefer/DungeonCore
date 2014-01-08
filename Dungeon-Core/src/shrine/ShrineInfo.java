/*
 * Created on 07.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package shrine;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.memory.ShrineMemory;
import game.InfoEntity;
import gui.Paragraph;
import util.JDColor;
import dungeon.JDPoint;

public class ShrineInfo extends InfoEntity  {

	private final Shrine s;

	public ShrineInfo(Shrine s, DungeonVisibilityMap map) {
		super(map);
		this.s = s;
	}

	public int getShrineIndex() {
		JDPoint location = getLocation();
		if (location != null
				&& map.getDiscoveryStatus(location) >= RoomObservationStatus.VISIBILITY_SHRINE) {
			return s.getShrineIndex();
		}
		return -1;
	}
	
	@Override
	public ShrineMemory getMemoryObject(FigureInfo info) {
		return s.getMemoryObject(info);
	}

	public int getType() {
		if (map.getDiscoveryStatus(getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE) {

			return s.getType();
		}
		return -1;
	}

	@Override
	public String toString() {
		if (map.getDiscoveryStatus(getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE) {

			return s.toString();
		}
		return null;
	}

	@Override
	public Paragraph[] getParagraphs() {

		Paragraph[] p = new Paragraph[3];
		p[0] = new Paragraph(toString());
		p[0].setSize(20);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();

		p[1] = new Paragraph(getText());
		p[1].setSize(16);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();
		
		String str3 = "";
		if(s.getStatus() != null) {
			str3 = s.getStatus();
		}
		p[2] = new Paragraph(str3);
		p[2].setSize(16);
		p[2].setCentered();
		p[2].setColor(JDColor.black);
		p[2].setBold();

		return p;
	}

	public String getText() {
		if (map.getVisibilityStatus(getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE) {
			return s.getText();
		}
		return null;

	}

	public JDPoint getLocation() {
		// if (map.getDiscoveryStatus(s.getLocation()) >=
		// RoomObservationStatus.VISIBILITY_SHRINE) {

			return s.getLocation();
		// }
		// return null;
	}
}
