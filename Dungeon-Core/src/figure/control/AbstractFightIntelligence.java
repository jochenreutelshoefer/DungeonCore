/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.control;
import ai.FightIntelligence;
import figure.action.*;
import figure.FigureInfo;
import dungeon.Door;

/**
 * Abstrakte Klasse fuer Entscheidungsverhalten im Kampf
 */
public abstract class AbstractFightIntelligence extends ActionFactory implements FightIntelligence {

	int fighterID;
	FigureInfo f;
	
	public AbstractFightIntelligence(FigureInfo f) {
		this.fighterID = f.getFighterID();
		this.f = f;
	}
	public abstract Action chooseFightAction();
	
	protected int getRandomPassableDir() {
		int doors [] = f.getRoomDoors();
		boolean poss [] = new boolean[4];
		boolean dirExisting = false;
		for(int i = 0; i < doors.length; i++) {
			if(doors[i] == Door.DOOR || doors[i] == Door.DOOR_LOCK_OPEN) {
				poss[i] = true;
				dirExisting = true;
			}
			else {
				poss[i] = false;
			}
		}
		
		if(!dirExisting) {
			System.out.println("AbstractFightIntelligence.getRandomFleeDir() - NO VALID DIRECTION!!!");
			//System.exit(0);
			return 0;
		}
		int k = -1;
		int dir = ((int)(Math.random()*4));
		while(k == -1) {
			if(poss[dir]) {
				k = dir;
			}
			dir = ((int)(Math.random()*4));
		}
		return k;
	}
	
}
