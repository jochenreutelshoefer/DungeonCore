package ai;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.percept.MovePercept;
import figure.percept.Percept;
import game.JDGUI;

public abstract class AI implements FightIntelligence, MovementIntelligence {

	/**
	 * @param args
	 */

	protected Attitude att;

	public abstract boolean isHostileTo(FigureInfo f);

	public abstract void setFigure(FigureInfo info);

	private JDGUI gui;
	protected boolean wait = false;
	protected FigureInfo info;

	public void plugSpectatorGui(JDGUI gui) {
		this.gui = gui;
	}

	public abstract void resetingRoomVisibility(JDPoint p);

	public final void tellPercept(Percept p) {
		if (att != null) {
			att.tellPercept(p);
		} else {
			System.out.println("att is null! ");
		}
		if (gui != null) {
			gui.tellPercept(p);
		}
		// if(this instanceof D3AI) {
		tellPerceptforKBupdate(p);
		// }
		processPercept(p);
	}

	protected abstract void processPercept(Percept p);

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public void tellPerceptforKBupdate(Percept p) {
		if (p instanceof MovePercept) {
			if (((MovePercept) p).getFigure().equals(this.info)) {
				figureMoved(((MovePercept) p));
			}
		}

	}

	private void figureMoved(MovePercept percept) {
		updateKBdueMovement(percept);

	}

	protected abstract void updateKBdueMovement(MovePercept p);

}
