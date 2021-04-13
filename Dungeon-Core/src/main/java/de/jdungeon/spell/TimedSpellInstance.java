package de.jdungeon.spell;

import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.Turnable;

public abstract class TimedSpellInstance implements Turnable{
	
public abstract void stopEffect();

	private int timer;
	
	public void expire() {
		stopEffect();
		AbstractSpell.removeTimedSpell(this);
	}
	
	@Override
	public void turn(int k, GameLoopMode mode) {
		timer++;
		if(timer > getDuration()) {
			expire();
		}
		
	}
	
	public abstract int getDuration();

}
