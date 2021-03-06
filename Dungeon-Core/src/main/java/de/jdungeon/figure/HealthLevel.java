package de.jdungeon.figure;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public enum HealthLevel {
	Strong(5),
	Good(4),
	Ok(3),
	Injured(2),
	Weak(1),
	Dying(0),
	Dead(-1);

	public int getValue() {
		return value;
	}

	private final int value;

	HealthLevel(int value) {
		this.value = value;
	}

	public static HealthLevel fromPercent(int percent) {
		if (percent > 85) {
			return Strong;
		}
		else if (percent > 65) {
			return Good;
		}
		else if (percent > 40) {
			return Ok;
		}
		else if (percent > 25) {
			return Injured;
		}
		else if (percent > 10) {
			return Weak;
		}
		else if (percent > 0) {
			return Dying;
		} else {
			return Dead;
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
		else if (value == -1) {
			return Dead;
		}
		return null;
	}


}
