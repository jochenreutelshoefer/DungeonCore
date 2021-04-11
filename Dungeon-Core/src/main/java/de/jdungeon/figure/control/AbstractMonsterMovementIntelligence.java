/*
 * Created on 23.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.control;
import de.jdungeon.ai.MovementIntelligence;
import de.jdungeon.figure.action.Action;

import de.jdungeon.figure.monster.MonsterInfo;


/**
 * Abstrakte Superklasse fuer Bewegungsverhalten eines Monsters 
 *
 */
public abstract class AbstractMonsterMovementIntelligence extends AbstractMovementIntelligence implements
		MovementIntelligence {
	
	
	
	/**
	 * Dieses MonsterInfo-Objekt liefert die Informationen zur Verfuegung, 
	 * die als Entscheidungsgrundlage dienen sollen
	 */
	public MonsterInfo monster;
	
		
	public AbstractMonsterMovementIntelligence(MonsterInfo m) {
		monster = m;
		this.fighterID = m.getFigureID();
	}
	
	/* (non-Javadoc)
	 * @see de.jdungeon.figure.monster.control.MonsterMovementIntelligence#chooseMovementAction()
	 */
	public abstract Action chooseMovementAction();
}
