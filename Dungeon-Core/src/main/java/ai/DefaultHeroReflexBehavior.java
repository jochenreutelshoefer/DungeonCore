package ai;

import figure.action.Action;
import figure.hero.Hero;

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
