package spell;
import dungeon.Door;
import dungeon.DoorInfo;
import figure.Figure;
import figure.percept.TextPercept;
import game.Game;
import game.JDEnv;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KeyLocator extends Spell implements TargetSpell {

	public static int [][] values = { {3,4,5,10,1},
								{6,12,7,30,1}
								};
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public KeyLocator(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;
		
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public int getType() {
		return Spell.SPELL_KEYLOCATOR;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_keyLocator_text");
			return s;
	}
	public KeyLocator(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = true;
		isPossibleInFight = false;
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Door) {
			return true;
		}
		return false;
	}

	public int getLernCost() {
			return 1;
		}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}

	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		if(target instanceof Door) {
			tellDirection(((Door)target),mage);
		}
				
	}

	public void tellDirection(Door d,Figure f) {
		
		if(d != null && d.hasLock()) {
			f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("spell_keyLocator_cast_found")+": "+d.getKey().getOwner().getLocation().toString()));
		}
		else {
			f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("spell_keyLocator_cast_nothing")));
		}
			
			
	}
	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_keyLocator_name");
	}

}
