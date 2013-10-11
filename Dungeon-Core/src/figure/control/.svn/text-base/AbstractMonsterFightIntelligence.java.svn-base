/*
 * Created on 08.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.control;

import ai.FightIntelligence;
import figure.monster.MonsterInfo;
import figure.action.Action;

/**
 * 
 * Abstrakte Oberklasse fuer Kampfentscheidungsverhalten eines Monsters
 *
 * 
 */
public abstract class AbstractMonsterFightIntelligence extends AbstractFightIntelligence implements
		FightIntelligence {
	
//	public static final int DIRECTION_NORTH = 1;
//	public static final int DIRECTION_EAST = 2;
//	public static final int DIRECTION_SOUTH = 3;
//	public static final int DIRECTION_WEST = 4;
//	
	/**
	 * Dieses MonsterInfo-Objekt liefert die Informationen zur Verfuegung, 
	 * die als Entscheidungsgrundlage dienen sollen
	 */
	public MonsterInfo monster;
	
	/**
	 * Nummer des Monsters, muss beim erstellen von Action-Objekten mit uebergeben werden
	 */
	//public int monsterID;
	
	
	public AbstractMonsterFightIntelligence(MonsterInfo m) {
		super(m);
		monster = m;
		
	}
	
	/* (non-Javadoc)
	 * @see figure.monster.control.MonsterFightIntelligence#chooseFightAction()
	 */
	public abstract Action chooseFightAction();

}
