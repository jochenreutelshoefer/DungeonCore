package de.jdungeon.user;

public interface WinLossCriterion {

	boolean isMet(DefaultDungeonSession session);

	String getMessage();
}
