/*
 * Created on 23.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.mainframe.component;

//import javax.swing.*;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import graphics.ImageManager;
import gui.JDGUISwing;
import gui.JDJPanel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShowPanel extends JDJPanel implements MouseListener {

	private final Rectangle rect = new Rectangle(40, 5, 40, 40);

	public ShowPanel(JDGUISwing gui) {
		super(gui);
		this.addMouseListener(this);
	}

	@Override
	public void paint(Graphics g) {

		FigureInfo f = gui.getFigure();
		if (f instanceof HeroInfo) {
			HeroInfo info = ((HeroInfo) f);

			if (info.hasLuziaBall().booleanValue()) {
				Image im = null;
				if (info.LuziaBallSeesEnemy().booleanValue()) {
					im = (Image) ImageManager.luzia_ball_redImage.getImage();
				} else {
					im = (Image) ImageManager.luzia_ball_greyImage.getImage();
				}

				g.drawImage(im, rect.x, rect.y, rect.width, rect.height, null);
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent me) {

	}

	@Override
	public void mousePressed(MouseEvent me) {
		Point p = me.getPoint();
		if (rect.contains(p)) {
			FigureInfo f = gui.getFigure();
			if (f instanceof HeroInfo) {
				HeroInfo info = ((HeroInfo) f);

				if (info.hasLuziaBall().booleanValue()) {
					gui.getControl().wannaUseLuziaBall();
					// frame.getGame().getHero().getLuziasBall().use(frame.getGame().getHero());

				}

			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {

	}

	@Override
	public void mouseExited(MouseEvent me) {

	}
}