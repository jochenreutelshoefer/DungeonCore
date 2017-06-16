package spell;

import dungeon.Door;
import dungeon.DoorInfo;
import figure.Figure;
import game.InfoEntity;
import game.JDEnv;

public class EscapeRoute extends AbstractTargetSpell {

	public static int[][] values = { { 7, 5, 8, 10, 1 }, { 15, 13, 12, 25, 2 } };

	public static final String suffix = "escape_route";

	Door d;

	Figure mage;
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	public EscapeRoute(int level) {
		super(level, values[level - 1]);
		this.isPossibleInFight = false;
		this.isPossibleNormal = true;
	}


	@Override
	public int getType() {
		return AbstractSpell.SPELL_ESCAPEROUTE;
	}

	@Override
	public String getText() {
		return JDEnv.getString("spell_"+suffix+"_text");
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Door) {
			return true;
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, Object target) {
		if (target instanceof Door) {
			AbstractSpell.addTimedSpell(new EscapeRouteInstance(this.getStrength(),
					(Door) target, mage));
		}
	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return DoorInfo.class;
	}

	@Override
	public TargetScope getTargetScope() {
		// TODO
		return null;
	}

	@Override
	public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

}
