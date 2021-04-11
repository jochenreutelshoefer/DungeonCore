/*
 * Created on 26.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.dungeon;

import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.memory.MemoryObject;
import de.jdungeon.game.RoomInfoEntity;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.util.JDColor;

public class PositionInRoomInfo extends RoomInfoEntity {

	private final Position pos;

	public PositionInRoomInfo(Position p, DungeonVisibilityMap map) {
		super(map);
		pos = p;
	}

	public int getIndex() {
		return pos.getIndex();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof PositionInRoomInfo)) return false;
		return pos.equals(((PositionInRoomInfo) obj).pos);
	}

	public RouteInstruction.Direction getPossibleFleeDirection() {
		return pos.getPossibleFleeDirection();
	}

	public JDPoint getLocation() {
		return pos.getRoomNumber();
	}

	public int getNextIndex() {
		return pos.getNext().getIndex();
	}

	public boolean isOccupied() {
		return pos.getFigure() != null;
	}

	public int getPreviousIndex() {
		return pos.getPrevious().getIndex();
	}

	public FigureInfo getFigure() {
		if (pos.getFigure() == null) {
			return null;
		}
		else {
			return FigureInfo.makeFigureInfo(pos.getFigure(), map);
		}
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[3];
		p[0] = new Paragraph("Position " + pos.getIndex());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);

		p[1] = new Paragraph("Pos " + pos.getIndex() + " in Raum " + pos.getRoom().getRoomNumber());
		p[1].setSize(14);
		p[1].setCentered();

		String s3 = new String();
		if (pos.getFigure() == null) {
			s3 += "frei";
		}
		else {
			s3 += pos.getFigure().getName();
		}

		p[2] = new Paragraph(s3);
		p[2].setSize(14);
		p[2].setCentered();
		return p;
	}

	@Override
	public MemoryObject getMemoryObject(FigureInfo info) {

		return null;
	}

	@Override
	public Collection<PositionInRoomInfo> getInteractionPositions() {
		// we return itself and the two neighbour positions
		Collection<PositionInRoomInfo> result = new HashSet<>();
		DungeonVisibilityMap map = this.getMap();
		Room room = map.getDungeon().getRoom(this.getLocation());
		result.add(new PositionInRoomInfo(room.getPositions()[this.getPreviousIndex()], map));
		result.add(new PositionInRoomInfo(room.getPositions()[this.getNextIndex()], map));
		result.add(this);
		return result;
	}
}
