/*
 * Created on 06.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ai;

import figure.action.Action;

/**
 * Interface fuer ein Bewegungsverhalten
 *
 */
public interface MovementIntelligence {
	
	/**
	 * Diese Methode soll das Entscheidungsverhalten implementieren, 
	 * es soll die auszufuehrende Aktion zurueckgegeben werden.
	 * 
	 * 
	 * @see ActionFactory
	 * @see Action
	 * @return auszufuehrende Aktion
	 */
	public Action chooseMovementAction();
	
	

}
