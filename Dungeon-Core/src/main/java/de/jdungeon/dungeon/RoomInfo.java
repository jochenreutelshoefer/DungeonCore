/*
 * Created on 13.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.dungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.switchPos.SwitchPositionRequestManager;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.memory.RoomMemory;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.location.LocationInfo;
import de.jdungeon.util.JDColor;

public class RoomInfo extends RoomInfoEntity implements ItemInfoOwner {

	private final Room r;

	public Room getRoom() {
		return r;
	}

	public RoomInfo(Room r, DungeonVisibilityMap stats) {
		super(stats);
		this.r = r;
	}

	public static RoomInfo makeRoomInfo(Room r, DungeonVisibilityMap stats) {
		if (r == null) {
			return null;
		}

		return new RoomInfo(r, stats);
	}

	@Override
	public RoomMemory getMemoryObject(FigureInfo info) {
		return (RoomMemory) r.getMemoryObject(info);
	}

	public DoorInfo getDoor(int dir) {
		Door door = r.getDoor(dir);
		if(door == null) return null;
		return new DoorInfo(door, map);
	}

	public DoorInfo getDoor(RouteInstruction.Direction dir) {
		Door door = r.getDoor(dir);
		if (door != null) {
			return new DoorInfo(door, map);
		}
		return null;
	}


	public Boolean hasConnectionTo(RoomInfo r1) {
		return r.hasConnectionTo(r1.getRoomNumber());
	}

	public int getConnectionDirectionTo(RoomInfo r1) {
		return r.getConnectionDirectionTo(r1);
	}

	public Collection<SwitchPositionRequestManager.SwitchPosRequest> getSwitchPosRequests(FigureInfo requestedFigure) {
		Figure figure = getRoom().getDungeon().getFigureIndex().get(requestedFigure.getFigureID());
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return figure.getDungeon().getSwitchPositionRequestManager().getSwitchPosRequests(figure);
		}
		return null;
	}

	@Override
	public String toString() {
		return r.toString();
	}

	@Override
	public int hashCode() {
		return r.getX() + r.getY();
	}

	public int getDirectionTo(RoomInfo other) {
		return r.getConnectionDirectionTo(other);
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof RoomInfo) {

			boolean b = ((RoomInfo) o).r.equals(r);

			return b;
		}
		return false;
	}

	public JDPoint getNumber() {
		return r.getNumber();
	}

	@Override
	public Paragraph[] getParagraphs() {

		String room = new String();
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FOUND) {

			room = JDEnv.getResourceBundle().getString("room") + ": "
					+ getNumber().toString();
		}
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(room);
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();

		String shrine = new String();
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_SHRINE) {
			if (getLocation() != null) {
				shrine = getLocation().toString();
			}
		}
		p[1] = new Paragraph(shrine);
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();

		String monster = new String();
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			monster = JDEnv.getResourceBundle().getString("figures") + ": "
					+ getFigureInfos().size();
		}
		p[2] = new Paragraph(monster);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);

		String itemS = new String();
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_ITEMS) {

			itemS = JDEnv.getResourceBundle().getString("items") + ": "
					+ cntEntrys(getItemArray());
		}
		p[3] = new Paragraph(itemS);
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(JDColor.black);

		return p;
	}

	private int cntEntrys(Object[] a) {
		if (a == null) {
			return 0;
		}
		int cnt = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != null) {
				cnt++;
			}
		}
		return cnt;
	}

	public Boolean fightRunning() {
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return Boolean.valueOf(r.fightRunning());
		}
		return null;
	}

	public List<MonsterInfo> getMonsterInfos() {

		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			List<Figure> l = r.getRoomFigures();
			List<MonsterInfo> res = new LinkedList<MonsterInfo>();
			for (Iterator<Figure> iter = l.iterator(); iter.hasNext(); ) {
				Figure element = iter.next();
				if (element instanceof Monster) {
					res.add(MonsterInfo.makeMonsterInfo((Monster) element, map));
				}
			}
			return res;
		}
		return null;
	}

	public PositionInRoomInfo getPositionInRoom(int index) {
		return new PositionInRoomInfo(r.getPositions()[index], map);
	}

	public List<FigureInfo> getFigureInfos() {

		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			List<FigureInfo> res = new LinkedList<FigureInfo>();
			for (int i = 0; i < 8; i++) {
				if (r.getPositions()[i].getFigure() != null) {
					Figure f = r.getPositions()[i].getFigure();
					// todo: harmonize - treat equally
					if (f instanceof Monster) {
						res.add(MonsterInfo.makeMonsterInfo((Monster) f, map));
					}
					else {
						res.add(new HeroInfo((Hero) f, map));
					}
				}
			}
			return res;
		}
		return Collections.emptyList();
	}

	public List<FigureInfo> getDeadFigureInfos() {
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			final Collection<Figure> deadFigures = r.getDeadFigures();
			List<FigureInfo> res = new LinkedList<FigureInfo>();
			for (Figure f : deadFigures) {
				// todo: harmonize - treat equally
				if (f instanceof Monster) {
					res.add(MonsterInfo.makeMonsterInfo((Monster) f, map));
				}
				else {
					res.add(new HeroInfo((Hero) f, map));
				}
			}
			return res;
		}
		return Collections.emptyList();
	}

	public JDPoint getPoint() {
		return r.getPoint();
	}

	public int getVisibilityStatus() {

		return map.getVisibilityStatus(r.getRoomNumber());
	}

	public int getFloorIndex() {
		return r.getFloorIndex();
	}


	public LocationInfo getLocation() {

		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_SHRINE) {

			if (r.getLocation() != null) {
				return new LocationInfo(r.getLocation(), map);
			}
		}
		return null;
	}

	@Deprecated
	@Override
	public ItemInfo[] getItemArray() {

		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_ITEMS) {

			Item[] itArray = r.getItemArray();
			ItemInfo[] wrappedIts = new ItemInfo[itArray.length];
			for (int i = 0; i < itArray.length; i++) {
				if (itArray[i] != null) {
					wrappedIts[i] = (ItemInfo) itArray[i].makeInfoObject(map);
				}
			}
			return wrappedIts;
		}
		return null;
	}

	@Override
	public List<ItemInfo> getItems() {
		List<ItemInfo> result = null;
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_ITEMS) {
			result = new ArrayList<>();
			List<Item> items = r.getItems();
			for (Item item : items) {
				if(item != null) {
					result.add((ItemInfo) item.makeInfoObject(this.map));
				}
			}
		}
		return result;
	}

	public JDPoint getRoomNumber() {
		return r.getRoomNumber();
	}

	@Deprecated // try to treat all figures similarly
	public HeroInfo getHeroInfo() {

		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			Collection figures = getFigureInfos();
			for (Iterator iter = figures.iterator(); iter.hasNext(); ) {
				FigureInfo element = (FigureInfo) iter.next();
				if (element instanceof HeroInfo) {
					return (HeroInfo) element;
				}
			}
		}
		return null;
	}

	@Deprecated // all figures should be treated similarly
	public List<HeroInfo> getHeroInfos() {
		if (map.getVisibilityStatus(r.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			Collection figures = getFigureInfos();
			List<HeroInfo> result = new ArrayList<>();
			for (Iterator iter = figures.iterator(); iter.hasNext(); ) {
				FigureInfo element = (FigureInfo) iter.next();
				if (element instanceof HeroInfo) {
					result.add((HeroInfo) element);
				}
			}
			return result;
		}
		return Collections.emptyList();
	}

	public ChestInfo getChest() {

		if (map.getVisibilityStatus(r.getRoomNumber()) < RoomObservationStatus.VISIBILITY_SHRINE) {
			return null;
		}
		if (r.getChest() == null) {
			return null;
		}
		else {
			return (ChestInfo) r.getChest().makeInfoObject(map);
		}
	}

	public DoorInfo[] getDoors() {

		if (map.getVisibilityStatus(r.getRoomNumber()) < RoomObservationStatus.VISIBILITY_FOUND) {
			return null;
		}
		Door[] doors = r.getDoors();
		DoorInfo[] infos = new DoorInfo[4];
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				infos[i] = new DoorInfo(doors[i], map);
			}
		}
		return infos;
	}

	public RoomInfo getNeighbourRoom(int dir) {
		return makeRoomInfo(r.getNeighbourRoom(dir), map);
	}

	public RoomInfo getNeighbourRoom(RouteInstruction.Direction dir) {
		return makeRoomInfo(r.getNeighbourRoom(dir), map);
	}

	@Override
	public Collection<PositionInRoomInfo> getInteractionPositions() {
		Collection<PositionInRoomInfo> result = new HashSet<>();
		Collection<Position> positions = r.getInteractionPositions();
		for (Position position : positions) {
			result.add(new PositionInRoomInfo(position, map));
		}
		return result;
	}
}
