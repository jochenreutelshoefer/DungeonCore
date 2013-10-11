/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package shrine;

import java.awt.Color;

import dungeon.*;

import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.Hero;
import game.JDEnv;
import gui.Texts;

public class HealthFountain extends Shrine {

	
	Attribute health;

	int rate;
	//String story;
public void metaClick(Figure f){
		
	}
	
	public HealthFountain(int max, int rate, Room p) {
		
		super(p);
		health = new Attribute(Attribute.FOUNTAIN, max);
		type = 0;
		this.rate = rate;
		story = JDEnv.getResourceBundle().getString("see_health_fountain");;
		
		
	}
	public HealthFountain(int max, int rate) {
		
		super();
		health = new Attribute(Attribute.FOUNTAIN, max);
		type = 0;
		this.rate = rate;
		story = JDEnv.getResourceBundle().getString("see_health_fountain");
		
		
	}
	
	public boolean needsTarget() {
		return false;
	}
	
	public int getSecondIdentifier() {
		return type;
	}
	
	public String getText() {
		return toString()+" "+health.getValue()+" / "+health.getBasic();	
	}
	
	public String getStory() {
		return story;
	}
	public boolean usableOnce() {
		return false;
	}
	
	public boolean canBeUsedBy(Figure f) {
		   return true;
	   }
	public void turn(int round) {
		if((health.getBasic() - health.getValue()) > rate) {
			health.modValue(rate);
		}
		else if(health.getBasic() > health.getValue()) {
			health.setValue(health.getBasic());
		}
	}
	
	public Color getColor() {
		return java.awt.Color.blue;	
	}
	public int getShrineIndex() {
		return Shrine.SHRINE_HEALTH_FOUNTAIN;
	}
	
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_fountain_name");
	}
	
	public boolean use(Figure f,Object target,boolean meta) {
		//System.out.println("erfrischen am Brunnen!");
		Attribute h = f.getHealth();
		double act = h.getValue();
		double max = h.getBasic();
		int rest = (int) (max - act);
		if(health.getValue() >= rest) {
			health.modValue((-1)*rest);
			f.heal(rest);
		}
		else {
			f.heal((int)health.getValue());
			health.setValue(0);
		}
		return true;
	}
			
	
	
	public String getStatus() {
		return toString()+"\n"+health.toString();
	}

}
