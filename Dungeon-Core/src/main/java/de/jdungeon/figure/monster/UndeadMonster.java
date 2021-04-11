package de.jdungeon.figure.monster;

import de.jdungeon.ai.AI;



public abstract class UndeadMonster extends Monster{

    public UndeadMonster(int value) {
		super(value);	
	}

	public UndeadMonster(int value, AI ai) {
		super(value, ai);
	}

}
