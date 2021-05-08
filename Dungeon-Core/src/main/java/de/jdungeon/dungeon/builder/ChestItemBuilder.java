package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.Room;
import de.jdungeon.item.Item;

public class ChestItemBuilder extends AbstractLocationBuilder {

	private Item item;

	public ChestItemBuilder(Item item) {
		this.item = item;
	}

	@Override
	public String getIdentifier() {
		return item.toString();
	}

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		Room room = dungeon.getRoom(x, y);
		Chest chest = room.getChest();
		if(chest == null) {
			chest = new Chest();
			room.setChest(chest);
		}
		chest.takeItem(item);
	}
}
