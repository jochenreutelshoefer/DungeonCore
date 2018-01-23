package spell;

import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.RoomEntity;
import figure.Figure;
import game.JDEnv;
import game.RoomInfoEntity;

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
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Door) {
			return true;
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target) {
		if (target instanceof Door) {
			AbstractSpell.addTimedSpell(new EscapeRouteInstance(this.getStrength(),
					(Door) target, mage));
		}
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
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
