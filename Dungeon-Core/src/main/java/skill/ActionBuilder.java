package skill;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class ActionBuilder<T extends SkillAction> {

	public abstract T get();
}
