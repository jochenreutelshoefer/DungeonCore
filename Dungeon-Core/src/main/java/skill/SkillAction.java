package skill;

//import com.sun.istack.internal.NotNull;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;

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
		return actor.getMap().getDungeon().getFigureIndex().get(actor.getFigureID());
	}

	public Skill getSkill() {
		return skill;
	}
}
