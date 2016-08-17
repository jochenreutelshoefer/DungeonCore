package figure.monster;

import ai.AI;
import dungeon.Dungeon;


public abstract class NatureMonster extends Monster{
	
	public NatureMonster(int v) {
		super(v);	
	}

	public NatureMonster(int v, AI ai) {
		super(v, ai);
	}


}
