package ai;

import dungeon.RoomInfo;
import figure.action.Action;
import figure.action.StepAction;
import figure.percept.Percept;

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
