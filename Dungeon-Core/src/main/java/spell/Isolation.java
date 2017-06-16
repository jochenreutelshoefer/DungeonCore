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
import game.InfoEntity;
import game.JDEnv;

import java.util.LinkedList;
import java.util.List;

import dungeon.Door;
import dungeon.Room;
public class Isolation extends AbstractSpell {
	
//	public static int[] diffArray = { 5, 13 };
//	public static int[] diffMinArray = { 9, 15 };
	
	public static int [][] values = { {8,5,8,3,1},
								{15,13,12,25,2}
								};
	
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	List<Door> doors = new LinkedList<Door>();
	
	public Isolation(int level){
		super(level, values[level-1]);
		isPossibleNormal = true;
		isPossibleInFight = true;
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
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_isolation_name");
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_isolation_text");
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_ISOLATION;
	}
	
	@Override
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
	
	@Override
	public void sorcer(Figure sorcerer, Object o) {
		Room r = sorcerer.getRoom();
		Door[] d = r.getDoors();
		IsolationInstance instance = new IsolationInstance(this.getStrength(),d);
		AbstractSpell.addTimedSpell(instance);
		String str = JDEnv.getResourceBundle().getString("spell_isolation_cast");
		sorcerer.tellPercept(new TextPercept(str));
		
	}
	
	public void stopEffect(){
		for(int i =0 ; i < doors.size(); i++) {
			doors.get(i).removeBlocking(this);
		}
		
	}

}
