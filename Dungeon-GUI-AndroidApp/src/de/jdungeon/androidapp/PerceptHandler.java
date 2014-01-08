package de.jdungeon.androidapp;

import java.util.List;

import text.Statement;
import text.StatementManager;
import animation.AnimationSet;
import animation.AnimationUtils;
import audio.AudioEffectsManager;
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

public class PerceptHandler {

	private final GameScreen screen;

	private final FigureInfo figure;

	public PerceptHandler(GameScreen screen) {
		this.screen = screen;
		figure = FigureInfo.makeFigureInfo(screen.getHero(), screen.getHero()
				.getRoomVisibility());
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
			newStatement(s, 2);
		}
		if (p instanceof TextPercept) {
			newStatement(((TextPercept) p).getText(), 2);
		}
		if (p instanceof DisappearPercept) {
			newStatement(StatementManager.getStatement(((DisappearPercept) p),
					figure));
		}
		if (p instanceof FightEndedPercept) {
			handleFightEndedPercept((FightEndedPercept) p);
		}
		if (p instanceof FightBeginsPercept) {
			newStatement(StatementManager.getStatement((FightBeginsPercept) p));
		}
	}

	private void handleFightEndedPercept(FightEndedPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleFleePercept(FleePercept p) {
		FigureInfo fleeingFigure = p.getFigure();
		AnimationSet set = AnimationUtils.getFigure_running(fleeingFigure);
		if (set != null) {
			screen.startAnimation(set, fleeingFigure);
		}

	}

	private void handleUsePercept(UsePercept p) {
		FigureInfo user = p.getFigure();
		AnimationSet set = AnimationUtils.getFigure_using(user);
		if (set != null) {
			screen.startAnimation(set, user);
		}

	}

	private void handleTakePercept(TakePercept p) {
		FigureInfo taker = p.getFigure();
		AnimationSet set = AnimationUtils.getFigure_using(taker);
		if (set != null) {
			screen.startAnimation(set, taker);
		}

	}

	private void handleStepPercept(StepPercept p) {
		FigureInfo fig = p.getFigure();
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_walking(fig);

		int fromIndex = p.getFromIndex();
		int toIndex = p.getToIndex();

		if (set != null) {
			screen.startAnimation(set, fig);
		}

	}

	private void handleSpellPercept(SpellPercept p) {
		FigureInfo user = p.getFigure();
		AnimationSet set = AnimationUtils.getFigure_using(user);
		if (set != null) {
			screen.startAnimation(set, user);
		}

	}

	private void handleScoutPercept(ScoutPercept p) {
		FigureInfo user = p.getFigure();
		AnimationSet set = AnimationUtils.getFigure_using(user);
		if (set != null) {
			screen.startAnimation(set, user);
		}

	}

	private void handleMovePercept(MovePercept p) {
		FigureInfo fig = p.getFigure();
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_walking(fig);

		RoomInfo from = p.getFrom();
		RoomInfo to = p.getTo();

		if (set != null) {
			screen.startAnimation(set, fig);
		}

		if (fig.equals(this.figure)) {
			screen.scrollTo(info.getNumber(), 50f);
		}

	}

	private void handleDiePercept(DiePercept p) {
		FigureInfo deadFigure = p.getFigure();

		int damage = p.getDamage();
		AnimationSet set = AnimationUtils.getFigure_tipping_over(deadFigure);
		if (set != null) {
			if (damage != -1) {
				screen.startAnimation(set, deadFigure, "-" + damage);
			} else {
				screen.startAnimation(set, deadFigure);
			}
		}

	}

	private void handleTumblingPercept(TumblingPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleMissPercept(MissPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleItemDroppedPercept(ItemDroppedPercept p) {
		FigureInfo user = p.getFigure();
		AnimationSet set = AnimationUtils.getFigure_using(user);
		if (set != null) {
			screen.startAnimation(set, user);
		}

	}

	private void handleShieldBlockPercept(ShieldBlockPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleBreakSpellPercept(BreakSpellPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleDoorSmashPercept(DoorSmashPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleWaitPercept(WaitPercept p) {
		// TODO Auto-generated method stub

	}

	private void handleHitPercept(HitPercept p) {
		FigureInfo victim = p.getVictim();
		newStatements(StatementManager.getStatements(p, figure));
		if (p.getDamage() > 0) {

			RoomInfo info = victim.getRoomInfo();

			int damage = p.getDamage();

			AnimationSet set = AnimationUtils.getFigure_been_hit(victim);

			if (set != null) {
				screen.startAnimation(set, victim, "-" + damage);
			}
		}

	}

	private void handleAttackPercept(AttackPercept p) {
		FigureInfo fig = p.getAttacker();
		RoomInfo info = fig.getRoomInfo();
		AnimationSet set = AnimationUtils.getFigure_slays(fig);
		if (set != null) {
			screen.startAnimation(set, fig);
		}
	}

	private void newStatement(String text, int styleCode) {
		screen.newStatement(text, styleCode);
	}

	private void newStatement(Statement s) {
		screen.newStatement(s);
	}

	private void newStatements(List<Statement> statements) {
		for (Statement statement : statements) {
			newStatement(statement);
		}
	}

}