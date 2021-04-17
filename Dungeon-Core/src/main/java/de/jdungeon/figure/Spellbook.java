/**
 * Zauberbuch des Helden. Enthaelt eine Liste von Zauberspruechen, die der Held
 * beherrscht. Durch das erlernen neuer Zaubersprueche koennen einzeln welche hinzukommen.
 */

package de.jdungeon.figure;
import java.io.Serializable;
import java.util.*;

import de.jdungeon.spell.*;
import de.jdungeon.util.Clonable;


public class Spellbook implements Serializable, Clonable<Spellbook> {

	
	List<Spell> spells = new ArrayList<Spell>();

	
	public Spellbook() {
	}
	
	public Spellbook(List<Spell> l) {
		spells.addAll(l);
	}
	
	public void addSpell(Spell s) {
		spells.add(s);
	}
	
	public Spell getSpell(int i) {
		if(i>= 0 && i < spells.size()) {
			return spells.get(i);
		}
		return null;
	}

	
	public List<Spell> getSpells() {
		return spells;
	}

	
	public Spell getSpell(String s) {
		for(int i = 0; i < spells.size(); i++) {
			String t = ((Spell)spells.get(i)).getName();
			if(t.equals(s)){
				return (Spell)spells.get(i);
			}
		}
		return null;
	}

	@Override
	public Spellbook copy() {
		Spellbook spellbookCopy = new Spellbook();
		List<Spell> spells = this.spells;
		for (Spell spell : spells) {
			spellbookCopy.addSpell(spell);
		}
		return spellbookCopy;
	}
}
