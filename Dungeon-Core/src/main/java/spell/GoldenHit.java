/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;



import dungeon.RoomEntity;
import figure.Figure;
import figure.FigureInfo;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;
import game.RoomInfoEntity;

public class GoldenHit extends AbstractTargetSpell implements TargetSpell{
	
	public int [] diff = { 1 , 4 };
	public int [] diffMin = { 5 , 8};
	
	public static int [][] values = { {1,1,5,10,1},
								{7,5,5,20,2}
								};
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public GoldenHit(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	public GoldenHit() {
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
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
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}
	

	
	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_GOLDENHIT;
	}
	
	@Override
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_goldenHit_text");
			return s;
		}
	
//	public int getLernCost() {
//			return level;
//		}
	
	public GoldenHit(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_goldenHit_name");
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	
	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {
		
				mage.setGolden_hit(level);
				//setzt nur die Trefferwahrscheinlichkeit f�r den n�chsten Schlag hoch
				
				//und l�st dann einen Schlag aus
				String str = JDEnv.getResourceBundle().getString("spell_goldenHit_cast");
				mage.tellPercept(new TextPercept(str, round));
				
				if(target instanceof Figure){
				mage.attack((Figure)target, round);
				}
				else {
					System.out.println("kein fighter als target bei golden_hit!");
				}
			
				
	}
	
	
	@Override
	public String toString(){
		return getName();
	}
		

}
