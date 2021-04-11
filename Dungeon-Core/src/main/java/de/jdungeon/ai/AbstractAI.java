package de.jdungeon.ai;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.game.JDGUI;

public abstract class AbstractAI implements AI {

	protected Attitude att;

	public AbstractAI(Attitude att) {
		this.att = att;
	}

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {
		// most simple AIs do not use this information
		// override this method if required
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return att.isHostile(f);
	}

	@Override
	public void setFigure(FigureInfo info) {
		this.info = info;
	}

	protected FigureInfo info;

	private JDGUI gui;
	protected boolean wait = false;

	@Override
	public final void tellPercept(Percept p) {
		if (att != null) {
			att.tellPercept(p);
		}

		if (gui != null) {
			gui.tellPercept(p);
		}
		processPercept(p);
	}

	protected abstract void processPercept(Percept p);

}
