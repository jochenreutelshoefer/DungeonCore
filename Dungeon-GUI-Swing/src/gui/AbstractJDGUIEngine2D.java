package gui;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import text.Statement;
import control.AnimationPerceptHandler;
import figure.percept.Percept;
import gui.mainframe.component.BoardView;

public abstract class AbstractJDGUIEngine2D implements JDGUISwing {
	
	protected final AnimationPerceptHandler perceptHandler;

	public AbstractJDGUIEngine2D() {
		perceptHandler = new SwingGUIPerceptHandler(this);
	}


	@Override
	public void tellPercept(Percept p) {
		perceptHandler.tellPercept(p);
	}

	public Graphics getGraphics() {
		return getBoard().getSpielfeldBild().getGraphics();
	}

	public abstract BoardView getBoard();

	public void newStatement(String s, int k) {
		if (getMainFrame() != null) {
			getMainFrame().newStatement(s, k);
		}
	}

	public void newStatement(Statement s) {
		if (s != null) {
			newStatement(s.getText(), s.getFormat());
		}
	}

	public void newStatements(List<Statement> l) {
		for (Iterator<Statement> iter = l.iterator(); iter.hasNext();) {
			Statement element = iter.next();
			newStatement(element);

		}
	}

	public void newStatement(String s, int code, int to) {
		getMainFrame().newStatement(s, code, to);
	}

}
