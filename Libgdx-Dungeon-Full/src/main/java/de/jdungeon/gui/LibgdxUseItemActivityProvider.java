package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.dungeon.ItemInfoOwner;
import de.jdungeon.figure.action.Action;
import de.jdungeon.item.ItemInfo;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ExecutableUseItemActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class LibgdxUseItemActivityProvider extends LibgdxItemActivityItemProvider {
	private final ItemInfoOwner info;
	private final PlayerController actionAssembler;
	private final LibgdxFocusManager focusManager;

	public LibgdxUseItemActivityProvider(ItemInfoOwner info, InventoryImageManager inventoryImageManager, PlayerController actionAssembler, LibgdxFocusManager focusManager) {
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
		if (infoEntity == null) {
			return;
		}
		Object o = infoEntity.getObject();
		if (o instanceof ItemInfo) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			List<Action> actions = actionAssembler.getActionAssembler().getActionAssemblerHelper().wannaUseItem((ItemInfo) o, focusManager.getWorldFocusObject(), false);
			actionAssembler.plugActions(actions);
		}
	}


}
