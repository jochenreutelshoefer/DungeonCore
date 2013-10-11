package item.paper;
import game.JDEnv;
import spell.Spell;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ScrollMagic extends Scroll {
	
	public ScrollMagic(Spell s) {
		super(s, 0);
	}
	
	
	
	public String getText() {
		String s = new String();
		int t = theSpell.getLevel();
		if(t == 1) {
			s = Spell.spell()+": (1)\n";
		}
		else if(t == 2) {
			s = Spell.spell()+": (2)\n";
		}
		else if(t == 3) {
			s = Spell.spell()+": (3)\n";
		}
		else if(t == 4) {
			s = Spell.spell()+": (4)\n";
		}
		else if(t == 5) {
			s = Spell.spell()+": (5)\n";
		}
		s += theSpell.getName();
		
		return s;
	}
	public String toString() {
		String a = (Scroll.scroll()+": "+theSpell.getName()+"("+theSpell.getLevel()+")");
		return a;
	}
}
