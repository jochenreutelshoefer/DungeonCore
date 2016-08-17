package ai;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.percept.Percept;
import game.JDGUI;

public abstract class GuiAI implements AI {

	/**
	 * @param args
	 */

	protected Attitude att;

	public GuiAI(Attitude att) {
		this.att = att;
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return att.isHostile(f);
	}


	public void setFigure(FigureInfo info) {
		this.info = info;
	}

	private JDGUI gui;
	protected boolean wait = false;
	protected FigureInfo info;

	public void plugSpectatorGui(JDGUI gui) {
		this.gui = gui;
	}


	public void notifyVisibilityStatusIncrease(JDPoint p) {

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
