package item.paper;
import item.Item;
import item.interfaces.Usable;

import java.util.*;

import spell.Spell;

import figure.Figure;
import figure.Spellbook;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BookSpell extends Book implements Usable{

	/**
	 * @see Item#getText()
	 * 
	 */
	Spell theSpell;

	int dust = 2;
	public BookSpell(Spell s, int k) {
		super(s.getWorth());
		theSpell = s;
		dust = k;
	}
	
	public BookSpell(Spell s) {
		super(s.getWorth());
		theSpell = s;
		
	}
	
	public boolean needsTarget() {
		return false;
	}
	
	public boolean usableOnce() {
		return true;
	}
	
	public String getText() {
		String s = new String();
		int t = theSpell.getLevel();
		if(t == 1) {
			s = book()+": Einsteigerwissen: ";
		}
		else if(t == 2) {
			s = book()+": Basiswissen: ";
		}
		else if(t == 3) {
			s = book()+": von A-Z: ";
		}
		else if(t == 4) {
			s = book()+": Profiwissen: ";
		}
		else if(t == 5) {
			s = book()+": Zum Guru in...: ";
		}
		s += theSpell.getName();
		return s;
	}
	
	public String toString() {
		return book()+": "+theSpell.getName()+"("+theSpell.getLevel()+")";
	}
	
	public boolean canBeUsedBy(Figure f) {
		Spellbook b = f.getSpellbook();
		if(b != null) {
			return true;
		}
		return false;
	}
	
	public boolean use(Figure f,Object target,boolean meta) {
		List<Spell> theBook = f.getSpellbook().getSpells();
		String name = theSpell.getName();
		for(int i = 0; i < theBook.size(); i++) {
			String a = ((Spell)theBook.get(i)).getName();
			if(name.equals(a)) {
				int level = theSpell.getLevel();
				int l = ((Spell)theBook.get(i)).getLevel();
				if(level > l) {
					theBook.remove(i);
					
					
				}
				break;
			}
		}
		theBook.add(theSpell);
		f.recDust(dust);
		f.removeItem(this);
	return true;
	}

}
