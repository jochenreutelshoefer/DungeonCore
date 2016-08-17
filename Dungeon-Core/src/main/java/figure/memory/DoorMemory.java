package figure.memory;

import dungeon.Door;


public class DoorMemory extends MemoryObject {
	
	
	private boolean isLocked;
	private boolean hasLock;
	private String lock;
	
	public DoorMemory(Door d) {
		isLocked = d.getLocked();
		hasLock = d.hasLock();
		if(hasLock) {
			lock = d.getLock();
		}
		
	}

	public boolean isHasLock() {
		return hasLock;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public String getLock() {
		return lock;
	}

}
