package de.jdungeon.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;
import de.jdungeon.item.Item;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class LevelExit extends Location {

    private List<Figure> requiredFigures = new ArrayList<>();
    private List<Item> requiredItems = new ArrayList<>();

    public LevelExit() {

    }

    /**
     * Constructor to create exits that required some special de.jdungeon.item(s) to access the exit
     *
     * @param items that player needs to have to trigger exit
     */
    public LevelExit(Item... items) {
        requiredItems = Arrays.asList(items);
    }

    /**
     * Constructor to create exits that required presence of escorted NPCs
     *
     * @param figures that need to be in the room to trigger exit
     */
    public LevelExit(Figure... figures) {
        requiredFigures = Arrays.asList(figures);
    }

    @Override
    public void turn(int round, DungeonWorldUpdater mode) {

    }

    @Override
    public String getStory() {
        String basicStory = JDEnv.getResourceBundle().getString("shrine_exit_text");
        return (!this.requiredFigures.isEmpty() || !this.requiredItems.isEmpty()) ? basicStory + "Unter bestimmten Bedigungen..." : "";
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public String getText() {
        return JDEnv.getResourceBundle().getString("shrine_exit_name");
    }

    @Override
    public String getStatus() {
        return "";
    }

    @Override
    public int dustCosts() {
        return 0;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        // TODO: factor out de.jdungeon.text
        if (requiredFigureMissing()) {
            // some de.jdungeon.figure to be escorted to exit is not here -> refuse
            f.getRoomInfo()
                    .distributePercept(new TextPercept("Folgende Charaktere benötigt, um Dungeon zu verlassen: " + requiredItems
                            .toString(), round));
            return ActionResult.OTHER;
        }

        if (requiredItemMissing(f)) {
            // some de.jdungeon.item to be found to exit is not here -> refuse
            Room roomInfo = f.getRoomInfo();
            if (roomInfo != null) {
                roomInfo.distributePercept(new TextPercept("Folgende Gegenstände benötigt, um Dungeon zu verlassen: " + requiredItems
                        .toString(), round));
            }
            return ActionResult.ITEM;
        }

        if (doIt) {
            // de.jdungeon.level completion items are given away when passing the exit
            for (Item requiredItem : requiredItems) {
                f.removeItem(requiredItem);
            }
            f.getControl().exitUsed(this, f);
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }

    private boolean requiredFigureMissing() {
        for (Figure requiredFigure : requiredFigures) {
            if (!this.getRoom().getRoomFigures().contains(requiredFigure)) {
                return true;
            }
        }
        return false;
    }

    private boolean requiredItemMissing(Figure figure) {
        for (Item item : requiredItems) {
            if (!figure.getItems().contains(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean usableOnce() {
        return true;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return f instanceof Hero;
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}
