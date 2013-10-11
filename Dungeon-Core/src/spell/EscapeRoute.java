package spell;

import dungeon.Door;
import figure.Figure;
import game.JDEnv;

public class EscapeRoute extends TimedSpell {

	public static int[][] values = { { 7, 5, 8, 10, 1 }, { 15, 13, 12, 25, 2 } };

	public static final String suffix = "escape_route";

	Door d;

	Figure mage;
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;

	public EscapeRoute(int level) {
		super(level, values[level - 1]);
		this.isPossibleInFight = false;
		this.isPossibleNormal = true;
	}


	public int getType() {
		return Spell.SPELL_ESCAPEROUTE;
	}

	public String getText() {
		return JDEnv.getString("spell_"+suffix+"_text");
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Door) {
			return true;
		}
		return false;
	}

	public void sorcer(Figure mage, Object target) {
		if (target instanceof Door) {
			Spell.addTimedSpell(new EscapeRouteInstance(this.getStrength(),
					(Door) target, mage));
		}
	}

	public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

}
