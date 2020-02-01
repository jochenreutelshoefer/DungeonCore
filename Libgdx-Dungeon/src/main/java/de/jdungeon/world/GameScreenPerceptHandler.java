package de.jdungeon.world;

import java.util.List;

import animation.AnimationManager;
import animation.AnimationUtils;
import animation.DefaultAnimationSet;
import animation.DefaultAnimationTask;
import audio.AudioEffectsManager;
import dungeon.Position;
import dungeon.RoomInfo;
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
import figure.percept.OpticalPercept;
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
import game.PerceptHandler;
import game.RoomInfoEntity;
import graphics.JDImageProxy;
import log.Log;
import text.Statement;
import text.StatementManager;

import de.jdungeon.app.audio.MusicManager;
import de.jdungeon.game.Music;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class GameScreenPerceptHandler implements PerceptHandler {

	private final GameScreen screen;
	private final FigureInfo figure;
	private final AnimationManager animationManager;

	public GameScreenPerceptHandler(GameScreen screen, FigureInfo figure, AnimationManager animationManager) {
		this.screen = screen;
		this.figure = figure;
		this.animationManager = animationManager;
	}


	@Override
	public void tellPercept(Percept p) {
		handlePercept(p);
	}

	public void handlePercept(Percept p) {
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
			newStatement(new Statement(s, 2));
		}
		if (p instanceof TextPercept) {
			newStatement(new Statement(((TextPercept) p).getText(), 2));
		}
		if (p instanceof DisappearPercept) {
			newStatement(StatementManager.getStatement(((DisappearPercept) p),
					figure));
		}
		if (p instanceof FightEndedPercept) {
			handleFightEndedPercept((FightEndedPercept) p);
		}
		if (p instanceof FightBeginsPercept) {
			handleFightBeginsPercept((FightBeginsPercept)p);
		}
	}

	private void handleFightBeginsPercept(FightBeginsPercept p) {
		newStatement(StatementManager.getStatement(p));
		// todo:
		//screen.enterFightMode();

	}

	private void handleFightEndedPercept(FightEndedPercept p) {
		newStatement(StatementManager.getStatement(p));

		// todo:
		/*
		screen.getGui().fightEnded();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

				screen.exitFightMode();
			}
		}).start();
		*/
	}

	private void handleFleePercept(FleePercept p) {
		newStatement(StatementManager.getStatement(p, this.figure));

		FigureInfo fleeingFigure = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_running(fleeingFigure);
		if (set != null) {
			startAnimation(set, fleeingFigure, p);
		}
		// todo:
		/*
		if (fleeingFigure.equals(this.figure) && p.isSuccess()) {
			screen.exitFightMode();
		}
		*/
	}

	private void handleUsePercept(UsePercept p) {
		FigureInfo user = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_using(user);
		if (set != null) {
			startAnimation(set, user, p);
		}

	}

	private void handleTakePercept(TakePercept p) {
		FigureInfo taker = p.getFigure();

		// todo:
		/*
		if(taker.equals(this.figure)) {
			screen.focusTakenItem(p.getItem());
		}
		*/

		DefaultAnimationSet set = AnimationUtils.getFigure_using(taker);
		if (set != null) {
			startAnimation(set, taker, p);
		}

		AudioEffectsManager.playSound(AudioEffectsManager.TAKE_ITEM);
		newStatement(StatementManager.getStatement(p, this.figure));

	}

	private void handleStepPercept(StepPercept p) {
		FigureInfo fig = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_walking(fig);

		if (set != null) {
			startAnimation(set, fig, Position.Pos.fromValue(p.getFromIndex()), Position.Pos.fromValue(p.getToIndex()),"", false, true, false, p , null);
		}

	}

	private void handleSpellPercept(SpellPercept p) {
		FigureInfo user = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_using(user);

		if (set != null) {
			startAnimation(set, user, p);
		}

	}

	private void handleScoutPercept(ScoutPercept p) {
		FigureInfo user = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_using(user);

		if (set != null) {
			startAnimation(set, user,  p);
		}

	}

	private void handleMovePercept(MovePercept p) {
		FigureInfo fig = p.getFigure();
		RoomInfo info = fig.getRoomInfo();
		DefaultAnimationSet set = AnimationUtils.getFigure_walking(fig);

		if(fig.equals(this.figure)) {
			// we reset the selected room, as hero has moved on
			screen.getFocusManager().setWorldFocusObject((RoomInfoEntity)null);
		}

		if (set != null) {
			// clear old queued animations if there are some
			animationManager.clearFigure(fig);

			// start "walk in" animation
			startAnimation(set, fig,  p);
		}

		if (!fig.equals(this.figure)
				&& // check whether a fight has just started
				!figure.getRoomInfo().fightRunning()) {
			screen.scrollTo(info.getNumber(), 0.4f, "move percept");
		}
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo figure, OpticalPercept percept) {
		Position.Pos pos = Position.Pos.fromValue(figure.getPositionInRoomIndex());
		startAnimation(ani, figure, pos, pos, "", false, false, false, percept, null);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo figure, String text, OpticalPercept percept) {
		Position.Pos pos = Position.Pos.fromValue(figure.getPositionInRoomIndex());
		startAnimation(ani, figure, pos, pos, text, false, false, false, percept, null);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo figure, String text, boolean delayed, boolean urgent, boolean postDelay, OpticalPercept percept, JDImageProxy delayImage) {
		Position.Pos pos = Position.Pos.fromValue(figure.getPositionInRoomIndex());
		startAnimation(ani, figure, pos, pos,  text, delayed, urgent, postDelay, percept, delayImage);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo figure, Position.Pos from, Position.Pos to, String text, boolean delayed, boolean urgent, boolean postDelay, OpticalPercept percept, JDImageProxy delayImage) {
		DefaultAnimationTask task = new DefaultAnimationTask(ani, text, figure, from, to, percept);
		task.setUrgent(urgent);
		animationManager.startAnimation(task, figure,  delayed, postDelay, delayImage);
	}

	private void handleDiePercept(DiePercept p) {
		FigureInfo deadFigure = p.getFigure();
		if(deadFigure.equals(this.figure)) {
			Music music = screen.getGame().getAudio().createMusic("music/" + "Dark_Times.mp3");
			MusicManager.getInstance().playMusic(music);
		}
		// we reset highlighted entity if a selected figure was killed
		if(deadFigure.equals(this.screen.getFocusManager().getWorldFocusObject())) {
			this.screen.getFocusManager().setWorldFocusObject((RoomInfoEntity)null);
		}

		newStatement(StatementManager.getStatement(p, figure));

		DefaultAnimationSet set = AnimationUtils.getFigure_tipping_over(deadFigure);
		if (set != null) {
			int damage = p.getDamage();
			String text = null;
			if (damage != -1) {
				text = "-" + damage;
			}
			// should better be delay = true to show the figure falling AFTER the hit, but the figure is rendered as dead by the default render process...not so nice though
			startAnimation(set, deadFigure, text, false, true, false, ((OpticalPercept)p), null);
		}

	}

	private void handleTumblingPercept(TumblingPercept p) {
		newStatement(StatementManager.getStatement(p, figure));
	}

	private void handleMissPercept(MissPercept p) {
		newStatement(StatementManager.getStatement(p, figure));

	}

	private void handleItemDroppedPercept(ItemDroppedPercept p) {
		FigureInfo user = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_using(user);

		if (set != null) {
			startAnimation(set, user, p);
		}

	}

	private void handleShieldBlockPercept(ShieldBlockPercept p) {
		newStatement(StatementManager.getStatement(p, figure));

	}

	private void handleBreakSpellPercept(BreakSpellPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleDoorSmashPercept(DoorSmashPercept p) {
		FigureInfo victim = p.getVictim();
		AudioEffectsManager.playSound(AudioEffectsManager.DOOR_SMASH);
		newStatement(StatementManager.getStatement(p, figure));

		if (p.getValue() > 0) {
			int damage = p.getValue();
			DefaultAnimationSet set = AnimationUtils.getFigure_been_hit(victim);
			if (set != null) {
				startAnimation(set, victim, "-" + damage, p);
			}
		}
	}

	private void handleWaitPercept(WaitPercept p) {
		FigureInfo fig = p.getFigure();
		DefaultAnimationSet set = AnimationUtils.getFigure_using(fig);

		// wait percept is not that exciting I guess..
		/*
		if (set != null) {
			startAnimation(set, fig,"",  (OpticalPercept)p );

		}
		*/
	}

	private void handleHitPercept(HitPercept p) {
		FigureInfo victim = p.getVictim();
		newStatements(StatementManager.getStatements(p, figure));

		if (p.getDamage() > 0) {
			int damage = p.getDamage();
			DefaultAnimationSet set = AnimationUtils.getFigure_been_hit(victim);
			if (set != null) {
				startAnimation(set, victim, "-" + damage, true, true, false, p, null);
			}
		}
	}

	private void handleAttackPercept(AttackPercept p) {
		FigureInfo fig = p.getAttacker();
		DefaultAnimationSet set = AnimationUtils.getFigure_slays(fig);

		if (set != null) {
			startAnimation(set, fig, p.getFromPos(), Position.Pos.fromValue(fig.getPositionInRoomIndex()),  null, false, false, false, p, null);
		}
	}



	public void newStatement(Statement s) {
		screen.getGuiRenderer().newStatement(s);
	}

	private void newStatements(List<Statement> statements) {
		for (Statement statement : statements) {
			newStatement(statement);
		}
	}
}
