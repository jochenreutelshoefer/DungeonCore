package location.defender;

import java.util.List;

import dungeon.Room;
import dungeon.RoomEntity;
import dungeon.util.RouteInstruction;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.npc.DefaultNPCFactory;
import figure.npc.RescuedNPCAI;
import figure.percept.LocationStateChangePercept;
import location.Location;
import location.LocationState;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.04.20.
 */
public class DefenderLocation extends Location {



	public enum DefenderState implements LocationState {
		Inactive,
		Activated,
		Fighting,
		Dead;
	}

	private final Room room;

	private final Hero defenderFigure;

	private DefenderState state;
	private Figure activator;
	public DefenderLocation(Room room) {
		this.room = room;
		defenderFigure = DefaultNPCFactory.createDefaultNPC("Willibald", Hero.HEROCODE_WARRIOR);
		defenderFigure.createVisibilityMap(room.getDungeon());
		room.getDungeon().insertFigure(defenderFigure);
		FigureInfo defenderInfo = FigureInfo.makeFigureInfo(defenderFigure, defenderFigure.getRoomVisibility());
		RescuedNPCAI ai = new RescuedNPCAI();
		ai.setFigure(defenderInfo);
		defenderFigure.setControl(new FigureControl(defenderInfo, ai)); // todo: create DefenderAI
		defenderFigure.setLookDir(RouteInstruction.Direction.South);
		state = DefenderState.Inactive;
	}

	@Override
	public DefenderState getState() {
		return state;
	}

	public Hero getDefenderFigure() {
		return defenderFigure;
	}

	@Override
	public void turn(int round) {
		if(state == DefenderState.Inactive) {
			// we do nothing
			return;
		}
		if(defenderFigure.isDead()) {
			changeStateTo(DefenderState.Dead, round);

		}

		if(state == DefenderState.Dead) {
			// nothing can happen
			return;
		}

		if(state == DefenderState.Activated) {
			boolean actionRequired = actionRequired();
			if(actionRequired) {
				room.figureEntersAtPosition(defenderFigure, 2, round);
				changeStateTo(DefenderState.Fighting, round);
			}
		}

		if(state == DefenderState.Fighting) {
			if(!actionRequired()) {
				room.figureLeaves(defenderFigure);
				changeStateTo(DefenderState.Activated, round);
			}
		}
	}

	private void changeStateTo(DefenderState newState, int round) {
		if(state != newState) {
			room.distributePercept(new LocationStateChangePercept(this, room, state, newState, round));
			state = newState;
		}
	}

	private boolean actionRequired() {
		List<Figure> roomFigures = room.getRoomFigures();
		boolean activatorThere = roomFigures.contains(activator);
		if(activatorThere) {
			for (Figure otherFigure : roomFigures) {
				if(otherFigure != activator && activator.getControl().isHostileTo(FigureInfo.makeFigureInfo(otherFigure, defenderFigure.getRoomVisibility()))) {
					// found at least one hostile figure
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getStory() {
		return toString();
	}

	@Override
	public String toString() {
		return  "Beschützer";
	}

	@Override
	public String getText() {
		return toString();
	}

	@Override
	public String getStatus() {
		return state.name();
	}

	@Override
	public int dustCosts() {
		return 10;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		this.activator = f;
		this.state = DefenderState.Activated;
		return true;
	}

	@Override
	public boolean usableOnce() {
		return true;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
}
