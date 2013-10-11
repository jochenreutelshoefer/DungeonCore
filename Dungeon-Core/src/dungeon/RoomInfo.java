/*
 * Created on 13.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.memory.RoomMemory;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import game.InfoEntity;
import game.JDEnv;
import gui.Paragraph;
import item.Item;
import item.ItemInfo;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import shrine.ShrineInfo;

public class RoomInfo extends InfoEntity  {

	private Room r;

	//private RoomObservationStatus visStats;

	public Room getRoom() {
		return r;
	}

	public RoomInfo(Room r, DungeonVisibilityMap stats) {
		super(stats);
		this.r = r;
		//this.visStats = stats.getStatusObject(this.getLocation());
	}

	public static RoomInfo makeRoomInfo(Room r, DungeonVisibilityMap stats) {
		if (r != null) {
			return new RoomInfo(r, stats);
		}
		return null;
	}
	
	public RoomMemory getMemoryObject(FigureInfo info) {
		return (RoomMemory)r.getMemoryObject(info);
	}
	
	public DoorInfo getDoor(int dir) {
		return new DoorInfo(r.getDoor(dir),map);
	}

	public Boolean hasHero() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return new Boolean(r.hasHero());
		}
		return null;

	}
	
	public Boolean hasConnectionTo(RoomInfo r1) {
		return r.hasConnectionTo(r1.getLocation());
	}

	public int getConnectionDirectionTo(RoomInfo r1) {
		return r.getConnectionDirectionTo(r1);
	}
	
	public String toString() {
		return r.toString();
	}
	
	

	public int hashCode() {
		return r.getX() + r.getY();
	}

	public int getDirectionTo(RoomInfo other) {
		return r.getConnectionDirectionTo(other);
	}

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

	public Paragraph[] getParagraphs() {
		
		String room = new String();
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND
				|| JDEnv.visCheat) {

			room = JDEnv.getResourceBundle().getString("room") + ": "
					+ getNumber().toString();
			if (JDEnv.visCheat) {
				if (r.getHall() != null) {
					room += "\n Halle: " + r.getHall().getName();
				}
			}

		}
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(room);
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(Color.orange);
		p[0].setBold();

		String shrine = new String();
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE) {
			if (getShrine() != null) {
				shrine = getShrine().toString();
			}
		}
		p[1] = new Paragraph(shrine);
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(Color.black);
		p[1].setBold();

		String monster = new String();
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			monster = JDEnv.getResourceBundle().getString("figures") + ": "
					+ getFigureInfos().size();

		}
		p[2] = new Paragraph(monster);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(Color.black);

		String itemS = new String();
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_ITEMS) {

			itemS = JDEnv.getResourceBundle().getString("items") + ": "
					+ cntEntrys(getItems());

		}
		p[3] = new Paragraph(itemS);
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(Color.black);

		return p;
	}

	private int cntEntrys(Object[] a) {
		if(a == null) {
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
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return new Boolean(r.fightRunning());
		}
		return null;
	}

	public List getMonsterInfos() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			List l = r.getRoomFigures();
			List res = new LinkedList();
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				Figure element = (Figure) iter.next();
				if (element instanceof Monster) {
					res.add(MonsterInfo.makeMonsterInfo((Monster) element,
									map));

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
	
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			List<FigureInfo> res = new LinkedList<FigureInfo>();
			for (int i = 0; i < 8; i++) {
				if (r.getPositions()[i].getFigure() != null) {
					Figure f = r.getPositions()[i].getFigure();
					if (f instanceof Monster) {
						res.add(MonsterInfo.makeMonsterInfo((Monster) f, map));
					} else {
						res.add(new HeroInfo((Hero) f, map));
					}
				}
			}
			return res;
		}
		return null;
	}

	public JDPoint getPoint() {
		return r.getPoint();
	}
	
	

	public int getVisibilityStatus() {
		
		return map.getVisibilityStatus(r.getLocation());

	}
	
	public List getNeighboursWithDoor() {
		List l = r.getNeighboursWithDoor();
		List res = new LinkedList();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Room element = (Room) iter.next();
			res.add(RoomInfo.makeRoomInfo(element,map));
			
		}
		return res;
	}

	public int getFloorIndex() {
		return r.getFloorIndex();
	}

	public boolean isPart_scouted() {
		return r.isPart_scouted();

	}

	public Door getConnectionTo(RoomInfo otherRoom) {
		
		if (map.getVisibilityStatus(r.getLocation()) < RoomObservationStatus.VISIBILITY_FOUND
				&& !JDEnv.visCheat) {
			return null;
		}
		return r.getConnectionTo(otherRoom.getLocation());
	}

	public HiddenSpot getSpot() {
		return r.getSpot();
	}

	public ShrineInfo getShrine() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE) {

			if (r.getShrine() != null) {
				return new ShrineInfo(r.getShrine(), map);
			}
		}
		return null;
	}
	
	public ItemInfo[] getItemArray() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_ITEMS) {

			Item [] itArray = r.getItemArray();
			ItemInfo[] wrappedIts = new ItemInfo[itArray.length];
			for (int i = 0; i < itArray.length; i++) {
				if(itArray[i]!= null) {
					wrappedIts[i] = (ItemInfo) ((Item)itArray[i]).makeInfoObject(map);
				}
			}
			return wrappedIts;
		}
		return null;
	}
	
	public int getItemCnt() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_ITEMS) {

			Item [] itArray = r.getItemArray();
			ItemInfo[] wrappedIts = new ItemInfo[itArray.length];
			int items = 0;
			for (int i = 0; i < itArray.length; i++) {
				if(itArray[i]!= null) {
					wrappedIts[i] = (ItemInfo) ((Item)itArray[i]).makeInfoObject(map);
					items++;
				}
			}
			return items;
		}
		return -1;
	}

	public ItemInfo[] getItems() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_ITEMS) {

			List l = r.getItems();
			ItemInfo[] wrappedIts = new ItemInfo[l.size()];
			for (int i = 0; i < l.size(); i++) {
				wrappedIts[i] = (ItemInfo) ((Item)l.get(i)).makeInfoObject(map);
			}
			return wrappedIts;
		}
		return null;
	}

	public JDPoint getLocation() {
		return r.getLocation();
	}

	// public Hero getHero() {
	// return r.getHero();
	// }

	public HeroInfo getHeroInfo() {
		
		if (map.getVisibilityStatus(r.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			Collection figures = getFigureInfos();
			for (Iterator iter = figures.iterator(); iter.hasNext();) {
				FigureInfo element = (FigureInfo) iter.next();
				if (element instanceof HeroInfo) {
					return (HeroInfo) element;
				}

			}
		}
		return null;
	}

	public ChestInfo getChest() {
		
		if (map.getVisibilityStatus(r.getLocation()) < RoomObservationStatus.VISIBILITY_SHRINE
				&& !JDEnv.visCheat) {
			return null;
		}
		if (r.getChest() == null) {
			return null;
		} else {
			return (ChestInfo) r.getChest().makeInfoObject(map);
		}
	}

	public boolean isClaimed() {

		return r.isClaimed();
	}

	public DoorInfo[] getDoors() {
		
		if (map.getVisibilityStatus(r.getLocation()) < RoomObservationStatus.VISIBILITY_FOUND
				&& !JDEnv.visCheat) {
			return null;
		}
		Door[] doors = r.getDoors();
		DoorInfo[] infos = new DoorInfo[4];
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null && !doors[i].isHidden()) {
				infos[i] = new DoorInfo(doors[i], map);
			}
		}
		return infos;
	}

	public RoomInfo getNeighbourRoom(int dir) {
		return makeRoomInfo(r.getNeighbourRoom(dir), map);
	}

}
