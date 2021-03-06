package de.jdungeon.gui.activity;

import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.spell.SpellInfo;
import de.jdungeon.spell.TargetScope;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.gui.LibgdxFocusManager;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class SpellActivity extends AbstractExecutableActivity<SpellInfo> {

	private final SpellInfo spell;
	private final ActionAssembler actionAssembler;
	private final LibgdxFocusManager focusManager;

	public SpellActivity(SpellInfo spell, PlayerController controller) {
		super(controller);
		this.spell = spell;
		this.actionAssembler = controller.getActionAssembler();
		this.focusManager = controller.getGameScreen().getFocusManager();
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt, Object targetObject) {
		TargetScope targetScope = spell.getTargetScope();
		RoomInfoEntity target = null;
		if (targetScope != null) {
			target = TargetSkillActivity.findTarget(playerController.getFigure(), focusManager, targetScope, doIt, targetObject);
		}
		return new SimpleActivityPlan(this, actionAssembler.getActionAssemblerHelper().wannaSpell(spell, target));
	}

	@Override
	public ActionResult possible(Object targetObject) {
		TargetScope targetScope = spell.getTargetScope();
		RoomInfoEntity target = TargetSkillActivity.findTarget(playerController.getFigure(), focusManager, targetScope, false, targetObject);
		return spell.isCurrentlyPossible(actionAssembler.getFigure(), target);
	}

	@Override
	public SpellInfo getObject() {
		return spell;
	}
}
