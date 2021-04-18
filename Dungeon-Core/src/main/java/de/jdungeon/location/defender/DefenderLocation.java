package de.jdungeon.location.defender;

import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.npc.DefaultNPCFactory;
import de.jdungeon.figure.npc.RescuedNPCAI;
import de.jdungeon.figure.percept.LocationStateChangePercept;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.location.Location;
import de.jdungeon.location.LocationState;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.04.20.
 */
public class DefenderLocation extends Location {


    public enum DefenderState implements LocationState {
        Inactive("Inaktiv"),
        Activated("Aktiv"),
        Fighting("Kämpfend"),
        Dead("Tot");

        String verbalization;

        DefenderState(String verbalization) {
            this.verbalization = verbalization;
        }

        @Override
        public String toString() {
            return verbalization;
        }
    }

    private final Room room;

    private final Hero defenderFigure;

    private DefenderState state;
    private Figure activator;

    public DefenderLocation(Room room) {
        this.room = room;
        defenderFigure = DefaultNPCFactory.createDefaultNPC("Willibald", Hero.HEROCODE_WARRIOR);
        defenderFigure.createVisibilityMap(room.getDungeon());
        room.getDungeon().insertFigure(defenderFigure);
        FigureInfo defenderInfo = FigureInfo.makeFigureInfo(defenderFigure, defenderFigure.getViwMap());
        RescuedNPCAI ai = new RescuedNPCAI();
        ai.setFigure(defenderInfo);
        defenderFigure.setControl(new FigureControl(defenderInfo, ai)); // todo: create DefenderAI
        defenderFigure.setLookDir(RouteInstruction.Direction.South);
        state = DefenderState.Inactive;
    }

    @Override
    public DefenderState getState() {
        return state;
    }

    public Hero getDefenderFigure() {
        return defenderFigure;
    }

    @Override
    public void turn(int round, DungeonWorldUpdater mode) {
        if (state == DefenderState.Inactive) {
            // we do nothing
            return;
        }
        if (defenderFigure.isDead()) {
            changeStateTo(DefenderState.Dead, round);

        }

        if (state == DefenderState.Dead) {
            // nothing can happen
            return;
        }

        if (state == DefenderState.Activated) {
            boolean actionRequired = actionRequired();
            if (actionRequired) {
                room.figureEntersAtPosition(defenderFigure, 2, round);
                changeStateTo(DefenderState.Fighting, round);
            }
        }

        if (state == DefenderState.Fighting) {
            if (!actionRequired()) {
                room.figureLeaves(defenderFigure);
                changeStateTo(DefenderState.Inactive, round);
            }
        }
    }

    private void changeStateTo(DefenderState newState, int round) {
        if (state != newState) {
            room.distributePercept(new LocationStateChangePercept(this, room, state, newState, round));
            state = newState;
        }
    }

    private boolean actionRequired() {
        List<Figure> roomFigures = room.getRoomFigures();
        boolean activatorThere = roomFigures.contains(activator);
        if (activatorThere) {
            for (Figure otherFigure : roomFigures) {
                if (otherFigure != activator && activator.getControl().isHostileTo(FigureInfo.makeFigureInfo(otherFigure, defenderFigure.getViwMap()))) {
                    // found at least one hostile de.jdungeon.figure
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getStory() {
        return "Kann Dich in diesem Raum beschützen wenn aktiviert.";
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public String getText() {
        return "Beschützer";
    }

    @Override
    public String getStatus() {
        return state.verbalization;
    }

    @Override
    public int dustCosts() {
        return 10;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        if (doIt) {
            this.activator = f;
            DefenderState oldState = this.state;
            DefenderState newState = DefenderState.Activated;
            this.state = newState;
            room.distributePercept(new LocationStateChangePercept(this, room, oldState, newState, round));
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }

    @Override
    public boolean usableOnce() {
        return true;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return true;
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}
