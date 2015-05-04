package ai;

import figure.FigureInfo;
import figure.percept.Percept;

public interface AI extends FightIntelligence, MovementIntelligence {
	
	void tellPercept(Percept p);

	boolean isHostileTo(FigureInfo f);

}
