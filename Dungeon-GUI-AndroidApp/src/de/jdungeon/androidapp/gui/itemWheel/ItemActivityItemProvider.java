package de.jdungeon.androidapp.gui.itemWheel;

import item.ItemInfo;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.androidapp.gui.InventoryImageManager;
import de.jdungeon.game.Image;
import figure.hero.HeroInfo;

public class ItemActivityItemProvider implements ItemWheelActivityProvider {

	private final HeroInfo info;
	private final GameScreen screen;

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
		if (a.getObject() instanceof ItemInfo) {
			return InventoryImageManager.getImage((ItemInfo)a.getObject(), screen);
		}
		return null;
	}

	@Override
	public void activityPressed(ItemWheelActivity infoEntity) {
		Control control = screen.getControl();
		control.itemWheelActivityClicked(infoEntity);
	}

}
