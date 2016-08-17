package ai;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.percept.Percept;

public interface AI extends FightIntelligence, MovementIntelligence {
	
	void tellPercept(Percept p);

	boolean isHostileTo(FigureInfo f);

	// todo: decide - should this become a percept also ?
	void notifyVisibilityStatusDecrease(JDPoint p);

	// todo: decide - should this become a percept also ?
	void notifyVisibilityStatusIncrease(JDPoint p);

	void setFigure(FigureInfo info);
}
