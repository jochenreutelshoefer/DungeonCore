/*
 * Created on 05.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.spell;

import de.jdungeon.dungeon.util.InfoUnitUnwrapper;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.memory.MemoryObject;
import de.jdungeon.dungeon.InfoEntity;
import de.jdungeon.gui.Paragraph;

public class SpellInfo extends InfoEntity {
	
	private final Spell spell;
	public SpellInfo(Spell s, DungeonVisibilityMap map) {
		super(map);
		this.spell = s;
	}

	
	public Class<? extends InfoEntity> getTargetClass() {
		if(spell instanceof TargetSpell) {
			return ((TargetSpell) spell).getTargetClass();
		} else {
			return null;
		}
	}

	public TargetScope getTargetScope() {
		if(spell instanceof TargetSpell) {
			return ((TargetSpell) spell).getTargetScope();
		} else {
			return null;
		}
	}

	public int getType() {
		return spell.getType();
	}

	public Class<? extends Spell> getSpellClass(){
		return this.spell.getClass();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SpellInfo spellInfo = (SpellInfo) o;

		return !(spell != null ? !spell.equals(spellInfo.spell) : spellInfo.spell != null);

	}

	public ActionResult isCurrentlyPossible(FigureInfo actor, InfoEntity target) {
		InfoUnitUnwrapper unwrapper = new InfoUnitUnwrapper(map.getDungeon());
		Figure figure = (Figure) unwrapper.unwrappObject(actor);
		if(figure == null) return ActionResult.UNKNOWN;
		return spell.fire(figure, unwrapper.unwrappObject(target), false, -1);
	}

	@Override
	public int hashCode() {
		return spell != null ? spell.hashCode() : 0;
	}

	public boolean needsTarget() {
		return spell instanceof TargetSpell;
	}
	

	@Override
	public String toString()  {
		return spell.toString();
	}
	public int getLevel() {
		return spell.getLevel();
	}

	
	@Override
	public Paragraph[] getParagraphs() {
		return spell.getParagraphs();
	}
	
	public int getCost() {
		return spell.getCost();
	}
	
	public String getText() {
		return spell.getText();
	}
	
	public String getName(){
		return spell.getName();
	}

	@Override
	public MemoryObject getMemoryObject(FigureInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
