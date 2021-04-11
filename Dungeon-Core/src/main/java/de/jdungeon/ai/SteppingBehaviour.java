package de.jdungeon.ai;

import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 16.06.17.
 */
public class SteppingBehaviour extends AbstractMonsterBehaviour {

	DefaultMonsterIntelligence defaultFightAI = new DefaultMonsterIntelligence();


	@Override
	public void tellPercept(Percept p) {

	}

	@Override
	public Action chooseFightAction() {
		return defaultFightAI.chooseFightAction();
	}

	@Override
	public Action chooseMovementAction() {
		int index = (int) (Math.random() * 8);
		return new StepAction(this.info, index);
	}
}
