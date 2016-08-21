package de.jdungeon.androidapp.gui.itemWheel;

import item.ItemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.androidapp.gui.InventoryImageManager;
import de.jdungeon.game.Image;
import figure.hero.HeroInfo;

public class ItemActivityItemProvider implements ItemWheelActivityProvider {

	private final HeroInfo info;
	private final GameScreen screen;
	private final Map<Class, Image> imageCache = new HashMap<Class, Image>();

	public ItemActivityItemProvider(HeroInfo info, GameScreen screen) {
		this.info = info;
		this.screen = screen;
	}

	@Override
	public List<ItemWheelActivity> getActivities() {
		List<ItemWheelActivity> result = new ArrayList<ItemWheelActivity>();
		List<ItemInfo> figureItemList = info.getFigureItemList();
		for (ItemInfo itemInfo : figureItemList) {
			result.add(new ItemWheelActivity(itemInfo));
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
		Image i = InventoryImageManager.getImage(item, screen);
		imageCache.put(itemClass, i);
		return i;
	}

	@Override
	public void activityPressed(ItemWheelActivity infoEntity) {
		Control control = screen.getControl();
		control.itemWheelActivityClicked(infoEntity,
				screen.getHighlightedEntity());
	}

}
