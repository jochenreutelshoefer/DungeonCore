/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.control;
import de.jdungeon.ai.MovementIntelligence;
import de.jdungeon.figure.action.*;
/**
 * Abstrakte Klasse fuer Bewegungsverhalten 
 */
public abstract class AbstractMovementIntelligence extends ActionFactory implements MovementIntelligence {

	int fighterID;
	
	public abstract Action chooseMovementAction();
}
