package gui;

import figure.FigureInfo;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import spell.Spell;
import text.Statement;
import text.StatementManager;
import animation.AnimationSet;
import animation.AnimationUtils;
import audio.AudioEffectsManager;
import control.MainFrameI;
import dungeon.RoomInfo;

public class PerceptHandler {

	private final AbstractJDGUIEngine2D gui;
	private final Map<RoomInfo, MasterAnimation> masterAnis = new HashMap<RoomInfo, MasterAnimation>();

	public PerceptHandler(AbstractJDGUIEngine2D gui) {
		this.gui = gui;
	}

	void tellPercept(Percept p) {
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
			newStatement(StatementManager.getStatement(((DisappearPercept) p),
					gui.getFigure()));
			gui.repaintPicture();
			// pliing!
			// figureDisappearAnimation(p.getFigure());
		}

		if (p instanceof FightBeginsPercept) {
			MainFrameI frame = gui.getMainFrame();
			if (frame != null) {
				frame.clearFightLog();
			}
			newStatement(StatementManager.getStatement((FightBeginsPercept) p));
		}
	}

	private void newStatement(String text, int i) {
		gui.newStatement(text, i);

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

	private void handleAttackPercept(AttackPercept p) {

		figureSlaysAnimation(p);
	}

	private void handleBreakSpellPercept(BreakSpellPercept p) {

		newStatement(StatementManager.getStatement(p, gui.getFigure()));

	}

	private void handleDiePercept(DiePercept p) {
		newStatement(StatementManager.getStatement(p, gui.getFigure()));
		gui.repaintPicture();
		figureDiesAnimation(p.getFigure());
	}

	private void newStatement(Statement statement) {
		gui.newStatement(statement);

	}

	private void handleDoorSmashPercept(DoorSmashPercept p) {

		newStatement(StatementManager.getStatement(p, gui.getFigure()));
		figureBeenHitAnimation(p.getVictim());
	}

	private void handleFightEndedPercept(FightEndedPercept p) {

		newStatement(StatementManager.getStatement(p));
		MasterAnimation ani = masterAnis.get(gui.getFigure().getRoomInfo());
		if (ani != null) {
			ani.myStop();

		}
		gui.repaintPicture();
	}

	private void handleFleePercept(FleePercept p) {
		newStatement(StatementManager.getStatement(p, gui.getFigure()));
		figureRunningAnimation(p.getFigure());
	}

	private void handleHitPercept(HitPercept p) {
		FigureInfo victim = p.getVictim();
		newStatements(StatementManager.getStatements(p, gui.getFigure()));
		if (p.getDamage() > 0) {
			figureBeenHitAnimation(victim);
		}
		gui.getMainFrame().updateHealth();

	}

	private void newStatements(List<Statement> statements) {
		gui.newStatements(statements);

	}

	private void handleItemDroppedPercept(ItemDroppedPercept p) {
		newStatement(StatementManager.getStatement(p));
	}

	private void handleMissPercept(MissPercept p) {
		newStatement(StatementManager.getStatement(p, gui.getFigure()));
	}

	private void handleMovePercept(MovePercept p) {
		MasterAnimation ani = masterAnis.get(gui.getFigure().getRoomInfo());
		if (ani != null) {
			ani.resetQueue();
		}
		if (p.getFigure().getRoomInfo().equals(gui.getFigure().getRoomInfo())
				&& !p.getFigure().equals(gui.getFigure())) {
			newStatement(StatementManager.getStatement(p, gui.getFigure()));

			AudioEffectsManager.playSound(AudioEffectsManager.DOOR_CLOSE);
		}

		figureWalkingAnimation(p.getFigure());
	}

	private void handleScoutPercept(ScoutPercept p) {
		figureUsingAnimation(p.getFigure());
	}

	private void handleShieldBlockPercept(ShieldBlockPercept p) {
		newStatement(StatementManager.getStatement(p, gui.getFigure()));
	}

	private void handleSpellPercept(SpellPercept p) {

		figureSorceringAnimation(p.getFigure());

		newStatement(StatementManager.getStatement(p, gui.getFigure()));
		if (p.getSpell().getType() == Spell.SPELL_FIREBALL) {
			AudioEffectsManager.playSound(AudioEffectsManager.MAGIC_FIREBALL);
		} else {
			AudioEffectsManager.playSound(AudioEffectsManager.MAGIC_SOUND);

		}
	}

	private void handleStepPercept(StepPercept p) {
		MasterAnimation ani = masterAnis.get(gui.getFigure().getRoomInfo());
		gui.repaintPicture();
		if (ani != null) {
			ani.resetQueue();
		}
		figureWalkingAnimationFromTo(p.getFigure(), p.getFromIndex(),
				p.getToIndex());
	}

	private void handleTakePercept(TakePercept p) {
		// gui.repaintPicture();
		newStatement(StatementManager.getStatement(p, gui.getFigure()));
		figureUsingAnimation(p.getFigure());
	}

	private void handleTumblingPercept(TumblingPercept p) {
		newStatement(StatementManager.getStatement(p, gui.getFigure()));
	}

	private void handleUsePercept(UsePercept p) {
		figureUsingAnimation(p.getFigure());
	}

	private void handleWaitPercept(WaitPercept p) {
		figurePauseAnimation(p.getFigure());
	}

	public void figureAppearAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = gui.getBoard().getSpielfeldBild().getPuff();

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, -1, info, gui);
			ani.setSizeModifier(0.5);
			runAnimation(ani, 0);
		}
	}

	public void figureBeenHitAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = AnimationUtils.getFigure_been_hit(fig);

		if (set != null) {
			Animation ani = new AnimationReal(set, fig, AnimationReal.BEEN_HIT,
					info, gui);
			runAnimation(ani, 8);
		}

	}

	public void figureDiesAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = AnimationUtils.getFigure_tipping_over(fig);
		Animation ani = new AnimationReal(set, fig, AnimationReal.SLAYS, info,
				gui);
		ani.setDeathAnimation(true);

		runNewAnimation(ani);
	}

	public void figureDisappearAnimation(FigureInfo m) {
		RoomInfo info = m.getRoomInfo();

		AnimationSet set = gui.getBoard().getSpielfeldBild().getPuff();

		Animation ani = null;
		if (set != null) {
			ani = new AnimationReal(set, m, AnimationReal.SLAYS, info, gui);

		}

		runAnimation(ani, 0);
	}

	public void figurePauseAnimation(FigureInfo fig) {
		figurePauseAnimation(fig, 0);
	}

	public void figurePauseAnimation(FigureInfo fig, int offset) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_pause(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.PAUSE,
					info, gui);
			runAnimation(ani, offset);
		}

	}

	public void figureRunningAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();

		AnimationSet set = AnimationUtils.getFigure_running(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.RUNNING,
					info, gui);
			runAnimation(ani, 0);
		}
	}

	public void figureSlaysAnimation(AttackPercept p) {
		FigureInfo fig = p.getAttacker();
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_slays(fig);
		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.SLAYS, info, gui);
			runAnimation(ani, 0);
		}
	}

	public void figureSorceringAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_sorcering(fig);
		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					Animation.SORCERING, info, gui);
			runAnimation(ani, 0);
		}
	}

	public void figureUsingAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_using(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig, Animation.USING,
					info, gui);
			runAnimation(ani, 0);
		}
	}

	public void figureWalkingAnimation(FigureInfo fig) {

		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_walking(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.WALKING, info, gui);
			runAnimation(ani, 0);
		}
	}

	public void figureWalkingAnimationFromTo(FigureInfo fig, int fromPos,
			int toPos) {
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_walking(fig);

		if (set != null) {
			AnimationReal ani = new AnimationReal(set, fig,
					AnimationReal.WALKING, info, gui);
			ani.setFromPosIndex(fromPos);
			ani.setToPosIndex(toPos);
			runAnimation(ani, 0);
		}

	}

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

			GraphBoard bord = gui.getBoard().getSpielfeldBild();
			masterAni = new MasterAnimation(bord.getRoomSize(), bord, r, gui);

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
		GraphBoard bord = gui.getBoard().getSpielfeldBild();
		masterAni = new MasterAnimation(bord.getRoomSize(), bord, gui
				.getFigure().getRoomInfo(), gui);

		masterAni.addAnimationAsNext(ani);
		masterAnis.put(r, masterAni);

		masterAni.start();

	}

	public void stopAllAnimtation() {
		Collection<RoomInfo> c = masterAnis.keySet();
		for (Iterator<RoomInfo> iter = c.iterator(); iter.hasNext();) {
			RoomInfo element = iter.next();
			MasterAnimation ani = masterAnis.get(element);
			ani.myStop();
		}

	}

	public boolean currentAnimationThreadRunning(RoomInfo r) {
		MasterAnimation ani = masterAnis.get(r);

		if (ani != null) {

			return !ani.finished;
		}
		return false;

	}

}
