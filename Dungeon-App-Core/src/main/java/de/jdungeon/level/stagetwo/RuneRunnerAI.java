package de.jdungeon.level.stagetwo;

import de.jdungeon.ai.AbstractAI;
import de.jdungeon.ai.ActionAssemblerHelper;
import de.jdungeon.ai.AttitudeMonsterDefault;
import de.jdungeon.ai.ChaserAI;
import de.jdungeon.ai.SurvivorBehaviour;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.item.ItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class RuneRunnerAI extends AbstractAI {

	private final SurvivorBehaviour survivor;
	private final FigureInfo figure;
	private final ItemInfo item;
	private final ChaserAI chaser;
	private final ActionAssemblerHelper actionAssembler;

	public RuneRunnerAI(FigureInfo figure, ItemInfo item) {
		super(new AttitudeMonsterDefault());
		this.figure = figure;
		this.item = item;
		survivor = new SurvivorBehaviour(figure);
		chaser = new ChaserAI();
		chaser.setFigure(figure);
		actionAssembler = new ActionAssemblerHelper(figure);
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
