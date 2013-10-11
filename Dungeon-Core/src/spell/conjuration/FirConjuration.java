package spell.conjuration;

import spell.Spell;
import spell.TargetSpell;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import figure.Figure;
import figure.other.Fir;

public class FirConjuration extends Spell implements TargetSpell{

	private int level;
	
	public FirConjuration(int lvl) {
		this.level = lvl;
	}
	
	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}

	@Override
	public int getType() {
		return SPELL_FIR;
	}

	@Override
	public String getText() {
		return "Beschwört eine Fichte.";
	}

	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Position) {
			if(((Position)target).getFigure() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPossibleNormal() {
		return false;
	}

	@Override
	public boolean isPossibleFight() {
		return true;
	}

	@Override
	public void sorcer(Figure mage, Object target) {
				Fir fir = new Fir(500 * level, mage.getRoom().getDungeon());
				Room room = mage.getRoom();
				int pos = ((Position)target).getIndex();
				room.figureEntersAtPosition(fir, pos);
	}

	@Override
	public String getName() {
		return "Fichte";
	}

}
