package ai;

import figure.action.Action;
import figure.hero.Hero;

public class DefaultHeroReflexBehavior extends AbstractReflexBehavior {

	Hero h;
	
	public DefaultHeroReflexBehavior(Hero h) {
		super(h);
		this.h = h;
	}
	
	
	public Action getAction() {
		Action a = checkRaid();
		if(a != null) {
			return a;
		}
		return null;
	}
	
	

}
