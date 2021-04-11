package de.jdungeon.ai;

import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.hero.Hero;

public class DefaultHeroReflexBehavior extends AbstractReflexBehavior {

	private final Hero h;
	
	public DefaultHeroReflexBehavior(Hero h) {
		super(h);
		this.h = h;
	}
	
	
	@Override
	public Action getAction() {
		Action a = checkRaid();
		if(a != null) {
			return a;
		}
		return null;
	}
	
	

}
