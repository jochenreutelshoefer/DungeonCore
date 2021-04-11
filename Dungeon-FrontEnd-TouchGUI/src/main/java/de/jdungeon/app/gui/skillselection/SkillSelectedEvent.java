package de.jdungeon.app.gui.skillselection;

import de.jdungeon.event.Event;
import de.jdungeon.spell.Spell;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class SkillSelectedEvent extends Event {

	public Spell getSpell() {
		return spell;
	}

	private final Spell spell;

	public SkillSelectedEvent(Spell spell) {
		this.spell = spell;
	}
}
