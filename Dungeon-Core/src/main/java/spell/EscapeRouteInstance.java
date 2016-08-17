package spell;

import dungeon.Door;
import figure.Figure;

public class EscapeRouteInstance extends TimedSpellInstance {

	private Door d;
	private int duration;
	private Figure mage;
	public EscapeRouteInstance(int time, Door d, Figure mage) {
		this.mage = mage;
		this.d = d;
		this.duration = time;
		d.setEscapeRoute(mage);
	}
	
	public void stopEffect() {
		d.unsetEscapeRoute(mage);

	}

	public int getDuration() {
		
		return duration;
	}

}
