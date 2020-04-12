package skill;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import figure.FigureInfo;

/**
 * A skill where an action does not need additional parameters to be executed,
 * hence only one generic parameter (actor) is required.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.04.20.
 */
public abstract class SimpleSkill extends Skill<SimpleSkill.SimpleSkillAction>{

	public SimpleSkill(int dustCosts) {
		super(dustCosts);
	}

	public SimpleSkill() {
		super(0);
	}

	@Override
	public SimpleActionBuilder newActionFor(FigureInfo actor) {
		return new SimpleActionBuilder(this, actor, SimpleSkillAction.class);
	}

	public static class SimpleSkillAction extends SkillAction {

		public SimpleSkillAction(SimpleSkill skill, FigureInfo info) {
			super(skill, info);
		}
	}

	@Override
	protected boolean checkPositionOk(SimpleSkillAction action) {
		// assumed position for SimpleSkills is arbitrary by default (overwrite otherwise)
		return true;
	}

	@Override
	protected boolean checkDistanceOk(SimpleSkillAction action) {
		// as we dont deal with a target here, the distance to target is okay.
		return true;
	}

	/**
	 * @author Jochen Reutelshoefer (denkbares GmbH)
	 * @created 11.04.20.
	 */
	public static class SimpleActionBuilder<SKILL extends SimpleSkill, ACTION extends SimpleSkillAction> extends ActionBuilder<SKILL, ACTION> {

		private final Class<ACTION> clazz;

		public SimpleActionBuilder(SKILL skill, FigureInfo actor, Class<ACTION> clazz) {
			super(skill, actor);
			this.clazz = clazz;
		}

		@Override
		public ACTION get() {
			try {
				Constructor constructor = clazz.getConstructor(new Class[]{SimpleSkill.class, FigureInfo.class});
				return (ACTION)constructor.newInstance(this.skill, actor);
			}
			catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (InstantiationException e) {
				e.printStackTrace();
			}
			catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
