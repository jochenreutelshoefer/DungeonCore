/*
 * Created on 05.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;

import dungeon.util.InfoUnitUnwrapper;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.memory.MemoryObject;
import game.InfoEntity;
import gui.Paragraph;
import item.ItemInfo;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SpellInfo spellInfo = (SpellInfo) o;

		return !(spell != null ? !spell.equals(spellInfo.spell) : spellInfo.spell != null);

	}

	public ActionResult isCurrentlyPossible(FigureInfo actor, InfoEntity target) {
		InfoUnitUnwrapper unwrapper = new InfoUnitUnwrapper(map.getDungeon());
		return spell.fire((Figure)unwrapper.unwrappObject(actor), unwrapper.unwrappObject(target), false);
	}

	@Override
	public int hashCode() {
		return spell != null ? spell.hashCode() : 0;
	}

	public boolean needsTarget() {
		return spell instanceof TargetSpell;
	}
	
	public int getLernCost() {
		return spell.getLernCost();
	}
	@Override
	public String toString()  {
		return spell.toString();
	}
	public int getLevel() {
		return spell.getLevel();
	}
	
	public boolean isFight() {
		return spell.isPossibleFight();
	}
	
	public boolean isNormal() {
		return spell.isPossibleNormal();
		
	}
	
	@Override
	public Paragraph[] getParagraphs() {
		return spell.getParagraphs();
	}
	
	public int getCost() {
		return spell.getCost();
	}
	
	public int getDifficulty() {
		return spell.getDifficulty();
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
