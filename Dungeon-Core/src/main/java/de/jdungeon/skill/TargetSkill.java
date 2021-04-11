package de.jdungeon.skill;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.spell.TargetScope;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public abstract class TargetSkill<TARGET> extends Skill<TargetSkill.TargetSkillAction<TARGET>> {

	@Override
	public TargetSkillActionBuilder newActionFor(FigureInfo actor) {
		return new TargetSkillActionBuilder(this, actor, TargetSkillAction.class);
	}

	public abstract TargetScope getTargetScope();

	public static class TargetSkillActionBuilder<SKILL extends TargetSkill, ACTION extends TargetSkillAction<TARGET>, TARGET> extends ActionBuilder<SKILL, ACTION> {

		private final Class<ACTION> clazz;
		private TARGET target;

		public TargetSkillActionBuilder(SKILL skill, FigureInfo actor, Class<ACTION> clazz) {
			super(skill, actor);
			this.clazz = clazz;
		}


		public TargetSkillActionBuilder target(TARGET target) {
			this.target = target;
			return this;
		}


		@Override
		public ACTION get() {
			if(target == null) {
				throw new IllegalStateException("no target defined in target action builder " +clazz.getSimpleName()+ " de.jdungeon.skill: " +  this.skill);
			}
			try {
				Constructor<?>[] constructors = clazz.getConstructors();
				Constructor constructor = clazz.getConstructor(new Class[]{TargetSkill.class, FigureInfo.class, Object.class});
				return (ACTION)constructor.newInstance(this.skill, actor, target);
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

	public static class TargetSkillAction<TARGET> extends SkillAction {

		private final TARGET target;

		public TargetSkillAction(TargetSkill skill, FigureInfo actor, TARGET target) {
			super(skill, actor);
			this.target = target;
		}

		public TARGET getTarget() {
			return target;
		}
	}


}
