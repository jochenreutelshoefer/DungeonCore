/*
 * Created on 30.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.spell;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
public class Isolation extends AbstractSpell {
	
	public static int [][] values = { {8,5,8,3,1},
								{15,13,12,25,2}
								};

	private final boolean isPossibleNormal = true;
	private final boolean isPossibleInFight = false;
	private final List<Door> doors = new ArrayList<Door>();
	
	public Isolation(int level){
		super(level, values[level-1]);
	}

	public Isolation() {
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
	public void sorcer(Figure sorcerer, RoomEntity o, int round) {
		Room r = sorcerer.getRoom();
		Door[] d = r.getDoors();
		IsolationInstance instance = new IsolationInstance(this.getStrength(),d);
		AbstractSpell.addTimedSpell(instance);
		String str = JDEnv.getResourceBundle().getString("spell_isolation_cast");
		sorcerer.tellPercept(new TextPercept(str, round));
	}
	
}
