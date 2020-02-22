package de.jdungeon.app.gui.smartcontrol;

import figure.action.TakeItemAction;
import game.RoomInfoEntity;
import item.ItemInfo;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class ExecutableTakeItemActivity extends AbstractExecutableActivity {

	protected final ActionAssembler guiControl;
	protected final ItemInfo item;

	public ExecutableTakeItemActivity(ActionAssembler control, ItemInfo item) {
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

}
