package figure.memory;

import figure.Figure;
import figure.FigureInfo;

public class FigureMemory extends MemoryObject{
	
	private Class clazz;
	private String name;
	private int value;
	private int healthStatus;
	private boolean hostile;

	public FigureMemory(Figure fig,FigureInfo f) {
		this.clazz = fig.getClass();
		this.name = fig.getName();
		this.healthStatus = fig.getHealthLevel();
		this.value = fig.getWorth();
		this.hostile = fig.getControl().isHostileTo(f);
		
	}

	public Class getClazz() {
		return clazz;
	}

	public int getHealthStatus() {
		return healthStatus;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public boolean isHostile() {
		return hostile;
	}
}

