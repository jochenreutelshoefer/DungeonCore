package spell;
import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;
import item.interfaces.ItemOwner;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class KeyLocator extends AbstractTargetSpell implements TargetSpell {

	public static int [][] values = {
								{3,4,5,10,1},
								{6,12,7,30,1}
								};
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public KeyLocator(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;
		
	}
	
	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_KEYLOCATOR;
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_keyLocator_text");
	}
	public KeyLocator(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = true;
		isPossibleInFight = false;
	}
	
	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Door) {
			return true;
		}
		return false;
	}

	@Override
	public int getLernCost() {
			return 1;
		}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}

	@Override
	public void sorcer(Figure mage, Object target) {
		if(target instanceof Door) {
			tellDirection(((Door)target),mage);
		}
				
	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return DoorInfo.class;
	}

	public void tellDirection(Door d,Figure f) {
		
		if(d != null && d.hasLock()) {
			ItemOwner owner = d.getKey().getOwner();
			JDPoint location = owner.getLocation();
			f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("spell_keyLocator_cast_found")+": "+ location));
			f.getRoomVisibility().setVisibilityStatus(location, RoomObservationStatus.VISIBILITY_ITEMS);
		}
		else {
			f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("spell_keyLocator_cast_nothing")));
		}
	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_keyLocator_name");
	}

}
