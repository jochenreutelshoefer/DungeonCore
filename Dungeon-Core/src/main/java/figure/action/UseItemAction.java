/*
 * Created on 27.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

/**
 * Gegenstand-benutzen-Aktion. Enthaelt den Gegenstand der benutzt werden soll.
 */

import java.util.Collection;

import dungeon.PositionInRoomInfo;
import dungeon.util.InfoUnitUnwrapper;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.percept.Percept;
import figure.percept.UsePercept;
import game.RoomInfoEntity;
import item.Item;
import item.ItemInfo;
import item.interfaces.Usable;

public class UseItemAction extends AbstractExecutableAction {

	private final ItemInfo it;

	private RoomInfoEntity target;
	private boolean meta;
	private final Figure figure;

	public UseItemAction(FigureInfo info, ItemInfo i, RoomInfoEntity target, boolean meta) {
		it = i;
		this.target = target;
		this.meta = meta;
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
	}

	public UseItemAction(FigureInfo info, ItemInfo i) {
		it = i;
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
	}

	/**
	 * @return Returns the item.
	 */
	public ItemInfo getItem() {
		return it;
	}

	public RoomInfoEntity getTarget() {
		return target;
	}

	public boolean isMeta() {
		return meta;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		// TODO check auf use moeglich
		if (figure.canPayActionPoints(1)) {
			ItemInfo info = getItem();
			Item it = figure.getItem(info);
			if (it instanceof Usable) {
				Usable usable = (Usable) it;
				RoomInfoEntity target = getTarget();
				InfoUnitUnwrapper unwrapper = figure.getRoom().getDungeon().getUnwrapper();
				if (((Usable) it).needsTarget() && target == null) {
					return ActionResult.NO_TARGET;
				}
				if (((Usable) it).needsTarget()) {
					Collection<PositionInRoomInfo> interactionPositions = target.getInteractionPositions();
					Collection<Object> positions = unwrapper.unwrappObjects(interactionPositions);
					if (!positions.contains(figure.getPos())) {
						return ActionResult.POSITION;
					}
				}
				if (usable.canBeUsedBy(figure)) {
					if (doIt) {
						boolean used = ((Usable) it).use(figure, figure.getActualDungeon()
								.getUnwrapper()
								.unwrappObject(target), this.isMeta(), round);
						figure.payActionPoint(this, round);
						Percept p = new UsePercept(figure, (Usable) it, round);
						figure.getRoom().distributePercept(p);

						if (used && (((Usable) it).usableOnce())) {
							figure.removeItem(it);
						}
						if (used) {

							return ActionResult.DONE;
						}
						else {
							return ActionResult.FAILED;
						}
					}
					return ActionResult.POSSIBLE;
				}
				return ActionResult.OTHER;
			}
			return ActionResult.ITEM;
		}
		return ActionResult.NOAP;
	}
}
