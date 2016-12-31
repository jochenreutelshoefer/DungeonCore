package de.jdungeon.user;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.03.16.
 */
public interface Session {

	/**
	 * Returns the user object of this session
	 *
	 * @return the user of this session
	 */
	User getUser();

}
