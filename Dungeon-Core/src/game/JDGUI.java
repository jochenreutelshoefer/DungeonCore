/*
 * Created on 13.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;


import figure.FigureInfo;
import figure.action.Action;
import gui.AbstractStartWindow;
import item.ItemInfo;

import java.applet.Applet;

import spell.SpellInfo;

public interface JDGUI extends ControlUnit{
	
	public void plugAction(Action a);
	
	public int getSelectedItemIndex();
	
	public ItemInfo getSelectedItem();
	
	public SpellInfo getSelectedSpellInfo();
	
	public void setSpellMetaDown(boolean b);
	
	public FigureInfo getFigure();
	
	public void setUseWithTarget(boolean b);
	
	public void gameRoundEnded();
	
	public void initGui(AbstractStartWindow start, Applet applet,
			String playerName);
	
	public abstract void setFigure(FigureInfo info);

	public void animationDone();
}
