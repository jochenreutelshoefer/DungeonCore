package de.jdungeon.figure.memory;

import de.jdungeon.figure.FigureInfo;

public interface MemoryProvider {
	public abstract MemoryObject getMemoryObject(FigureInfo fig);
}
