package de.jdungeon.level.stageone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.ai.AbstractAI;
import de.jdungeon.ai.AttitudeMonsterDefault;
import de.jdungeon.ai.ChaserAI;
import de.jdungeon.ai.FleeBehaviour;
import de.jdungeon.ai.HeroPositionLog;
import de.jdungeon.ai.PatrolBehaviour;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.HealthLevel;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.percept.Percept;

import static de.jdungeon.level.stageone.HadrianAI.State.chase;
import static de.jdungeon.level.stageone.HadrianAI.State.flee;
import static de.jdungeon.level.stageone.HadrianAI.State.patrol;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 15.03.20.
 */
public class HadrianAI extends AbstractAI {

	enum State {
		patrol,
		chase,
		flee
	}

	private final PatrolBehaviour patrolAI;
	private final FleeBehaviour fleeAI;
	private final ChaserAI chaserAI = new ChaserAI();
	private HeroPositionLog heroLog;

	private State currentState = patrol;



	public HadrianAI() {
		super(new AttitudeMonsterDefault());
		Map<JDPoint, Double> patrolObjectives = new HashMap<>();
		patrolObjectives.put(new JDPoint(4,5), 1.0);
		patrolObjectives.put(new JDPoint(3,5), 1.0);
		patrolObjectives.put(new JDPoint(5,5), 1.0);
		patrolObjectives.put(new JDPoint(4,6), 0.5);
		patrolObjectives.put(new JDPoint(3,6), 0.5);
		patrolObjectives.put(new JDPoint(5,6), 0.5);
		patrolObjectives.put(new JDPoint(4,4), 2.0);
		patrolObjectives.put(new JDPoint(3,4), 0.5);
		patrolObjectives.put(new JDPoint(5,4), 0.5);
		patrolAI = new PatrolBehaviour(patrolObjectives);

		List<JDPoint> fleePoints = new ArrayList<>();
		fleePoints.add(new JDPoint(4, 4));
		fleeAI = new FleeBehaviour(fleePoints);



	}

	@Override
	public void setFigure(FigureInfo info) {
		super.setFigure(info);
		patrolAI.setFigure(info);
		chaserAI.setFigure(info);
		fleeAI.setFigure(info);
		heroLog = new HeroPositionLog(this.info);
	}

	@Override
	protected void processPercept(Percept p) {
		chaserAI.processPercept(p);
		heroLog.tellPecept(p);
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public Action chooseFightAction() {
		heroLog.processPercepts();


		/*
		Do we need a state transition because of health state
		 */
		if(this.info.getHealthLevel().getValue() < HealthLevel.Good.getValue()) {
			currentState = flee;
		}

		if(currentState == flee) {
			return fleeAI.chooseFightAction();
		}

		if(currentState == patrol) {
			// if we are in 'patrol' and enter a de.jdungeon.fight, we go directly in 'chase' mode
			currentState = chase;
		}

		if(currentState == chase) {
			return chaserAI.chooseFightAction();
		}

		return null;
	}

	@Override
	public Action chooseMovementAction() {
		heroLog.processPercepts();

		// at first stay alive
		if(this.info.getHealthLevel().getValue() < HealthLevel.Good.getValue()) {
			currentState = flee;
		}

		if(currentState == patrol && heroLog.getLastHeroPosition() != null && !heroLog.lastEnemyPositionVisited()) {
			// go from patrol mode to chase mode
			currentState = chase;
		}



		// if he is in 'chase' mode
		if(currentState == chase) {
			if(heroLog.lastEnemyPositionVisited()) {
				// -> if he lost track of enemy then go to patrol mode
				currentState = patrol;
			} else {
				return chaserAI.chooseMovementAction();
			}
		}

		// if he is in 'flee' mode
		if(currentState == flee) {
			if(this.info.getHealthLevel().getValue() >= HealthLevel.Good.getValue()) {
				// -> if he has recovered then go back to patrol mode
				currentState = patrol;
			} else {
				return fleeAI.chooseMovementAction();
			}
		}

		if(currentState == patrol) {
			return patrolAI.chooseMovementAction();
		}

		return null;
	}
}
