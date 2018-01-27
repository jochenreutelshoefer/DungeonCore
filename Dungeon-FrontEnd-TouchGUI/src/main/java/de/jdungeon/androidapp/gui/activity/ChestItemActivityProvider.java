package de.jdungeon.androidapp.gui.activity;

import java.util.ArrayList;
import java.util.List;

import dungeon.ChestInfo;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.StepAction;
import figure.action.TakeItemFromChestAction;
import item.ItemInfo;

import de.jdungeon.androidapp.GUIControl;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class ChestItemActivityProvider extends ItemActivityItemProvider {

	private final FigureInfo info;
	private final GUIControl guiControl;

	public ChestItemActivityProvider(FigureInfo info, Game game, GUIControl guiControl) {
		super(info, game);
		this.info = info;
		this.guiControl = guiControl;
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
				result.add(new DefaultActivity(itemInfo));
			}
		}
		return result;
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		List<Action> actions = new ArrayList<>();
		if(! (this.info.getPos().getIndex() == Position.Pos.NW.getValue())) {
			StepAction stepAction = new StepAction(Position.Pos.NW.getValue());
			actions.add(stepAction);
		}
		TakeItemFromChestAction takeAction = new TakeItemFromChestAction((ItemInfo) infoEntity.getObject());
		actions.add(takeAction);
		this.guiControl.plugActions(actions);

	}

	@Override
	public boolean isCurrentlyPossible(Activity infoEntity) {
		PositionInRoomInfo chestPos = info.getRoomInfo().getPositionInRoom(0);
		return info.getRoomInfo().getChest() != null && (!chestPos.isOccupied() || chestPos.getFigure().equals(info));
	}
}
