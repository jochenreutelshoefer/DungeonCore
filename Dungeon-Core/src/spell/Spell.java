package spell;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.action.result.ActionResult;
import figure.percept.Percept;
import figure.percept.SpellPercept;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;
import gui.Paragraph;
import gui.Texts;

import java.util.LinkedList;
import java.util.List;

import spell.conjuration.FirConjuration;
import util.JDColor;
import dungeon.Position;

/**
 * Abstrakte Oberklasse aller Zaubersprueche In fire() wird geprueft ob die
 * Figur die den Zauberspruch ausfuehren will prinzipiell genug Wissen
 * (Psyche-Wert) hat und ob sie genug Zauberstaub hat und ob der Zauberspruch
 * jetzt im Moment ueberhaupt anwendbar ist. In canFire() wird dann
 * zufallsbasiert ausgewuerfelt obs jetzt konkret geklappt hat. Erst dann wird
 * sorcer() aufgerufen. Dort sind in den Unterklassen dann die Modifikationen an
 * der Umwelt implementiert.
 * 
 * Jeder Zauberspruch hat einen Mindestwissen-Wert (muss mit Psyche
 * ueberschritten werden, damit eine Figur ihn versuchen darf), einen
 * Schwierigkeitswert (wirkt sich auf die Wahrscheinlichkeit des gelingens aus)
 * und einen Kosten-Wert (Menge an Zauberstaub).
 */
public abstract class Spell {
	
	
	public static final int SPELL_BONEBREAKER = 1;
	public static final int SPELL_CONVINCE = 2;
	public static final int SPELL_DISCOVER = 3;
	public static final int SPELL_ESCAPE = 4;
	public static final int SPELL_FIREBALL = 5; 
	public static final int SPELL_GOLDENHIT = 6;
	public static final int SPELL_GOLDENTHROW = 7;  
	public static final int SPELL_HEAL = 8 ;
	public static final int SPELL_ISOLATION = 9; 
	public static final int SPELL_KEYLOCATOR = 10;
	public static final int SPELL_LIGHT = 11 ;
	public static final int SPELL_RAID = 12;
	public static final int SPELL_SPY = 13;
	public static final int SPELL_STEAL = 14;
	public static final int SPELL_THUNDERSTORM = 15;
	public static final int SPELL_REPAIR = 16;
	public static final int SPELL_SEARCH = 17;	
	public static final int SPELL_THREAT = 18;	
	public static final int SPELL_COBWEB = 19;
	public static final int SPELL_ESCAPEROUTE = 20;
	public static final int SPELL_POISONING = 21;
	public static final int SPELL_RUST = 22;
	public static final int SPELL_TRIPLEATTACK = 23;
	public static final int SPELL_MIGHTYSTRUCK = 24;
	public static final int SPELL_STEALORC = 25;
	public static final int SPELL_PRAYER = 26;
	public static final int SPELL_FIR = 27;
	public static final int SPELL_LIONESS = 28;
	
	//public static int[][] values = { { -1, -1, -1, -1 }, { -1, -1, -1, -1 } };

	public static List<TimedSpellInstance> timedSpells = new LinkedList<TimedSpellInstance>();
	
	protected int[] valueSet = new int[5];

	public static void addTimedSpell(TimedSpellInstance s) {
		timedSpells.add(s);

	}

	public static void removeTimedSpell(TimedSpellInstance s) {
		timedSpells.remove(s);
	}
	
	//protected int [] configValues;
	
	public int[] getConfigValues() {
		return valueSet;
	}

	protected int level;

	protected int diff;

	protected int diffMin;

	protected int worth;

	protected int strength;

	protected int fixCost = -1;

	protected int cost;

	private static String spell = null;
	
	private boolean costsAP = true;

	public void setCostsAP(boolean costsAP) {
		this.costsAP = costsAP;
	}

	public static String spell() {
		if (spell == null) {
			spell = JDEnv.getResourceBundle().getString("spell");
		}
		return spell;
	}

	public Spell() {

	}
	
	public abstract int getType();

	public Spell(int level, int diffMin, int diff, int cost, int strength,int learnCost) {
		this.level = level;
		this.diffMin = diffMin;
		this.diff = diff;
		this.cost = cost;
		this.strength = strength;
		valueSet[0]=diffMin;
		valueSet[1]=diff;
		valueSet[2]=cost;
		valueSet[3]=strength;
		valueSet[4]=learnCost;

	}

	public InfoEntity getInfoObject(DungeonVisibilityMap map) {
		return new SpellInfo(this, map);
	}

	public Spell(int level, int[] values) {
		this.level = level;
		this.diffMin = values[0];
		this.diff = values[1];
		this.cost = values[2];
		this.strength = values[3];
		valueSet = values;
	}

	public int getLernCost() {
		return this.getConfigValues()[4];
	}

	public static Spell getSpell(String s) {
		if (s.equals("repair")) {
			return new Repair(1);
		} else if (s.equals("heal")) {
			return new Heal(1);
		} else if (s.equals("escape")) {
			return new Escape(1);
		} else if (s.equals("golden_hit")) {
			return new GoldenHit(1);
		} else if (s.equals("spy")) {
			return new Spy(1);
		} else if (s.equals("thunderstorm")) {
			return new Thunderstorm(1);
		} else if (s.equals("fireball")) {
			return new Fireball(1);
		} else if (s.equals("bonebreaker")) {
			return new Bonebreaker(1);
		} else if (s.equals("key_locator")) {
			return new KeyLocator(1);
		} else if (s.equals("discover")) {
			return new Discover(1);
		} else if (s.equals("golden_throw")) {
			return new GoldenThrow(1);
		} else if (s.equals("steal")) {
			return new Steal(1);
		} else if (s.equals("search")) {
			return new Search(1);
		} else if (s.equals("isolation")) {
			return new Isolation(1);
		} else if (s.equals("escapeRoute")) {
			return new EscapeRoute(1);
		} else if (s.equals("fir")) {
			return new FirConjuration(1);
		} else
			return null;
	}

	@Override
	public String toString() {
		return getName() + " " + Integer.toString(level);
	}

	public abstract String getText();

	public boolean canFire(Figure mage) {
		int diff = getDifficulty();
		int diffMin = getDifficultyMin();
		double psy = mage.getPsycho().getValue();
		double k = (Math.random() * psy);
		if (k < diff) {

			String str = JDEnv.getResourceBundle().getString("spell_failed");
			mage.tellPercept(new TextPercept(str));

			return false;
		}

		return true;
	}

	public boolean rightModus(Figure mage) {
		if (!mage.getRoom().fightRunning()) {
			if (isPossibleNormal()) {
				return true;
			}
		} else {
			if (isPossibleFight()) {
				return true;
			}
		}
		return false;
	}

	public abstract boolean isApplicable(Figure mage, Object target);
		

	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[5];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();

		p[1] = new Paragraph(JDEnv.getResourceBundle().getString("level")
				+ ": " + getLevel());
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();

		p[2] = new Paragraph(JDEnv.getResourceBundle().getString("cost") + ": "
				+ getCost());
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);

		p[3] = new Paragraph(JDEnv.getResourceBundle().getString(
				"spell_difficulty")
				+ ": " + getDifficulty());
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(JDColor.black);

		p[4] = new Paragraph(JDEnv.getResourceBundle().getString(
				"spell_min_wisdom")
				+ ": " + getDifficultyMin());
		p[4].setSize(14);
		p[4].setCentered();
		p[4].setColor(JDColor.black);

		return p;
	}

	// public abstract int getDifficulty();

	public int getDifficultyMin() {
		return this.getConfigValues()[0];
	}

//	public boolean fightModus() {
//		return isPossibleInFight;
//	}
//
//	public boolean normalModus() {
//		return isPossibleNormal;
//	}

	public int getCost() {
		return this.getConfigValues()[2];
	}
	
	public int getStrength() {
		return this.getConfigValues()[3];
	}

	public void setCost(int k) {
		fixCost = k;
	}

//	public boolean isFight() {
//		return fightModus();
//	}

	/**
	 * Returns the level.
	 * 
	 * @return int
	 * 
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns the normal.
	 * 
	 * @return boolean
	 * 
	 */
//	public boolean isNormal() {
//		return normalModus();
//	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isAbleToCast(Figure mage) {
		double psy = mage.getPsycho().getValue();
		if (psy < getDifficultyMin()) {
			return false;

		}
		if(mage.getRoom().fightRunning()) {
			if(!this.isPossibleFight()) {
				return false;
			}
		}else {
			if(!this.isPossibleNormal()) {
				return false;
			}
		}
		return true;
	}
	
	public abstract boolean isPossibleNormal();
	public abstract boolean isPossibleFight();
	
	
	private int calcCost() {
		int c;
		if (fixCost == -1) {
			c = getCost();
			// System.out.println("getCost(): "+c);
		} else {
			c = fixCost;
			// System.out.println("fix_cost!: "+c);
		}
		return c;
	}
	
	public static boolean distanceMax(Figure mage, Object target,int max) {
		if(target instanceof Figure) {
			Figure victim = ((Figure)target);
			if(mage.getRoom() == victim.getRoom()) {
				int magePos = mage.getPositionInRoom();
				int victimPos = victim.getPositionInRoom();
				int dist = Position.getMinDistanceFromTo(magePos,victimPos);
				if(dist <= max) {
					return true;
				}
			}
		}
		return false;
	}
	
	

	public ActionResult fire(Figure mage, Object target, boolean doIt) {

		double psy = mage.getPsycho().getValue();
		if (psy < getDifficultyMin()) {
			String str = JDEnv.getResourceBundle().getString("spell_no_wisdom");
			mage.tellPercept(new TextPercept(str));
			// mage.getGame().getMain().log("Psyche kleiner
			// Minimalanforderung!", 20);

			return ActionResult.KNOWLEDGE;
		}
		if (!isApplicable(mage, target)) {
			String str = JDEnv.getResourceBundle().getString(
					"spell_wrong_target");
			mage.tellPercept(new TextPercept(str));
			return ActionResult.TARGET;
		}
		if(this instanceof TargetSpell) {
			if(!((TargetSpell)this).distanceOkay(mage,target)) {
				return ActionResult.DISTANCE;
			}
		}
		
		int d = (int) mage.getDust().getValue();
		int c = calcCost();

		if (rightModus(mage)) {
			if (d >= c) {

				if (doIt) {
					if(costsAP) {
						mage.payActionPoint();
					}
					if (canFire(mage)) {				
						
						sorcerStep(mage, target);
						
						return ActionResult.DONE;
					}else {
						if(this.stepsNec == 1) {
							mage.resetLastSpell();
						}
						return ActionResult.FAILED;
					}
					
					

				}
				return ActionResult.POSSIBLE;

			} else {

				String str = Texts.noDust();
				mage.tellPercept(new TextPercept(str));
				return ActionResult.DUST;
			}
		} else {
			String str = Texts.notNow();
			mage.tellPercept(new TextPercept(str));
			return ActionResult.MODE;
		}

	}
	
	protected int stepsNec = 1;
	private int stepCnt = 0;
	
	protected void sorcerStep(Figure mage, Object target) {
		stepCnt++;
		if(stepCnt == stepsNec) {
			payAndSorcer(mage,target);
		}else {
			Percept p = new SpellPercept(mage, this,true);
			mage.getRoom().distributePercept(p);
		}
	}
	
	public void resetSpell() {
		stepCnt = 0;
	}
	
	private void payAndSorcer(Figure mage, Object target) {
		int c = calcCost();
		mage.getDust().modValue(c * (-1));
		Percept p = new SpellPercept(mage, this);
		mage.getRoom().distributePercept(p);
		sorcer(mage,target);
		mage.resetLastSpell();
		stepCnt = 0;
	}

	public abstract void sorcer(Figure mage, Object target);
	
	public abstract Class<? extends InfoEntity> getTargetClass();

	/**
	 * Returns the difficulty.
	 * 
	 * @return int
	 * 
	 */
	public int getDifficulty() {
		return this.getConfigValues()[1];
	}

	/**
	 * Returns the worth.
	 * 
	 * @return int
	 * 
	 */
	public int getWorth() {
		return worth;
	}

	public abstract String getName();

	/**
	 * Sets the difficulty.
	 * 
	 * @param difficulty
	 *            The difficulty to set
	 * 
	 */
	public void setDifficulty(int difficulty) {
		this.diff = difficulty;
	}

	/**
	 * Sets the worth.
	 * 
	 * @param worth
	 *            The worth to set
	 * 
	 */
	public void setWorth(int worth) {
		this.worth = worth;
	}

	/**
	 * Returns the fix_cost.
	 * 
	 * @return int
	 * 
	 */
	public int getFix_cost() {
		return fixCost;
	}

	/**
	 * Sets the fix_cost.
	 * 
	 * @param fix_cost
	 *            The fix_cost to set
	 * 
	 */
	public void setFix_cost(int fix_cost) {
		this.fixCost = fix_cost;
	}

	public int getStepCnt() {
		return stepCnt;
	}

}
