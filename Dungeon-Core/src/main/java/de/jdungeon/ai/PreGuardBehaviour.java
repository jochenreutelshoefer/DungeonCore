package de.jdungeon.ai;

import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class PreGuardBehaviour extends AbstractMonsterBehaviour {

    private Room room;
    DefaultMonsterIntelligence defaultAI = new DefaultMonsterIntelligence();
    private boolean activated = false;

    public PreGuardBehaviour(Room room) {
        this.room = room;
    }


    @Override
    public void setFigure(FigureInfo info) {
        super.setFigure(info);
        defaultAI.setFigure(info);
    }

    @Override
    public void tellPercept(Percept p) {
        List<FigureInfo> perceivedFigures = p.getInvolvedFigures(this.info);
        for (FigureInfo perceivedFigure : perceivedFigures) {
            if (perceivedFigure.isHostile(this.info)) {
                activated = true;
            }
        }
    }

    @Override
    public Action chooseFightAction() {
        return defaultAI.chooseFightAction();
    }

    @Override
    public Action chooseMovementAction() {
        if (activated) {
            // todo: implement more clear behaviour here
            return defaultAI.chooseMovementAction();
        }
        return new EndRoundAction();
    }
}
