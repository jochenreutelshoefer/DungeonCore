/*
 * Abstrakte Superklasse fuer alle Aktionen
 *
 * Stellt Methoden zum erstellen von konkreten Aktionen zur Verfuegung
 * 
 */
package figure.action;

public abstract class Action {

	@Override
	public String toString() {
		return this.getClass() + " " + super.toString();
	}

}
