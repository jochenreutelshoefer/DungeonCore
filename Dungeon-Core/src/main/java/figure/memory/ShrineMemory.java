package figure.memory;

import shrine.Location;

public class ShrineMemory extends MemoryObject{
	
	private int index;
	private int index2;
	private Class clazz;
	
	public ShrineMemory(Location s){
		index = s.getShrineIndex();
		index2 = s.getSecondIdentifier();
		clazz = s.getClass();
	}

	public Class getClazz() {
		return clazz;
	}

	public int getIndex() {
		return index;
	}

	public int getIndex2() {
		return index2;
	}

}
