package de.jdungeon.location;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.item.quest.MoonRune;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class MoonRuneFinderShrine extends Location {

    private static int COST = 3;
    private final MoonRune rune;

    public MoonRuneFinderShrine(MoonRune rune) {
        this.rune = rune;
    }

    @Override
    public void turn(int round) {

    }

    @Override
    public String getStory() {
        return "Vielleicht kann man von ihm etwas erfahren.";
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public String getText() {
        return "Alter Druide";
    }

    @Override
    public String getStatus() {
        return "";
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
            JDPoint location = rune.getRoomNumber();
            tellDirection(location, f, round);
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }

    }

    private void tellDirection(JDPoint location, Figure f, int round) {
        // TODO: factor out de.jdungeon.text
        f.tellPercept(new TextPercept("Die Mondrune befindet sich im Moment bei" + ": " + location, round));
        f.getRoomVisibility().setVisibilityStatus(location, RoomObservationStatus.VISIBILITY_ITEMS);
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
