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
	
	private Spell s;
	public SpellInfo(Spell s, DungeonVisibilityMap map) {
		super(map);
		this.s = s;
		
	}
	
	public int getType() {
		return s.getType();
	}
	
	public boolean equals(Object o) {
		if(o instanceof SpellInfo) {
			if(((SpellInfo)o).s.equals(this.s)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean needsTarget() {
		return s instanceof TargetSpell;
	}
	
	public int getLernCost() {
		return s.getLernCost();
	}
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

	public MemoryObject getMemoryObject(FigureInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
