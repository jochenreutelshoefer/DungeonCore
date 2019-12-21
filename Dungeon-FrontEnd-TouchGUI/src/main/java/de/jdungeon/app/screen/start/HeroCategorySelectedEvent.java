package de.jdungeon.app.screen.start;

import event.Event;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.04.16.
 */
public class HeroCategorySelectedEvent extends Event {

	private final int heroType;

	public HeroCategorySelectedEvent(int heroType) {
		this.heroType = heroType;
	}

	public int getHeroType() {
		return heroType;
	}
}
