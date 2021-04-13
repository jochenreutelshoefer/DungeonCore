package de.jdungeon.ai;

import java.util.List;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Path;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.DungeonUtils;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.skill.attack.AttackSkill;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 15.03.20.
 */
public class FleeBehaviour extends DefaultMonsterIntelligence {

	private final List<JDPoint> fleeTargets;

	public FleeBehaviour(List<JDPoint> fleeTargets) {
		this.fleeTargets = fleeTargets;
	}

	@Override
	public Action chooseFightAction() {
		Action fleeAction = getFleeAction(this.monster);
		if (fleeAction != null) {
			return fleeAction;
		}

		List<FigureInfo> figureInfos = this.monster.getRoomInfo().getFigureInfos();
		FigureInfo enemy = selectEnemy(figureInfos);
		if (enemy != null) {
			Action stepAwayAction = getStepAwayAction(this.monster, enemy);
			if (stepAwayAction != null) {
				return stepAwayAction;
			}
		}

		// can not flee, hence attack
		return this.info.getSkill(AttackSkill.class)
				.newActionFor(info)
				.target(getHero())
				.get();
	}

	private FigureInfo selectEnemy(List<FigureInfo> figureInfos) {
		for (FigureInfo figureInfo : figureInfos) {
			if (figureInfo.isHostile(this.monster)) {
				return figureInfo;
			}
		}
		return null;
	}

	@Override
	public Action chooseMovementAction() {

		// TODO : need to do something to prevent fleeing into the enemy (-> avoid last known enemy position?)
		for (JDPoint fleeTarget : fleeTargets) {
			if (fleeTarget.equals(this.monster.getRoomNumber())) return EndRoundAction.instance; // we stay and wait

			Path shortestPath = DungeonUtils.findShortestPath(this.monster, this.monster.getRoomInfo()
					.getNumber(), fleeTarget, false);
			if (shortestPath != null) {
				RoomInfo nextRoom = shortestPath.get(1);
				return walk(this.monster.getRoomInfo().getDirectionTo(nextRoom));
			}
		}

		return super.chooseMovementAction();
	}
}
