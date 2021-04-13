package de.jdungeon.level.stagetwo;

import java.util.List;

import de.jdungeon.ai.DefaultMonsterIntelligence;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.monster.Orc;
import de.jdungeon.skill.attack.AttackSkill;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.18.
 */
public class OrcHaterAI extends DefaultMonsterIntelligence {

	@Override
	public boolean isHostileTo(FigureInfo figureInfo) {
		return figureInfo.getFigureClass().equals(Orc.class);
	}

	@Override
	public Action chooseFightAction() {
		List<FigureInfo> figureInfos = this.info.getRoomInfo().getFigureInfos();
		for (FigureInfo figureInfo : figureInfos) {
			if (figureInfo.getFigureClass().equals(Orc.class)) {
				if (Math.random() * 2 > 1) {
					StepAction step = stepToEnemy();
					if (step != null) {
						return step;
					}
				}
				return this.info.getSkill(AttackSkill.class)
						.newActionFor(info)
						.target(figureInfo)
						.get();
			}
		}
		return new EndRoundAction();
	}

	@Override
	public Action chooseMovementAction() {
		return new EndRoundAction();
	}
}
