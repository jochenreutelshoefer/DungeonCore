/*
 * Created on 07.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;

import figure.FigureInfo;
import figure.action.Action;
import figure.action.LearnSpellAction;
import figure.action.TakeItemAction;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.memory.Memory;
import figure.monster.MonsterInfo;
import figure.other.Fir;
import graphics.AbstractImageLoader;
import gui.init.StartView;
import gui.mainframe.MainFrame;
import gui.mainframe.component.BoardView;
import item.ItemInfo;

import java.applet.Applet;
import java.awt.Point;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;

import spell.SpellInfo;
import text.StatementManager;
import control.ActionAssembler;
import dungeon.JDPoint;
import dungeon.RoomInfo;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MyJDGui extends AbstractJDGUIEngine2D {

	public final static int BUTTON_SLAP = 1;

	public final static int BUTTON_GO_NORTH = 2;

	public final static int BUTTON_GO_EAST = 3;

	public final static int BUTTON_GO_SOUTH = 4;

	public final static int BUTTON_GO_WEST = 5;

	private final ActionAssembler control;

	private MainFrame frame;

	private boolean visibilityCheat = false;

	private Memory memory;


	private FigureInfo figure;

	/**
	 * @return Returns the figure.
	 */
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

	public boolean getVisibility() {
		return visibilityCheat;
	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		if (memory != null) {
			memory.storeRoom(figure.getRoomInfo(p), figure.getGameRound(),
					figure);
		}
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if (f.getFigureClass().equals(Fir.class)) {
			return false;
		}
		if (f instanceof MonsterInfo) {
			return true;
		}
		return false;
	}

	@Override
	public void gameRoundEnded() {
		newStatement("--------", 0);
		// this.repaintPicture();
		this.updateGui();
	}

	boolean justPainted = false;

	@Override
	public Action getAction() {
		if (memory == null) {
			memory = new Memory(figure.getDungeonSize());
		}
		if (actionQueue.size() > 0) {
			return actionQueue.remove(0);
		}
		return null;
	}

	@Override
	public void plugAction(Action a) {

		actionQueue.add(a);
	}


	public void setVisibility(boolean b) {
		visibilityCheat = b;
	}

	public MyJDGui(FigureInfo f) {
		this.figure = f;
		control = new ActionAssembler();
		control.setGui(this);
	}

	public MyJDGui() {
		control = new ActionAssembler();
		control.setGui(this);
	}

	@Override
	public MainFrame getMainFrame() {
		return frame;
	}



	public void initGui(AbstractStartWindow start, Applet applet,
			String playerName) {

		frame = new MainFrame((StartView) start,
				MainFrame.clearString(playerName), applet, this,
				"Java Dungeon V.22.08.06 - 3");
		frame.init();
		frame.initMainframe();
	}

	public Map<String, String> getHighScoreString(String playerName,
			String comment, boolean reg, boolean liga) {
		if (figure instanceof FigureInfo) {
			return ((HeroInfo) figure).getHighScoreString(playerName, comment,
					reg, liga);
		} else {
			return null;
		}
	}

	@Override
	public void stopAllAnimation() {
		perceptHandler.stopAllAnimtation();


	}

	@Override
	public void gameOver() {
		frame.gameOver();
		perceptHandler.stopAllAnimtation();
	}


	public int getChoosenEnemy() {
		return frame.getChoosenEnemy();
	}


	private int attackDelay = 0;

	private final Vector<Action> actionQueue = new Vector<Action>();

	public void fightRoundEnded() {
		attackDelay = 0;

	}

	int clearAniThreadHashCnt = 0;



	@Override
	public boolean currentAnimationThreadRunning(RoomInfo r) {
		return perceptHandler.currentAnimationThreadRunning(r);
	}

	/**
	 * @return Returns the control.
	 */
	@Override
	public ActionAssembler getActionAssembler() {
		return control;
	}

	@Override
	public void repaintPicture() {
		this.getMainFrame().repaint();
	}

	@Override
	public Point getViewportPosition() {
		return getMainFrame().getSpielfeld().getViewport().getViewPosition();
	}

	@Override
	public void updateGui() {

		getMainFrame().updateGUI2(MainFrame.UPDATE_ALL, false);

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
				JComboBox combo = this.getMainFrame().getStaub().getSorcCombo();
				combo.setSelectedItem(((LearnSpellAction) a).getSpell());
			}
			if (a instanceof TakeItemAction) {
				JComboBox combo = this.getMainFrame().getGesundheit()
						.getItemCombo();
				combo.setSelectedItem(((TakeItemAction) a).getItem());
			}
		}
	}

	@Override
	public void setUseWithTarget(boolean b) {
		getMainFrame().getSpielfeld().getSpielfeldBild().setUseWithTarget(b);

	}

	@Override
	public void setSpellMetaDown(boolean b) {
		getMainFrame().getSpielfeld().getSpielfeldBild().setSpellMetaDown(b);

	}

	@Override
	public SpellInfo getSelectedSpellInfo() {
		return (SpellInfo) getMainFrame().getStaub().getSorcCombo()
				.getSelectedItem();
	}

	@Override
	public int getSelectedItemIndex() {
		return getMainFrame().getGesundheit().getSelectedItemIndex();
	}

	@Override
	public ItemInfo getSelectedItem() {
		return (ItemInfo) getMainFrame().getGesundheit().getItemCombo()
				.getSelectedItem();
	}

	@Override
	public void setSelectedItemIndex(int i) {
		getMainFrame().getGesundheit().getItemCombo().setSelectedIndex(i);
	}


	@Override
	public BoardView getBoard() {
		return this.frame.getSpielfeld();
	}

	@Override
	public AbstractImageLoader getImageSource() {
		return MainFrame.imageSource;
	}

}