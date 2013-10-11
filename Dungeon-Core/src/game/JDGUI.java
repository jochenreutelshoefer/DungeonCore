/*
 * Created on 13.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;


import java.applet.Applet;

import javax.swing.JFrame;

import dungeon.Room;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.action.Action;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import figure.percept.Percept;
import gui.AbstractStartWindow;

public interface JDGUI extends ControlUnit{
	
	
	public void gameRoundEnded();
	
	public void initGui(AbstractStartWindow start, Applet applet,
			String playerName);
	
	public abstract void setFigure(FigureInfo info);


}
