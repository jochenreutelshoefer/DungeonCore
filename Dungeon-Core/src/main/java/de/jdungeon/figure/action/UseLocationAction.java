package de.jdungeon.figure.action;

import de.jdungeon.dungeon.Position;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.location.Location;

public class UseLocationAction extends AbstractExecutableAction {

	private final RoomInfoEntity target;
	private final boolean meta;
	private final Figure figure;

	public UseLocationAction(FigureInfo info, RoomInfoEntity target, boolean meta) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFigureID());
		this.target = target;
		this.meta = meta;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		Location s = figure.getRoom().getLocation();
		if (s != null && figure.isAbleToUseShrine()) {
			if (!(figure.getPos().getIndex() == Position.Pos.NE.getValue())) {
				return ActionResult.POSITION;
			}
			if(!(figure.canPayDust(s.dustCosts()))) {
				return ActionResult.DUST;
			}
			if (doIt) {
				if (s.canBeUsedBy(figure)) {
					s.use(figure, figure.getActualDungeon().getUnwrapper().unwrappObject(target), meta, round, doIt);
					figure.payDust(s.dustCosts());
					return ActionResult.DONE;
				}
				return ActionResult.OTHER;
			}
			else {
				return ActionResult.POSSIBLE;
			}
		}
		return ActionResult.OTHER;
	}
}
