package spell.conjuration;

import spell.Spell;
import spell.TargetSpell;
import dungeon.Position;
import dungeon.Room;
import figure.Figure;
import figure.FigureInfo;
import figure.other.Lioness;

public class LionessConjuration extends Spell implements TargetSpell  {

private int level;
	
	public LionessConjuration(int lvl) {
		this.level = lvl;
	}
	
	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}

	@Override
	public int getType() {
		return SPELL_LIONESS;
	}

	@Override
	public String getText() {
		return "Beschwört eine Löwin.";
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
		return true;
	}

	@Override
	public boolean isPossibleFight() {
		return true;
	}

	@Override
	public void sorcer(Figure mage, Object target) {
				Lioness lioness = new Lioness(600 * level, mage.getRoom().getDungeon(),FigureInfo.makeFigureInfo(mage, mage.getRoomVisibility()));
				Room room = mage.getRoom();
				int pos = ((Position)target).getIndex();
				room.figureEntersAtPosition(lioness, pos);
	}

	@Override
	public String getName() {
		return "Löwin";
	}

}
