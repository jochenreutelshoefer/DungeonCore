package de.jdungeon.androidapp.gui.activity;

import java.util.ArrayList;
import java.util.List;

import event.ActionEvent;
import event.EventManager;
import figure.FigureInfo;
import figure.action.TakeItemAction;
import item.ItemInfo;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class TakeItemActivityProvider extends ItemActivityItemProvider {

	private final FigureInfo info;

	public TakeItemActivityProvider(FigureInfo info, Game game) {
		super(info, game);
		this.info = info;
	}

	@Override
	public List<Activity> getActivities() {
		List<Activity> result = new ArrayList<>();
		List<ItemInfo> figureItemList = info.getRoomInfo().getItems();
		if(figureItemList == null) {
			return result;
		}
		for (ItemInfo itemInfo : figureItemList) {
			if(itemInfo != null) {
				result.add(new DefaultActivity(itemInfo));
			}
		}
		return result;
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		EventManager.getInstance().fireEvent(new ActionEvent(new TakeItemAction((ItemInfo)infoEntity.getObject())));
	}

	@Override
	public boolean isCurrentlyPossible(Activity infoEntity) {
		return true;
	}
}
