/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package shrine;

import util.JDColor;
import dungeon.Room;
import figure.Figure;
import figure.attribute.Attribute;
import game.JDEnv;

public class HealthFountain extends Shrine {

	
	private final Attribute health;

	private final int rate;

	public HealthFountain(int max, int rate) {
		
		super();
		health = new Attribute(Attribute.FOUNTAIN, max);
		this.rate = rate;
		story = JDEnv.getResourceBundle().getString("see_health_fountain");
		
		
	}
	
	@Override
	public boolean needsTarget() {
		return false;
	}
	
	@Override
	public int getSecondIdentifier() {
		// TODO: WTF?
		return -1;
	}
	
	@Override
	public String getText() {
		return toString()+" "+health.getValue()+" / "+health.getBasic();	
	}
	
	@Override
	public String getStory() {
		return story;
	}
	@Override
	public boolean usableOnce() {
		return false;
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return true;
	   }
	@Override
	public void turn(int round) {
		if((health.getBasic() - health.getValue()) > rate) {
			health.modValue(rate);
		}
		else if(health.getBasic() > health.getValue()) {
			health.setValue(health.getBasic());
		}
	}
	
	@Override
	public JDColor getColor() {
		return JDColor.blue;
	}
	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_HEALTH_FOUNTAIN;
	}
	
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_fountain_name");
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f,Object target,boolean meta) {
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
			
	
	
	@Override
	public String getStatus() {
		return toString()+"\n"+health.toString();
	}

}
