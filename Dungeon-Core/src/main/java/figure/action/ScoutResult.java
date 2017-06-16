package figure.action;

import dungeon.Position;
import figure.Figure;
import figure.VisibilityModifier;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.06.17.
 */
public class ScoutResult implements VisibilityModifier {

	private final ScoutAction action;
	private final Figure actor;
	private final int visLevel;
	private final String text;
	private final Position position;

	public ScoutResult(ScoutAction action, Figure actor, int visLevel, String text) {
		this.action = action;
		this.actor = actor;
		this.visLevel = visLevel;
		this.text = text;
		this.position = actor.getPos();
	}

	@Override
	public int getVisibilityStatus() {
		return visLevel;
	}

	public Figure getScoutingFigure() {
		return actor;
	}

	public Position getPosition() {
		return position;
	}
}
