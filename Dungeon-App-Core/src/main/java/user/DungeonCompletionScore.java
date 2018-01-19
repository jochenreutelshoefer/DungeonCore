package user;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 19.01.18.
 */
public class DungeonCompletionScore {

	private final int rounds;

	public int getScore() {
		return score;
	}

	public int getRounds() {
		return rounds;
	}

	private final int score;

	public DungeonCompletionScore(int rounds, int score) {
		this.rounds = rounds;
		this.score = score;
	}
}
