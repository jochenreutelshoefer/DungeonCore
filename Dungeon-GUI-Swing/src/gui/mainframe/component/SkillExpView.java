/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.mainframe.component;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import gui.JDGUISwing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.border.EtchedBorder;

import util.JDColor;

public class SkillExpView extends HealthPot {

	public SkillExpView(int val, int max, int sizeX, int sizeY, JDGUISwing gui) {
		super(val, max, sizeX, sizeY, JDColor.black, gui);

		repaint();

	}

	public SkillExpView() {
		super();
		repaint();

	}

	public SkillExpView(int val, int max, JDGUISwing gui) {
		super(val, max, 50, 12, JDColor.black, gui);

		this.setBorder(new EtchedBorder());
		this.setSize(100, 8);
		repaint();

	}

	// public void setChar(Character c) {
	// charac = c;
	// }

	private int skillPoints = 0;

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}

	@Override
	public void paint(Graphics g) {

		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		if (info instanceof HeroInfo) {
			heroInfo = ((HeroInfo) info);
		}
		// System.out.println("male skillExpView!");
		double percent = (((double) healthValue) / healthMax) * sizeX;
		int p = (int) percent;

		g.setColor(c);
		g.fillRect(0, 0, p, sizeY);
		g.setColor(Color.lightGray);
		g.fillRect(p, 0, sizeX - p, sizeY);
		g.setColor(Color.black);
		g.drawRect(0, 0, sizeX, sizeY);
		g.drawRect(1, 1, sizeX - 2, sizeY - 2);

		if (skillPoints > 0) {
			// System.out.println("zeichne!");
			// System.out.println("Draw SkillPoints: "+skillPoints);
			g.setColor(Color.red);
			g.drawString(Integer.toString(skillPoints), sizeX / 2 - 5, 11);
		}
		// String s =
		// Integer.toString(healthValue)+"/"+Integer.toString(healthMax);
		// g.setFont(new Font("Arial", 0, 26));
		// g.drawString(s, 8 ,30);
	}

}
