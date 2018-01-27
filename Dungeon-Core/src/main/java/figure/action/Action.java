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

	public static AttackAction makeActionAttack(int target) {
		return new AttackAction(target);
	}

	public static MoveAction makeActionMove(int dir) {
		return new MoveAction(dir);
	}

	public static FleeAction makeActionFlee(boolean panic) {
		return new FleeAction(panic);
	}

	public static FleeAction makeActionFlee() {
		return makeActionFlee(false);
	}

	@Override
	public String toString() {
		return this.getClass().toString();
	}

	public static EndRoundAction makeEndRoundAction() {

		return new EndRoundAction();
	}


}
