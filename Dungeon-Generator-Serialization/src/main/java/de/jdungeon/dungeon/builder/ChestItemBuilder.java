package de.jdungeon.dungeon.builder;

import java.util.Objects;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.serialization.ItemDTO;
import de.jdungeon.item.Item;

public class ChestItemBuilder extends AbstractLocationBuilder {

	public ItemDTO item;

	public ChestItemBuilder(ItemDTO item) {
		this.item = item;
	}

	/**
	 * Required for JSON serialization
	 */
	public ChestItemBuilder() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChestItemBuilder that = (ChestItemBuilder) o;
		return Objects.equals(item, that.item);
	}

	@Override
	public int hashCode() {
		return Objects.hash(item);
	}

	@Override
	public String getIdentifier() {
		return item.getClass() + ("" + System.identityHashCode(item))
				.replaceAll(" ", "_")
				.replaceAll(",", "_")
				.replaceAll(":", "_")
				.replaceAll("\\(", "_")
				.replaceAll("\\)", "_")
				;
	}

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		Room room = dungeon.getRoom(x, y);
		Chest chest = room.getChest();
		if (chest == null) {
			chest = new Chest();
			room.setChest(chest);
		}
		Item itemInstance = this.item.create();
		chest.takeItem(itemInstance);
	}
}
