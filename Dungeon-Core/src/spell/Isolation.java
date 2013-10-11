/*
 * Created on 30.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import figure.Figure;
import figure.percept.TextPercept;
import game.Game;
import game.JDEnv;
import dungeon.Room;
import dungeon.Door;
import dungeon.DoorBlock;
import java.util.LinkedList;
import java.util.List;
public class Isolation extends TimedSpell {
	
//	public static int[] diffArray = { 5, 13 };
//	public static int[] diffMinArray = { 9, 15 };
	
	public static int [][] values = { {8,5,8,3,1},
								{15,13,12,25,2}
								};
	
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	List doors = new LinkedList();
	
	public Isolation(int level){
		super(level, values[level-1]);
		isPossibleNormal = true;
		isPossibleInFight = true;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_isolation_name");
	}
	
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_isolation_text");
	}
	
	public int getType() {
		return Spell.SPELL_ISOLATION;
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		
		return true;
	}
	
//	public int getLernCost() {
//		return 1;
//	}
	
//	public int getDuration(){
//		return rounds;
//	}
	
	public boolean fightModus(){
		return true;
	}
	
	public boolean normalModus(){
		return true;
	}
	
	public void sorcer(Figure sorcerer, Object o) {
		Room r = sorcerer.getRoom();
		Door[] d = r.getDoors();
		IsolationInstance instance = new IsolationInstance(this.getStrength(),d);
		Spell.addTimedSpell(instance);
		String str = JDEnv.getResourceBundle().getString("spell_isolation_cast");
		sorcerer.tellPercept(new TextPercept(str));
		
	}
	
	public void stopEffect(){
		for(int i =0 ; i < doors.size(); i++) {
			((Door)doors.get(i)).removeBlocking(this);
		}
		
	}

}
