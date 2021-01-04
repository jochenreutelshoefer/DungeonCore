/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.ChestMemory;
import game.RoomInfoEntity;
import gui.Paragraph;
import item.Item;
import item.ItemInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ChestInfo extends RoomInfoEntity {
	
	private final Chest chest;
	
	public ChestInfo(Chest c, DungeonVisibilityMap stats) {
		super(stats);
		chest = c;
	}
	public boolean hasLock() {
		return chest.hasLock();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ChestInfo))
			return false;

		return chest.equals(((ChestInfo) obj).chest);
	}

	@Override
	public Paragraph[] getParagraphs() {
			return chest.getParagraphs();
	}
	
	public JDPoint getLocation() {
		return chest.getRoomNumber();
	}
	
	@Override
	public ChestMemory getMemoryObject(FigureInfo info) {
		return chest.getMemoryObject(info);
	}
	
	public ItemInfo[] getItems() {
		List l = chest.getItems();
		ItemInfo infos [] = new ItemInfo[l.size()];
		int i = 0;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			infos [i] = new ItemInfo(element,map);
			i++;
		}
		return infos;
	}

	public List<ItemInfo> getItemList() {
		return Arrays.asList(getItems());
	}

	@Override
	public int hashCode() {
		return chest != null ? chest.hashCode() : 0;
	}

	@Override
	public Collection<PositionInRoomInfo> getInteractionPositions() {
		return Collections.singleton(new PositionInRoomInfo(map.getDungeon().getRoom(this.getLocation()).getPositions()[Position.Pos.NW.getValue()], map));
	}
}
