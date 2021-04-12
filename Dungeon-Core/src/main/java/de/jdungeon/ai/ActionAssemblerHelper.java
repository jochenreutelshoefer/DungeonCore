/*
 * Created on 12.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.Dir;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.switchPos.ProposeSwitchPositionAction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.action.EquipmentChangeAction;
import de.jdungeon.figure.action.LayDownItemAction;
import de.jdungeon.figure.action.LockAction;
import de.jdungeon.figure.action.MoveAction;
import de.jdungeon.figure.action.ScoutAction;
import de.jdungeon.figure.action.UseLocationAction;
import de.jdungeon.figure.action.SpellAction;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.action.TakeItemAction;
import de.jdungeon.figure.action.UseChestAction;
import de.jdungeon.figure.action.UseItemAction;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.skill.AttackSkill;
import de.jdungeon.skill.FleeSkill;
import de.jdungeon.skill.Skill;
import de.jdungeon.spell.SpellInfo;

public class ActionAssemblerHelper {

    private final FigureInfo figure;

    public ActionAssemblerHelper(FigureInfo figure) {
        this.figure = figure;
    }

    public List<Action> wannaAttack(FigureInfo o) {
        AttackSkill.AttackSkillAction attackSkillAction = figure.getSkill(AttackSkill.class)
                .newActionFor(figure)
                .target(o)
                .get();
        return Collections.singletonList(attackSkillAction);
    }

    public List<Action> wannaSwitchPositions(FigureInfo other) {
        return Collections.singletonList(new ProposeSwitchPositionAction(this.figure, other));
    }

    public FigureInfo getFigure() {
        return figure;
    }

    public List<Action> wannaDo(Class<? extends Skill> skillClass) {
        return Collections.singletonList(figure.getSkill(skillClass).newActionFor(figure).get());
    }

    public List<Action> wannaFlee() {
        return Collections.singletonList(figure.getSkill(FleeSkill.class).newActionFor(figure).get());
    }

    /**
     * Tries to identify the next best door in the current room of the de.jdungeon.figure to be locked/unlocked
     * and creates a action sequence respectively
     * <p>
     * returns null if there is no door to lock/unlock or if there are multiple and it's not clear which to use
     *
     * @return actions to lock the next best door
     */
    public List<Action> wannaLockDoor() {
        DoorInfo[] doors = this.figure.getRoomInfo().getDoors();
        DoorInfo doorWithLock = null;
        for (DoorInfo door : doors) {
            if (door != null) {
                if (door.hasLock()) {
                    // found door with a lock
                    if (doorWithLock != null) {
                        // it is the second door in this room with a lock
                        int currentFigurePosition = figure.getPositionInRoomIndex();
                        if (figure.getPositionInRoomIndex() == door.getPositionAtDoor(figure.getRoomInfo(), false)
                                .getIndex()) {
                            // de.jdungeon.figure is already on correct position for this door
                            doorWithLock = door;
                        }
                    } else {
                        doorWithLock = door;
                    }
                }
            }
        }
        if (doorWithLock == null) {
            // did not find an appropriate door the lock/unlock
            return null;
        }
        return wannaLockDoor(doorWithLock);
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
        RouteInstruction.Direction dir = d.getDirection(figure.getRoomNumber());
        if (dir != null) {
            Action a = new LockAction(figure, d);
            actions.add(a);
        }
        return actions;
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
        Action a = new SpellAction(this.figure, sp, target);
        List<Action> result = new ArrayList<>();
        result.add(a);
        return result;
    }

    public List<Action> wannaUseItem(ItemInfo it, RoomInfoEntity target, boolean meta) {
        List<Action> actions = new ArrayList<>();
        if (target == null && it.needsTarget()) {
            de.jdungeon.log.Log.warning("Wanna use de.jdungeon.item without target but target required; should not happen!");
            return Collections.emptyList();
        }

        Action a = new UseItemAction(this.figure, it, target, meta);
        actions.add(a);
        return actions;
    }


    public List<Action> wannaUseShrine(RoomInfoEntity target, boolean right) {
        List<Action> actions = new ArrayList<>();
        if (this.getFigure().getPositionInRoomIndex() != Position.Pos.NE.getValue()) {
            actions.addAll(wannaStepToPosition(Position.Pos.NE));
        }
        Action a = new UseLocationAction(this.figure, target, right);
        actions.add(a);
        return actions;
    }

    public List<Action> wannaSwitchEquipmentItem(int type, int index) {
        Action a = new EquipmentChangeAction(type, index);
        return Collections.singletonList(a);
    }

    public List<Action> wannaLayDownItem(ItemInfo it) {
        Action a = new LayDownItemAction(this.figure, it);
        return Collections.singletonList(a);
    }

    @Deprecated
    public List<Action> wannaLayDownEquipmentItem(int type) {
        Action a = new LayDownItemAction(this.figure, true, type);
        return Collections.singletonList(a);
    }

    @Deprecated
    public List<Action> wannaLayDownItem(int index) {
        Action a = new LayDownItemAction(this.figure, false, index);
        return Collections.singletonList(a);
    }

    public List<Action> wannaUseChest(boolean right) {
        List<Action> actions = new ArrayList<>();
        if (this.getFigure().getPositionInRoomIndex() != Position.Pos.NW.getValue()) {
            actions.addAll(wannaStepToPosition(Position.Pos.NW));
        }
        Action a = new UseChestAction(this.figure, right);
        actions.add(a);
        return actions;
    }

    public List<Action> wannaTakeItem(ItemInfo item) {
        Action take = new TakeItemAction(this.figure, item);
        return Collections.singletonList(take);
    }

    public List<Action> wannaWalk(int dir) {
        if (figure.isDead()) return Collections.emptyList(); // death problem
        List<Action> actions = new ArrayList<>();
        FigureInfo f = getFigure();
        int index = Figure.getDirPos(dir);
        if (f.getPositionInRoomIndex() != index) {
            actions.addAll(doStepTo(index));
        }
		/*
		if (f.getActionPoints() < 1) {
			EndRoundAction endRound = new EndRoundAction();
			actions.add(endRound);
		}
		*/
        MoveAction moveAction = new MoveAction(f, f.getRoomNumber(), dir);
        actions.add(moveAction);
        return actions;
    }

    private List<Action> doStepTo(int i) {
        List<Action> actions = new ArrayList<>();
        if (getFigure().getActionPoints() >= 1) {

            StepAction step = new StepAction(this.figure, i);
            actions.add(step);
            if (getFigure().getActionPoints() == 1) {
                EndRoundAction endRoundAction = new EndRoundAction();
                actions.add(endRoundAction);
            }
        } else {
            EndRoundAction ndRoundAction = new EndRoundAction();
            StepAction stepAction = new StepAction(this.figure, i);
            actions.add(ndRoundAction);
            actions.add(stepAction);
            EndRoundAction endRoundAction = new EndRoundAction();
            actions.add(endRoundAction);
        }
        return actions;
    }

    public List<Action> monsterClicked(FigureInfo o, boolean right) {
        if (o.isHostile(this.figure)) {
            return wannaAttack((o));
        } else {
            return Collections.singletonList(new ProposeSwitchPositionAction(this.figure, o));
        }
    }

    public List<Action> itemClicked(ItemInfo o, boolean right) {
        Action a = new TakeItemAction(this.figure, o);
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
            return wannaScout(dir);
        } else {

            if (f.getRoomInfo().fightRunning() != null && f.getRoomInfo().fightRunning()) {
                int pos = getFigure().getPositionInRoomIndex();
                if ((dir == 1 & pos == 1) || (dir == 2 && pos == 3)
                        || (dir == 3 && pos == 5) || (dir == 4 && pos == 7)) {
                    return this.wannaFlee();
                }
            } else {
                return this.wannaWalk(dir);
            }
        }
        return Collections.emptyList();
    }

    public List<Action> shrineClicked(boolean right) {
        return wannaUseShrine(null, right);
    }

    public List<Action> chestClicked(Object o, boolean right) {
        if (o == null) {
            ChestInfo chest = this.figure.getRoomInfo().getChest();
            if (chest != null) {
                o = chest;
            } else {
                return Collections.emptyList();
            }
        }
        FigureInfo f = getFigure();
        if (o instanceof ChestInfo) {
            ChestInfo chest = ((ChestInfo) o);
            if (chest.getLocation().equals(f.getRoomNumber())) {
                return wannaUseChest(right);
            }
        }
        return Collections.emptyList();
    }

    public List<Action> positionClicked(PositionInRoomInfo pos, boolean right) {
        return wannaStepToPosition(pos);
    }

    public List<Action> wannaStepToPosition(Position.Pos pos) {
        return wannaStepToPosition(figure.getRoomInfo().getPositionInRoom(pos.getValue()));
    }

    public List<Action> wannaStepNorth() {
        PositionInRoomInfo pos = figure.getPos();
        int currentPosIndex = pos.getIndex();
        Position.Pos currentPos = Position.Pos.fromValue(currentPosIndex);
        if (currentPos == Position.Pos.NW || currentPos == Position.Pos.N || currentPos == Position.Pos.NE) {
            return Collections.emptyList();
        }
        if (currentPos == Position.Pos.E) {
            return wannaStepToPosition(Position.Pos.NE);
        }
        if (currentPos == Position.Pos.SE) {
            return wannaStepToPosition(Position.Pos.E);
        }
        if (currentPos == Position.Pos.S) {
            return wannaStepToPosition(Position.Pos.N);
        }
        if (currentPos == Position.Pos.SW) {
            return wannaStepToPosition(Position.Pos.W);
        }
        if (currentPos == Position.Pos.W) {
            return wannaStepToPosition(Position.Pos.NW);
        }
        return Collections.emptyList();
    }

    public List<Action> wannaStepSouth() {
        PositionInRoomInfo pos = figure.getPos();
        int currentPosIndex = pos.getIndex();
        Position.Pos currentPos = Position.Pos.fromValue(currentPosIndex);
        if (currentPos == Position.Pos.SW || currentPos == Position.Pos.S || currentPos == Position.Pos.SE) {
            return Collections.emptyList();
        }
        if (currentPos == Position.Pos.E) {
            return wannaStepToPosition(Position.Pos.SE);
        }
        if (currentPos == Position.Pos.NE) {
            return wannaStepToPosition(Position.Pos.E);
        }
        if (currentPos == Position.Pos.N) {
            return wannaStepToPosition(Position.Pos.S);
        }
        if (currentPos == Position.Pos.NW) {
            return wannaStepToPosition(Position.Pos.W);
        }
        if (currentPos == Position.Pos.W) {
            return wannaStepToPosition(Position.Pos.SW);
        }
        return Collections.emptyList();
    }

    public List<Action> wannaStepWest() {
        PositionInRoomInfo pos = figure.getPos();
        int currentPosIndex = pos.getIndex();
        Position.Pos currentPos = Position.Pos.fromValue(currentPosIndex);
        if (currentPos == Position.Pos.SW || currentPos == Position.Pos.W || currentPos == Position.Pos.NW) {
            return Collections.emptyList();
        }
        if (currentPos == Position.Pos.E) {
            return wannaStepToPosition(Position.Pos.W);
        }
        if (currentPos == Position.Pos.NE) {
            return wannaStepToPosition(Position.Pos.N);
        }
        if (currentPos == Position.Pos.N) {
            return wannaStepToPosition(Position.Pos.NW);
        }
        if (currentPos == Position.Pos.SE) {
            return wannaStepToPosition(Position.Pos.S);
        }
        if (currentPos == Position.Pos.S) {
            return wannaStepToPosition(Position.Pos.SW);
        }
        return Collections.emptyList();
    }

    public List<Action> wannaStepEast() {
        PositionInRoomInfo pos = figure.getPos();
        int currentPosIndex = pos.getIndex();
        Position.Pos currentPos = Position.Pos.fromValue(currentPosIndex);
        if (currentPos == Position.Pos.SE || currentPos == Position.Pos.E || currentPos == Position.Pos.NE) {
            return Collections.emptyList();
        }
        if (currentPos == Position.Pos.W) {
            return wannaStepToPosition(Position.Pos.E);
        }
        if (currentPos == Position.Pos.NW) {
            return wannaStepToPosition(Position.Pos.N);
        }
        if (currentPos == Position.Pos.N) {
            return wannaStepToPosition(Position.Pos.NE);
        }
        if (currentPos == Position.Pos.SW) {
            return wannaStepToPosition(Position.Pos.S);
        }
        if (currentPos == Position.Pos.S) {
            return wannaStepToPosition(Position.Pos.SE);
        }
        return Collections.emptyList();
    }

    public List<Action> wannaStepToPosition(PositionInRoomInfo pos) {
        return wannaStepToPosition(pos, false);
    }

    @Deprecated // unanimated not used
    public List<Action> wannaStepToPosition(PositionInRoomInfo pos, boolean unanimated) {
        Action a = new StepAction(this.figure, pos.getIndex());
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
