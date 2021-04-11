package de.jdungeon.ai;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.percept.Percept;

public abstract class Attitude {

	public abstract boolean isHostile(FigureInfo other);
		
	public abstract void tellPercept(Percept p);
}
