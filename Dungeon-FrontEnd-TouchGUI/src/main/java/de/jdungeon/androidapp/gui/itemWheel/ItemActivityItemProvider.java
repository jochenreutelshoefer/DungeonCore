package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeon.ItemInfoOwner;
import item.ItemInfo;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.InventoryImageManager;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Image;

public abstract class ItemActivityItemProvider implements ItemWheelActivityProvider {

	private final ItemInfoOwner info;
	protected final GameScreen screen;
	private final Map<Class, Image> imageCache = new HashMap<Class, Image>();
	private final InventoryImageManager inventoryImageManager;

	public ItemActivityItemProvider(ItemInfoOwner info, GameScreen screen) {
		this.info = info;
		this.screen = screen;
		inventoryImageManager = new InventoryImageManager(screen.getGuiImageManager());
	}

	@Override
	public List<ItemWheelActivity> getActivities() {
		List<ItemWheelActivity> result = new ArrayList<ItemWheelActivity>();
		List<ItemInfo> figureItemList = info.getItems();
		if(figureItemList == null) {
			return result;
		}
		for (ItemInfo itemInfo : figureItemList) {
			if(itemInfo != null) {
				result.add(new ItemWheelActivity(itemInfo));
			}
		}
		return result;
	}

	@Override
	public Image getActivityImage(ItemWheelActivity a) {

		ItemInfo item = (ItemInfo) a.getObject();
		Class itemClass = item.getItemClass();
		Image cachedImage = imageCache.get(itemClass);
		if (cachedImage != null) {
			return cachedImage;
		}
		Image i = inventoryImageManager.getImage(item);
		imageCache.put(itemClass, i);
		return i;
	}



}
