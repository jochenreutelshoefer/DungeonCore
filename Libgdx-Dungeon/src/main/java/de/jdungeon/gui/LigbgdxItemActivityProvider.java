package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeon.ItemInfoOwner;
import item.ItemInfo;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ExecutableTakeItemActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public abstract class LigbgdxItemActivityProvider implements LibgdxActivityProvider {

	private final ItemInfoOwner info;
	private final PlayerController guiControl;
	private final Map<Class, String> imageCache = new HashMap<>();
	private final InventoryImageManager inventoryImageManager;

	public LigbgdxItemActivityProvider(ItemInfoOwner info, InventoryImageManager inventoryImageManager, PlayerController guiControl) {
		this.info = info;
		this.guiControl = guiControl;
		this.inventoryImageManager = inventoryImageManager;
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
	public String getActivityImage(Activity a) {

		ItemInfo item = (ItemInfo) a.getObject();
		Class itemClass = item.getItemClass();
		String cachedImage = imageCache.get(itemClass);
		if (cachedImage != null) {
			return cachedImage;
		}
		String i = inventoryImageManager.getJDImage(item).getFilenameBlank();
		imageCache.put(itemClass, i);
		return i;
	}



}
