package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeon.ItemInfoOwner;
import figure.FigureInfo;
import graphics.JDImageProxy;
import item.ItemInfo;

import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ExecutableTakeItemActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public abstract class LibgdxItemActivityItemProvider implements LibgdxActivityProvider {

	private final ItemInfoOwner info;
	private final Map<Class, String> imageCache = new HashMap<>();
	private final InventoryImageManager inventoryImageManager;
	private final PlayerController guiControl;

	public LibgdxItemActivityItemProvider(ItemInfoOwner info, InventoryImageManager inventoryImageManager, PlayerController guiControl) {
		this.info = info;
		this.inventoryImageManager = inventoryImageManager;
		this.guiControl = guiControl;
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
		JDImageProxy jdImage = inventoryImageManager.getJDImage(item);
		String imageName = jdImage.getFilenameBlank();
		imageCache.put(itemClass, imageName);
		return imageName;
	}


}
