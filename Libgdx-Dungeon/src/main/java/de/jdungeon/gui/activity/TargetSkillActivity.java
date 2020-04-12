package de.jdungeon.gui.activity;

import dungeon.DoorInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.action.result.ActionResult;
import game.RoomInfoEntity;
import skill.TargetSkill;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.gui.LibgdxFocusManager;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public class TargetSkillActivity<TARGET> extends SkillActivity<TargetSkill<TARGET>> {

	private final TargetSkill<TARGET> skill;

	public TargetSkillActivity(PlayerController controller, TargetSkill<TARGET> skill) {
		super(controller);
		this.skill = skill;
	}

	@Override
	public ActivityPlan createExecutionPlan() {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		TargetSkill.TargetSkillAction<TARGET> action = createAction();
		// action cannot be null here, as it is guarded by the isPossible-mechanism
		return new SimpleActivityPlan(this, action);
	}

	private TargetSkill.TargetSkillAction<TARGET> createAction() {
		TARGET target = findUniqueTargetOfClass();
		if(target == null) return null;
		return skill.newActionFor(playerController.getFigure()).target(target).get();
	}

	@Override
	public ActionResult possible() {
		TargetSkill.TargetSkillAction<TARGET> action = createAction();
		if(action == null) return ActionResult.NO_TARGET;
		return skill.execute(action, false, -1);
	}

	private TARGET findUniqueTargetOfClass() {
		LibgdxFocusManager focusManager = playerController.getGameScreen().getFocusManager();
		RoomInfoEntity worldFocusObject = focusManager.getWorldFocusObject();
		// todo: handle highlighted entities

		Class<? extends TARGET> targetClass = skill.getTargetClass();
		if(targetClass.equals(RouteInstruction.Direction.class)) {

			// are we next to a door?
			int actorPositionIndex = playerController.getFigure().getPositionInRoomIndex();
			RoomInfo room = playerController.getFigure().getRoomInfo();
			if(room == null) return null; // exit problem
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
