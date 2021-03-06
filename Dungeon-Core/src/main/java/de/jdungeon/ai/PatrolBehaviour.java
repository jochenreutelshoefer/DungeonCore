package de.jdungeon.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Path;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.DungeonUtils;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.log.Log;
import de.jdungeon.skill.attack.AttackSkill;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 15.03.20.
 */
public class PatrolBehaviour extends DefaultMonsterIntelligence {

	private final Map<JDPoint, Double> patrolObjectives;

	private JDPoint currentTarget = null;

	public PatrolBehaviour(Map<JDPoint, Double> patrolObjectives) {
		super();
		this.patrolObjectives = normalize(patrolObjectives);
	}

	/*
	Normalize to sum up to 1
	 */
	private Map<JDPoint, Double> normalize(Map<JDPoint, Double> patrolObjectives) {
		double sum = 0;
		for (Double value : patrolObjectives.values()) {
			sum += value;
		}
		Map<JDPoint, Double> normalizedPatrolObjectives = new HashMap<>();

		for (JDPoint point : patrolObjectives.keySet()) {
			normalizedPatrolObjectives.put(point, patrolObjectives.get(point) / sum);
		}
		return normalizedPatrolObjectives;
	}

	@Override
	protected void processPercept(Percept p) {

	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public Action chooseFightAction() {
		return this.info.getSkill(AttackSkill.class)
				.newActionFor(info)
				.target(getHero())
				.get();
	}

	@Override
	public Action chooseMovementAction() {

		// take a break
		if (Math.random() < 0.4) {
			return EndRoundAction.instance;
		}

		if (currentTarget == null) {
			selectNewTarget();
		}

		if (this.monster.getRoomNumber().equals(currentTarget)) {
			// we have reached the current target
			// take a break
			if (Math.random() < 0.5) {
				return EndRoundAction.instance;
			}
			else {
				selectNewTarget();
				Action action = moveToCurrentTarget();
				if (action != null) return action;
			}
		}
		else {
			Action action = moveToCurrentTarget();
			if (action != null) return action;
		}

		return null;
	}

	private void selectNewTarget() {
		double dice = Math.random();
		double sum = 0;

		for (JDPoint jdPoint : patrolObjectives.keySet()) {
			sum += patrolObjectives.get(jdPoint);
			if (dice < sum) {
				// we have a new target
				currentTarget = jdPoint;
				if(currentTarget == this.monster.getRoomNumber()) {
					// we need a different goal
					selectNewTarget();
				}
				return;
			}
		}
		Log.severe("This should not happen as probabilities are always lower equal 1");
	}

	private Action moveToCurrentTarget() {
		Path shortestPath = DungeonUtils.findShortestPath(this.monster, this.monster.getRoomInfo()
				.getNumber(), currentTarget, false);
		if (shortestPath != null && shortestPath.size() > 1) {
			RoomInfo nextRoom = shortestPath.get(1);
			return walk(this.monster.getRoomInfo().getDirectionTo(nextRoom));
		}
		Log.severe("Cannot find path to target!");
		return null;
	}
}
