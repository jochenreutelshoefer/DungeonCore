package event;

import dungeon.Position;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class WannaStepEvent extends Event {

	private final Position.Pos target;

	public WannaStepEvent(Position.Pos target) {
		this.target = target;
	}

	public Position.Pos getTarget() {
		return target;
	}
}
