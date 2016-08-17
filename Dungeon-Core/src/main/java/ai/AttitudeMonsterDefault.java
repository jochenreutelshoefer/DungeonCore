package ai;

import figure.FigureInfo;
import figure.monster.MonsterInfo;
import figure.other.Fir;
import figure.percept.Percept;

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
