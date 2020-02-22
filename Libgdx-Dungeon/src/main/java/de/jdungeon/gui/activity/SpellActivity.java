package de.jdungeon.gui.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import figure.action.result.ActionResult;
import game.InfoEntity;
import game.RoomInfoEntity;
import spell.SpellInfo;
import spell.TargetScope;
import spell.TargetSpell;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.gui.LibgdxFocusManager;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class SpellActivity extends AbstractExecutableActivity {

	private final SpellInfo spell;
	private final ActionAssembler actionAssembler;
	private final LibgdxFocusManager focusManager;

	public SpellActivity(SpellInfo spell, ActionAssembler actionAssembler, LibgdxFocusManager focusManager) {
		this.spell = spell;
		this.actionAssembler = actionAssembler;
		this.focusManager = focusManager;
	}

	@Override
	public void execute() {
		RoomInfoEntity highlightedEntity = focusManager.getWorldFocusObject();
		RoomInfoEntity target = null;
		if (spell.needsTarget()) {
			TargetScope targetScope = spell.getTargetScope();
			List<? extends RoomInfoEntity> targetEntitiesInScope = targetScope.getTargetEntitiesInScope(actionAssembler.getFigure());
			Set<Class<? extends InfoEntity>> classes = getEntityClasses(targetEntitiesInScope);
			if (highlightedEntity != null && !classes.contains(highlightedEntity.getClass())) {
				// something completely wrong for this spell is selected by the user in the gui
				// we discard the selection and see whether auto target detection will work
				// or otherwise the user will be informed
				focusManager.setWorldFocusObject((RoomInfoEntity) null);
			}
			if (highlightedEntity != null) {
				// some target selected
				if (spell.getTargetClass().isAssignableFrom(highlightedEntity.getClass())) {
					// target has matching object class
					target = highlightedEntity;
				}
				else {

					//TODO: what should happen if something wrong is selected
					// and a unique target is available? Change target or wrong target feedback ?
					// should the gui remember the last selected entity for each entity class ? (Figure, Door, Item, etc)
					// NOTE: currently if a different entity class is selected, that selection is cleared, but if an object
					// of correct entity class is selected but not being in scope, we process and core will generated a message
					// for the user accordingly
				}
			}
			else {
				// no target selected
				if (targetEntitiesInScope.size() == 1) {
					RoomInfoEntity targetEntity = targetEntitiesInScope.get(0);
					focusManager.setWorldFocusObject(targetEntity);
					target = targetEntity;
				}
			}
		}
		else {
			// no target required
			target = null;
		}

		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		actionAssembler.plugActions(actionAssembler.getActionAssembler().wannaSpell(spell, target));
	}

	private Set<Class<? extends InfoEntity>> getEntityClasses(List<? extends InfoEntity> targetEntitiesInScope) {
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

	@Override
	public boolean isCurrentlyPossible() {
		actionAssembler.getActionAssembler().wannaSpell(spell, focusManager.getWorldFocusObject());

		ActionResult currentlyPossible = spell.isCurrentlyPossible(actionAssembler.getFigure(), focusManager.getWorldFocusObject());

		Boolean fightRunning = actionAssembler.getFigure().getRoomInfo().fightRunning();
		if(fightRunning != null && fightRunning) {
			return spell.isFight();
		}
		return spell.isNormal();
	}

	@Override
	public Object getObject() {
		return spell;
	}
}
