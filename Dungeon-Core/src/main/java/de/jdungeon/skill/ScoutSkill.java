package de.jdungeon.skill;

import java.util.List;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.action.ScoutResult;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.ScoutPercept;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.spell.DefaultTargetScope;
import de.jdungeon.spell.TargetScope;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public class ScoutSkill extends TargetSkill<RoomInfo> {

	@Override
	protected boolean checkPositionOk(TargetSkillAction<RoomInfo> action) {
		Figure figure = action.getActor();
		Position position = figure.getPos();
		RoomInfo target = action.getTarget();
		if(target == null) {
			return false;
		}
		int dir = getDir(target, figure);
		if (position.getIndex() != Figure.getDirPos(dir)) {
			return false;
		}
		return true;
	}

	private int getDir(RoomInfo target, Figure figure) {
		RoomInfo currentRoom = FigureInfo.makeFigureInfo(figure, figure.getViwMap()).getRoomInfo();
		return currentRoom.getDirectionTo(target);
	}

	@Override
	protected boolean checkDistanceOk(TargetSkillAction<RoomInfo> action) {
		return true;
	}

	@Override
	protected boolean isPossibleFight() {
		return false;
	}

	@Override
	protected boolean isPossibleNonFight() {
		return true;
	}

	@Override
	public ActionResult doExecute(TargetSkillAction<RoomInfo> action, boolean doIt, int round) {
		Figure figure = action.getActor();
		if (figure.getActionPoints() < 1) {
			return ActionResult.NOAP;
		}
		int dir = getDir(action.getTarget(), figure);
		Room toScout = figure.getRoom().getNeighbourRoom(dir);
		if (toScout == null) {
			return ActionResult.UNKNOWN;
		}
		Room scoutTarget = figure.getRoom().getNeighbourRoom(dir);
		if (figure.getViwMap()
				.getStatusObject(scoutTarget.getNumber())
				.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return ActionResult.UNKNOWN;
		}
		if (doIt) {
			figure.setLookDir(dir);
			ScoutResult result = scout(action, round);
			figure.getViwMap().addVisibilityModifier(toScout.getNumber(), result);
			Percept p = new ScoutPercept(figure, figure.getRoom(), dir, round);
			figure.getRoom().distributePercept(p);
			return ActionResult.DONE;
		}
		return ActionResult.POSSIBLE;
	}

	public ScoutResult scout(TargetSkillAction<RoomInfo> action, int round) {
		Figure figure = action.getActor();
		Room loc = figure.getRoom();
		Room toScout = action.getTarget().getRoom();
		int dir = loc.getConnectionDirectionTo(toScout);
		Door d = loc.getConnectionTo(toScout);

		boolean scoutable = ((d != null));
		String s = new String();
		int visStatusResult = -1;
		if ((scoutable)) {
			List<Figure> monsters = toScout.getRoomFigures();
			s += ("Du horchst und schaust duch die Ritzen in der Tür -" + "\n");

			figure.decActionPoints(action, round);
			int scoutlevel = calcScout(figure);
			if (scoutlevel == 0) {
				if (monsters.isEmpty()) {
					s += "...aber Du kannst leider nichts rauskriegen";
				}
				else {
					for (int i = 0; i < monsters.size(); i++) {
						s += "...aber Du kannst leider nichts rauskriegen";
						if (Math.random() < 0.4) {
							toScout.distributePercept(new ScoutPercept(figure, figure.getRoom(), dir, round));
						}
					}
				}
				visStatusResult = RoomObservationStatus.VISIBILITY_UNDISCOVERED;
			}
			else if (scoutlevel == 1) {

				if (monsters.isEmpty()) {
					s += "...aber Du kannst leider nichts rauskriegen";
				}
				else {
					for (int i = 0; i < monsters.size(); i++) {
						if (Math.random() < 0.4) {
							toScout.distributePercept(new ScoutPercept(figure, figure.getRoom(), dir, round));
						}
					}
					s += (" und ...Du wirst entdeckt!");
				}
			}
			else if (scoutlevel == 2 || scoutlevel == 3 || scoutlevel == 4) {
				s += ("... aber Du kannst leider nichts rauskriegen");
				visStatusResult = RoomObservationStatus.VISIBILITY_FOUND;
			}
			else if (scoutlevel >= 5) {
				if (!toScout.getRoomFigures().isEmpty()) {
					s += ("Du kannst den Gegner genau beobachten." + "\n");
					for (int i = 0; i < monsters.size(); i++) {
						s += (monsters.get(i) + "\n");
					}
				}
				else {
					s += ("Wenn da jemand gewesen wäre hättest Du es rausgekriegt.");
				}
				visStatusResult = RoomObservationStatus.VISIBILITY_FIGURES;
			}
			else if (scoutlevel >= 8) {
				if (!toScout.getRoomFigures().isEmpty()) {
					s += ("Gegner lokalisiert!\n");
				}
				else {
					s += "Der Raum ist frei\n";
				}
				if (!toScout.getItems().isEmpty()) {
					s += "...und sieh mal was da rumliegt!\n";
				}
				else {
					s += "keine Gegenstände dort\n";
				}
				visStatusResult = RoomObservationStatus.VISIBILITY_ITEMS;
			}
			else {
				s += ("Scout Level Error: " + scoutlevel);
			}
		}
		else {
			s += ("Das funktioniert so jetzt gerade nicht...");
		}
		figure.tellPercept(new TextPercept(s, round));
		return new ScoutResult(figure, visStatusResult);
	}

	protected int calcScout(Figure f) {
		int level = 0;
		int handycap = 120;
		for (int i = 0; i < f.getPsycho().getValue(); i++) { // scout
			// gibt
			// den
			// Wert
			// wie
			// oft
			// geprüft
			// wird,
			// der
			// beste
			// Wert
			// wird
			// genommen
			int a = scoutHelp(handycap, f);
			if (a > level) {
				level = a;
			}
		}

		return level;
	}

	private int scoutHelp(int handycap, Figure f) {
		int value = ((int) (Math.random() * handycap));
		double psychoValue = f.getPsycho().getValue();
		if (value < psychoValue - 5) {
			return 9;
		}
		else if (value < psychoValue * 1) {
			return 8;
		}
		else if (value < psychoValue * 2) {
			return 7;
		}
		else if (value < psychoValue * 3) {
			return 6;
		}
		else if (value < psychoValue * 4) {
			return 5;
		}
		else if (value < psychoValue * 5) {
			return 4;
		}
		else if (value < psychoValue * 6) {
			return 3;
		}
		else if (value < psychoValue * 7) {
			return 2;
		}
		else if (value < psychoValue * 8) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public TargetScope<RoomInfo> getTargetScope() {
		return new DefaultTargetScope<>(RoomInfo.class);
	}
}
