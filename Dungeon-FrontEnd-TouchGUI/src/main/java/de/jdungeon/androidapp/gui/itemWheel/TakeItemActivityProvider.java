package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.List;

import dungeon.ItemInfoOwner;
import event.ActionEvent;
import event.EventManager;
import figure.FigureInfo;
import figure.action.TakeItemAction;
import item.ItemInfo;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.screen.GameScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class TakeItemActivityProvider extends ItemActivityItemProvider {

	private final FigureInfo info;

	public TakeItemActivityProvider(FigureInfo info, GameScreen screen) {
		super(info, screen);
		this.info = info;
	}

	@Override
	public List<ItemWheelActivity> getActivities() {
		List<ItemWheelActivity> result = new ArrayList<ItemWheelActivity>();
		List<ItemInfo> figureItemList = info.getRoomInfo().getItems();
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
	public void activityPressed(ItemWheelActivity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		EventManager.getInstance().fireEvent(new ActionEvent(new TakeItemAction((ItemInfo)infoEntity.getObject())));
	}
}
