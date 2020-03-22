package figure;

import figure.hero.Hero;
import log.Log;

/**
 * Distinct class to manage action points
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.03.20.
 */
public class APCounter {

	private int currentAP = 0;
	private final Figure figure;
	private int lastSetRound;
	private int lastPayRound;
	public APCounter(Figure figure) {
		this.figure = figure;
	}

	public void setCurrentAP(int value, int round) {
		int before = currentAP;
		this.currentAP = value;
		if(figure instanceof Hero) {
			Log.info(System.currentTimeMillis()+" "+ round+" Hero SET AP round "+round+" - before: "+before +" - after: "+currentAP);
		}
		lastSetRound = round;

	}

	public void incrementActionPoint(int round) {
		int before = currentAP;
		currentAP += 1;
		if(figure instanceof Hero) {
			Log.info(System.currentTimeMillis()+" "+ round+" Hero INC AP round "+round+" - before: "+before +" - after: "+currentAP);
		}
		lastSetRound = round;
	}

	public void payActionpoints(int k, int round) {
		int before = currentAP;
		if(currentAP >= k){
			currentAP -= k;
		} else {
			throw new IllegalArgumentException("cannot pay action points: "+k+ " only having "+currentAP);
		}
		if(figure instanceof Hero) {
			Log.info(System.currentTimeMillis()+" "+ round+" Hero pays APs round "+round + " "+k+" - before: "+before +" - after: "+currentAP);
		}
		lastPayRound = round;
	}

	public void payActionpoint(int round) {
		int before = currentAP;
		if(currentAP > 0){
			currentAP -= 1;
		}
		if(figure instanceof Hero) {
			Log.info(System.currentTimeMillis()+" "+ round+" Hero pays AP round "+round + " - before: "+before +" - after: "+currentAP);
		}
		lastPayRound = round;
	}

	public int getCurrentAP() {
		return currentAP;
	}

	public boolean canPayActionpoints(int k) {
		return currentAP >= k;
	}
}
