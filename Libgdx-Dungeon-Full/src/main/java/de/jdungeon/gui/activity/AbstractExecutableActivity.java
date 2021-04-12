package de.jdungeon.gui.activity;

import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.gui.Paragraphable;

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
	public ActionResult plugToController(Object target) {
		return playerController.plugActivity(this, target);
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
	public ActionResult isCurrentlyPossible(Object target) {
		return possible(target);
	}

	@Override
	public PlayerController getPlayerController() {
		return playerController;
	}
}
