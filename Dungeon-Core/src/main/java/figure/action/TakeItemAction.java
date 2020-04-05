
package figure.action;

import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.percept.TakePercept;
import item.Item;
import item.ItemInfo;

/**
 * Gegenstand-Aufnehm-Aktion. Enthaelt den Gegenstand, der aufgenommen werden soll.
 */

public class TakeItemAction extends AbstractExecutableAction {
	private final ItemInfo item;
	private final Figure figure;

	public TakeItemAction(FigureInfo info, ItemInfo it) {
		super();
		this.item = it;
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
	}

	/**
	 * @return Returns the item.
	 */
	public ItemInfo getItem() {
		return item;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		boolean fight = figure.getRoom().fightRunning();
		if (fight) {
			if (figure.canPayActionPoints(1)) {
				ItemInfo info = getItem();
				Item item = figure.getRoom().getItem(info);
				if (figure.isAbleToTakeItemInFight(item)) {
					if (figure.getRoom().hasItem(item)) {
						if (figure.canTakeItem(item)) {
							if (doIt) {
								figure.takeItem(item);
								figure.payActionPoint(this, round);
								figure.getRoom().distributePercept(new TakePercept(figure, item, round));
								return ActionResult.DONE;
							}
							else {
								return ActionResult.POSSIBLE;
							}
						}
					}
				}
				return ActionResult.OTHER;
			}
			else {
				return ActionResult.NOAP;
			}
		}
		else {
			ItemInfo info = getItem();
			Item item = figure.getRoom().getItem(info);
			if (figure.isAbleToTakeItem(item)) {
				if (figure.getRoom().hasItem(item)) {
					if (figure.canTakeItem(item)) {
						if (doIt) {
							figure.takeItem(item);
							figure.getRoom().distributePercept(new TakePercept(figure, item, round));
							return ActionResult.DONE;
						}
						else {
							return ActionResult.POSSIBLE;
						}
					}
				}
			}
			return ActionResult.OTHER;
		}
	}
}
