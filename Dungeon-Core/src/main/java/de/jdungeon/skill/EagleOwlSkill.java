package de.jdungeon.skill;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.SkillActionPercept;

public class EagleOwlSkill extends SimpleSkill {

    private static final int EAGLE_OWL_DUST_COSTS = 10;

    public EagleOwlSkill() {
        super(EAGLE_OWL_DUST_COSTS);
    }

    @Override
    protected boolean isPossibleFight() {
        return false;
    }

    @Override
    protected boolean isPossibleNonFight() {
        return true;
    }

    @Override
    public ActionResult doExecute(SimpleSkillAction action, boolean doIt, int round) {
        if (doIt) {
            Figure actor = action.getActor();
            actor.getViwMap().addVisibilityModifier(actor.getRoomNumber(), new EagleOwl());
            actor.getRoom().distributePercept(new SkillActionPercept(action, round));
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }
}
