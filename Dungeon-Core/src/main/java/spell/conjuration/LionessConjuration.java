package spell.conjuration;

import dungeon.Position;
import dungeon.Room;
import figure.Figure;
import figure.FigureInfo;
import figure.other.Lioness;
import game.InfoEntity;
import spell.Spell;

public class LionessConjuration extends Spell {

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
	public boolean isApplicable(Figure mage, Object target) {
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
	public void sorcer(Figure mage, Object target) {
		Lioness lioness = Lioness.createLioness(600 * level, mage.getRoom()
				.getDungeon(), FigureInfo.makeFigureInfo(mage, mage.getRoomVisibility()));
		Room room = mage.getRoom();
		int targetPosition = Position.getFreePositionNear(mage.getRoom(), mage.getPositionInRoom());
		room.figureEntersAtPosition(lioness, targetPosition);
	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return null;
	}

	@Override
	public String getName() {
		return "Löwin";
	}

}
