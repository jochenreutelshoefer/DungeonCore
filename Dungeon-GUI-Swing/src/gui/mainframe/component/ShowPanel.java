/*
 * Created on 23.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.mainframe.component;

//import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import gui.mainframe.*;
import gui.MyJDGui;
import gui.JDJPanel;

/**
 * @author Jochen
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShowPanel extends JDJPanel implements MouseListener{

	MainFrame frame;
	Rectangle rect = new Rectangle(40,5,40,40);

	public ShowPanel(MainFrame f, MyJDGui gui) {
		super(gui);
		frame = f;
		this.addMouseListener(this);
	}

	public void paint(Graphics g) {

		
			//if (frame.getGame().getHero() != null) {
		FigureInfo f = gui.getFigure();
		if(f instanceof HeroInfo) {
			HeroInfo info = ((HeroInfo)f);
			
				if (info.hasLuziaBall().booleanValue()) {
					Image im = null;
					if (info.LuziaBallSeesEnemy().booleanValue()) {
						im = gui.getMainFrame()
								.getSpielfeld().getSpielfeldBild()
								.getLuzia_ball_redImage();
					} else {
						im = gui.getMainFrame()
								.getSpielfeld().getSpielfeldBild()
								.getLuzia_ball_greyImage();

					}

					g.drawImage(im, rect.x, rect.y, rect.width, rect.height, null);
				}
			//}
	}
		
}
	
	public void mouseClicked(MouseEvent me) {
		
	}
	
	public void mousePressed(MouseEvent me) {
		Point p = me.getPoint();
		if(rect.contains(p)) {
			FigureInfo f = gui.getFigure();
			if(f instanceof HeroInfo) {
				HeroInfo info = ((HeroInfo)f);
				
					if (info.hasLuziaBall().booleanValue()) {
						gui.getControl().wannaUseLuziaBall();
						//frame.getGame().getHero().getLuziasBall().use(frame.getGame().getHero());
								
					}
				
			
			}
		}
	}
	
	public void mouseReleased(MouseEvent me) {
		
	}
	
	public void mouseEntered(MouseEvent me) {
		
	}
	
	public void mouseExited(MouseEvent me) {
		
	}
}