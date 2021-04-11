/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.control;
import de.jdungeon.ai.FightIntelligence;
import de.jdungeon.figure.action.*;
import de.jdungeon.figure.FigureInfo;

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
