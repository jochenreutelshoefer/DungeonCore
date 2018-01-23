package spell;

import dungeon.Door;
import figure.Figure;

public class EscapeRouteInstance extends TimedSpellInstance {

	private final Door d;
	private final int duration;
	private final Figure mage;
	public EscapeRouteInstance(int time, Door d, Figure mage) {
		this.mage = mage;
		this.d = d;
		this.duration = time;
		d.setEscapeRoute(mage);
	}
	
	@Override
	public void stopEffect() {
		d.unsetEscapeRoute(mage);

	}

	@Override
	public int getDuration() {
		
		return duration;
	}

}
