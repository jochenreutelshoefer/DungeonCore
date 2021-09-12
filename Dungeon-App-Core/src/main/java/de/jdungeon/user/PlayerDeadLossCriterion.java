package de.jdungeon.user;

public class PlayerDeadLossCriterion extends LossCriterion {
	@Override
	public boolean isMet(DefaultDungeonSession session) {
		return session.getCurrentHero().isDead();
	}

	@Override
	public String getMessage() {
		return "Du bist tot!";
	}
}
