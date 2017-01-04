package dungeon;

import java.util.List;

import item.ItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public interface ItemInfoOwner {

	List<ItemInfo> getItems();

	ItemInfo [] getItemArray();
}
