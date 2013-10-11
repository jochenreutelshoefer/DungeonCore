/*
 * Created on 07.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;

import java.applet.Applet;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.awt.event.*;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import spell.Fireball;
import spell.Spell;

import dungeon.JDPoint;
import dungeon.Position;
import dungeon.Room;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.action.LearnSpellAction;
import figure.action.TakeItemAction;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.memory.Memory;
import figure.monster.Monster;
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
import game.JDEnv;
import game.JDGUI;
import gui.audio.AudioEffectsManager;
import gui.engine2D.GraphBoard;
import gui.engine2D.animation.Animation;
import gui.engine2D.animation.AnimationFake;
import gui.engine2D.animation.AnimationReal;
import gui.engine2D.animation.AnimationSet;
import gui.engine2D.animation.AnimationTask;
import gui.engine2D.animation.MasterAnimation;
import gui.engine2D.animation.ThreadPool;
import gui.mainframe.MainFrame;
import gui.mainframe.component.BoardView;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MyJDGui implements JDGUI {

	public final static int BUTTON_SLAP = 1;

	public final static int BUTTON_GO_NORTH = 2;

	public final static int BUTTON_GO_EAST = 3;

	public final static int BUTTON_GO_SOUTH = 4;

	public final static int BUTTON_GO_WEST = 5;

	private ActionAssembler control;

	private MainFrame frame;

	private boolean visibilityCheat = false;

	private Memory memory;

	private Map masterAnis = new HashMap();

	private FigureInfo figure;

	/**
	 * @return Returns the figure.
	 */
	public FigureInfo getFigure() {
		return figure;
	}

	/**
	 * @param figure
	 *            The figure to set.
	 */
	public void setFigure(FigureInfo figure) {
		this.figure = figure;

	}

	public boolean getVisibility() {
		return visibilityCheat;
	}

	public void resetingRoomVisibility(JDPoint p) {
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

	public boolean isHostileTo(FigureInfo f) {
		if(f.getFigureClass().equals(Fir.class)) {
			return false;
		}
		if (f instanceof MonsterInfo) {
			return true;
		}
		return false;
	}

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
		// repaintPicture();
		MasterAnimation ani = (MasterAnimation) this.masterAnis.get(this.figure
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

	public Action getAction() {
		if (memory == null) {
			memory = new Memory(figure.getDungeonSize());
		}
		if (actionQueue.size() > 0) {
			return (Action) actionQueue.remove(0);
		}
		return null;
	}

	public void plugAction(Action a) {

		actionQueue.add(a);
	}

	// public void resetAction() {
	// control.setSpecifiedAction(null);
	// }

	private void handleScoutPercept(ScoutPercept p) {
		// this.repaintPicture();
		figureUsingAnimation(p.getFigure());
	}

	private void handleSpellPercept(SpellPercept p) {
		// this.repaintPicture();

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
		MasterAnimation ani = (MasterAnimation) this.masterAnis.get(this.figure
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
		// this.repaintPicture();
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
		FigureInfo attacker = p.getAttacker();
		// figureSlaysAnimation(attacker);
		newStatement(StatementManager.getStatement(p, figure));
	}

	private void handleItemDroppedPercept(ItemDroppedPercept p) {
		// this.repaintPicture();
		newStatement(StatementManager.getStatement(p));
	}

	private void handleFightEndedPercept(FightEndedPercept p) {

		newStatement(StatementManager.getStatement((FightEndedPercept) p));
		MasterAnimation ani = (MasterAnimation) this.masterAnis.get(this.figure
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

		newStatement(StatementManager.getStatement((DoorSmashPercept) p,
				this.figure));
		figureBeenHitAnimation(p.getVictim());
	}

	private void handleShieldBlockPercept(ShieldBlockPercept p) {
		newStatement(StatementManager.getStatement(p, figure));
	}

	private void handleWaitPercept(WaitPercept p) {
		figurePauseAnimation(p.getFigure());
	}

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

		// newStatement(p.getGuiStatement(),1);
	}

	public void setVisibility(boolean b) {
		visibilityCheat = b;
	}

	public MyJDGui(FigureInfo f) {
		this.figure = f;
		control = new ActionAssembler();
		control.setGui(this);
		// threadManager = new ThreadPool(20);
	}

	public MyJDGui() {
		control = new ActionAssembler();
		control.setGui(this);
	}

	public void setMain(MainFrame m) {
		frame = m;
	}

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
			newStatement(s.getText(), s.format);
		}
	}

	public void newStatements(List l) {
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Statement element = (Statement) iter.next();
			newStatement(element);

		}
	}

	public void newStatement(String s, int code, int to) {
		frame.newStatement(s, code, to);
	}

	public void initGui(AbstractStartWindow start, Applet applet,
			String playerName) {
		if (start instanceof StartView) {
			frame = new MainFrame((StartView) start,
					MainFrame.clearString(playerName), applet, this,
					"Java Dungeon V.22.08.06 - 3");
			// setMain(main);
			frame.initMainframe();
		} else {
			System.out.println("MyJDGui.iniGUI: wrong StartWindow instance");
			System.exit(0);
		}
	}

	// public RoomInfo getMasterAnimationRoomInfo() {
	// if (masterAni == null) {
	// return null;
	// }
	// return masterAni.getRoom();
	//
	// }

	public Map getHighScoreString(String playerName, String comment,
			boolean reg, boolean liga) {
		if (figure instanceof FigureInfo) {
			return ((HeroInfo) figure).getHighScoreString(playerName, comment,
					reg, liga);
		} else {
			return null;
		}
	}

	// public void fightEnded() {
	// // int k = queue.size();
	// // for (int i = 0; i < k - 1; i++) {
	// // queue.remove(0);
	// // }
	// // masterAni.stop();
	//
	// // stopAllAnimation();
	// // frame.fightEnded();
	// repaintPicture();
	// }

	public void stopAllAnimation() {
		Collection c = masterAnis.keySet();
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			RoomInfo element = (RoomInfo) iter.next();
			MasterAnimation ani = (MasterAnimation) masterAnis.get(element);
			ani.myStop();
		}

	}

//	public void resetViewPoint() {
//		frame.getSpielfeld().resetViewPoint();
//	}

	public void gameOver() {
		frame.gameOver();
		Collection s = masterAnis.keySet();
		for (Iterator iter = s.iterator(); iter.hasNext();) {
			RoomInfo element = (RoomInfo) iter.next();
			MasterAnimation ani = (MasterAnimation) masterAnis.get(element);
			ani.myStop();
		}
		// this.threadManager.complete();
		// this.threadManager.doFinalize();

	}

	public void disableControl() {
		frame.disableControl();
	}

	// public void updateGUI(int key, boolean repaint) {
	// // Thread th = new Thread(frame);
	// // th.start();
	// frame.updateGUI(key, repaint);
	// }

	// public void repaintSpielfeldBild() {
	// frame.getSpielfeld().getSpielfeldBild().repaint();
	// }

	// public void repaintFigureRoom() {
	// frame.getSpielfeld().getSpielfeldBild().repaintRoom(getGraphics(),
	// figure.getRoomInfo(), true);
	// }

	public Graphics getGraphics() {
		return frame.getSpielfeld().getSpielfeldBild().getGraphics();
	}

	public void repaintRoom(RoomInfo wannaGo) {
		frame.getSpielfeld().getSpielfeldBild()
				.repaintRoom(getGraphics(), wannaGo, true);
	}

	// public void setRoomViewPoint(JDPoint wannaGo) {
	// frame.getSpielfeld().setRoomViewPoint(wannaGo);
	// }

	public int getChoosenEnemy() {
		return frame.getChoosenEnemy();
	}

	// public void causeActionEvent(int key, String commando) {
	// if (key == this.BUTTON_SLAP) {
	// ActionEvent ae = new ActionEvent(frame.slap, 0, commando);
	// frame.actionPerformed(ae);
	// }
	// if (key == this.BUTTON_GO_EAST) {
	// ActionEvent ae = new ActionEvent(frame.getSteuerung().east, 0,
	// commando);
	// frame.getSteuerung().actionPerformed(ae);
	// }
	// if (key == this.BUTTON_GO_SOUTH) {
	// ActionEvent ae = new ActionEvent(frame.getSteuerung().south, 0,
	// commando);
	// frame.getSteuerung().actionPerformed(ae);
	// }
	// if (key == this.BUTTON_GO_WEST) {
	// ActionEvent ae = new ActionEvent(frame.getSteuerung().west, 0,
	// commando);
	// frame.getSteuerung().actionPerformed(ae);
	// }
	// if (key == this.BUTTON_GO_NORTH) {
	// ActionEvent ae = new ActionEvent(frame.getSteuerung().north, 0,
	// commando);
	// frame.getSteuerung().actionPerformed(ae);
	// }
	// }

	public void animationDone() {
		// if (!queue.isEmpty()) {
		// Animation nextAni = (Animation) (queue.remove(0));
		// current = nextAni;
		// currentThread = new Thread(nextAni);
		// currentThread.start();
		// } else {
		// current = null;
		// frame.getSpielfeld().getSpielfeldBild().drawHero(
		// getGraphics(),
		// frame.getSpielfeld().getSpielfeldBild().getHeroPoint()
		// .getX(),
		// frame.getSpielfeld().getSpielfeldBild().getHeroPoint()
		// .getY());
		// }
	}

	public void figureSlaysAnimation(AttackPercept p) {
		FigureInfo fig = p.getAttacker();
		// RoomInfo info =
		// RoomInfo.makeRoomInfo(game.getDungeon().getRoom(figure.getRoomNumber()),
		// figure.getObservationStatusObject(figure.getRoomNumber()));
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_slays(fig);
		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.SLAYS, info, this);
//			if(p.getResult().getValue() > 0) {
//				ani.setFinishingSound(AudioEffectsManager.HIT);
//			}
			runAnimation(ani, 0);
		}
	}

	public void figureWalkingAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		// int code = game.getHero().getHeroCode();
		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_walking(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.WALKING, info, this);
			runAnimation(ani, 0);
		}
	}

	public void figureSorceringAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_sorcering(fig);
		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					Animation.SORCERING, info, this);
			runAnimation(ani, 0);
		}
	}

	public void figureUsingAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_using(fig);

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
		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_pause(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.PAUSE,
					info, this);
			runAnimation(ani, offset);
		}

	}

	public void figureRunningAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_running(fig);

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

		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_been_hit(fig);

		if (set != null) {
			Animation ani = new AnimationReal(set, fig, AnimationReal.BEEN_HIT,
					info, this);
			runAnimation(ani, 8);
		}

	}

	public void figureDiesAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_tipping_over(fig);
		Animation ani = new AnimationReal(set, fig, AnimationReal.SLAYS, info,
				this);
		ani.deathAnimation = true;

		runNewAnimation(ani);
	}

	public void figureDisappearAnimation(FigureInfo m) {
		RoomInfo info = m.getRoomInfo();

		AnimationSet set = frame.getSpielfeld().getSpielfeldBild().getPuff();

		Animation ani = null;
		if (set != null) {
			ani = new AnimationReal(set, m, AnimationReal.SLAYS, info, this);
			((AnimationReal) ani).setInverted(true);

		}
		// else {
		// ani = new AnimationFake(frame.getSpielfeld().getSpielfeldBild()
		// .getMonsterImage(m), m, Animation.SLAYS,info);
		// }

		runAnimation(ani, 0);
	}

	private void removeAllAnisOf(FigureInfo f, LinkedList l) {
		LinkedList toRemove = new LinkedList();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			AnimationTask element = (AnimationTask) iter.next();
			if (element.getFigure().equals(f)) {
				toRemove.add(element);
			}

		}

		for (Iterator iter = toRemove.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			l.remove(element);
		}
	}

	private AnimationTask getLastAniTimeOf(FigureInfo f, Vector map) {
		AnimationTask maxTask = null;
		int max = 0;
		for (Iterator iter = map.iterator(); iter.hasNext();) {
			AnimationTask element = (AnimationTask) iter.next();
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

	Vector actionQueue = new Vector();

	public void fightRoundEnded() {
		attackDelay = 0;

	}

	private void printAniMap() {
		System.out.println("AniMap: " + masterAnis.size());
		Collection c = masterAnis.keySet();
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			RoomInfo element = (RoomInfo) iter.next();
			MasterAnimation ani = (MasterAnimation) masterAnis.get(element);
			System.out.println(element.toString() + "  -  " + ani.toString());
		}
		System.out.println("\n");
	}

	int clearAniThreadHashCnt = 0;

	private void runAnimation(Animation ani, int timeOffset) {

		RoomInfo r = ani.getRoomInfo();
		// printAniMap();
		MasterAnimation masterAni = (MasterAnimation) masterAnis.get(r);

		if (masterAni != null && !masterAni.finished) {
			// System.out.println("also animation dazupacken!");
			Vector map = masterAni.getAnimations();
			Vector map2 = new Vector();
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
			// } else if(masterAni != null && !masterAni.isAlive()) {
			// masterAni.addAnimationAsNext(ani);
			// masterAni.start();
		} else {

			GraphBoard bord = getMainFrame().getSpielfeld().getSpielfeldBild();
			masterAni = new MasterAnimation(bord.getRoomSize(), bord, r, this);

			masterAni.addAnimationAsNext(ani);

			masterAnis.put(r, masterAni);
			// threadManager.assign(masterAni);
			masterAni.start();

		}

	}

	private void runNewAnimation(Animation ani) {
		RoomInfo r = ani.getRoomInfo();
		MasterAnimation masterAni = (MasterAnimation) masterAnis.get(r);
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

		// threadManager.assign(masterAni);
		masterAni.start();

	}

	// public void onlyTipOver(MonsterInfo m) {
	// int k = 0;
	// while (k < queue.size()) {
	// Animation ani = (Animation) queue.get(k);
	// if (ani.getObject() == m && ani.getType() != Animation.TIPPING_OVER) {
	// queue.remove(ani);
	// k--;
	// }
	// k++;
	// }
	// }
	//
	// public void suspendAnimation() {
	// if (currentThread != null) {
	// currentThread.suspend();
	// }
	// }

	// public void resumeAnimation() {
	// if (currentThread != null) {
	// currentThread.resume();
	// }
	// }

	public boolean currentThreadRunning(RoomInfo r) {
		MasterAnimation ani = (MasterAnimation) masterAnis.get(r);

		if (ani != null) {

			return !ani.finished;
		}
		return false;
	}

	// public Animation getCurrent() {
	// return current;
	// }

	/**
	 * @return Returns the control.
	 */
	public ActionAssembler getControl() {
		return control;
	}

	public void repaintPicture() {
		this.getMainFrame().repaint();
	}

	public void updateGui() {

		getMainFrame().updateGUI2(MainFrame.UPDATE_ALL, false);

	}

	public void onTurn() {
		updateGui();
		repaintPicture();
	}

	public void actionDone(Action a, ActionResult res) {

		if (res.getKey1() == ActionResult.KEY_IMPOSSIBLE) {
			newStatement(StatementManager.getStatement(res));
		} else {
			if (!(a instanceof AttackAction)) {

				// repaintPicture();
			}

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

	// public Action getMovementAction() {
	// return control.getSpecifiedMovementAction();
	// }

	// public void resetMovementAction() {
	// control.setSpecifiedAction(null);
	// }

	public void figureWalkingAnimationFromTo(FigureInfo fig, int fromPos,
			int toPos) {
		RoomInfo info = fig.getRoomInfo();
		// int code = game.getHero().getHeroCode();
		AnimationSet set = frame.getSpielfeld().getSpielfeldBild()
				.getFigure_walking(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.WALKING, info, this);
			ani.setFromPosIndex(fromPos);
			ani.setToPosIndex(toPos);
			runAnimation(ani, 0);
		}

	}

}