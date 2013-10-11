/*
 * Created on 08.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;


import dungeon.*;
import figure.Figure;
import game.JDEnv;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Raid extends Spell implements TargetSpell {

//	public int[] diff = { 4, 4 };
//	public int[] diffMin = { 7, 8 };

	public static int[][] values = { { 7, 3, 6, 10,1 }, {
			7, 5, 5, 20,2 }
	};

	private boolean isPossibleNormal;
	private boolean isPossibleInFight;

	public Raid(
		int level,
		int diffMin,
		int diff,
		int cost,
		int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight =false;

	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_raid_text");
			return s;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

//	public int getLernCost() {
//		return level;
//	}
	
	public int getType() {
		return Spell.SPELL_RAID;
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}

	public Raid(int level) {

		super(level, values[level - 1]);
		this.level = level;
		isPossibleNormal = true;
		isPossibleInFight =false;
	}

	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_raid_name");
	}

	public boolean isApplicable(Figure mage, Object o) {
		if (o instanceof Figure) {
			Room targetRoom = ((Figure) o).getRoom();
			Room mageRoom = mage.getRoom();
			if (mageRoom.hasOpenConnectionTo(targetRoom)) {
				return true;
			}
		}
		return false;
	}

	//		public int getDifficulty(int level) {
	//			return diff[level - 1];	
	//		}

	//		public int getDifficultyMin(int level) {
	//			return diffMin[level - 1];	
	//		}

	//		public int getCost(int level) {
	//			return 5*level;
	//		}

	public void sorcer(Figure mage, Object target) {
		
		
		Room targetRoom = ((Figure) target).getRoom();
		Room mageRoom = mage.getRoom();

		Door d = mageRoom.getConnectionTo(targetRoom);
		int dir = mageRoom.getDir(d);
		mage.makeRaid((Figure)target);
		
		mage.walk(dir);
		
	}

	public String toString() {
		return getName();
	}

}
