package de.jdungeon.world;

import java.util.List;

import de.jdungeon.animation.AnimationManager;
import de.jdungeon.animation.AnimationUtils;
import de.jdungeon.animation.DefaultAnimationSet;
import de.jdungeon.animation.DefaultAnimationTask;
import de.jdungeon.audio.AudioEffectsManager;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.percept.*;
import de.jdungeon.figure.PerceptHandler;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.graphics.GraphicObjectRenderer;
import de.jdungeon.graphics.JDGraphicObject;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.log.Log;
import de.jdungeon.skill.EagleOwlSkill;
import de.jdungeon.skill.Skill;
import de.jdungeon.text.Statement;
import de.jdungeon.text.StatementManager;

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

    private void handlePercept(Percept p) {

        if (p instanceof LocationStateChangePercept) {
            screen.invalidateEntityRenderCache(((LocationStateChangePercept) p).getLocation(figure));
        }

        if (p instanceof InterruptPercept) {
            // this is a special case, no world percept, but only communication between PlayerController and PerceptHandler
            // to visualize an interrupt in the players action plan
            handleInterruptPercept((InterruptPercept) p);
        }

        if (p instanceof SkillActionPercept) {
            handleSkillActionPercept((SkillActionPercept) p);
        }

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
        if (p instanceof AttackPercept) {
            handleAttackPercept((AttackPercept) p);
        }
        if (p instanceof HitPercept) {
            handleHitPercept((HitPercept) p);
        }
        if (p instanceof DiePercept) {
            handleDiePercept((DiePercept) p);
        }
        if (p instanceof EntersPercept) {
            handleEntersPercept((EntersPercept) p);
        }
        if (p instanceof LeavesPercept) {
            handleLeavePercept((LeavesPercept) p);
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
            newStatement(new Statement(s, 2, p.getRound()));
        }
        if (p instanceof TextPercept) {
            newStatement(new Statement(((TextPercept) p).getText(), 2, p.getRound()));
        }
        if (p instanceof DisappearPercept) {
            newStatement(StatementManager.getStatement(((DisappearPercept) p),
                    figure));
        }
        if (p instanceof FightEndedPercept) {
            handleFightEndedPercept((FightEndedPercept) p);
        }
    }

    private void handleSkillActionPercept(SkillActionPercept percept) {
        Skill skill = percept.getSkill();
        if (skill instanceof EagleOwlSkill) {
            playUseAnimation(percept, percept.getFigure(this.figure));
            AudioEffectsManager.playSound(AudioEffectsManager.OWL_HOOT);
        }
    }

    private void handleInterruptPercept(InterruptPercept p) {
        GraphicObjectRenderer objectRenderer = screen.getGraphicObjectRenderer();
        JDGraphicObject heroGraphicObject = objectRenderer.getHeroGraphicObject((HeroInfo) this.figure);
        JDImageProxy<?> image = heroGraphicObject.getLocatedImage().getImage();
        // constant image, as we just want to display the exclamation mark de.jdungeon.text
        JDImageProxy[] images = {image, image, image, image,};
        int[] times = {70, 70, 70, 70};
        DefaultAnimationSet ani = new DefaultAnimationSet(images, times);
        startAnimation(ani, this.figure, "   !", p);
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

        if (p.isSuccess()) {
            AudioEffectsManager.playSound(AudioEffectsManager.FOOTSTEPS_QUICK_FADING);
            AudioEffectsManager.playSound(AudioEffectsManager.DOOR_CLOSE);
        } else {
            AudioEffectsManager.playSound(AudioEffectsManager.FOOTSTEPS_QUICK);
        }

        FigureInfo fleeingFigure = p.getFigure(this.figure);
        DefaultAnimationSet set = AnimationUtils.getFigure_running(fleeingFigure);
        if (set != null) {
            startAnimation(set, fleeingFigure, p);
        }
        // todo:
		/*
		if (fleeingFigure.equals(this.de.jdungeon.figure) && p.isSuccess()) {
			screen.exitFightMode();
		}
		*/
    }

    private void handleUsePercept(UsePercept p) {
        FigureInfo user = p.getFigure(this.figure);
        playUseAnimation(p, user);
    }

    private void playUseAnimation(OpticalPercept p, FigureInfo user) {
        DefaultAnimationSet set = AnimationUtils.getFigure_using(user);
        if (set != null) {
            startAnimation(set, user, p);
        }
    }

    private void handleTakePercept(TakePercept p) {
        FigureInfo taker = p.getFigure(this.figure);

        // todo:
		/*
		if(taker.equals(this.de.jdungeon.figure)) {
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
        FigureInfo fig = p.getFigure(this.figure);
        if (fig != null) {
            DefaultAnimationSet set = AnimationUtils.getFigure_walking(fig);

            if (set != null) {
                startAnimation(set, fig, Position.Pos.fromValue(p.getFromIndex()), Position.Pos.fromValue(p.getToIndex()), "", false, true, false, p, null);
            }
        } else {
            Log.severe("de.jdungeon.figure for StepPercept was null!");
        }
    }

    private void handleSpellPercept(SpellPercept p) {
        FigureInfo user = p.getFigure(this.figure);
        DefaultAnimationSet set = AnimationUtils.getFigure_using(user);

        if (set != null) {
            startAnimation(set, user, p);
        }
    }

    private void handleScoutPercept(ScoutPercept p) {
        FigureInfo user = p.getFigure(this.figure);
        DefaultAnimationSet set = AnimationUtils.getFigure_using(user);

        if (set != null) {
            startAnimation(set, user, p);
        }
    }

    private void handleEntersPercept(EntersPercept p) {
        FigureInfo fig = p.getFigure(this.figure);
        RoomInfo info = fig.getRoomInfo();
        DefaultAnimationSet set = AnimationUtils.getFigure_walking(fig);

        if (fig.equals(this.figure)) {
            // we reset the selected room, as hero has moved on
            screen.getFocusManager().setWorldFocusObject((RoomInfoEntity) null);
        } else {
            AudioEffectsManager.playSound(AudioEffectsManager.DOOR_ENTERS);
        }

        if (set != null) {
            // clear old queued animations if there are some
            animationManager.clearFigure(fig);

            // start "walk in" de.jdungeon.animation
            startAnimation(set, fig, p);
        }

        RoomInfo roomInfo = figure.getRoomInfo();
        Boolean fightRunning = roomInfo.fightRunning();
        if (!fig.equals(this.figure)
                && // check whether a de.jdungeon.fight has just started
                fightRunning != null && !fightRunning) {
            screen.scrollTo(info.getNumber(), 0.4f, "enters percept");
        }
    }

    private void handleLeavePercept(LeavesPercept p) {
        FigureInfo fig = p.getFigure(this.figure);
        RoomInfo info = fig.getRoomInfo();
        DefaultAnimationSet set = AnimationUtils.getFigure_walking(fig);

        if (fig.equals(this.figure)) {
            // we reset the selected room, as hero has moved on
            screen.getFocusManager().setWorldFocusObject((RoomInfoEntity) null);
        }

        if (set != null) {
            // clear old queued animations if there are some
            animationManager.clearFigure(fig);

            // start "walk in" de.jdungeon.animation
            startAnimation(set, fig, p);
        }

        if (!fig.equals(this.figure)
                && // check whether a de.jdungeon.fight has just started
                !figure.getRoomInfo().fightRunning()) {
            screen.scrollTo(info.getNumber(), 0.4f, "leaves percept");
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
        startAnimation(ani, figure, pos, pos, text, delayed, urgent, postDelay, percept, delayImage);
    }

    public void startAnimation(DefaultAnimationSet ani, FigureInfo figure, Position.Pos from, Position.Pos to, String text, boolean delayed, boolean urgent, boolean postDelay, OpticalPercept percept, JDImageProxy delayImage) {
        DefaultAnimationTask task = new DefaultAnimationTask(ani, text, figure, from, to, percept);
        task.setUrgent(urgent);
        animationManager.startAnimation(task, figure, delayed, postDelay, delayImage);
    }

    private void handleDiePercept(DiePercept p) {
        FigureInfo deadFigure = p.getFigure(this.figure);
        if (deadFigure.equals(this.figure)) {
            Music music = screen.getGame().getAudio().createMusic("Dark_Times.mp3");
            MusicManager.getInstance().playMusic(music);
        }
        // we reset highlighted entity if a selected de.jdungeon.figure was killed
        if (deadFigure.equals(this.screen.getFocusManager().getWorldFocusObject())) {
            this.screen.getFocusManager().setWorldFocusObject((RoomInfoEntity) null);
        }

        newStatement(StatementManager.getStatement(p, figure));

        DefaultAnimationSet set = AnimationUtils.getFigure_tipping_over(deadFigure);
        if (set != null) {
            int damage = p.getDamage();
            String text = null;
            if (damage != -1) {
                text = "-" + damage;
            }
            // should better be delay = true to show the de.jdungeon.figure falling AFTER the hit, but the de.jdungeon.figure is rendered as dead by the default render process...not so nice though
            startAnimation(set, deadFigure, text, false, true, false, ((OpticalPercept) p), null);
        }
    }

    private void handleMissPercept(MissPercept p) {
        newStatement(StatementManager.getStatement(p, figure));
    }

    private void handleItemDroppedPercept(ItemDroppedPercept p) {
        FigureInfo user = p.getFigure(this.figure);
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
        FigureInfo victim = p.getVictim(this.figure);
        AudioEffectsManager.playSound(AudioEffectsManager.DOOR_SMASH);
        AudioEffectsManager.playSound(AudioEffectsManager.DOOR_SOUND);
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
        FigureInfo fig = p.getFigure(this.figure);
        DefaultAnimationSet set = AnimationUtils.getFigure_using(fig);

        // wait percept is not that exciting I guess..
		/*
		if (set != null) {
			startAnimation(set, fig,"",  (OpticalPercept)p );

		}
		*/
    }

    private void handleHitPercept(HitPercept p) {
        FigureInfo victim = p.getVictim(this.figure);
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
        FigureInfo fig = p.getAttacker(this.figure);
        DefaultAnimationSet set = AnimationUtils.getFigure_slays(fig);

        if (set != null) {
            startAnimation(set, fig, p.getFromPos(), Position.Pos.fromValue(fig.getPositionInRoomIndex()), null, false, false, false, p, null);
        }

        if(p.getVictim(this.figure).equals(figure)) {
            this.screen.getFocusManager().setWorldFocusObject(p.getAttacker(this.figure));
        }
    }

    private void newStatement(Statement s) {
        screen.getGuiRenderer().newStatement(s);
    }

    private void newStatements(List<Statement> statements) {
        for (Statement statement : statements) {
            newStatement(statement);
        }
    }
}
