package de.jdungeon.gui.activity;

import figure.FigureInfo;
import figure.action.result.ActionResult;
import gui.Paragraph;
import gui.Paragraphable;

import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public abstract class AbstractExecutableActivity<T> implements Activity<T> {

	protected final PlayerController playerController;

	public AbstractExecutableActivity(PlayerController playerController) {
		this.playerController = playerController;
	}

	@Override
	public boolean plugToController() {
		return playerController.plugActivity(this);
	}

	@Override
	public Paragraph[] getParagraphs() {
		if(getObject() instanceof Paragraphable) {
			return ((Paragraphable)getObject()).getParagraphs();
		} else {
			Paragraph[] p = new Paragraph[2];
			p[0] = new Paragraph(getObject().toString());
			p[1] = new Paragraph("Kosten: 0");
			return p;
		}
	}


	@Override
	public boolean isCurrentlyPossible() {
		return possible().getSituation() == ActionResult.Situation.possible;
	}
}
