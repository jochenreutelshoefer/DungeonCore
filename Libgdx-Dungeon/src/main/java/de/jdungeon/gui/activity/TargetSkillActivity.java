package de.jdungeon.gui.activity;

import java.util.Collections;

import dungeon.DoorInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.action.result.ActionResult;
import skill.TargetSkill;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public class TargetSkillActivity<TARGET> extends AbstractExecutableActivity<TargetSkill<TARGET>> {

	private final PlayerController controller;
	private final TargetSkill<TARGET> skill;

	public TargetSkillActivity(PlayerController controller, TargetSkill<TARGET> skill) {
		this.controller = controller;
		this.skill = skill;
	}

	@Override
	public void execute() {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		TargetSkill.TargetSkillAction<TARGET> action = createAction();
		// action cannot be null here, as it is guarded by the isPossible-mechanism
		controller.getActionAssembler().plugActions(Collections.singletonList(action));
	}

	private TargetSkill.TargetSkillAction<TARGET> createAction() {
		TARGET target = findTargetOfClass();
		if(target == null) return null;
		return skill.newActionFor(controller.getFigure()).target(target).get();
	}

	@Override
	public boolean isCurrentlyPossible() {
		TARGET target = findTargetOfClass();
		TargetSkill.TargetSkillAction<TARGET> action = createAction();
		if(action == null) return false;
		ActionResult testResult = skill.execute(action, false, -1);
		return testResult.getSituation() == ActionResult.Situation.possible;
	}

	private TARGET findTargetOfClass() {
		Class<? extends TARGET> targetClass = skill.getTargetClass();
		if(targetClass.equals(RouteInstruction.Direction.class)) {
			int actorPositionIndex = controller.getFigure().getPositionInRoomIndex();
			RoomInfo room = controller.getFigure().getRoomInfo();
			DoorInfo[] doors = room.getDoors();
			for (DoorInfo door : doors) {
				if(door != null && door.getPositionAtDoor(room).getIndex() == actorPositionIndex) {
					return (TARGET)door.getDirection(room.getNumber());
				}
			}
		}

		return null;
	}

	@Override
	public TargetSkill<TARGET> getObject() {
		return skill;
	}
}
