package de.jdungeon.gui.activity;

import figure.FigureInfo;
import figure.action.TakeItemAction;
import figure.action.result.ActionResult;
import item.ItemInfo;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class ExecutableTakeItemActivity extends AbstractExecutableActivity<ItemInfo> {

	protected final ItemInfo item;
	private final FigureInfo figure;

	public ExecutableTakeItemActivity(PlayerController playerController, ItemInfo item) {
		super(playerController);
		this.figure = playerController.getFigure();
		this.item = item;
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		return new SimpleActivityPlan(this, new TakeItemAction(figure, getObject()));
	}

	@Override
	public ActionResult possible() {
		return ActionResult.POSSIBLE;
	}

	@Override
	public ItemInfo getObject() {
		return item;
	}

}
