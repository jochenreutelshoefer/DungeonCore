package de.jdungeon.figure.memory;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Lock;

public class DoorMemory extends MemoryObject {
	
	
	private final boolean isLocked;
	private final boolean hasLock;
	private Lock lock;
	
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

	public Lock getLock() {
		return lock;
	}

}
