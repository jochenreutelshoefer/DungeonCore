package de.jdungeon.skill;

//import com.sun.istack.internal.NotNull;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class SkillAction extends Action {

	protected final Skill skill;
	protected final FigureInfo actor;

	public SkillAction(Skill skill, FigureInfo actor) {
		this.skill = skill;
		this.actor = actor;
	}

	public FigureInfo getActorInfo() {
		return actor;
	}

	public Figure getActor() {
		return actor.getVisMap().getDungeon().getFigureIndex().get(actor.getFigureID());
	}

	public Skill getSkill() {
		return skill;
	}
}
