package figure.action;

import dungeon.Chest;
import dungeon.Position;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;

public class UseChestAction extends AbstractExecutableAction {
	
	private final boolean meta;
	private final Figure figure;

	public UseChestAction(FigureInfo info, boolean meta) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		this.meta = meta;
	}


	@Override
	public ActionResult handle(boolean doIt, int round) {
		boolean right = false;
		if (meta) {
			right = true;
		}
		if (!(figure.getPos().getIndex() == Position.Pos.NW.getValue())) {
			return ActionResult.POSITION;
		}
		Chest s = figure.getRoom().getChest();
		if (s != null && figure.isAbleToUseChest()) {
			if (doIt) {
				s.clicked(figure, right);
				return ActionResult.DONE;
			}
			else {
				return ActionResult.POSSIBLE;
			}
		}
		return ActionResult.OTHER;
	}
}
