package fight;

import figure.Figure;

public class Frightening {
	
	public static final int TYPE_THUNDER = 1;
	public static final int TYPE_THREAT = 2;
	
	int type;
	int value;
	Figure actor;
	
	public Frightening(Figure actor, int value, int type) {
		this.actor = actor;
		this.value = value;
		this.type = type;
	}

	public Figure getActor() {
		return actor;
	}

	public int getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

}
