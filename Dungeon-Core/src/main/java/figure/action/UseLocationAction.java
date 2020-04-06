package figure.action;

import dungeon.Position;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import game.RoomInfoEntity;
import shrine.Location;

public class UseLocationAction extends AbstractExecutableAction {

	private final RoomInfoEntity target;
	private final boolean meta;
	private final Figure figure;

	public UseLocationAction(FigureInfo info, RoomInfoEntity target, boolean meta) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		this.target = target;
		this.meta = meta;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		Location s = figure.getRoom().getShrine();
		if (s != null && figure.isAbleToUseShrine()) {
			if (!(figure.getPos().getIndex() == Position.Pos.NE.getValue())) {
				return ActionResult.POSITION;
			}
			if (doIt) {
				if (s.canBeUsedBy(figure)) {
					s.use(figure, figure.getActualDungeon().getUnwrapper().unwrappObject(target), meta, round);
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
