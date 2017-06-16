package fight;

import figure.Figure;

/**
 * Wenn eine Figur eine andere schlaegt (Slap) wird ein Ergebnis des Schlages 
 * zurueckgeschickt (SlapResult). Wichtig fuer die Punktevergabe an den Helden.
 *
 */
public class SlapResult {

	
	private final int exp;
	private final boolean lethal;
	private final Figure victim;
	private final Slap s;
	private final int value;
	
	public SlapResult(int exp, boolean lethal,Figure victim, int value, Slap s) {
		this.exp = exp;
		this.lethal = lethal;		
		this.victim = victim;
		this. value = value;
		this.s = s;
	}

	/**
	 * Returns the exp.
	 * @return int
	 * 
	 */
	public int getExp() {
		return exp;
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
