package figure.action;

import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import game.RoomInfoEntity;
import spell.Spell;
import spell.SpellInfo;

public class SpellAction extends AbstractExecutableAction {
	
	private final SpellInfo spell;
	private final RoomInfoEntity target;
	private final Figure figure;

	public SpellAction(FigureInfo info, SpellInfo spell, RoomInfoEntity target) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		this.spell = spell;
		this.target = target;
		
	}

	public SpellInfo getSpell() {
		return spell;
	}

	public RoomInfoEntity getTarget() {
		return target;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		Spell sp = figure.unWrappSpellInfo(spell);
		if (sp != null) {
			// todo: refactor
			if (figure.lastSpell != null && figure.lastSpell != sp) {
				figure.lastSpell.resetSpell();
				figure.lastSpell = null;
			}

			if (figure.canPayActionPoints(1)) {
				return sp.fire(figure, figure.getActualDungeon().getUnwrapper().unwrappObject(target), doIt, round);
			}
			return ActionResult.NOAP;
		}
		return ActionResult.UNKNOWN;
	}
}
