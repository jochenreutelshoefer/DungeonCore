package figure.other;

import ai.AI;
import figure.Figure;
import figure.monster.Monster;
import figure.percept.DisappearPercept;
import figure.percept.ItemDroppedPercept;
import figure.percept.Percept;

public abstract class ConjuredMagicFigure extends Monster {
	
	private int numberOfRoundsToLive = -1;
	private int numberOfRoundsLived = 0;

	public ConjuredMagicFigure(int value, int numberOfRounds, AI ai) {
		super(value, ai);
		numberOfRoundsToLive = numberOfRounds;
	}

	public ConjuredMagicFigure(int value, int numberOfRounds) {
		super(value);
		numberOfRoundsToLive = numberOfRounds;
	}
	
	public abstract boolean disappearAtEndOfFight();
	


	public void disappear(int round) {
		Percept p = new DisappearPercept(this, this.getRoom(), round);
		this.getRoom().distributePercept(p);
		pos.figureLeaves();
		if (!items.isEmpty()) {
			getRoom().distributePercept(new ItemDroppedPercept(items, this, round));
			getRoom().addItems(items, null);
		}

		Figure.removeFigure(this);
		
	}

}
