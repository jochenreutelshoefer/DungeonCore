package ai;

import figure.FigureInfo;
import figure.monster.MonsterInfo;
import figure.percept.Percept;

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
