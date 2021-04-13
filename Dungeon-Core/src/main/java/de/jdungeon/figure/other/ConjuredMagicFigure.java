package de.jdungeon.figure.other;

import de.jdungeon.ai.AI;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.percept.DisappearPercept;
import de.jdungeon.figure.percept.ItemDroppedPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.game.GameLoopMode;

public abstract class ConjuredMagicFigure extends Monster {
	
	private int numberOfRoundsToLive = -1;
	private int birthRound = -1;

	public ConjuredMagicFigure(int value, int numberOfRounds, AI ai) {
		super(value, ai);
		numberOfRoundsToLive = numberOfRounds;
	}

	public ConjuredMagicFigure(int value, AI ai) {
		this(value, Integer.MAX_VALUE, ai);
	}

	public ConjuredMagicFigure(int value, int numberOfRounds) {
		super(value);
		numberOfRoundsToLive = numberOfRounds;
	}


	public ConjuredMagicFigure(int value) {
		this(value, Integer.MAX_VALUE);
	}

	public abstract boolean disappearAtEndOfFight();

	@Override
	public void turn(int round, GameLoopMode mode) {
		super.turn(round, mode);

		// init birth round
		if(birthRound == -1) {
			// first call of turn
			this.birthRound = round;
		}

		// check number of rounds to live
		if(round - this.birthRound > numberOfRoundsToLive) {
			// time is up!
			disappear(round);
		}

	}

	public void disappear(int round) {
		pos.figureLeaves();

		// we drop any items on the floor that the de.jdungeon.figure might have
		if (!items.isEmpty()) {
			getRoom().addItems(items, null);
			getRoom().distributePercept(new ItemDroppedPercept(items, this, round));
		}

		getActualDungeon().removeFigureFromIndex(this);


		// distribute disappear percept
		Percept p = new DisappearPercept(this, this.getRoom(), round);
		this.getRoom().distributePercept(p);

	}

}
