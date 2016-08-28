/*
 * Created on 23.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.monster;

//import game.Game;
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
	
	public final static int MONSTER_CLASS_INDEX_ORC = 1;
	public final static int MONSTER_CLASS_INDEX_OGRE = 2;
	public final static int MONSTER_CLASS_INDEX_WOLF = 3;
	public final static int MONSTER_CLASS_INDEX_SPIDER = 4;
	public final static int MONSTER_CLASS_INDEX_GHUL = 5;
	public final static int MONSTER_CLASS_INDEX_SKEL = 6;
	public final static int MONSTER_CLASS_INDEX_DWARF = 7;
	public final static int MONSTER_CLASS_INDEX_MASTER = 8;
	
	public int getMonsterClassIndex() {
		if(map.getVisibilityStatus(monster.getLocation())>= RoomObservationStatus.VISIBILITY_FIGURES) {
			if(monster instanceof Wolf) {
				return MONSTER_CLASS_INDEX_WOLF;
			}
			if(monster instanceof Spider) {
				return MONSTER_CLASS_INDEX_SPIDER;
			}
			if(monster instanceof Ghul) {
				return MONSTER_CLASS_INDEX_GHUL;
			}
			if(monster instanceof Skeleton) {
				return MONSTER_CLASS_INDEX_SKEL;
			}
			
			if(monster instanceof Orc) {
				return MONSTER_CLASS_INDEX_ORC;
			}
			if(monster instanceof Ogre) {
				return MONSTER_CLASS_INDEX_OGRE;
			}
			if(monster instanceof DarkMaster) {
				return MONSTER_CLASS_INDEX_MASTER;
			}
			if(monster instanceof Dwarf) {
				return MONSTER_CLASS_INDEX_DWARF;
			}
		
			else return 0;
		}
		return -1;
	}

	public Class<? extends Monster> getMonsterClass() {
		return this.monster.getClass();
	}
	
	public int getMonsterClassCode() {
		if(map.getVisibilityStatus(monster.getLocation())>= RoomObservationStatus.VISIBILITY_FIGURES) {
		if(monster instanceof Wolf) {
			return Monster.WOLF;
		}
		if(monster instanceof Spider) {
			return Monster.BEAR;
		}
		if(monster instanceof Ghul) {
			return Monster.GHUL;
		}
		if(monster instanceof Skeleton) {
			return Monster.SKELETON;
		}
		if(monster instanceof Orc) {
			return Monster.ORC;
		}
		if(monster instanceof Ogre) {
			return Monster.OGRE;
		}
		if(monster instanceof DarkMaster) {
			return Monster.DARKMASTER;
		}
		if(monster instanceof Dwarf) {
			return Monster.DWARF;
		}
		if(monster instanceof Fir) {
			return Monster.FIR;
		}
	
		else return 0;
	}
	return -1;
	}
	
	
	@Override
	public List<ItemInfo> getAllItems() {
		return this.getFigureItemList();
	}
	
}
