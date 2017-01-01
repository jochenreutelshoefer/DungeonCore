package event;

import dungeon.util.RouteInstruction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class WannaMoveEvent extends Event {

	private final RouteInstruction.Direction direction;

	public WannaMoveEvent(RouteInstruction.Direction direction) {
		this.direction = direction;
	}

	public RouteInstruction.Direction getDirection() {
		return direction;
	}
}
