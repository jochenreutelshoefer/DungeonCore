/*
 * Created on 27.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.action;

/**
 * Gegenstand-benutzen-Aktion. Enthaelt den Gegenstand der benutzt werden soll.
 */

import java.util.Collection;

import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.util.InfoUnitUnwrapper;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.UsePercept;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.interfaces.Usable;

public class UseItemAction extends AbstractExecutableAction {

	private final ItemInfo it;

	private RoomInfoEntity target;
	private boolean meta;
	private final Figure figure;

	public UseItemAction(FigureInfo info, ItemInfo i, RoomInfoEntity target, boolean meta) {
		it = i;
		this.target = target;
		this.meta = meta;
		figure = info.getVisMap().getDungeon().getFigureIndex().get(info.getFigureID());
	}

	public UseItemAction(FigureInfo info, ItemInfo i) {
		it = i;
		figure = info.getVisMap().getDungeon().getFigureIndex().get(info.getFigureID());
	}

	/**
	 * @return Returns the de.jdungeon.item.
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
			Item it = figure.unwrapItem(info);
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
						ActionResult result = ((Usable) it).use(figure, figure.getActualDungeon()
								.getUnwrapper()
								.unwrappObject(target), this.isMeta(), round, doIt);
						Percept p = new UsePercept(figure, (Usable) it, round);
						figure.getRoom().distributePercept(p);

						if (result == ActionResult.DONE && (((Usable) it).usableOnce())) {
							figure.removeItem(it);
						}
						return result;
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
