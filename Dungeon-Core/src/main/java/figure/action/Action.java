/*
 * Abstrakte Superklasse fuer alle Aktionen
 *
 * Stellt Methoden zum erstellen von konkreten Aktionen zur Verfuegung
 * 
 */
package figure.action;

import item.ItemInfo;

/**
 * Abstrakte Superklasse fuer alle Aktionen
 * 
 * Stellt Methoden zum erstellen von konkreten Aktionen zur Verfuegung
 */
public abstract class Action {

	private boolean unanimated = false;

	public void setUnanimated() {
		unanimated = true;

	}

	@Deprecated
	public static AttackAction makeActionAttack(int target) {
		return new AttackAction(target);
	}

	@Deprecated
	public static MoveAction makeActionMove(int dir) {
		return new MoveAction(dir);
	}

	@Deprecated
	public static FleeAction makeActionFlee(boolean panic) {
		return new FleeAction(panic);
	}

	@Deprecated
	public static FleeAction makeActionFlee() {
		return makeActionFlee(false);
	}

	@Override
	public String toString() {
		return this.getClass().toString() + " " + super.toString();
	}

	@Deprecated
	public static EndRoundAction makeEndRoundAction() {

		return new EndRoundAction();
	}


}
