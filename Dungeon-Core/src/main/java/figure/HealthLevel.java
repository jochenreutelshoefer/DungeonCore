package figure;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public enum HealthLevel {
	Strong(4),
	Good(3),
	Injured(2),
	Weak(1),
	Dying(0);

	public int getValue() {
		return value;
	}

	private int value;

	HealthLevel(int value) {
		this.value = value;
	}

	public static HealthLevel fromPercent(int percent) {
		if (percent > 70) {
			return Strong;
		}
		else if (percent > 45) {
			return Good;
		}
		else if (percent > 25) {
			return Injured;
		}
		else if (percent > 10) {
			return Weak;
		}
		else {
			return Dying;
		}
	}

	public static HealthLevel fromValue(int value) {
		if (value == 4) {
			return Strong;
		}
		else if (value == 3) {
			return Good;
		}
		else if (value == 2) {
			return Injured;
		}
		else if (value == 1) {
			return Weak;
		}
		else if (value == 0) {

			return Dying;
		}
		return null;
	}


}
