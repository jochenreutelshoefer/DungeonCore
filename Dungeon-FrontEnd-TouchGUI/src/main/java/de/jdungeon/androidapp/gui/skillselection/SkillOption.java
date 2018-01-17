package de.jdungeon.androidapp.gui.skillselection;

import graphics.JDImageProxy;
import spell.Spell;

import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class SkillOption {

	private final Spell spell;

	public String getIcon() {
		return icon;
	}

	public Spell getSpell() {
		return spell;
	}

	private final String icon;

	public SkillOption(Spell spell, String icon) {
		this.spell = spell;
		this.icon = icon;
	}


}
