package figure.action;

import game.RoomInfoEntity;
import spell.SpellInfo;

public class SpellAction extends Action {
	
	private final SpellInfo spell;
	private final RoomInfoEntity target;
	
	public SpellAction(SpellInfo spell, RoomInfoEntity target) {
		this.spell = spell;
		this.target = target;
		
	}

	public SpellInfo getSpell() {
		return spell;
	}

	public RoomInfoEntity getTarget() {
		return target;
	}
	
	
}
