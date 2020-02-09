package figure.action;

import dungeon.Position;
import figure.Figure;
import figure.VisibilityModifier;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.06.17.
 */
public class ScoutResult implements VisibilityModifier {

	private final Figure actor;
	private final int visLevel;
	private final Position position;

	public ScoutResult(Figure actor, int visLevel) {
		this.actor = actor;
		this.visLevel = visLevel;
		this.position = actor.getPos();
	}

	@Override
	public int getVisibilityStatus() {
		return visLevel;
	}

	@Override
	public boolean stillValid() {
		// is handled explicitly during figure moves
		return true;
	}

	public Figure getScoutingFigure() {
		return actor;
	}

	public Position getPosition() {
		return position;
	}
}
