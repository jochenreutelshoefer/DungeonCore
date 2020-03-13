/*
 * Created on 23.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.monster;

import dungeon.Path;
import item.ItemInfo;

import java.util.List;

import dungeon.JDPoint;
import dungeon.util.DungeonUtils;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;

public class MonsterInfo extends FigureInfo {
	
	
	private final Monster monster;
	
	public MonsterInfo(Monster monster, DungeonVisibilityMap stats) {
		super(monster,stats);
		this.monster = monster;
	}
	
	public static MonsterInfo makeMonsterInfo(Monster m, DungeonVisibilityMap map) {
		if(m == null) {
			return null;
		}
		return new MonsterInfo(m, map);
	}
	
	@Override
	public Path getShortestWayFromTo(JDPoint p1, JDPoint p2) {
		return DungeonUtils.findShortestPath(this.monster.getActualDungeon(), p1, p2, this.map, false);
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
