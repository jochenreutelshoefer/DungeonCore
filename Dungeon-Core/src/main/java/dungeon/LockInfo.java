package dungeon;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.MemoryObject;
import game.InfoEntity;
import gui.Paragraph;
import gui.Paragraphable;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.18.
 */
public class LockInfo extends InfoEntity implements Paragraphable, Locatable {

	private final Lock lock;
	private final DungeonVisibilityMap m;

	public LockInfo(Lock lock, DungeonVisibilityMap m) {
		super(m);
		this.lock = lock;
		this.m = m;
	}


	@Override
	public JDPoint getLocation() {
		return lock.getLocation();
	}

	@Override
	public Paragraph[] getParagraphs() {
		return new Paragraph[0];
	}

	@Override
	public ItemOwner getOwner() {
		return null;
	}

	@Override
	public void setOwner(ItemOwner o) {

	}

	@Override
	public void getsRemoved() {

	}

	@Override
	public MemoryObject getMemoryObject(FigureInfo fig) {
		return null;
	}
}
