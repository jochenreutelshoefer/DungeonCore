/*
 * Created on 04.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action.result;

public class ActionResult {
	

	// TODO: find better name
	public enum Situation {
		possible,
		impossible,
		done,
		failed
	}

	public enum Reason {
		noActionPoints,
		wrongTarget,
		noTarget,
		noKnowledge,
		noItem,
		noDust,
		unknownAction,
		wrongFightMode,
		wrongPosition,
		wrongDistance,
		other
	}


	public static final ActionResult FAILED = new ActionResult(Situation.failed);
	public static final ActionResult DONE = new ActionResult(Situation.done);

	public static final ActionResult POSSIBLE = new ActionResult(Situation.possible);

	public static final ActionResult NOAP = new ActionResult(Reason.noActionPoints);
	public static final ActionResult OTHER = new ActionResult(Reason.other);
	public static final ActionResult WRONG_TARGET = new ActionResult(Reason.wrongTarget);
	public static final ActionResult NO_TARGET = new ActionResult(Reason.noTarget);
	public static final ActionResult POSITION = new ActionResult(Reason.wrongPosition);
	public static final ActionResult UNKNOWN = new ActionResult(Reason.unknownAction);
	public static final ActionResult MODE = new ActionResult(Reason.wrongFightMode);
	public static final ActionResult KNOWLEDGE = new ActionResult(Reason.noKnowledge);
	public static final ActionResult DUST = new ActionResult(Reason.noDust);
	public static final ActionResult ITEM = new ActionResult(Reason.noItem);
	public static final ActionResult DISTANCE = new ActionResult(Reason.wrongDistance);
	

	private final Situation key1;
	private Reason key2;
	
	private ActionResult(Situation key) {
		this.key1 = key;
	}

	private ActionResult(Reason key2) {
		this.key1 = Situation.impossible;
		this.key2 = key2;
	}

	public Reason getReason() {
		return key2;
	}


	public Situation getSituation() {
		return key1;
	}

	@Override
	public String toString() {
		return "ActionResult{" +
				"possible=" + key1 +
				", reason=" + key2 +
				'}';
	}
}
