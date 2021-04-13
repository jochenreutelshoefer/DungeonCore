package de.jdungeon.skill.attack;

import de.jdungeon.figure.Figure;

/**
 * Wenn eine Figur eine andere schlaegt (Slap) wird ein Ergebnis des Schlages 
 * zurueckgeschickt (SlapResult). Wichtig fuer die Punktevergabe an den Helden.
 *
 */
public class SlapResult {

	
	private final boolean lethal;
	private final Figure victim;
	private final Slap s;
	private final int value;
	
	public SlapResult(boolean lethal,Figure victim, int value, Slap s) {
		this.lethal = lethal;
		this.victim = victim;
		this. value = value;
		this.s = s;
	}

	/**
	 * Returns the lethal.
	 * @return boolean
	 * 
		 */
	public boolean isLethal() {
		return lethal;
	}

	/**
	 * Returns the value.
	 * @return int
	 * 
	 */
	public int getValue() {
		return value;
	}

	/**
	 * 
	 */
	public Figure getVictim() {
		return victim;
	}

	public Slap getSlap() {
		return s;
	}

}
