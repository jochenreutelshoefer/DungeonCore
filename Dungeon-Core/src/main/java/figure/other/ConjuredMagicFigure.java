package figure.other;

import figure.Figure;
import figure.monster.Monster;
import figure.percept.DisappearPercept;
import figure.percept.ItemDroppedPercept;
import figure.percept.Percept;

public abstract class ConjuredMagicFigure extends Monster {
	
	private int numberOfRoundsToLive = -1;
	private int numberOfRoundsLived = 0;

	public ConjuredMagicFigure(int value, int numberOfRounds) {
		super(value);
		numberOfRoundsToLive = numberOfRounds;
	}
	
	public abstract boolean disappearAtEndOfFight();
	
	@Override
	public boolean fight() {
		numberOfRoundsLived++;
		if(numberOfRoundsLived == numberOfRoundsToLive) {
			this.disappear();
			return true;
		}
		super.fight();
		return false;
	}

	public void disappear() {
		Percept p = new DisappearPercept(this, this.getRoom());
		this.getRoom().distributePercept(p);
		pos.figureLeaves();
//		try {
//			Thread.sleep(600);
//		} catch (Exception e) {
//		}
		if (items.size() > 0) {
			getRoom().distributePercept(new ItemDroppedPercept(items, this));
			getRoom().addItems(items, null);
		}

		Figure.removeFigure(this);
		
	}

}