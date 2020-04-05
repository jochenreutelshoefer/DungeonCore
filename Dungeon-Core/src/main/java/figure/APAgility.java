package figure;

import java.io.Serializable;
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
public class APAgility implements Serializable {

	private final Attribute oxygen;
	private final APCounter apCounter;
	private final double recoveryRate;

	public APAgility() {
		this(12, 1);
	}

	public APAgility(int max, double recoveryRate) {
		oxygen = new Attribute(Attribute.Type.Oxygen, max);
		apCounter = new APCounter();
		this.recoveryRate = recoveryRate;
	}

	public void turn(int round) {
		// figure gets an AP only if there is at least a little amount of oxygen
		if(oxygen.getValue() >= 1) {
			apCounter.setCurrentAP(1, round);
		}

		// take a breath instead
		oxygen.addToMax(recoveryRate);

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
		if(action != null && OXYGEN_ACTIONS.contains(action.getClass())) {
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