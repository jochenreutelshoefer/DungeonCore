/*
 * Created on 13.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;


import dungeon.Door;
import dungeon.HiddenSpot;
import dungeon.Room;
import figure.Figure;
import figure.percept.TextPercept;
import game.JDEnv;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Discover extends NoTargetSpell {
//	public int [] diff = { 3 , 13 };
//	public int [] diffMin = { 6 , 15};
	
	public static int [][] values = { {6,3,5,10,1},
								{15,10,7,30,2}
								};
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public Discover(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength,lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;
		
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public boolean isApplicable(Figure mage, Object target) {
		return true;
	}
	
	public Discover(int level) {
			
		super(level,values[level-1]);
		
		isPossibleNormal = true;
		isPossibleInFight = false;
	}
	
	@Override
	public int getType() {
		return Spell.SPELL_DISCOVER;
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	
	
	/**
	 * @see Spell#getDifficulty(int)
	 */
//	public int getDifficulty(int level) {
//		return diff[level-1];
//	}

	/**
	 * @see Spell#getDifficultyMin(int)
	 */
//	public int getDifficultyMin(int level) {
//		return diff[level-1];
//	}

	/**
	 * @see Spell#getCost(int)
	 */
//	public int getCost(int level) {
//		return level * 5;
//	}

	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	@Override
	public void sorcer(Figure mage) {
				
				Room r = mage.getRoom().getDungeon().getRoom(mage.getLocation());
				HiddenSpot spot = r.getSpot();
				boolean foundSomething = false;
				if(spot != null) {
					spot.setFound(true);
					
					String str = JDEnv.getResourceBundle().getString("spell_discover_spot");
					mage.tellPercept(new TextPercept(str));
					foundSomething = true;
				}
				Door[] doors = r.getDoors();
				for(int i = 0; i < 4 ; i++) {
					if(doors[i] != null && doors[i].isHidden()) {
						doors[i].setHidden(false);
						String str = JDEnv.getResourceBundle().getString("spell_discover_door");
						mage.tellPercept(new TextPercept(str));
						foundSomething = true;
					}
				}
				if(!foundSomething) {
					
					String str = JDEnv.getResourceBundle().getString("spell_discover_nothing");
					mage.tellPercept(new TextPercept(str));
				}
				
	}

	/**
	 * @see Spell#getName()
	 */
	@Override
	public String getName() {
		return  JDEnv.getResourceBundle().getString("spell_discover_name");
	}
	
	@Override
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_discover_text");
			return s;
		}
}