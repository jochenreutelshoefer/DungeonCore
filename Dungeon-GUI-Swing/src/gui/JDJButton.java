package gui;

//import javax.swing.Action;
//import javax.swing.Icon;
//import javax.swing.JButton;

import graphics.ImageManager;
import gui.mainframe.MainFrame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import javax.swing.JComponent;
/*
 * Created on 06.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import javax.swing.border.EtchedBorder;

public class JDJButton extends JComponent implements MouseListener {

	ActionListener listener;

	static Image icon;

	String text;

	boolean down = false;

	/**
	 * @param text
	 */

	@Override
	public void paintComponent(Graphics g) {

		g.drawImage(JDJPanel.getBackGroundImage(), 0, 0, this.getWidth(),
				this.getHeight(), null);
		super.paintComponent(g);
	}

	boolean logo = true;

	public JDJButton(String text) {
		this(text, true);
	}

	public JDJButton(String text, boolean b) {

		super();
		logo = b;

		this.addMouseListener(this);
		this.setPreferredSize((new Dimension(10 * text.length() + 16, 34)));
		this.text = text;
		if (!b) {
			this.setBackground(JDJPanel.bgColor);
			this.setBorder(new EtchedBorder());
		}
		if (icon == null) {

			icon = (Image) ImageManager.button1.getImage();

		}

	}

	/**
	 * 
	 * @uml.property name="text"
	 */
	public void setText(String t) {
		text = t;
	}

	@Override
	public void paint(Graphics g) {
		int x = (int) this.getSize().getWidth();
		int y = (int) this.getSize().getHeight();
		if (logo) {

			if (down) {
				g.drawImage(icon, -2, -2, x + 4, y + 4, null);
			} else {

				g.drawImage(icon, 0, 0, x, y, null);
			}
		}
		g.setFont(MainFrame.ButtonFont);
		g.drawString(text, (this.getWidth() - 10 * text.length()) / 2,
				y / 2 + 5);
	}

	/**
	 * @param a
	 * 
	 * @uml.property name="listener"
	 */
	// public JDJButton(Action a) {
	// super(a);
	// // TODO Auto-generated constructor stub
	// }
	public void addActionListener(ActionListener l) {
		listener = l;
	}

	@Override
	public void mouseClicked(MouseEvent me) {

	}

	@Override
	public void mousePressed(MouseEvent me) {
		down = true;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent me) {

	}

	@Override
	public void mouseReleased(MouseEvent me) {
		down = false;
		String meta = "";
		if (me.isMetaDown()) {
			meta = "meta";
		}
		this.listener.actionPerformed(new ActionEvent(this, 0, meta));
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent me) {

	}

	/**
	 * @param text
	 * @param icon
	 */
	// public JDJButton(String text, Icon icon) {
	// super(text, icon);
	// // TODO Auto-generated constructor stub
	// }

}
