/*
 * Created on 06.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.ai;

import de.jdungeon.figure.action.Action;


/**
 * 
 * Interface fuer ein Entscheidungsverhalten im Kampf
 *
 * 
 */
public interface FightIntelligence {
	
	/**
	 * Diese Methode soll den Entscheidungsalgorithmus enthalten und die auszufuehrende Aktion
	 * zurueckgeben
	 * 
	 * @see Action
	 * @return auszufuehrende Aktion
	 */
	Action chooseFightAction();

}
