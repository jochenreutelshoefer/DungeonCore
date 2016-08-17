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

	/**
	 * Liefert eine neues ActionMove-Objekt Richtungen: 1 --> Norden; 2 -->
	 * Osten; 3 --> Sueden; 4 --> Westen;
	 * 
	 * @param fighterIndex
	 *            Identifikationsindex der Figur
	 * @param dir
	 *            Richtung fuer die Bewegung
	 * @return Bewegungsaktion
	 */
	public static MoveAction makeActionMove(int fighterIndex, int dir) {
		return new MoveAction(/* fighterIndex, */dir);
	}

	/**
	 * Liefert eine neues ActionFlee-Objekt Richtungen: 1 --> Norden; 2 -->
	 * Osten; 3 --> Sueden; 4 --> Westen;
	 * 
	 * @param fighterIndex
	 *            Identifikationsindex der Figur
	 * @param dir
	 *            Richtung fuer die Flucht
	 * @param panic
	 *            ob Flucht panisch sein soll
	 * @return Fluchtaktion
	 */
	public static FleeAction makeActionFlee(boolean panic) {
		return new FleeAction(panic);
	}

	/**
	 * Liefert ein neues ActionFlee-Objekt Richtungen: 1 --> Norden; 2 -->
	 * Osten; 3 --> Sueden; 4 --> Westen;
	 * 
	 * @param fighterIndex
	 *            Identifikationsindex der Figur
	 * @param dir
	 *            Richtung fuer die Flucht
	 * @return Fluchtaktion
	 */
	public static FleeAction makeActionFlee() {
		return makeActionFlee(false);
	}

	/**
	 * Liefert eine neues ActionUseItem-Objekt
	 * 
	 * 
	 * @param fighterIndex
	 *            Identifikationsindex der Figur
	 * @param itemIndex
	 *            Nummer des Gegenstandes im Inventar
	 * @return Gegenstand-Benutzen-Aktion
	 */

	public static UseItemAction makeActionUseItem(ItemInfo info) {
		return new UseItemAction(info);
	}

	/**
	 * Liefert eine neues ActionTakeItem-Objekt
	 * 
	 * 
	 * @param fighterIndex
	 *            Identifikationsindex der Figur
	 * @param index
	 *            Nummer des Gegenstandes im Raum
	 * @return Gegenstand-Aufnehm-Aktion
	 */
	public static TakeItemAction makeActionTakeItem(
	/* int fighterIndex, */ItemInfo info) {
		// Fighter f = game.getFighter(fighterIndex);
		// Item it = f.getRoom().getItemNumber(index);
		return new TakeItemAction(/* fighterIndex, */info);
	}

	@Override
	public String toString() {
		return this.getClass().toString();
	}

	/**
	 * Liefert eine neues EndRoundAction-Objekt
	 * 
	 * 
	 * @param fighterIndex
	 *            Identifikationsindex der Figur
	 * @return Rundenende-Aktion
	 */
	public static EndRoundAction makeEndRoundAction(/* int fighterIndex */) {

		return new EndRoundAction(/* fighterIndex */);
	}

	/**
	 * @return Returns the unanimated.
	 */
	public boolean isUnanimated() {
		return unanimated;
	}

}
