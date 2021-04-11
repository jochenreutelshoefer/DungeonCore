/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.control;
import de.jdungeon.figure.action.*;
import de.jdungeon.figure.hero.*;
/**
 * Abstrakte Klasse fuer ein Entscheidungsverhalten des Helden im Kampf
 */
public abstract class AbstractHeroFightIntelligence extends AbstractFightIntelligence {
	
	protected HeroInfo h;
	
	public AbstractHeroFightIntelligence(HeroInfo h) {
		super(h);
		this.h = h;
		
	}
	
	public abstract Action chooseFightAction();

}
