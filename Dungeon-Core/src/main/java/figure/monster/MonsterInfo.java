/*
 * Created on 23.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.monster;

//import game.Game;
import figure.other.Lioness;
import item.ItemInfo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.JDPoint;
import dungeon.Room;
import dungeon.util.DungeonUtils;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.other.Fir;

/**
 * 
 * Diese Klasse stellt der Agentensteuerung alle Informationen 
 * zu Verf�gung, �ber die ein Monster verf�gt.
 * Alles was das Monster "sieht" kann hier abgefragt werden.
 * 
 */
public class MonsterInfo extends FigureInfo {
	
	
	private final Monster monster;
	
	
	/**
	 * Erzeugt ein MonsterInfo Objekt f�r ein Monster
	 * 
	 * @param monster Monster
	 */
	public MonsterInfo(Monster monster, DungeonVisibilityMap stats) {
		super(monster,stats);
		this.monster = monster;
	}
	
	@Override
	public int getMight() {
		return monster.getWorth()/100;
	}
	
	public static MonsterInfo makeMonsterInfo(Monster m,DungeonVisibilityMap map) {
		if(m == null) {
			return null;
		}
		return new MonsterInfo(m,map);
	}
	
	@Override
	public List<JDPoint> getShortestWayFromTo(JDPoint p1, JDPoint p2) {
		if(!map.getFigure().equals(monster)) {
			return null;
		}
		List<Room> l = DungeonUtils.findShortestWayFromTo(
				monster.getRoom().getDungeon(), p1, p2,
				DungeonVisibilityMap.getAllVisMap(monster.getRoom().getDungeon()));
		List<JDPoint> l2 = new LinkedList<JDPoint>();
		for (Iterator<Room> iter = l.iterator(); iter.hasNext();) {
			Room element = iter.next();
			l2.add(element.getNumber());
		}
		return l2;
	}
	
	public int getLastMove() {
		if(map.getFigure().equals(monster)) {
		return monster.getLastMove();
		}
		return -1;
	}
	

	@Deprecated //use FigureInfo#getFigureClass()
	public Class<? extends Monster> getMonsterClass() {
		return this.monster.getClass();
	}
	
	@Override
	public List<ItemInfo> getAllItems() {
		return this.getFigureItemList();
	}
	
}
