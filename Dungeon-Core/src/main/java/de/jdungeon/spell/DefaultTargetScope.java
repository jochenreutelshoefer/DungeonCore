package de.jdungeon.spell;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.04.20.
 */
public class DefaultTargetScope<TARGET> extends AbstractTargetScope<TARGET> {

	private final Class<TARGET> targetClass;

	public DefaultTargetScope(Class<TARGET> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public Class<TARGET> getTargetClass() {
		return targetClass;
	}


}
