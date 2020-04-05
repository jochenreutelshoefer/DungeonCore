/*
 * Abstrakte Superklasse fuer alle Aktionen
 *
 * Stellt Methoden zum erstellen von konkreten Aktionen zur Verfuegung
 * 
 */
package figure.action;

import figure.action.result.ActionResult;

/**
 * Abstrakte Superklasse fuer alle Aktionen
 * 
 */
public abstract class Action {

	@Override
	public String toString() {
		return this.getClass().toString() + " " + super.toString();
	}

}
