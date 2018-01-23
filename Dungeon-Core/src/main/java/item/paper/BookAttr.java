package item.paper;
import dungeon.RoomEntity;
import item.interfaces.Usable;
import util.Arith;
import figure.Figure;
import figure.attribute.Attribute;
import game.JDEnv;
import gui.Texts;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BookAttr extends Book {
	
	int attribute;
	int level;
	public BookAttr(int attr, int level) {
		super(Arith.exp(10,level));
		this.level = level;
		attribute = attr;
	}
	
	
	@Override
	public boolean usableOnce() {
		return true;
	}
	
	@Override
	public boolean needsTarget() {
		return false;
	}
	
	
	
	@Override
	public String getText() {
		String s = new String();
		int t = level;
		if(t == 1) {
			s = book()+": Einsteigerwissen: ";
		}
		else if(t == 2) {
			s =  book()+": Basiswissen: ";
		}
		else if(t == 3) {
			s =  book()+": von A-Z: ";
		}
		else if(t == 4) {
			s =  book()+": Profiwissen: ";
		}
		else if(t == 5) {
			s =  book()+": Zum Guru in...: ";
		}
		s += attribute;
		return s;
	}
	
	public String toString() {
		return  book()+": "+Texts.getAttributeName(attribute)+"("+level+")";
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		Attribute a = f.getAttribute(attribute);
		if(a != null) { 
			return true;
		}
		return false;
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta) {
		Attribute a = f.getAttribute(attribute);
		double value = a.getBasic();
		int add = 0;
		if(level == 1) {
			if(value <= 2) {
				add = 8;
			}
			else if(value <= 9) {
				add = 4;
			}
			else if(value <= 15) {
				add = 2;
			}
			else if(value <= 19) {
				add = 1;
			}
			
		}	
		if(level == 2) {
			if(value <= 12) {
				add = 8;
			}
			else if(value <= 19) {
				add = 4;
			}
			else if(value <= 25) {
				add = 2;
			}
			else if(value <= 29) {
				add = 1;
			}
			
		}	
		a.modBasic(add);
		a.modValue(add);
	return true;	
		
	}

}
