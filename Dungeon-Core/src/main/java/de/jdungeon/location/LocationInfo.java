/*
 * Created on 07.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.location;

import java.util.Collection;
import java.util.Collections;

import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.memory.ShrineMemory;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.util.JDColor;
import de.jdungeon.dungeon.JDPoint;

public class LocationInfo extends RoomInfoEntity {

	private final Location s;

	public LocationInfo(Location s, DungeonVisibilityMap map) {
		super(map);
		this.s = s;
	}

	public Class<? extends Location> getShrineClass() {
		return s.getClass();
	}

	public LocationState getState() {
		if (map.getDiscoveryStatus(getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE) {
			return s.getState();
		}
		return null;
	}

	public static LocationInfo makeLocationInfo(Location location, DungeonVisibilityMap map) {
		return new LocationInfo(location, map);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof LocationInfo))
			return false;

		return s.equals(((LocationInfo) obj).s);
	}

	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@Deprecated
	@Override
	public ShrineMemory getMemoryObject(FigureInfo info) {
		return null;
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
	public String getStatus() {
		return getState().getStateText();
	}

	@Override
	public String getRole() {
		return getText();
	}

	@Override
	public String getDescription() {
		return this.s.getStory();
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
			return s.getRoomNumber();
	}

	@Override
	public Collection<PositionInRoomInfo> getInteractionPositions() {
		return Collections.singletonList(new PositionInRoomInfo(s.getRoom().getPositions()[2], this.map));
	}
}
