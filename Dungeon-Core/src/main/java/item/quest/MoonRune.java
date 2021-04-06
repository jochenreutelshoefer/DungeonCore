package item.quest;

import dungeon.RoomEntity;
import figure.Figure;
import figure.action.result.ActionResult;
import item.Item;
import item.interfaces.Locatable;
import item.interfaces.Usable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class MoonRune extends Item implements Usable, Locatable {

    public static final int COST = 5;

    public MoonRune() {
        super(10, true);
    }

    @Override
    public String getText() {
        return "Mondrune";
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public int dustCosts() {
        return COST;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        if (!f.canPayDust(COST)) return ActionResult.DUST;
        if (doIt) {
            f.payDust(COST);
            f.heal(15, round);
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }

    @Override
    public boolean usableOnce() {
        return false;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return f.canPayDust(COST);
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}
