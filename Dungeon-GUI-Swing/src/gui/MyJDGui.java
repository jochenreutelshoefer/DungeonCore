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
import figure.percept.AttackPercept;
import figure.percept.BreakSpellPercept;
import figure.percept.DiePercept;
import figure.percept.DisappearPercept;
import figure.percept.DoorSmashPercept;
import figure.percept.FightBeginsPercept;
import figure.percept.FightEndedPercept;
import figure.percept.FleePercept;
import figure.percept.HitPercept;
import figure.percept.InfoPercept;
import figure.percept.ItemDroppedPercept;
import figure.percept.MissPercept;
import figure.percept.MovePercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;
import figure.percept.ShieldBlockPercept;
import figure.percept.SpellPercept;
import figure.percept.StepPercept;
import figure.percept.TakePercept;
import figure.percept.TextPercept;
import figure.percept.TumblingPercept;
import figure.percept.UsePercept;
import figure.percept.WaitPercept;
import gui.engine2D.GraphBoard;
import gui.engine2D.animation.Animation;
import gui.engine2D.animation.AnimationReal;
import gui.engine2D.animation.AnimationTask;
import gui.engine2D.animation.MasterAnimation;
import gui.mainframe.MainFrame;
import gui.mainframe.component.BoardView;
import item.ItemInfo;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;

import spell.Spell;
import spell.SpellInfo;
import text.Statement;
import text.StatementManager;
import animation.AnimationSet;
import animation.AnimationUtils;
import audio.AudioEffectsManager;
import control.ActionAssembler;
import dungeon.JDPoint;
import dungeon.RoomInfo;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MyJDGui implements JDGUISwing {

	public final static int BUTTON_SLAP = 1;

	public final static int BUTTON_GO_NORTH = 2;

	public final static int BUTTON_GO_EAST = 3;

	public final static int BUTTON_GO_SOUTH = 4;

	public final static int BUTTON_GO_WEST = 5;

	private final ActionAssembler control;

	private MainFrame frame;

	private boolean visibilityCheat = false;

	private Memory memory;

	private final Map<RoomInfo, MasterAnimation> masterAnis = new HashMap<RoomInfo, MasterAnimation>();

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

	private void handleAttackPercept(AttackPercept p) {

		figureSlaysAnimation(p);
	}

	private void handleHitPercept(HitPercept p) {
		FigureInfo victim = p.getVictim();
		newStatements(StatementManager.getStatements(p, figure));
		if (p.getDamage() > 0) {
			figureBeenHitAnimation(victim);
		}
		this.getMainFrame().getGesundheit().updateView();

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if(f.getFigureClass().equals(Fir.class)) {
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

	private void handleDiePercept(DiePercept p) {
		newStatement(StatementManager.getStatement(p, figure));
		repaintPicture();
		figureDiesAnimation(p.getFigure());
	}

	private void handleMovePercept(MovePercept p) {
		MasterAnimation ani = this.masterAnis.get(this.figure
				.getRoomInfo());
		if (ani != null) {
			ani.resetQueue();
		}
		if (p.getFigure().getRoomInfo().equals(figure.getRoomInfo())
				&& !p.getFigure().equals(figure)) {
			newStatement(StatementManager.getStatement(p, figure));
			
			AudioEffectsManager.playSound(AudioEffectsManager.DOOR_CLOSE);
		}

		figureWalkingAnimation(p.getFigure());
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


	private void handleScoutPercept(ScoutPercept p) {
		figureUsingAnimation(p.getFigure());
	}

	private void handleSpellPercept(SpellPercept p) {

		figureSorceringAnimation(p.getFigure());

		newStatement(StatementManager.getStatement(p, figure));
		if (p.getSpell().getType() == Spell.SPELL_FIREBALL) {
			AudioEffectsManager.playSound(AudioEffectsManager.MAGIC_FIREBALL);
		} else {
			AudioEffectsManager.playSound(AudioEffectsManager.MAGIC_SOUND);
			
		}
	}

	private void handleBreakSpellPercept(BreakSpellPercept p) {

		newStatement(StatementManager.getStatement(p, figure));

	}

	private void handleStepPercept(StepPercept p) {
		MasterAnimation ani = this.masterAnis.get(this.figure
				.getRoomInfo());
		repaintPicture();
		if (ani != null) {
			ani.resetQueue();
		}
		figureWalkingAnimationFromTo(p.getFigure(), p.getFromIndex(),
				p.getToIndex());
	}

	private void handleTakePercept(TakePercept p) {
		// this.repaintPicture();
		newStatement(StatementManager.getStatement(p, figure));
		figureUsingAnimation(p.getFigure());
	}

	private void handleUsePercept(UsePercept p) {
		figureUsingAnimation(p.getFigure());
	}

	private void handleFleePercept(FleePercept p) {
		newStatement(StatementManager.getStatement(p, figure));
		figureRunningAnimation(p.getFigure());
		if (p.getFigure().equals(figure) && !figure.getRoomInfo().fightRunning()) {
			this.frame.getSpielfeld().zoomOutOfRoom();
		}
	}

	private void handleTumblingPercept(TumblingPercept p) {
		newStatement(StatementManager.getStatement(p, figure));
	}

	private void handleMissPercept(MissPercept p) {
		newStatement(StatementManager.getStatement(p, figure));
	}

	private void handleItemDroppedPercept(ItemDroppedPercept p) {
		newStatement(StatementManager.getStatement(p));
	}

	private void handleFightEndedPercept(FightEndedPercept p) {

		newStatement(StatementManager.getStatement(p));
		MasterAnimation ani = this.masterAnis.get(this.figure
				.getRoomInfo());
		if (ani != null) {
			ani.myStop();
			
		}
		BoardView spielfeld = this.getMainFrame().getSpielfeld();
		if(this.frame.isAutoZoom()) {
			spielfeld.zoomOutOfRoom();
		}
		this.repaintPicture();
	}

	private void handleDoorSmashPercept(DoorSmashPercept p) {

		newStatement(StatementManager.getStatement(p,
				this.figure));
		figureBeenHitAnimation(p.getVictim());
	}

	private void handleShieldBlockPercept(ShieldBlockPercept p) {
		newStatement(StatementManager.getStatement(p, figure));
	}

	private void handleWaitPercept(WaitPercept p) {
		figurePauseAnimation(p.getFigure());
	}

	@Override
	public void tellPercept(Percept p) {

		if (p instanceof WaitPercept) {
			handleWaitPercept((WaitPercept) p);
		}

		if (p instanceof DoorSmashPercept) {
			handleDoorSmashPercept((DoorSmashPercept) p);
		}

		if (p instanceof BreakSpellPercept) {
			handleBreakSpellPercept((BreakSpellPercept) p);
		}

		if (p instanceof ShieldBlockPercept) {
			handleShieldBlockPercept((ShieldBlockPercept) p);
		}
		if (p instanceof FightEndedPercept) {
			handleFightEndedPercept((FightEndedPercept) p);
		}

		if (p instanceof ItemDroppedPercept) {
			handleItemDroppedPercept((ItemDroppedPercept) p);
		}
		if (p instanceof MissPercept) {
			handleMissPercept((MissPercept) p);
		}
		if (p instanceof TumblingPercept) {
			handleTumblingPercept((TumblingPercept) p);
		}
		if (p instanceof AttackPercept) {
			handleAttackPercept((AttackPercept) p);
		}
		if (p instanceof HitPercept) {
			handleHitPercept((HitPercept) p);
		}
		if (p instanceof DiePercept) {
			handleDiePercept((DiePercept) p);
		}
		if (p instanceof MovePercept) {
			handleMovePercept((MovePercept) p);
		}
		if (p instanceof ScoutPercept) {
			handleScoutPercept((ScoutPercept) p);
		}
		if (p instanceof SpellPercept) {
			handleSpellPercept((SpellPercept) p);
		}
		if (p instanceof StepPercept) {
			handleStepPercept((StepPercept) p);
		}
		if (p instanceof TakePercept) {
			handleTakePercept((TakePercept) p);
		}
		if (p instanceof UsePercept) {
			handleUsePercept((UsePercept) p);
		}
		if (p instanceof FleePercept) {
			handleFleePercept((FleePercept) p);
		}
		if (p instanceof InfoPercept) {
			
			/*
			 * play sounds if applicable
			 */
			if (((InfoPercept) p).getCode() == InfoPercept.LOCKED_DOOR
					|| ((InfoPercept) p).getCode() == InfoPercept.UNLOCKED_DOOR) {
				AudioEffectsManager.playSound(AudioEffectsManager.DOOR_LOCK);
			}
			if (((InfoPercept) p).getCode() == InfoPercept.RESPAWN) {
				AudioEffectsManager.playSound(AudioEffectsManager.MAGIC_BLING);
			}
			
			/*
			 * write text messages
			 */
			String s = StatementManager.getStatement((InfoPercept) p);
			newStatement(s, 2);
		}
		if (p instanceof TextPercept) {
			newStatement(((TextPercept) p).getText(), 2);
		}
		if (p instanceof DisappearPercept) {
			newStatement(StatementManager.getStatement(((DisappearPercept)p), figure));
			repaintPicture();
			// pliing!
			//figureDisappearAnimation(p.getFigure());
		}

		if (p instanceof FightBeginsPercept) {
			MainFrame frame = this.getMainFrame();
			if (frame != null) {
				frame.getKampfVerlauf().cls();
			}
			if(this.frame.isAutoZoom()) {
				this.frame.getSpielfeld().zoomIntoRoom();
			}
			newStatement(StatementManager.getStatement((FightBeginsPercept) p));
		}

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

	public void setMain(MainFrame m) {
		frame = m;
	}

	@Override
	public MainFrame getMainFrame() {
		return frame;
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

	public void initGui(AbstractStartWindow start, Applet applet,
			String playerName) {
			frame = new MainFrame((StartView) start,
					MainFrame.clearString(playerName), applet, this,
					"Java Dungeon V.22.08.06 - 3");
			frame.initMainframe();
	}

	public Map<String, String> getHighScoreString(String playerName,
			String comment,
			boolean reg, boolean liga) {
		if (figure instanceof FigureInfo) {
			return ((HeroInfo) figure).getHighScoreString(playerName, comment,
					reg, liga);
		} else {
			return null;
		}
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
	public void gameOver() {
		frame.gameOver();
		Collection<RoomInfo> s = masterAnis.keySet();
		for (Iterator<RoomInfo> iter = s.iterator(); iter.hasNext();) {
			RoomInfo element = iter.next();
			MasterAnimation ani = masterAnis.get(element);
			ani.myStop();
		}

	}


	public Graphics getGraphics() {
		return frame.getSpielfeld().getSpielfeldBild().getGraphics();
	}


	public int getChoosenEnemy() {
		return frame.getChoosenEnemy();
	}




	public void figureSlaysAnimation(AttackPercept p) {
		FigureInfo fig = p.getAttacker();
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_slays(fig);
		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.SLAYS, info, this);
			runAnimation(ani, 0);
		}
	}

	public void figureWalkingAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_walking(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.WALKING, info, this);
			runAnimation(ani, 0);
		}
	}

	public void figureSorceringAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_sorcering(fig);
		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					Animation.SORCERING, info, this);
			runAnimation(ani, 0);
		}
	}

	public void figureUsingAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_using(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.USING,
					info, this);
			runAnimation(ani, 0);
		}
	}

	public void figurePauseAnimation(FigureInfo fig) {
		figurePauseAnimation(fig, 0);
	}

	public void figurePauseAnimation(FigureInfo fig, int offset) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_pause(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.PAUSE,
					info, this);
			runAnimation(ani, offset);
		}

	}

	public void figureRunningAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = AnimationUtils.getFigure_running(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.RUNNING,
					info, this);
			runAnimation(ani, 0);
		}
	}

	public void figureAppearAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = frame.getSpielfeld().getSpielfeldBild().getPuff();

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, -1, info, this);
			ani.setSizeModifier(0.5);
			runAnimation(ani, 0);
		}
	}

	public void figureBeenHitAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = AnimationUtils.getFigure_been_hit(fig);

		if (set != null) {
			Animation ani = new AnimationReal(set, fig, AnimationReal.BEEN_HIT,
					info, this);
			runAnimation(ani, 8);
		}

	}

	public void figureDiesAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = AnimationUtils.getFigure_tipping_over(fig);
		Animation ani = new AnimationReal(set, fig, AnimationReal.SLAYS, info,
				this);
		ani.setDeathAnimation(true);

		runNewAnimation(ani);
	}

	public void figureDisappearAnimation(FigureInfo m) {
		RoomInfo info = m.getRoomInfo();

		AnimationSet set = frame.getSpielfeld().getSpielfeldBild().getPuff();

		Animation ani = null;
		if (set != null) {
			ani = new AnimationReal(set, m, AnimationReal.SLAYS, info, this);

		}

		runAnimation(ani, 0);
	}

	private AnimationTask getLastAniTimeOf(FigureInfo f,
			Vector<AnimationTask> map) {
		AnimationTask maxTask = null;
		int max = 0;
		for (Iterator<AnimationTask> iter = map.iterator(); iter.hasNext();) {
			AnimationTask element = iter.next();
			FigureInfo aniFigure = element.getFigure();
			if (f.equals(aniFigure)) {
				int value = element.getRound();
				if (value > max) {
					max = value;
					maxTask = element;
				}
			}
		}
		return maxTask;
	}

	int attackDelay = 0;

	Vector<Action> actionQueue = new Vector<Action>();

	public void fightRoundEnded() {
		attackDelay = 0;

	}



	int clearAniThreadHashCnt = 0;

	private void runAnimation(Animation ani, int timeOffset) {

		RoomInfo r = ani.getRoomInfo();
		MasterAnimation masterAni = masterAnis.get(r);

		if (masterAni != null && !masterAni.finished) {
			Vector<AnimationTask> map = masterAni.getAnimations();
			Vector<AnimationTask> map2 = new Vector<AnimationTask>();
			map2.addAll(map);
			masterAni.setRoom(r);
			AnimationTask maxAni = getLastAniTimeOf(ani.getObject(), map2);
			if (maxAni != null) {
				masterAni
						.addAnimationAt(ani,
								maxAni.getRound() + maxAni.getLength() + 4
										+ timeOffset);
			} else {
				masterAni.addAnimationAsNext(ani);
			}

		} else {

			GraphBoard bord = getMainFrame().getSpielfeld().getSpielfeldBild();
			masterAni = new MasterAnimation(bord.getRoomSize(), bord, r, this);

			masterAni.addAnimationAsNext(ani);

			masterAnis.put(r, masterAni);
			masterAni.start();

		}

	}

	private void runNewAnimation(Animation ani) {
		RoomInfo r = ani.getRoomInfo();
		MasterAnimation masterAni = masterAnis.get(r);
		if (masterAni != null) {
			masterAni.myStop();
			masterAni = null;

		}
		System.gc();
		GraphBoard bord = getMainFrame().getSpielfeld().getSpielfeldBild();
		masterAni = new MasterAnimation(bord.getRoomSize(), bord,
				figure.getRoomInfo(), this);

		masterAni.addAnimationAsNext(ani);
		masterAnis.put(r, masterAni);

		masterAni.start();

	}

	@Override
	public boolean currentAnimationThreadRunning(RoomInfo r) {
		MasterAnimation ani = masterAnis.get(r);

		if (ani != null) {

			return !ani.finished;
		}
		return false;
	}


	/**
	 * @return Returns the control.
	 */
	@Override
	public ActionAssembler getControl() {
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

	public void figureWalkingAnimationFromTo(FigureInfo fig, int fromPos,
			int toPos) {
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_walking(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.WALKING, info, this);
			ani.setFromPosIndex(fromPos);
			ani.setToPosIndex(toPos);
			runAnimation(ani, 0);
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
		return getMainFrame().getGesundheit()
				.getSelectedItemIndex();
	}
	
	@Override
	public ItemInfo getSelectedItem() {
		return (ItemInfo)getMainFrame().getGesundheit()
				.getItemCombo().getSelectedItem();
	}

	@Override
	public void setSelectedItemIndex(int i) {
		getMainFrame().getGesundheit().getItemCombo().setSelectedIndex(i);

	}

}