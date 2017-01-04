package de.jdungeon.androidapp.gui.itemWheel;

import dungeon.ItemInfoOwner;
import event.ActionEvent;
import event.EventManager;
import figure.action.TakeItemAction;
import item.ItemInfo;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.screen.GameScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class TakeItemActivityProvider extends ItemActivityItemProvider {

	public TakeItemActivityProvider(ItemInfoOwner info, GameScreen screen) {
		super(info, screen);
	}

	@Override
	public void activityPressed(ItemWheelActivity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		EventManager.getInstance().fireEvent(new ActionEvent(new TakeItemAction((ItemInfo)infoEntity.getObject())));
	}
}
