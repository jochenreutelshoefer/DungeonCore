package de.jdungeon.gui.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import figure.FigureInfo;
import figure.action.result.ActionResult;
import game.InfoEntity;
import game.RoomInfoEntity;
import skill.TargetSkill;
import spell.TargetScope;

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
	public ActivityPlan createExecutionPlan(boolean doIt) {
		TargetSkill.TargetSkillAction<TARGET> action = createAction(doIt);
		// action cannot be null here, as it is guarded by the isPossible-mechanism
		return new SimpleActivityPlan(this, action);
	}

	private TargetSkill.TargetSkillAction<TARGET> createAction(boolean doIt) {
		TARGET target = findUniqueTargetOfClass(doIt);
		if(target == null) return null;
		return skill.newActionFor(playerController.getFigure()).target(target).get();
	}

	@Override
	public ActionResult possible() {
		TargetSkill.TargetSkillAction<TARGET> action = createAction(false);
		if(action == null) return ActionResult.NO_TARGET;
		return skill.execute(action, false, -1);
	}

	private TARGET findUniqueTargetOfClass(boolean doIt) {
		RoomInfoEntity target = findTarget(playerController.getFigure(), playerController.getGameScreen().getFocusManager(), skill.getTargetScope(), doIt);
		if(target != null) {
			return (TARGET) target;
		}
		return null;
	}

	@Override
	public TargetSkill<TARGET> getObject() {
		return skill;
	}

	public static RoomInfoEntity findTarget(FigureInfo figure, LibgdxFocusManager focusManager, TargetScope targetScope, boolean doIt) {

		RoomInfoEntity highlightedEntity = focusManager.getWorldFocusObject();
		List<? extends RoomInfoEntity> potentialTargets = targetScope.getTargetEntitiesInScope(figure, highlightedEntity);

		if(potentialTargets != null && potentialTargets.size() == 1) {
			RoomInfoEntity target = potentialTargets.get(0);
				if(doIt) {
					focusManager.setWorldFocusObject((RoomInfoEntity) target);
				}
			return target;
		}
		// we do not have a unique target, maybe the GUI will help to disambiguate one day (todo)
		return null;
	}

	private static Set<Class<? extends InfoEntity>> toClasses(List<? extends InfoEntity> targetEntitiesInScope) {
		Set<Class<? extends InfoEntity>> result = new HashSet<>();
		if(targetEntitiesInScope == null) {
			return result;
		}
		for (InfoEntity infoEntity : targetEntitiesInScope) {
			if(infoEntity != null) {
				result.add(infoEntity.getClass());
			}
		}
		return result;
	}

}
