package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.List;

import dungeon.ItemInfoOwner;
import item.ItemInfo;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.smartcontrol.ExecutableUseItemActivity;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class LibgdxUseItemActivityProvider extends LibgdxItemActivityItemProvider {
	private final ItemInfoOwner info;
	private final ActionAssembler actionAssembler;
	private final LibgdxFocusManager focusManager;

	public LibgdxUseItemActivityProvider(ItemInfoOwner info, InventoryImageManager inventoryImageManager, ActionAssembler actionAssembler, LibgdxFocusManager focusManager) {
		super(info, inventoryImageManager, actionAssembler);
		this.info = info;
		this.actionAssembler = actionAssembler;
		this.focusManager = focusManager;
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
				result.add(new ExecutableUseItemActivity(actionAssembler, itemInfo));
			}
		}
		return result;
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		actionAssembler.itemWheelActivityClicked(infoEntity,
				focusManager.getWorldFocusObject());
	}


}
