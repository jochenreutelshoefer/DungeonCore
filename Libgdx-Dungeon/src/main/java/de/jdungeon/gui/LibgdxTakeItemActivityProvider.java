package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.List;

import figure.FigureInfo;
import figure.action.TakeItemAction;
import item.ItemInfo;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ExecutableTakeItemActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class LibgdxTakeItemActivityProvider extends LibgdxItemActivityItemProvider {
	private final FigureInfo info;
	private final PlayerController guiControl;

	public LibgdxTakeItemActivityProvider(FigureInfo info, InventoryImageManager inventoryImageManager, PlayerController guiControl) {
		super(info, inventoryImageManager, guiControl);
		this.info = info;
		this.guiControl = guiControl;
	}

	@Override
	public List<Activity> getActivities() {
		List<Activity> result = new ArrayList<>();
		List<ItemInfo> figureItemList = info.getRoomInfo().getItems();
		if (figureItemList == null) {
			return result;
		}
		for (ItemInfo itemInfo : figureItemList) {
			if (itemInfo != null) {
				result.add(new ExecutableTakeItemActivity(guiControl, itemInfo));
			}
		}
		return result;
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		guiControl.plugAction(new TakeItemAction(this.info, (ItemInfo) infoEntity.getObject()));
	}

}
