package gui;

import graphics.ImageManager;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.awt.Color;
import java.awt.Image;

import javax.swing.JPanel;

public class JDJPanel extends JPanel {

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public final static Color bgColor = MyComboRenderer.bgColor;
	protected JDGUISwing gui;
	
	public static Image getBackGroundImage() {
		return (Image)ImageManager.woodTextureImage.getImage();
	}
	
	public static final int texSizeX = 96;

	public static final int texSizeY = 96;
	
	/**
	 * 
	 */
	public JDJPanel(JDGUISwing gui) {
		super();
		this.gui = gui;
		this.setBackground(bgColor);
		this.setOpaque(false);
	}

}
