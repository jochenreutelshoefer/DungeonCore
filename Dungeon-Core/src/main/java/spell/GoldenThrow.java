package spell;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;




public class GoldenThrow extends AbstractTargetSpell implements TargetSpell{
	
	public static int [][] values = { {5,3,5,10,1},
								{7,5,5,20,2}
								};
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public GoldenThrow(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	
	@Override
	public Class<? extends InfoEntity> getTargetClass() {
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
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_GOLDENTHROW;
	}
	
	@Override
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_goldenThrow_text");
			return s;
		}
	
//	public int getLernCost() {
//			return level;
//		}
	
	public GoldenThrow(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_goldenThrow_name");
	}
	
	
//	public int getDifficulty(int level) {
//		return diff[level - 1];	
//	}
	
//	public int getDifficultyMin(int level) {
//		return diffMin[level - 1];	
//	}
	
//	public int getCost(int level) {
//		return 5*level;
//	}
	
	@Override
	public void sorcer(Figure mage, Object target) {
		
				((Figure)target).setGolden_hit(level);
				//setzt nur die Trefferwahrscheinlichkeit f�r den n�chsten Schlag hoch
				//GuiFacade f = mage.getGame().getGui();
				//und l�st dann einen Schlag aus
				//ActionEvent ae = new ActionEvent(f.slap,0,"golden_throw");
				//f.causeActionEvent(GuiFacade.BUTTON_SLAP,"golden_throw");
				String str = JDEnv.getResourceBundle().getString("spell_goldenThrow_cast");
				mage.tellPercept(new TextPercept(str));
				//mage.incFightAP(1);
				mage.setDouble_bonus(true);
				mage.attack((Figure)target);
				//f.actionPerformed(ae);
				Inventory inv = ((Hero)mage).getInventory();
				
				inv.layDown(inv.getWeapon1());
				
	}
	
	
	@Override
	public String toString(){
		return getName();
	}
		

}



