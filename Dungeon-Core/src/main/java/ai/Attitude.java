package ai;

import figure.FigureInfo;
import figure.percept.Percept;

public abstract class Attitude {

	public abstract boolean isHostile(FigureInfo other);
		
	public abstract void tellPercept(Percept p);
}
