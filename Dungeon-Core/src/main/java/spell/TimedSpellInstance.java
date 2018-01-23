package spell;

import game.Turnable;

public abstract class TimedSpellInstance implements Turnable{
	
public abstract void stopEffect();

	private int timer;
	
	public void expire() {
		stopEffect();
		AbstractSpell.removeTimedSpell(this);
	}
	
	@Override
	public void turn(int k) {
		timer++;
		if(timer > getDuration()) {
			expire();
		}
		
	}
	
	public abstract int getDuration();

}
