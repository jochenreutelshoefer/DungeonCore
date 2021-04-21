package de.jdungeon.figure.action;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.spell.Spell;
import de.jdungeon.spell.SpellInfo;

public class SpellAction extends AbstractExecutableAction {
	
	private final SpellInfo spell;
	private final RoomInfoEntity target;
	private final Figure figure;

	public SpellAction(FigureInfo info, SpellInfo spell, RoomInfoEntity target) {
		figure = info.getVisMap().getDungeon().getFigureIndex().get(info.getFigureID());
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
			if (figure.canPayActionPoints(1)) {
				return sp.fire(figure, figure.getDungeon().getUnwrapper().unwrappObject(target), doIt, round);
			}
			return ActionResult.NOAP;
		}
		return ActionResult.UNKNOWN;
	}
}
