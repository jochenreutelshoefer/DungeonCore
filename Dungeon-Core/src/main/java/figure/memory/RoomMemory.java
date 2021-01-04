package figure.memory;

import item.Item;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import figure.FigureInfo;

public class RoomMemory extends MemoryObject{
	
	private DoorMemory [] doors = new DoorMemory[4]; 
	private PositionMemory [] posis = new PositionMemory[8];
	private ChestMemory chest;
	private ShrineMemory shrine;
	private List<ItemMemory> items = new LinkedList<ItemMemory>();
	private int round;
	
	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public RoomMemory(Room r,FigureInfo info) {
		
		for (int i = 0; i < doors.length; i++) {
			if(doors[i] != null) {
				doors[i] = r.getDoors()[i].getMemoryObject(info);
			}
		}
		for (int i = 0; i < posis.length; i++) {
			posis[i] = r.getPositions()[i].getMemoryObject(info);
		}
		if(r.getChest() != null) {
			chest = r.getChest().getMemoryObject(info);
		}
		if(shrine != null) {
			//shrine = r.getRoomNumber().getMemoryObject(info);
		}
		List its = r.getItems();
		for (Iterator iter = its.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			items.add(element.getMemoryObject(info));
		}
	}

	public ChestMemory getChest() {
		return chest;
	}

	public DoorMemory[] getDoors() {
		return doors;
	}

	public List<ItemMemory> getItems() {
		return items;
	}

	public PositionMemory[] getPosis() {
		return posis;
	}

	public ShrineMemory getShrine() {
		return shrine;
	}

}
