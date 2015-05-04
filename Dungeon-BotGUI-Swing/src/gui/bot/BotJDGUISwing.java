package gui.bot;

import figure.FigureInfo;
import figure.HeroControlWithSpectator;
import figure.action.Action;
import figure.action.LearnSpellAction;
import figure.action.TakeItemAction;
import figure.action.result.ActionResult;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.hero.HeroUtil;
import figure.hero.Profession;
import figure.hero.Zodiac;
import figure.percept.Percept;
import game.DungeonGame;
import game.JDEnv;
import graphics.AbstractImageLoader;
import gui.AbstractJDGUIEngine2D;
import gui.engine2D.AWTImageLoader;
import gui.engine2D.animation.MasterAnimation;
import gui.mainframe.MainFrame;
import gui.mainframe.component.BoardView;
import item.ItemInfo;

import java.awt.Image;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComboBox;

import spell.SpellInfo;
import text.Statement;
import text.StatementManager;
import ai.GuiAI;
import control.AbstractSwingMainFrame;
import control.ActionAssembler;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.generate.DungeonFactory;
import dungeon.util.DungeonManager;

public class BotJDGUISwing extends AbstractJDGUIEngine2D {

	private final BotGUIMainframe frame;
	private FigureInfo figure;
	private final Vector<Action> actionQueue = new Vector<Action>();
	protected final Map<RoomInfo, MasterAnimation> masterAnis = new HashMap<RoomInfo, MasterAnimation>();
	private final AbstractImageLoader<Image> imageLoader;
	private HeroControlWithSpectator control;
	private ResourceBundle res;
	private final DungeonFactory dungeonFactory;
	private boolean initialized = false;

	private Thread th;

	public BotJDGUISwing(DungeonFactory dungeonFactory) {
		this.dungeonFactory = dungeonFactory;
		imageLoader = new AWTImageLoader(null);
		if (JDEnv.isEnglish()) {
			res = ResourceBundle.getBundle("texts_bot", Locale.ENGLISH);
		} else {
			res = ResourceBundle.getBundle("texts_bot", Locale.GERMAN);

		}
		frame = new BotGUIMainframe(this,
				"Java Dungeon Bot GUI");
		frame.initMainframe();

	}

	public void startGame(Hero h) {

		DungeonGame dungeon = dungeonFactory.createDungeon();

		HeroInfo figureInfo = DungeonManager.enterDungeon(h,
				dungeon.getDungeon(), new JDPoint(18, 39));
		this.setFigure(figureInfo);


		GuiAI botInstance = getCurrentlySelectedAIInstance();
		HeroControlWithSpectator control = new HeroControlWithSpectator(
				figureInfo, botInstance, this);
		this.setControl(control);
		h.setControl(control);
		botInstance.setFigure(figureInfo);
		dungeon.putGuiFigure(h, this);
		// new StartView(h.getName(), 0, null, false)

		th = new Thread(dungeon);
		th.start();


		initialized = true;
	}

	private GuiAI getCurrentlySelectedAIInstance() {
		Class<? extends GuiAI> aiClass = frame.getSelectedBotAIClass();
		try {
			return aiClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void restartGame() {
		th.stop();
		

		// this.frame.dispose();
		frame.clearGUI();
		actionQueue.clear();
		masterAnis.clear();
		initialized = false;
		startGame(HeroUtil.getBasicHero(1, "BotStarterBot", Zodiac.Taurus,
				Profession.Nobleman));
		initialized = true;
	}

	@Override
	public FigureInfo getFigure() {
		return figure;
	}

	@Override
	public BoardView getBoard() {
		return this.frame.getSpielfeld();
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
				JComboBox combo = frame.getGesundheit().getItemCombo();
				combo.setSelectedItem(((TakeItemAction) a).getItem());
			}
		}

	}

	@Override
	public void tellPercept(Percept p) {
		if (!this.isInitialized())
			return;
		super.tellPercept(p);
		frame.updateGUI(MainFrame.UPDATE_ALL, false);
	}

	private boolean isInitialized() {
		return initialized;
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
		if (isInitialized()) {
		newStatement("--------", 0);
			this.updateGui();
		}

	}

	@Override
	public void newStatement(String s, int k) {
		if (frame != null) {
			frame.newStatement(s, k);
		}
	}

	@Override
	public void newStatement(Statement s) {
		if (s != null) {
			newStatement(s.getText(), s.getFormat());
		}
	}

	@Override
	public void newStatements(List<Statement> l) {
		for (Iterator<Statement> iter = l.iterator(); iter.hasNext();) {
			Statement element = iter.next();
			newStatement(element);

		}
	}

	@Override
	public void newStatement(String s, int code, int to) {
		frame.newStatement(s, code, to);
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
	public ActionAssembler getActionAssembler() {
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

	@Override
	public AbstractImageLoader<Image> getImageSource() {
		return imageLoader;
	}

	public void setControl(HeroControlWithSpectator control) {
		this.control = control;
	}

	public HeroControlWithSpectator getControl() {
		return control;
	}

	public ResourceBundle getResourceBundle() {
		return res;
	}

}
