/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package item.paper;



import figure.Figure;
import figure.action.result.ActionResult;
import game.JDEnv;
import gui.Paragraph;
import item.Item;
import item.interfaces.Usable;
import item.interfaces.UsableWithTarget;
import spell.AbstractSpell;
import spell.TargetScope;
import spell.TargetSpell;
import util.JDColor;
public class Scroll extends Item implements UsableWithTarget {
	
	
	protected final AbstractSpell theSpell;

	public Scroll(AbstractSpell s, int cost) {
		super(s.getCost(),false);
		this.theSpell = s;
		s.setCost((int)(cost*0.5));
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		return theSpell.isAbleToCast(f);
	}
	
	@Override
	public boolean usableOnce() {
		return true;
	}
	
	@Override
	public boolean use(Figure f,Object target,boolean meta) {
		theSpell.setCostsAP(false);
		ActionResult res = theSpell.fire(f,target,true);
		if(res.getValue() == ActionResult.VALUE_DONE) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean needsTarget() {
		return theSpell instanceof TargetSpell;
	}

	@Override
	public TargetScope getTargetScope() {
		if(theSpell instanceof TargetSpell) {
			return ((TargetSpell)theSpell).getTargetScope();
		}
		return null;
	}


	private static String scroll = null;
	public static String scroll() {
		if(scroll == null) {
			scroll = JDEnv.getResourceBundle().getString("scroll");
		}
		return scroll;
	}
	
	@Override
	public String getText() {
		String s = new String();
		int t = theSpell.getLevel();
		if(t == 1) {
			s = scroll()+": (1)\n";
		}
		else if(t == 2) {
			s = scroll()+": (2)\n";
		}
		else if(t == 3) {
			s = scroll()+": (3)\n";
		}
		else if(t == 4) {
			s = scroll()+": (4)\n";
		}
		else if(t == 5) {
			s = scroll()+": (5)\n";
		}
		s += theSpell.getName();
		s += "\n"+JDEnv.getResourceBundle().getString("cost")+":"+theSpell.getCost();
		return s;
	}
	@Override
	public String toString() {
		String a = (scroll()+": "+theSpell.getName()+"("+theSpell.getLevel()+")("+theSpell.getCost()+")");
		return a;
	}
	
	@Override
	public Paragraph[] getParagraphs() {
		Paragraph []p = new Paragraph[5];
		p[0] = new Paragraph(scroll()+": "+theSpell.getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.blue);
		p[0].setBold();
		
		p[1] = new Paragraph(JDEnv.getResourceBundle().getString("spell")+JDEnv.getResourceBundle().getString("level")+": "+theSpell.getLevel());
		p[1].setSize(16);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();
		
		p[2] = new Paragraph((JDEnv.getResourceBundle().getString("cost")+": "+theSpell.getCost()));
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);
		
		p[3] = new Paragraph((JDEnv.getResourceBundle().getString("spell_min_wisdom")+": "+theSpell.getDifficultyMin()));
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(JDColor.black);
		
		p[4] = new Paragraph((JDEnv.getResourceBundle().getString("spell_difficulty")+": "+theSpell.getDifficulty()));
		p[4].setSize(14);
		p[4].setCentered();
		p[4].setColor(JDColor.black);
		
		return p;
	}
		
	

}
