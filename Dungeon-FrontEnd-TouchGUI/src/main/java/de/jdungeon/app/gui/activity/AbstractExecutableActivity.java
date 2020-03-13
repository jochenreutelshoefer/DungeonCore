package de.jdungeon.app.gui.activity;

import figure.action.result.ActionResult;
import gui.Paragraph;
import gui.Paragraphable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public abstract class AbstractExecutableActivity implements ExecutableActivity {

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
	public ActionResult possible() {
		if(this.isCurrentlyPossible()) return ActionResult.POSSIBLE;
		return ActionResult.UNKNOWN;
	}
}
