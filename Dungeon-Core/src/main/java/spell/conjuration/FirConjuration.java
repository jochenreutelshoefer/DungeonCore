package spell.conjuration;

import dungeon.RoomEntity;
import game.RoomInfoEntity;
import spell.AbstractSpell;
import spell.AbstractTargetSpell;
import spell.TargetSpell;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import figure.Figure;
import figure.other.Fir;
import game.InfoEntity;

public class FirConjuration extends AbstractTargetSpell {

	private final int level;
	
	public FirConjuration(int lvl) {
		this.level = lvl;
	}
	
	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
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
	public boolean isApplicable(Figure mage, RoomEntity target) {
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
	public void sorcer(Figure mage, RoomEntity target, int round) {
				Fir fir = new Fir(500 * level, mage.getRoom().getDungeon());
				Room room = mage.getRoom();
				int pos = ((Position)target).getIndex();
				room.figureEntersAtPosition(fir, pos, round);
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return PositionInRoomInfo.class;
	}

	@Override
	public String getName() {
		return "Fichte";
	}

}
