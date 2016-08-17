package figure.action;

import spell.SpellInfo;

public class SpellAction extends Action {
	
	private SpellInfo spell;
	private Object target;
	
	public SpellAction(SpellInfo spell, Object target) {
		this.spell = spell;
		this.target = target;
		
	}

	public SpellInfo getSpell() {
		return spell;
	}

	public Object getTarget() {
		return target;
	}
	
	
}
