/**
 * Zauberbuch des Helden. Enthaelt eine Liste von Zauberspruechen, die der Held
 * beherrscht. Durch das erlernen neuer Zaubersprueche koennen einzeln welche hinzukommen.
 */

package figure;
import java.io.Serializable;
import java.util.*;

import spell.*;



public class Spellbook implements Serializable {

	
	List<AbstractSpell> spells = new ArrayList<AbstractSpell>();

	
	public Spellbook() {
	}
	
	public Spellbook(List<AbstractSpell> l) {
		spells.addAll(l);
	}
	
	public void addSpell(AbstractSpell s) {
		spells.add(s);
	}
	
	public AbstractSpell getSpell(int i) {
		if(i>= 0 && i < spells.size()) {
			return spells.get(i);
		}
		return null;
	}

	
	public List<AbstractSpell> getSpells() {
		return spells;
	}

	
	public AbstractSpell getSpell(String s) {
		for(int i = 0; i < spells.size(); i++) {
			String t = ((AbstractSpell)spells.get(i)).getName();
			if(t.equals(s)){
				return (AbstractSpell)spells.get(i);
			}
		}
		return null;
	}

}
