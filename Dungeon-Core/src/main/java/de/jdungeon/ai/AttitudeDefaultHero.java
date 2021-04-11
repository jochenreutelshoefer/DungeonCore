package de.jdungeon.ai;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.percept.Percept;

public class AttitudeDefaultHero extends Attitude{
	
	public boolean isHostile(FigureInfo other) {
		if(other instanceof MonsterInfo) {
			return true;
		}
		return false;
	}
	
	public void tellPercept(Percept p) {
		
	}

}
