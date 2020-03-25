package figure;

import java.util.Collection;
import java.util.HashSet;

import figure.action.Action;
import figure.action.AttackAction;
import figure.action.FleeAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.attribute.Attribute;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.03.20.
 */
public class APAgility {

	private final Figure figure;



	private final Attribute oxygen;
	private final APCounter apCounter;

	public APAgility(Figure figure) {
		this.figure = figure;
		oxygen = new Attribute(Attribute.OXYGEN, 10);
		apCounter = new APCounter(figure);
	}

	public void turn(int round) {
		// figure gets an AP only if there is at least a little amount of oxygen
		if(oxygen.getValue() > 1) {
			apCounter.setCurrentAP(1, round);
		}

		// take a breath instead
		oxygen.addToMax(1);

	}

	public Attribute getOxygen() {
		return oxygen;
	}

	private static final Collection<Class<? extends Action>> OXYGEN_ACTIONS = new HashSet<>();

	static {
		OXYGEN_ACTIONS.add(MoveAction.class);
		OXYGEN_ACTIONS.add(StepAction.class);
		OXYGEN_ACTIONS.add(AttackAction.class);
		OXYGEN_ACTIONS.add(FleeAction.class);

	}

	public void payActionPoint(Action action, int round) {
		apCounter.payActionpoint(round);
		if(OXYGEN_ACTIONS.contains(action.getClass())) {
			oxygen.modValue(-2);
		}
	}

	public int getCurrentAP() {
		return this.apCounter.getCurrentAP();
	}

	public boolean canPayActionpoints(int k) {
		return this.apCounter.canPayActionpoints(k);
	}
}
