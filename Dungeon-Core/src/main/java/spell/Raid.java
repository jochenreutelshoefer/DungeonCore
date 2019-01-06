/*
 * Created on 08.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;

import java.util.Collections;
import java.util.List;

import dungeon.Door;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import dungeon.RoomEntity;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import game.JDEnv;
import game.RoomInfoEntity;

/**
 * @author Jochen
 *         <p>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Raid extends AbstractTargetSpell implements TargetSpell {

	public static int[][] values = { { 5, 3, 6, 10, 1 }, {
			7, 5, 5, 20, 2 }
	};

	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	public Raid(
			int level,
			int diffMin,
			int diff,
			int cost,
			int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;

	}

	public Raid() {
		this.strength = 10;
		this.cost = 6;
		isPossibleNormal = true;
		isPossibleInFight = false;
	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		// TODO: implement
		return true;
	}

	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_raid_text");
	}

	@Override
	public TargetScope getTargetScope() {
		return new TargetScope() {
			@Override
			public List<? extends RoomInfoEntity> getTargetEntitiesInScope(FigureInfo actor) {
				PositionInRoomInfo position = actor.getPos();
				if (position == null) {
					return Collections.emptyList();
				}
				RouteInstruction.Direction possibleRaidDirection =
						position.getPossibleFleeDirection();
				if (possibleRaidDirection == null) {
					return Collections.emptyList();
				}
				RoomInfo neighbourRoom = actor.getRoomInfo().getNeighbourRoom(possibleRaidDirection);
				if (neighbourRoom == null || !actor.getRoomInfo().getDoor(possibleRaidDirection).isPassable()) {
					return Collections.emptyList();
				}
				return neighbourRoom.getFigureInfos();
			}
		};
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
	public int getType() {
		return AbstractSpell.SPELL_RAID;
	}

	public boolean fightModus() {
		return isPossibleInFight;
	}

	public boolean normalModus() {
		return isPossibleNormal;
	}

	public Raid(int level) {

		super(level, values[level - 1]);
		this.level = level;
		isPossibleNormal = true;
		isPossibleInFight = false;
	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_raid_name");
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity o) {
		if (o instanceof Figure) {
			Room targetRoom = ((Figure) o).getRoom();
			Room mageRoom = mage.getRoom();
			if (mageRoom.hasOpenConnectionTo(targetRoom)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target) {

		Room targetRoom = ((Figure) target).getRoom();
		Room mageRoom = mage.getRoom();

		Door d = mageRoom.getConnectionTo(targetRoom);
		RouteInstruction.Direction dir = mageRoom.getDirection(d);
		mage.makeRaid((Figure) target);

		mage.walk(dir);

	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
	public String toString() {
		return getName();
	}

}
