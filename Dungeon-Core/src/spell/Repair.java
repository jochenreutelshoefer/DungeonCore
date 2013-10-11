package spell;
import item.*;
import game.JDEnv;
import item.equipment.EquipmentItem;
import figure.Figure;
import figure.percept.TextPercept;


/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Repair extends Spell implements TargetSpell {
	
//	public int [] diff = { 2 , 4 };
//	public int [] diffMin = { 5 , 10};
	
	public static int [][] values = { {5,2,5,10,1},
								{10,4,7,30,2}
								};
	
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof EquipmentItem) {
			return true;
		}
		return false;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}

	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	public Repair(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength,  lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;
		
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
//	public boolean fightModus(){
//		return isPossibleInFight;
//	}
//	
//	public boolean normalModus(){
//		return isPossibleNormal;
//	}
	
	public int getType() {
		return Spell.SPELL_REPAIR;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_rapair_text");
			return s;
		}
	public Repair(int level) {
			
		super(level,values[level-1]);
		
		isPossibleNormal = true;
		isPossibleInFight = false;
	}


//	public int getLernCost() {
//			return 1;
//		}
	/**
	 * @see Spell#getDifficulty(int)
	 */
//	public int getDifficulty(int level) {
//		return diff[level-1];
//	}
//	
//	public int getDifficultyMin(int level) {
//		return diffMin[level-1];
//	}

public String getName() {
		return JDEnv.getResourceBundle().getString("spell_rapair_name");
	}
	/**
	 * @see Spell#getCost(int)
	 */
	public int getCost(int level) {
		return 5*level;
	}
	
	
	
	

	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		//ItemChoiceView v = new ItemChoiceView(mage.getGame().getGui().getMainFrame(),"Gegenstand ausw�hlen",this,mage, false);
		if(target instanceof Item) {
			repairItem((Item)target);
			String str = JDEnv.getResourceBundle().getString("spell_rapair_cast");
			mage.tellPercept(new TextPercept(str));
		}
		
	}

	/**
	 * @see Spell#getName()
	 */
	
	public void repairItem(Item i) {
		if(i instanceof EquipmentItem) {
			System.out.println("reairing: "+i.toString());
			((EquipmentItem)i).repair(level*5);
		}
	}

}
