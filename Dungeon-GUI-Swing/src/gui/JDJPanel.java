package gui;

import graphics.ImageManager;

import java.awt.Component;
import java.awt.Image;

import javax.swing.JPanel;
/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class JDJPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312764915549053922L;
	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	// public final static Color bgColor = MyComboRenderer.bgColor;
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
		// this.setBackground(bgColor);
		this.setOpaque(false);
	}

	public void updateView() {
		Component[] components = this.getComponents();

		for (Component child : components) {
			if (child instanceof JDJPanel) {
				((JDJPanel) child).updateView();
			}
		}
	}

}
