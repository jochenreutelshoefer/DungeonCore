package de.jdungeon.user;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.03.16.
 */
public interface Session {

	/**
	 * Returns the de.jdungeon.user object of this session
	 *
	 * @return the de.jdungeon.user of this session
	 */
	User getUser();

}
