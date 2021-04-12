package de.jdungeon.gui.activity;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.TakeItemAction;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.item.ItemInfo;

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
	public ActivityPlan createExecutionPlan(boolean doIt, Object target) {
		return new SimpleActivityPlan(this, new TakeItemAction(figure, getObject()));
	}

	@Override
	public ActionResult possible(Object target) {
		return ActionResult.POSSIBLE;
	}

	@Override
	public ItemInfo getObject() {
		return item;
	}

}
