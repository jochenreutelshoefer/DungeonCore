package gui;

import game.JDGUI;
import graphics.ImageManager;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.*;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.awt.*;

public class JDJPanel extends JPanel {

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public final static Color bgColor = MyComboRenderer.bgColor;
	protected MyJDGui gui;
	
	public static Image getBackGroundImage() {
		return (Image)ImageManager.woodTextureImage.getImage();
	}
	
	public static final int texSizeX = 96;

	public static final int texSizeY = 96;
	
	/**
	 * 
	 */
	public JDJPanel(MyJDGui gui) {
		super();
		this.gui = gui;
		this.setBackground(bgColor);
		this.setOpaque(false);
	}

}
