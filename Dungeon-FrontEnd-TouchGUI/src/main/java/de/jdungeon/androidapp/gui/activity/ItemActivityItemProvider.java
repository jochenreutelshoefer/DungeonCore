package de.jdungeon.androidapp.gui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeon.ItemInfoOwner;
import item.ItemInfo;

import de.jdungeon.androidapp.GUIControl;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.InventoryImageManager;
import de.jdungeon.androidapp.gui.smartcontrol.ExecutableTakeItemActivity;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

public abstract class ItemActivityItemProvider implements ActivityProvider {

	private final ItemInfoOwner info;
	private final GUIControl guiControl;
	private final Map<Class, Image> imageCache = new HashMap<Class, Image>();
	private final InventoryImageManager inventoryImageManager;

	public ItemActivityItemProvider(ItemInfoOwner info, Game game, GUIControl guiControl) {
		this.info = info;
		this.guiControl = guiControl;
		inventoryImageManager = new InventoryImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));
	}

	@Override
	public List<Activity> getActivities() {
		List<Activity> result = new ArrayList<>();
		List<ItemInfo> figureItemList = info.getItems();
		if(figureItemList == null) {
			return result;
		}
		for (ItemInfo itemInfo : figureItemList) {
			if(itemInfo != null) {
				result.add(new ExecutableTakeItemActivity(guiControl, itemInfo));
			}
		}
		return result;
	}

	@Override
	public Image getActivityImage(Activity a) {

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
