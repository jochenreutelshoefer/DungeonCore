/*
 * Created on 06.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ai;

import figure.monster.DarkMaster;
import figure.monster.Dwarf;
import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import item.ItemInfo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import spell.SpellInfo;
import dungeon.Dir;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.action.SpellAction;
import figure.action.StepAction;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import figure.percept.Percept;

public class DefaultMonsterIntelligence extends AbstractAI {

	protected MonsterInfo monster;

	protected Vector<Action> actionQueue = new Vector<Action>();

	protected Action lastAction = null;

	public DefaultMonsterIntelligence() {
		super(new AttitudeMonsterDefault());
	}

	@Override
	public void setFigure(FigureInfo info) {
		if (info instanceof MonsterInfo) {
			// TODO: remove monster info object
			monster = ((MonsterInfo) info);
		}
		this.info = info;
	}

	@Override
	protected void processPercept(Percept p) {

	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return att.isHostile(f);
	}

	private int getRandomWalkFactor() {
		Class<? extends Monster> monsterClass = monster.getMonsterClass();
		int factor = 0;
		if (monsterClass == Orc.class) {
			factor = 25;
		}
		if (monsterClass == Ogre.class) {
			factor = 3;
		}
		if (monsterClass == Wolf.class) {
			factor = 40;
		}
		if (monsterClass == Spider.class) {
			factor = 5;
		}
		if (monsterClass == Ghul.class) {
			factor = 3;
		}
		if (monsterClass == Skeleton.class) {
			factor = 30;
		}
		if (monsterClass == Dwarf.class) {
			factor = 0;
		}
		if (monsterClass == DarkMaster.class) {
			factor = 30;
		}

		return factor;
	}

	@Override
	public Action chooseMovementAction() {

		if (monster.getActionPoints() == 0) {
			Action a = Action.makeEndRoundAction(/* monster.getFigureID() */);
			lastAction = a;
			return a;
		}
		if (!actionQueue.isEmpty()) {
			Action a = actionQueue.remove(0);
			lastAction = a;
			return a;
		}


		int randomWalkFactor = getRandomWalkFactor();
		if (Math.random() * 100 < 1.0 * randomWalkFactor) {
			// System.out.println(monster.getName()+" wanna walk");
			int dir = getRandomPassableDir();
			if (dir == 0 || dir == -1) {
				return new EndRoundAction();
			}
			Action a = walk(dir);
			lastAction = a;
			return a;
		}

		return new EndRoundAction();

	}

	protected Action walk(int dir) {
		Action a = new MoveAction(this.monster.getRoomNumber(), dir);
		// System.out.println("Dir to walk: "+dir);

		int pos = -1;
		if (dir == Dir.EAST) {
			pos = 3;
		}
		if (dir == Dir.WEST) {
			pos = 7;
		}
		if (dir == Dir.NORTH) {
			pos = 1;
		}
		if (dir == Dir.SOUTH) {
			pos = 5;
		}

		if (monster.getPositionInRoomIndex() == pos) {
			return a;
		} else {
			actionQueue.add(a);
			return new StepAction(pos);
		}

	}

	protected int getRandomPassableDir() {
		int doors[] = monster.getRoomDoors();
		boolean poss[] = new boolean[4];
		boolean dirExisting = false;
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] == Door.DOOR || doors[i] == Door.DOOR_LOCK_OPEN) {
				poss[i] = true;
				dirExisting = true;
			} else {
				poss[i] = false;
			}
		}

		if (!dirExisting) {
			return 0;
		}
		int k = -1;
		int dir = ((int) (Math.random() * 4));
		while (k == -1) {
			if (poss[dir]) {
				k = dir;
			}
			dir = ((int) (Math.random() * 4));
		}
		return k + 1;
	}

	public static Action getStepAwayAction(FigureInfo monster, FigureInfo enemy) {
		int otherPosIndex = enemy.getPositionInRoomIndex();
		int posIndex = monster.getPositionInRoomIndex();
		int rightDist = Position.getDistanceFromTo(posIndex, otherPosIndex,
				true);
		int leftDist = Position.getDistanceFromTo(posIndex, otherPosIndex,
				false);
		if (rightDist > leftDist) {
			int wantIndex = Position.getNextIndex(posIndex, true);
			PositionInRoomInfo info = monster.getRoomInfo().getPositionInRoom(
					wantIndex);
			if (!info.isOccupied()) {
				return new StepAction(wantIndex);
			}
		} else if (leftDist > rightDist) {
			int wantIndex = Position.getNextIndex(posIndex, false);
			PositionInRoomInfo info = monster.getRoomInfo().getPositionInRoom(
					wantIndex);
			if (!info.isOccupied()) {
				return new StepAction(wantIndex);
			}
		}

		return null;
	}

	public static Action getFleeAction(FigureInfo monster) {
		Action a = Action.makeActionFlee();
		ActionResult res = monster.checkAction(Action.makeActionFlee());
		if (res.getSituation() == ActionResult.Situation.possible) {
			return a;
		} else {

			StepAction step = getStepActionToDoor(monster);
			if (step != null) {
				ActionResult stepRes = monster.checkAction(step);
				if (stepRes.getSituation() == ActionResult.Situation.possible) {
					return step;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}



	public static StepAction getStepActionToDoor(FigureInfo monster) {
		DoorStep[] stepsA = getDoorSteps(monster);
		if (stepsA.length > 0) {

			DoorStep step = stepsA[0];
			return new StepAction(step.getIndex());
		}
		return null;

	}

	private static DoorStep[] getDoorSteps(FigureInfo monster) {
		int doors[] = monster.getRoomDoors();
		List<DoorStep> steps = new LinkedList<>();
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] == Door.DOOR || doors[i] == Door.DOOR_LOCK_OPEN) {
				DoorStep step = calcDoorStep(i, monster);
				if (step != null) {
					steps.add(step);
				}

			}
		}
		Object[] stepsA = steps.toArray();
		Arrays.sort(stepsA);
		DoorStep[] a = new DoorStep[stepsA.length];
		for (int i = 0; i < stepsA.length; i++) {
			a[i] = (DoorStep) stepsA[i];
		}
		return a;

	}

	private static DoorStep calcDoorStep(int door, FigureInfo monster) {
		RoomInfo info = monster.getRoomInfo();
		int pos = monster.getPositionInRoomIndex();

		int doorPos = -1;
		if (door == 0) {
			doorPos = 1;
		}
		if (door == 1) {
			doorPos = 3;
		}
		if (door == 2) {
			doorPos = 5;
		}
		if (door == 3) {
			doorPos = 7;
		}
		boolean[] poses = getOccupiedPoses(info);
		boolean isFree = scanWay(poses, pos, doorPos, true, true);
		if (isFree) {
			return new DoorStep(Position.incIndex(pos),
					Position.getDistanceFromTo(doorPos, pos, true), door);
		}

		isFree = scanWay(poses, pos, doorPos, false, true);
		if (isFree) {
			return new DoorStep(Position.decIndex(pos),
					Position.getDistanceFromTo(doorPos, pos, false), door);
		}

		return null;
	}

	private static boolean scanWay(boolean[] poses, int a, int b,
			boolean dirIsRight, boolean freeTargetPosition) {
		int k = a;
		while (k != b) {
			if (dirIsRight) {
				k = Position.incIndex(k);
			} else {
				k = Position.decIndex(k);
			}
			if (!freeTargetPosition) {
				if (k == b) {
					break;
				}
			}
			if (poses[k]) {
				return false;
			}
		}
		return true;
	}

	private static boolean[] getOccupiedPoses(RoomInfo info) {
		boolean[] poses = new boolean[8];
		for (int i = 0; i < 8; i++) {
			if (info.getPositionInRoom(i).isOccupied()) {
				poses[i] = true;
			} else {
				poses[i] = false;
			}
		}
		return poses;
	}

	protected StepAction stepToEnemy() {
		RoomInfo info = this.monster.getRoomInfo();

		StepAction possibleAction = null;
		for (int i = 0; i < 8; i++) {

			PositionInRoomInfo pos = info.getPositionInRoom(i);
			FigureInfo otherFigure  = pos.getFigure();
			if (otherFigure != null && this.isHostileTo(otherFigure)) {
				if (Position.getMinDistanceFromTo(
						monster.getPositionInRoomIndex(), i) == 1) {
					return null;
				}
				StepAction a = stepTowardsPosition(i);
				if (a != null) {
					possibleAction = a;
					break;
				}
			}
		}

		return possibleAction;
	}

	private StepAction stepTowardsPosition(int doorPos) {
		int pos = monster.getPositionInRoomIndex();
		boolean poses[] = getOccupiedPoses(monster.getRoomInfo());
		boolean isFree = scanWay(poses, pos, doorPos, true, false);
		DoorStep step1 = null;
		DoorStep step2 = null;
		if (isFree) {
			step1 = new DoorStep(Position.incIndex(pos),
					Position.getDistanceFromTo(pos, doorPos, true), -1);
		}

		isFree = scanWay(poses, pos, doorPos, false, false);
		if (isFree) {
			step2 = new DoorStep(Position.decIndex(pos),
					Position.getDistanceFromTo(pos, doorPos, false), -1);
		}
		if (step1 != null && step2 != null) {
			if (step1.getDistance() > step2.getDistance()) {
				return new StepAction(step2.getIndex());
			} else {
				return new StepAction(step1.getIndex());
			}
		} else {
			if (step1 != null) {
				return new StepAction(step1.getIndex());
			} else {
				if (step2 != null) {
					new StepAction(step2.getIndex());
				}
			}
		}
		return null;
	}

	protected Action trySpell(SpellInfo s) {

		HeroInfo hero = monster.getRoomInfo().getHeroInfo();
		if (hero != null) {
			Action a = new SpellAction(s, hero);
			ActionResult res = monster.checkAction(a);
			if (res.getSituation() == ActionResult.Situation.possible) {
				return a;
			}

		}
		return null;

	}

	protected int getHeroIndex() {
		List<FigureInfo> infos = monster.getRoomInfo().getFigureInfos();
		int heroIndex = -1;
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i) instanceof HeroInfo) {
				heroIndex = ((HeroInfo) infos.get(i)).getFighterID();
			}
		}
		return heroIndex;
	}

	protected Action wannaSpell() {
		// Spell fortsetzen
		if (monster.getLastSpell() != null) {
			Action a = trySpell(monster.getLastSpell());
			if (a != null) {
				return a;
			}
		}

		List<SpellInfo> spells = monster.getSpells();
		for (Iterator<SpellInfo> iter = spells.iterator(); iter.hasNext();) {
			SpellInfo element = iter.next();
			Action a = trySpell(element);
			if (a != null) {
				return a;
			}

		}
		return null;
	}

	@Override
	public Action chooseFightAction() {

		Action a = wannaSpell();
		if (a != null) {
			return a;
		}

		int monsterCount = monster.getRoomInfo().getMonsterInfos().size();
		int heroIndex = getHeroIndex();

		if (monsterCount == 1) {
			return getActionForMonsterCount1();

		}
		if (monsterCount == 2) {
			return getActionForMonsterCount2();
		}
		if (monsterCount > 2) {
			return Action.makeActionAttack(heroIndex);
		}
		return Action.makeActionAttack(heroIndex);
	}

	protected Action getActionForMonsterCount2() {
		int heroIndex = getHeroIndex();
		int healthLevel = monster.getHealthLevel().getValue();
		if (healthLevel <= Figure.STATUS_CRITICAL && Math.random() < 0.4) {
			Action a = getFleeAction(monster);
			if (a != null) {
				return a;
			} else {
				return Action.makeActionAttack(heroIndex);
			}
		} else {
			if (Math.random() > 0.2) {
				return Action.makeActionAttack(heroIndex);
			} else {
				StepAction step = stepToEnemy();
				if (step != null) {
					return step;
				} else {
					return Action.makeActionAttack(heroIndex);
				}
			}
		}

	}

	protected Action getActionForMonsterCount1() {
		int heroIndex = getHeroIndex();
		int healthLevel = monster.getHealthLevel().getValue();
		if (healthLevel <= Figure.STATUS_HEALTHY && Math.random() < 0.05) {
			Action a = getFleeAction(monster);
			if (a != null) {
				return a;
			} else {
				return Action.makeActionAttack(heroIndex);
			}
		} else {
			if (Math.random() > 0.2) {
				return Action.makeActionAttack(heroIndex);
			} else {
				StepAction step = stepToEnemy();
				if (step != null) {
					return step;
				} else {
					return Action.makeActionAttack(heroIndex);
				}
			}
		}
	}

	public Action turnElse(int c) {

		int a = (int) (Math.random() * 100);
		if (c == 1) { // damit er auf jeden Fall weggeht
			a = (int) (Math.random() * 20) + 80;
		}
		if (a <= 80) {
			return null;
		} else if (a <= 85) {
			return new MoveAction(this.monster.getRoomInfo().getLocation(), RouteInstruction.SOUTH);
		} else if (a <= 90) {
			return new MoveAction(this.monster.getRoomInfo().getLocation(), RouteInstruction.EAST);
		} else if (a <= 95) {
			return new MoveAction(this.monster.getRoomInfo().getLocation(), RouteInstruction.NORTH);
		} else {
			return new MoveAction(this.monster.getRoomInfo().getLocation(), RouteInstruction.WEST);
		}
	}

}

class DoorStep implements Comparable<DoorStep> {
	int index;

	int distance;

	int door;

	public DoorStep(int i, int d, int door) {
		index = i;
		distance = d;
		this.door = door;
	}

	public int getIndex() {
		return index;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public int compareTo(DoorStep o) {
		if (o instanceof DoorStep) {
			DoorStep other = o;
			if (this.distance < other.distance) {
				return 1;
			} else {
				if (this.distance > other.distance) {
					return -1;
				}
			}
		}
		return 0;
	}

}