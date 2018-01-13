package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.List;

import control.ActionAssembler;
import dungeon.ChestInfo;
import dungeon.Position;
import event.ActionEvent;
import event.EventManager;
import figure.FigureInfo;
import figure.action.StepAction;
import figure.action.TakeItemFromChestAction;
import item.ItemInfo;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.screen.GameScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class ChestItemActivityProvider extends ItemActivityItemProvider {

	private final FigureInfo info;

	public ChestItemActivityProvider(FigureInfo info, GameScreen screen) {
		super(info, screen);
		this.info = info;
	}

	@Override
	public List<ItemWheelActivity> getActivities() {
		List<ItemWheelActivity> result = new ArrayList<>();
		ChestInfo chest = info.getRoomInfo().getChest();
		if(chest == null) {
			return result;
		}
		List<ItemInfo> figureItemList = chest.getItemList();
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
		if(! (this.info.getPos().getIndex() == Position.Pos.NW.getValue())) {
			EventManager.getInstance().fireEvent(new ActionEvent(new StepAction(Position.Pos.NW.getValue())));
		}
		EventManager.getInstance().fireEvent(new ActionEvent(new TakeItemFromChestAction((ItemInfo)infoEntity.getObject())));
	}
}
