/*
 * Created on 05.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.MemoryObject;
import game.InfoEntity;
import gui.Paragraph;

public class SpellInfo extends InfoEntity{
	
	private final Spell s;
	public SpellInfo(Spell s, DungeonVisibilityMap map) {
		super(map);
		this.s = s;
		
	}
	
	public Class<? extends InfoEntity> getTargetClass() {
		return s.getTargetClass();
	}

	public int getType() {
		return s.getType();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SpellInfo spellInfo = (SpellInfo) o;

		return !(s != null ? !s.equals(spellInfo.s) : spellInfo.s != null);

	}

	@Override
	public int hashCode() {
		return s != null ? s.hashCode() : 0;
	}

	public boolean needsTarget() {
		return s instanceof TargetSpell;
	}
	
	public int getLernCost() {
		return s.getLernCost();
	}
	@Override
	public String toString()  {
		return s.toString();
	}
	public int getLevel() {
		return s.getLevel();
	}
	
	public boolean isFight() {
		return s.isPossibleFight();
	}
	
	public boolean isNormal() {
		return s.isPossibleNormal();
		
	}
	
	@Override
	public Paragraph[] getParagraphs() {
		return s.getParagraphs();
	}
	
	public int getCost() {
		return s.getCost();
	}
	
	public int getDifficulty() {
		return s.getDifficulty();
	}
	
	public String getText() {
		return s.getText();
	}
	
	public String getName(){
		return s.getName();
	}

	@Override
	public MemoryObject getMemoryObject(FigureInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
