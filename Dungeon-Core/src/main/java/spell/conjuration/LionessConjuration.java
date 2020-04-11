package spell.conjuration;

import dungeon.Position;
import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.FigureInfo;
import figure.other.Lioness;
import game.InfoEntity;
import spell.AbstractSpell;

public class LionessConjuration extends AbstractSpell {

	private final int level;

	public LionessConjuration(int lvl) {
		this.level = lvl;
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
	public boolean canFire(Figure mage, int round) {
		Room roomInfo = mage.getRoomInfo();
		Position[] positions = roomInfo.getPositions();
		boolean freePositionAvailable = false;
		for (Position position : positions) {
			if (position.getFigure() == null) {
				freePositionAvailable = true;
				break;
			}
		}
		return freePositionAvailable;
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
		Lioness lioness = Lioness.createLioness(800 * level, mage.getRoom()
				.getDungeon(), FigureInfo.makeFigureInfo(mage, mage.getRoomVisibility()));
		Room room = mage.getRoom();
		int targetPosition = Position.getFreePositionNear(mage.getRoom(), mage.getPositionInRoom());
		room.figureEntersAtPosition(lioness, targetPosition, round);
	}

	@Override
	public String getName() {
		return "Löwin";
	}

}
