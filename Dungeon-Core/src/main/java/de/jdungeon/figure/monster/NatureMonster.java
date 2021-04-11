package de.jdungeon.figure.monster;

import de.jdungeon.ai.AI;


public abstract class NatureMonster extends Monster{
	
	public NatureMonster(int v) {
		super(v);	
	}

	public NatureMonster(int v, AI ai) {
		super(v, ai);
	}


}
