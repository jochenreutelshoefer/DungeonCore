package skill;

import figure.Figure;
import figure.action.result.ActionResult;

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
            actor.getRoomVisibility().addVisibilityModifier(actor.getRoomNumber(), new EagleOwl());
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }
}
