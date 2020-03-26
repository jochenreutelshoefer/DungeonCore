package figure;

import java.io.Serializable;

import log.Log;

/**
 * Distinct class to manage action points
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.03.20.
 */
public class APCounter implements Serializable {

	private static final boolean AP_LOGGING = false;

	private int currentAP = 0;
	private int lastSetRound; // for debugging
	private int lastPayRound; // for debugging

	public void setCurrentAP(int value, int round) {
		int before = currentAP;
		this.currentAP = value;
		if(AP_LOGGING) {
			Log.info(System.currentTimeMillis()+" "+ round+" Hero SET AP round "+round+" - before: "+before +" - after: "+currentAP);
		}
		lastSetRound = round;

	}

	public void incrementActionPoint(int round) {
		int before = currentAP;
		currentAP += 1;
		if(AP_LOGGING) {
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
		if(AP_LOGGING) {
			Log.info(System.currentTimeMillis()+" "+ round+" Hero pays APs round "+round + " "+k+" - before: "+before +" - after: "+currentAP);
		}
		lastPayRound = round;
	}

	public void payActionpoint(int round) {
		int before = currentAP;
		if(currentAP > 0){
			currentAP -= 1;
		}
		if(AP_LOGGING) {
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
