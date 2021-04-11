package de.jdungeon.ai;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.other.Fir;
import de.jdungeon.figure.percept.Percept;

public class AttitudeMonsterDefault extends Attitude {

	@Override
	public boolean isHostile(FigureInfo other) {
		if(other.getClass().equals(Fir.class)) {
			return true;
		}
		if(other instanceof MonsterInfo) {
			return false;
		}
		return true;
	}
	
	public void tellPercept(Percept p) {
		
	}

}
