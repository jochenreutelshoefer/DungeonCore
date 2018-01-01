package ai;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.percept.Percept;
import game.JDGUI;

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

	private JDGUI gui;
	protected boolean wait = false;
	protected FigureInfo info;

	public void plugSpectatorGui(JDGUI gui) {
		this.gui = gui;
	}

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

	public void setWait(boolean wait) {
		this.wait = wait;
	}

}
