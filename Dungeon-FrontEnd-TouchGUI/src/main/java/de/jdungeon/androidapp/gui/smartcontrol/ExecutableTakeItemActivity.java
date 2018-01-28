package de.jdungeon.androidapp.gui.smartcontrol;

import figure.action.TakeItemAction;
import game.RoomInfoEntity;
import item.ItemInfo;

import de.jdungeon.androidapp.GUIControl;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.activity.AbstractExecutableActivity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class ExecutableTakeItemActivity extends AbstractExecutableActivity {

	private final GUIControl guiControl;
	private final ItemInfo item;

	public ExecutableTakeItemActivity(GUIControl control, ItemInfo item) {
		this.guiControl = control;
		this.item = item;
	}

	@Override
	public void execute() {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		guiControl.plugAction(new TakeItemAction((ItemInfo) getObject()));
	}

	@Override
	public boolean isCurrentlyPossible() {
		return true;
	}

	@Override
	public Object getObject() {
		return item;
	}

	@Override
	public RoomInfoEntity getTarget() {
		return null;
	}
}
