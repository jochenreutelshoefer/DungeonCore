package de.jdungeon.gui.activity;

import figure.action.UseItemAction;
import figure.action.result.ActionResult;
import item.ItemInfo;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class ExecutableUseItemActivity extends AbstractExecutableActivity<ItemInfo> {

	protected final ItemInfo item;

	public ExecutableUseItemActivity(PlayerController control, ItemInfo item) {
		super(control);
		this.item = item;
	}

	@Override
	public ActivityPlan createExecutionPlan() {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		return new SimpleActivityPlan(this, new UseItemAction(playerController.getFigure(), getObject()));
	}

	@Override
	public ActionResult possible() {
		return ActionResult.POSSIBLE;
	}

	@Override
	public ItemInfo getObject() {
		return item;
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
