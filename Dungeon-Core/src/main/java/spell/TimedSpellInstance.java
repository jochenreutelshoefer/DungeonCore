package spell;

import game.Turnable;

public abstract class TimedSpellInstance implements Turnable{
	
public abstract void stopEffect();

	int timer;
	
	public void expire() {
		stopEffect();
		AbstractSpell.removeTimedSpell(this);
	}
	
	public void turn(int k) {
		timer++;
		if(timer > getDuration()) {
			expire();
		}
		
	}
	
	public abstract int getDuration();

}
