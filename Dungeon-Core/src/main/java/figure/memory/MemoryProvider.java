package figure.memory;

import figure.FigureInfo;

public interface MemoryProvider {
	public abstract MemoryObject getMemoryObject(FigureInfo fig);
}
