/*
 * Created on 06.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;

import java.awt.Graphics;

import javax.swing.*;

public class JDBackgroundPanel extends JPanel {

	public JDBackgroundPanel() {
		super();
		this.setBackground(JDJPanel.bgColor);
	}

	// public void paint(Graphics g) {
	// g.setColor(Color.red);
	// g.fillRect(0,0,600,600);
	// }

	private static final int texSizeX = 96;

	private static final int texSizeY = 96;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int i = 0;
		int j = 0;
		while (i * texSizeX < this.getWidth()) {
			j = 0;
			while (j * texSizeY < this.getHeight()) {

				g.drawImage(JDJPanel.getBackGroundImage(), i * texSizeX, j
						* texSizeY, texSizeX, texSizeY, null);
				j++;
			}
			i++;
		}

	}

}
