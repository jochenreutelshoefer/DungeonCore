/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.text;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.action.result.ActionResultWithGameRound;
import de.jdungeon.figure.percept.BreakSpellPercept;
import de.jdungeon.figure.percept.DiePercept;
import de.jdungeon.figure.percept.DisappearPercept;
import de.jdungeon.figure.percept.DoorSmashPercept;
import de.jdungeon.figure.percept.FightBeginsPercept;
import de.jdungeon.figure.percept.FightEndedPercept;
import de.jdungeon.figure.percept.FleePercept;
import de.jdungeon.figure.percept.HitPercept;
import de.jdungeon.figure.percept.InfoPercept;
import de.jdungeon.figure.percept.ItemDroppedPercept;
import de.jdungeon.figure.percept.MissPercept;
import de.jdungeon.figure.percept.EntersPercept;
import de.jdungeon.figure.percept.ShieldBlockPercept;
import de.jdungeon.figure.percept.SpellPercept;
import de.jdungeon.figure.percept.TakePercept;
import de.jdungeon.figure.percept.TumblingPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Texts;
import org.jetbrains.annotations.NotNull;

public class StatementManager {

	public static Statement getStatement(ActionResultWithGameRound actionResultWithGameRound) {
		return getStatement(actionResultWithGameRound.getResult(), actionResultWithGameRound.getRound());
	}

	public static Statement getStatement(ActionResult res, int round) {
		Statement s = new Statement("ActionResStatement", 0, round);
		if (res.getSituation() == ActionResult.Situation.impossible) {
			ActionResult.Reason k = res.getReason();
			if (k == ActionResult.Reason.noActionPoints) {
				s = new Statement(Texts.noAP(), 0, round);
			} else if (k == ActionResult.Reason.noDust) {
				s = new Statement(Texts.noDust(), 0, round);
			} else if (k == ActionResult.Reason.noItem) {
				s = new Statement(Texts.notWithThat(), 0, round);
			} else if (k == ActionResult.Reason.noKnowledge) {
				s = new Statement(Texts.noKnowledge(), 0, round);
			} else if (k == ActionResult.Reason.wrongPosition) {
				s = new Statement(Texts.wrongPosition(), 0, round);
			} else if (k == ActionResult.Reason.wrongTarget) {
				s = new Statement(Texts.wrongTarget(), 0, round);
			} else if (k == ActionResult.Reason.noTarget) {
				s = new Statement(Texts.noTarget(), 0, round);
			} else if (k == ActionResult.Reason.wrongDistance) {
				s = new Statement(JDEnv.getString("wrong_distance"), 0, round);
			} else if (res == ActionResult.WRONG_TARGET) {
				s = new Statement("Kein Platz da!", 0, round);
			} else {
				s = new Statement(Texts.doesNotWork(), 0, round);
			}
		}
		return s;
	}

	public static Statement getStatement(DoorSmashPercept p, FigureInfo to) {
		if (p.getVictim().equals(to)) {
			return new Statement(JDEnv.getResourceBundle().getString(
					"player_doorSmash") + appendDamageValue(p), 0, p.getRound());
		} else {
			String otherName = JDEnv.getResourceBundle().getString("somebody");
			if (p.getVictim().getName() != null) {
				otherName = p.getVictim().getName();
			}
			return new Statement(insertName(otherName, JDEnv
					.getResourceBundle().getString("other_doorSmash")+ appendDamageValue(p)), 0, p.getRound());
		}
	}

	@NotNull
	private static String appendDamageValue(DoorSmashPercept p) {
		return " (" + p.getValue() + ")";
	}

	public static Statement getStatement(DisappearPercept p, FigureInfo to) {
		Statement s = null;
		FigureInfo f = p.getFigure();
			s = new Statement(insertName(f.getName(), Texts.disappears()), 1, p.getRound());
		return s;
	}

	public static Statement getStatement(ShieldBlockPercept p, FigureInfo to) {
		if (p.getFigure().equals(to)) {
			return new Statement(Texts.playerShieldBlock(), 0, p.getRound());
		} else {
			String otherName = JDEnv.getResourceBundle().getString("somebody");
			if (p.getFigure().getName() != null) {
				otherName = p.getFigure().getName();
			}
			return new Statement(insertName(otherName, JDEnv
					.getResourceBundle().getString("other_shield_block")), 0, p.getRound());
		}
	}

	public static Statement getStatement(BreakSpellPercept p, FigureInfo to) {
		if (p.getFigure().equals(to)) {
			return new Statement(JDEnv.getString("player_spell_break"), 0, p.getRound());
		} else {
			String otherName = JDEnv.getResourceBundle().getString("somebody");
			if (p.getFigure().getName() != null) {
				otherName = p.getFigure().getName();
			}
			return new Statement(insertName(otherName, JDEnv
					.getResourceBundle().getString("other_spell_break")), 0, p.getRound());
		}
	}

	public static Statement getStatement(TakePercept p, FigureInfo to) {
		String itemString = p.getItem().toString();
		if (p.getFigure().equals(to)) {
			return new Statement(JDEnv.getResourceBundle().getString(
					"player_takes")
					+ ": " + itemString, 0, p.getRound());
		} else {

			String otherName = p.getFigure().getName();

			return new Statement(insertName(otherName, JDEnv
					.getResourceBundle().getString("other_takes"))
					+ ": " + itemString, 0, p.getRound());
		}
	}

	public static String getStatement(InfoPercept p) {
		String s = "invalid InfoPercept-code";
		int code = (p).getCode();
		if (code == InfoPercept.LOCKED_DOOR) {
			s = Texts.lockDoor;
		}
		if (code == InfoPercept.UNLOCKED_DOOR) {
			s = Texts.unlockDoor;
		}
		if (code == InfoPercept.LEVEL_UP) {
			s = Texts.levelUp();
		}
		if (code == InfoPercept.END_ROUND) {
			s = ("-----------");
		}
		if (code == InfoPercept.FOUND_ITEM) {
			s = Texts.foundItem();

		}
		if (code == InfoPercept.SPOTTED_FIGURES) {
			s = Texts.foundFigure();
		}
		if (code == InfoPercept.RESPAWN) {
			s = Texts.respawnByPrayer();
		}

		return s;
	}

	public static Statement getStatement(ItemDroppedPercept p) {
		return new Statement(Texts.otherDroppedItem(), 0, p.getRound());
	}

	public static Statement getStatement(FleePercept p, FigureInfo to) {
		Statement s = null;
		boolean success = p.isSuccess();
		if (success) {
			if (p.getFigure().equals(to)) {
				s = new Statement(Texts.playerFlees(), 2, p.getRound());
			} else {
				s = new Statement(insertName(p.getFigure().getName(), Texts
						.otherFlees()), 1, p.getRound());
			}
		} else {
			if (p.getFigure().equals(to)) {
				s = new Statement(Texts.playerFleesNot(), 2, p.getRound());
			} else {
				s = new Statement(insertName(p.getFigure().getName(), Texts
						.otherFleesNot()), 1, p.getRound());
			}
		}
		return s;
	}

	public static Statement getStatement(TumblingPercept p, FigureInfo to) {
		Statement s = null;
		if (p.getFigure().equals(to)) {
			s = new Statement(Texts.playerTumbles, 2, p.getRound());
		} else {
			s = new Statement(insertName(p.getFigure().getName(),
					Texts.otherTumbles), 1, p.getRound());
		}
		return s;
	}

	public static Statement getStatement(EntersPercept p, FigureInfo to) {
		Statement s = null;
		if (p.getFigure().equals(to)) {
			s = new Statement("entering room", 2, p.getRound());
		} else {
			s = new Statement(insertName(p.getFigure().getName(), JDEnv
					.getResourceBundle().getString("other_moves_in")), 1, p.getRound());
		}
		return s;
	}

	public static Statement getStatement(SpellPercept p, FigureInfo to) {
		Statement s = null;
		if (p.isBegins()) {
			if (p.getFigure().equals(to)) {
				s = new Statement(JDEnv.getString("player_starts_spell")
						+ p.getSpell().getName(), 2, p.getRound());
			} else {
				s = new Statement(insertName(p.getFigure().getName(), JDEnv
						.getResourceBundle().getString("other_starts_spell")+ p.getSpell().getName()),
						1, p.getRound());
			}
			return s;
		} else {
			if (p.getFigure().equals(to)) {
				s = new Statement(JDEnv.getString("player_spells")
						+ p.getSpell().getName(), 2, p.getRound());
			} else {
				s = new Statement(insertName(p.getFigure().getName(), JDEnv
						.getResourceBundle().getString("other_spells"))
						+ p.getSpell().getName(), 1, p.getRound());
			}
			return s;
		}
	}

	public static Statement getStatement(MissPercept p, FigureInfo to) {
		Statement s = null;
		FigureInfo f = p.getAttacker();
		if (f.equals(to)) {
			s = new Statement(Texts.playerMisses(), 2, p.getRound());
		} else {
			if (p.getVictim().equals(to)) {
				s = new Statement(insertName(f.getName(), Texts
						.otherMissesPlayer()), 1, p.getRound());
			} else {
				s = new Statement(insertNames(f.getName(), p.getVictim()
						.getName(), Texts.otherMissesOther), 1, p.getRound());
			}
		}
		return s;
	}

	private static String insertName(String name, String statement) {
		if (name != null) {
			statement = statement.replaceAll("[$][n][a][m][e]", name);
		}

		return statement;
	}

	private static String insertNames(String name1, String name2,
			String statement) {
		if (name1 != null) {
			statement = statement.replaceAll("[$][n][a][m][e][1]", name1);
		}
		if (name2 != null) {
			statement = statement.replaceAll("[$][n][a][m][e][2]", name2);
		}

		return statement;
	}

	public static Statement getStatement(DiePercept p, FigureInfo to) {
		Statement s = null;
		FigureInfo f = p.getFigure();
		if (f.equals(to)) {
			s = new Statement(Texts.playerDies(), 2, p.getRound());
		} else {
			s = new Statement(insertName(f.getName(), Texts.otherDies()), 1, p.getRound());
		}
		return s;
	}

	public static Statement getStatement(FightBeginsPercept p) {
		return new Statement(Texts.fightBegins(), 2, p.getRound());
	}

	public static Statement getStatement(FightEndedPercept p) {
		return new Statement(Texts.fightEnded(), 2, p.getRound());
	}

	public static List<Statement> getStatements(HitPercept p, FigureInfo to) {
		List<Statement> l = new LinkedList<Statement>();

		FigureInfo attacker = p.getAttacker();
		FigureInfo victim = p.getVictim();
		if (p.getStandardDamage() > 0) {
			Statement s = null;
			if (victim.equals(to)) {
				if (attacker != null) {
					s = new Statement(insertName(attacker.getName(), Texts
							.otherHitsPlayer())
							+ "(" + p.getDamage() + ")", 1, p.getRound());

				}

			} else if (attacker.equals(to)) {
				s = new Statement(insertName(victim.getName(), Texts
						.playerHits())
						+ "! (" + p.getDamage() + ")", 2, p.getRound());
			} else {
				s = new Statement(insertNames(attacker.getName(), victim
						.getName(), Texts.otherHitsOther + "(" + p.getDamage()
						+ ")"), 1, p.getRound());
			}
			l.add(s);
		}

		if (p.getLightningDamage() > 0) {
			Statement lightningStatement = null;
			if (victim.equals(to)) {
				lightningStatement = new Statement(JDEnv
						.getString("player_suffers_lightning")
						+ "(" + p.getLightningDamage() + ")", 1, p.getRound());
			} else {
				lightningStatement = new Statement(insertName(victim.getName(),
						JDEnv.getString("other_suffers_lightning") + "("
								+ p.getLightningDamage() + ")"), 2, p.getRound());
			}
			l.add(lightningStatement);
		}
		if (p.getFireDamage() > 0) {
			Statement FireStatement = null;
			if (victim.equals(to)) {
				FireStatement = new Statement(JDEnv
						.getString("player_suffers_fire")
						+ "(" + p.getFireDamage() + ")", 1, p.getRound());
			} else {
				FireStatement = new Statement(insertName(victim.getName(),
						JDEnv.getString("other_suffers_fire") + "("
								+ p.getFireDamage() + ")"), 2, p.getRound());
			}
			l.add(FireStatement);
		}
		if (p.getMagicDamage() > 0) {
			Statement MagicStatement = null;
			if (victim.equals(to)) {
				MagicStatement = new Statement(JDEnv
						.getString("player_suffers_magic")
						+ "(" + p.getMagicDamage() + ")", 1, p.getRound());
			} else {
				MagicStatement = new Statement(insertName(victim.getName(),
						JDEnv.getString("other_suffers_magic") + "("
								+ p.getMagicDamage() + ")"), 2, p.getRound());
			}
			l.add(MagicStatement);
		}
		if (p.getPoisonDamage() > 0) {
			Statement PoisonStatement = null;
			if (victim.equals(to)) {
				PoisonStatement = new Statement(JDEnv
						.getString("player_suffers_poison")
						+ "(" + p.getPoisonDamage() + ")", 1, p.getRound());
			} else {
				PoisonStatement = new Statement(insertName(victim.getName(),
						JDEnv.getString("other_suffers_poison") + "("
								+ p.getPoisonDamage() + ")"), 2, p.getRound());
			}
			l.add(PoisonStatement);
		}

		return l;
	}


}