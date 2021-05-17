package de.jdungeon.dungeon;

import java.util.Collection;

import de.jdungeon.item.Key;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.log.Log;

public interface Lockable extends Locatable {

	default Key getKey() {
		Lock lock = getLock();
		if (lock == null) return null;
		return lock.getKey();
	}

	Lock getLock();

	void setLock(Lock k);

	Collection<Position> getInteractionPositions();

	void setKey(Key k);

	boolean getLocked();

	void setLocked(boolean b);

	boolean hasLock();

	boolean lockMatches(Key k);

	void toggleLock(Key k);
}
