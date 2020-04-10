package level.stagetwo;

import java.util.List;

import ai.DefaultMonsterIntelligence;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.StepAction;
import figure.monster.Orc;
import skill.AttackSkill;

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
						.newAction()
						.attacker(info)
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
