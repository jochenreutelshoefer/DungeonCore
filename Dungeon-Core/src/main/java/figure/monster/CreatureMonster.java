package figure.monster;

import dungeon.Dungeon;


public abstract class CreatureMonster extends Monster{

    
    public CreatureMonster(int value) {
		super(value);	
		fireResistRate = 0.6;
	}
    
}
