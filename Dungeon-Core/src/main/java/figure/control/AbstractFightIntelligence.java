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

/**
 * Abstrakte Klasse fuer Entscheidungsverhalten im Kampf
 */
public abstract class AbstractFightIntelligence extends ActionFactory implements FightIntelligence {

	int fighterID;
	FigureInfo f;
	
	public AbstractFightIntelligence(FigureInfo f) {
		this.fighterID = f.getFigureID();
		this.f = f;
	}
	public abstract Action chooseFightAction();

	
}
