package de.jdungeon.androidapp.gui.activity;

import java.util.ArrayList;
import java.util.List;

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
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class ChestItemActivityProvider extends ItemActivityItemProvider {

	private final FigureInfo info;

	public ChestItemActivityProvider(FigureInfo info, Game game) {
		super(info, game);
		this.info = info;
	}

	@Override
	public List<Activity> getActivities() {
		List<Activity> result = new ArrayList<>();
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
				result.add(new Activity(itemInfo));
			}
		}
		return result;
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		if(! (this.info.getPos().getIndex() == Position.Pos.NW.getValue())) {
			EventManager.getInstance().fireEvent(new ActionEvent(new StepAction(Position.Pos.NW.getValue())));
		}
		EventManager.getInstance().fireEvent(new ActionEvent(new TakeItemFromChestAction((ItemInfo)infoEntity.getObject())));
	}
}
