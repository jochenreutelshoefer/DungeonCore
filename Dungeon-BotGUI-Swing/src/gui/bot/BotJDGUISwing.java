package gui.bot;

import figure.FigureInfo;
import figure.action.Action;
import figure.action.LearnSpellAction;
import figure.action.TakeItemAction;
import figure.action.result.ActionResult;
import figure.percept.Percept;
import gui.JDGUISwing;
import gui.engine2D.animation.MasterAnimation;
import gui.mainframe.MainFrame;
import item.ItemInfo;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;

import spell.SpellInfo;
import text.Statement;
import text.StatementManager;
import control.AbstractSwingMainFrame;
import control.ActionAssembler;
import dungeon.JDPoint;
import dungeon.RoomInfo;

public class BotJDGUISwing implements JDGUISwing {

	private BotGUIMainframe frame;
	private FigureInfo figure;
	private final Vector<Action> actionQueue = new Vector<Action>();
	protected final Map<RoomInfo, MasterAnimation> masterAnis = new HashMap<RoomInfo, MasterAnimation>();


	@Override
	public FigureInfo getFigure() {
		return figure;
	}

	/**
	 * @param figure
	 *            The figure to set.
	 */
	@Override
	public void setFigure(FigureInfo figure) {
		this.figure = figure;

	}

	@Override
	public void onTurn() {
		updateGui();
		repaintPicture();
	}

	@Override
	public void actionDone(Action a, ActionResult res) {
		if (res.getKey1() == ActionResult.KEY_IMPOSSIBLE) {
			newStatement(StatementManager.getStatement(res));
		} else {

			updateGui();
			if (a instanceof LearnSpellAction) {
				JComboBox combo = frame.getStaub().getSorcCombo();
				combo.setSelectedItem(((LearnSpellAction) a).getSpell());
			}
			if (a instanceof TakeItemAction) {
				JComboBox combo = frame.getGesundheit()
						.getItemCombo();
				combo.setSelectedItem(((TakeItemAction) a).getItem());
			}
		}

	}

	@Override
	public void tellPercept(Percept p) {
		frame.updateGUI(MainFrame.UPDATE_ALL, false);
	}

	@Override
	public void gameOver() {
		Collection<RoomInfo> s = masterAnis.keySet();
		for (Iterator<RoomInfo> iter = s.iterator(); iter.hasNext();) {
			RoomInfo element = iter.next();
			MasterAnimation ani = masterAnis.get(element);
			ani.myStop();
		}

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return true;
	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Action getAction() {
		if (actionQueue.size() > 0) {
			return actionQueue.remove(0);
		}
		return null;
	}

	@Override
	public void plugAction(Action a) {
		actionQueue.add(a);
	}

	@Override
	public void gameRoundEnded() {
		newStatement("--------", 0);
		this.updateGui();

	}

	public void newStatement(String s, int k) {
		if (frame != null) {
			frame.newStatement(s, k);
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
		frame.newStatement(s, code, to);
	}

	public void initGui(String name) {
		frame = new BotGUIMainframe(
				MainFrame.clearString(name), this,
				"Java Dungeon Bot GUI");
		frame.initMainframe();

	}


	@Override
	public boolean currentAnimationThreadRunning(RoomInfo r) {
		MasterAnimation ani = masterAnis.get(r);

		if (ani != null) {

			return !ani.finished;
		}
		return false;
	}

	@Override
	public int getSelectedItemIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSelectedItemIndex(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemInfo getSelectedItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpellInfo getSelectedSpellInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSpellMetaDown(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUseWithTarget(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopAllAnimation() {
		Collection<RoomInfo> c = masterAnis.keySet();
		for (Iterator<RoomInfo> iter = c.iterator(); iter.hasNext();) {
			RoomInfo element = iter.next();
			MasterAnimation ani = masterAnis.get(element);
			ani.myStop();
		}
	}

	@Override
	public ActionAssembler getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSwingMainFrame getMainFrame() {
		return frame;
	}

	@Override
	public void updateGui() {
		frame.updateGUI(MainFrame.UPDATE_ALL, false);

	}

	@Override
	public void repaintPicture() {
		this.getMainFrame().repaint();
	}

	@Override
	public Point getViewportPosition() {
		return frame.getSpielfeld().getViewport().getViewPosition();
	}

}