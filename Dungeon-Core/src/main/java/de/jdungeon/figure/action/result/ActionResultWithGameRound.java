package de.jdungeon.figure.action.result;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 06.01.21.
 */
public class ActionResultWithGameRound {

	private final ActionResult result;

	private final int round;
	public ActionResultWithGameRound(ActionResult result, int round) {
		this.result = result;
		this.round = round;
	}

	public ActionResult getResult() {
		return result;
	}

	public int getRound() {
		return round;
	}
}
