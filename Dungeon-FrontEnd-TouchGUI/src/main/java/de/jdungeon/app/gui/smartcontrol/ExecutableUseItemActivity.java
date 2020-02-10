package de.jdungeon.app.gui.smartcontrol;

import figure.action.UseItemAction;
import game.RoomInfoEntity;
import item.ItemInfo;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class ExecutableUseItemActivity extends AbstractExecutableActivity {

	protected final ActionAssembler guiControl;
	protected final ItemInfo item;

	public ExecutableUseItemActivity(ActionAssembler control, ItemInfo item) {
		this.guiControl = control;
		this.item = item;
	}

	@Override
	public void execute() {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		guiControl.plugAction(new UseItemAction((ItemInfo) getObject()));
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ExecutableUseItemActivity that = (ExecutableUseItemActivity) o;
		return item.equals(that.item);
	}

	@Override
	public int hashCode() {
		return item.hashCode();
	}
}
