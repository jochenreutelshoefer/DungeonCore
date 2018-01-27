/*
 * Created on 12.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import dungeon.ChestInfo;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.EquipmentChangeAction;
import figure.action.LayDownItemAction;
import figure.action.LearnSpellAction;
import figure.action.LockAction;
import figure.action.MoveAction;
import figure.action.ScoutAction;
import figure.action.ShrineAction;
import figure.action.SkillUpAction;
import figure.action.SpellAction;
import figure.action.StepAction;
import figure.action.TakeItemAction;
import figure.action.UseChestAction;
import figure.action.UseItemAction;
import game.RoomInfoEntity;
import item.ItemInfo;
import spell.SpellInfo;
import spell.TargetScope;

public class ActionAssembler {

	private final FigureInfo figure;

	public ActionAssembler(FigureInfo figure) {
		this.figure = figure;
	}

	public List<Action> wannaAttack(FigureInfo o) {
		Action a = Action.makeActionAttack(o.getFighterID());
		return Collections.singletonList(a);
	}

	public FigureInfo getFigure() {
		return figure;
	}

	public List<Action> wannaFlee() {
		Action a = Action.makeActionFlee();
		return Collections.singletonList(a);
	}

	public List<Action> wannaFleePanic(int dir) {
		Action a = Action.makeActionFlee(true);
		return Collections.singletonList(a);
	}

	public List<Action> wannaLernSpell(SpellInfo spell) {
		Action a = new LearnSpellAction(spell);
		return Collections.singletonList(a);
	}

	public List<Action> wannaLockDoor(DoorInfo d) {
		List<Action> actions = new ArrayList<>();
		PositionInRoomInfo positionAtDoor = d.getPositionAtDoor(this.getFigure().getRoomInfo(), false);
		if (!getFigure().getPos().equals(positionAtDoor)) {
			// we need to step towards the door first
			List<Action> stepAction = wannaStepToPosition(positionAtDoor);
			if (!stepAction.isEmpty() && (!getFigure().getRoomInfo().fightRunning())) {
				actions.addAll(stepAction);
				EndRoundAction endRoundAction = new EndRoundAction();
				actions.add(endRoundAction);
			}
		}
		FigureInfo f = getFigure();
		RouteInstruction.Direction dir = d.getDirection(f.getRoomNumber());
		if (dir != null) {
			Action a = new LockAction(d);
			actions.add(a);
		}
		return actions;
	}

	public List<Action> wannaScout(RouteInstruction.Direction dir) {
		return wannaScout(dir.getValue());
	}

	public List<Action> wannaScout(int direction) {
		List<Action> actions = new ArrayList<>();
		Action a = new ScoutAction(this.getFigure(), direction);
		int index = Figure.getDirPos(direction);
		if (figure.getPositionInRoomIndex() != index) {
			actions.addAll(doStepTo(index));
		}
		if (figure.getActionPoints() < 1) {
			EndRoundAction endRoundAction = new EndRoundAction();
			actions.add(endRoundAction);
		}
		actions.add(a);
		return actions;
	}

	public List<Action> wannaSpell(SpellInfo sp, RoomInfoEntity target) {
		Action a = new SpellAction(sp, target);
		return Collections.singletonList(a);
	}

	public List<Action> wannaUseItem(ItemInfo it, RoomInfoEntity target, boolean meta) {
		List<Action> actions = new ArrayList<>();
		if (target == null && it.needsTarget()) {
			target = findAndStepTowardsTarget(it, actions);
		}

		Action a = new UseItemAction(it, target, meta);
		actions.add(a);
		return actions;
	}

	private RoomInfoEntity findAndStepTowardsTarget(ItemInfo item, List<Action> actions) {
		RoomInfoEntity target = null;
		if (item.isUsableWithTarget()) {
			TargetScope targetScope = item.getTargetScope();
			List<? extends RoomInfoEntity> targetEntitiesInScope = targetScope.getTargetEntitiesInScope(this.getFigure());
			if (targetEntitiesInScope.size() == 1) {
				// there is only one possibility
				target = targetEntitiesInScope.get(0);
			}
		}
		if (target != null) {
			Collection<PositionInRoomInfo> interactionPositions = target.getInteractionPositions();
			if (!interactionPositions.isEmpty()) {
				PositionInRoomInfo currentPos = getFigure().getPos();
				if (!interactionPositions.contains(currentPos)) {
					// TODO: filter for position in same room !
					Collection<PositionInRoomInfo> interactionPositionsInCurrentRoom = new HashSet<>();
					for (PositionInRoomInfo interactionPosition : interactionPositions) {
						if (interactionPosition.getLocation().equals(getFigure().getRoomNumber())) {
							interactionPositionsInCurrentRoom.add(interactionPosition);
						}
					}
					PositionInRoomInfo position = null;
					if (!interactionPositionsInCurrentRoom.isEmpty()) {
						position = interactionPositionsInCurrentRoom.iterator().next();
					}
					else {
						if (!interactionPositions.isEmpty()) {
							position = interactionPositions.iterator().next();
						}
					}
					if (position != null) {
						wannaStepToPosition(position);
						EndRoundAction endR = new EndRoundAction();
						actions.add(endR);
					}
				}
			}
		}
		return target;
	}

	public List<Action> wannaUseShrine(RoomInfoEntity target, boolean right) {
		List<Action> actions = new ArrayList<>();
		if (this.getFigure().getPositionInRoomIndex() != Position.Pos.NE.getValue()) {
			actions.addAll(wannaStepToPosition(Position.Pos.NE));
		}
		Action a = new ShrineAction(target, right);
		actions.add(a);
		return actions;
	}

	public List<Action> wannaSwitchEquipmentItem(int type, int index) {
		Action a = new EquipmentChangeAction(type, index);
		return Collections.singletonList(a);
	}

	public List<Action> wannaLayDownItem(ItemInfo it) {
		Action a = new LayDownItemAction(it);
		return Collections.singletonList(a);
	}

	@Deprecated
	public List<Action> wannaLayDownEquipmentItem(int type) {
		Action a = new LayDownItemAction(true, type);
		return Collections.singletonList(a);
	}

	@Deprecated
	public List<Action> wannaLayDownItem(int index) {
		Action a = new LayDownItemAction(false, index);
		return Collections.singletonList(a);
	}

	public List<Action> wannaUseChest(boolean right) {
		List<Action> actions = new ArrayList<>();
		if (this.getFigure().getPositionInRoomIndex() != Position.Pos.NW.getValue()) {
			actions.addAll(wannaStepToPosition(Position.Pos.NW));
		}
		Action a = new UseChestAction(right);
		actions.add(a);
		return actions;
	}

	public List<Action> wannaTakeItem(ItemInfo item) {
		Action take = new TakeItemAction(item);
		return Collections.singletonList(take);
	}

	public List<Action> wannaWalk(int dir) {
		List<Action> actions = new ArrayList<>();
		FigureInfo f = getFigure();
		int index = Figure.getDirPos(dir);
		if (f.getPositionInRoomIndex() != index) {
			actions.addAll(doStepTo(index));
		}
		if (f.getActionPoints() < 1) {
			EndRoundAction endRound = new EndRoundAction();
			actions.add(endRound);
		}
		MoveAction moveAction = new MoveAction(dir);
		actions.add(moveAction);
		return actions;
	}

	private List<Action> doStepTo(int i) {
		List<Action> actions = new ArrayList<>();
		if (getFigure().getActionPoints() >= 1) {

			StepAction step = new StepAction(i);
			actions.add(step);
			if (getFigure().getActionPoints() == 1) {
				EndRoundAction endRoundAction = new EndRoundAction();
				actions.add(endRoundAction);
			}

		}
		else {
			EndRoundAction ndRoundAction = new EndRoundAction();
			StepAction stepAction = new StepAction(i);
			actions.add(ndRoundAction);
			actions.add(stepAction);
		}
		return actions;
	}

	public List<Action> monsterClicked(FigureInfo o, boolean right) {
		return wannaAttack((o));
	}

	public List<Action> itemClicked(ItemInfo o, boolean right) {
		Action a = new TakeItemAction(o);
		return Collections.singletonList(a);
	}

	public List<Action> roomClicked(Object o, boolean right) {
		FigureInfo f = getFigure();

		int dir = -1;
		if (o instanceof RoomInfo) {
			dir = Dir.getDirFromToIfNeighbour(f.getRoomNumber(), ((RoomInfo) o)
					.getNumber());
		}
		if (o instanceof JDPoint) {
			dir = Dir.getDirFromToIfNeighbour(f.getRoomNumber(), (JDPoint) o);
		}
		if (dir == -1) {
			return Collections.emptyList();
		}
		if (right) {
			if (f.getRoomInfo().fightRunning()) {

				return wannaFleePanic(dir);
			}
			else {
				return wannaScout(dir);
			}
		}
		else {

			if (f.getRoomInfo().fightRunning() != null && f.getRoomInfo().fightRunning()) {
				int pos = getFigure().getPositionInRoomIndex();
				if ((dir == 1 & pos == 1) || (dir == 2 && pos == 3)
						|| (dir == 3 && pos == 5) || (dir == 4 && pos == 7)) {
					return this.wannaFlee();
				}
			}
			else {
				return this.wannaWalk(dir);
			}
		}
		return Collections.emptyList();
	}

	public  List<Action> shrineClicked(boolean right) {
		return wannaUseShrine(null, right);
	}

	public List<Action>  chestClicked(Object o, boolean right) {
		FigureInfo f = getFigure();
		if (o instanceof ChestInfo) {
			ChestInfo chest = ((ChestInfo) o);
			if (chest.getLocation().equals(f.getRoomNumber())) {
				return wannaUseChest(right);

			}
		}
		return Collections.emptyList();
	}

	public  List<Action> positionClicked(PositionInRoomInfo pos, boolean right) {
		return wannaStepToPosition(pos);
	}

	public List<Action> wannaStepToPosition(Position.Pos pos) {
		return wannaStepToPosition(figure.getRoomInfo().getPositionInRoom(pos.getValue()));
	}

	public List<Action> wannaStepToPosition(PositionInRoomInfo pos) {
		return wannaStepToPosition(pos, false);
	}

	public List<Action> wannaStepToPosition(PositionInRoomInfo pos, boolean unanimated) {
		Action a = new StepAction(pos.getIndex());
		if (unanimated) {
			a.setUnanimated();
		}
		return Collections.singletonList(a);
	}

	public List<Action> wannaSkillUp(int key) {
		Action a = new SkillUpAction(key);
		return Collections.singletonList(a);
	}

	public List<Action> doorClicked(RoomInfoEntity o, boolean right) {
		DoorInfo d = ((DoorInfo) o);
		if (d.hasLock()) {
			return wannaLockDoor(d);
		}
		return Collections.emptyList();
	}

	public List<Action> wannaEndRound() {
		Action a = new EndRoundAction();
		return Collections.singletonList(a);
	}

}
