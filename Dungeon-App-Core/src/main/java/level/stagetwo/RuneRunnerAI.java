package level.stagetwo;

import ai.AbstractAI;
import ai.ActionAssembler;
import ai.AttitudeMonsterDefault;
import ai.ChaserAI;
import ai.DefaultMonsterIntelligence;
import ai.SurvivorBehaviour;
import dungeon.JDPoint;
import figure.FigureInfo;
import figure.action.Action;
import figure.percept.Percept;
import item.Item;
import item.ItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class RuneRunnerAI extends AbstractAI {

	private final SurvivorBehaviour survivor;
	private final FigureInfo figure;
	private final ItemInfo item;
	private final ChaserAI chaser;
	private final ActionAssembler actionAssembler;

	public RuneRunnerAI(FigureInfo figure, ItemInfo item) {
		super(new AttitudeMonsterDefault());
		this.figure = figure;
		this.item = item;
		survivor = new SurvivorBehaviour(figure);
		chaser = new ChaserAI();
		chaser.setFigure(figure);
		actionAssembler = new ActionAssembler(figure);
	}

	@Override
	protected void processPercept(Percept p) {
		survivor.tellPercept(p);
		chaser.tellPercept(p);
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	private boolean hasItem() {
		return figure.getAllItems().contains(item);
	}

	@Override
	public Action chooseFightAction() {
		if(hasItem()) {
			return survivor.chooseFightAction();
		} else {
			return chaser.chooseFightAction();
		}
	}

	@Override
	public Action chooseMovementAction() {
		if(hasItem()) {
			return survivor.chooseMovementAction();
		} else {
			return chaser.chooseMovementAction();
		}
	}
}
