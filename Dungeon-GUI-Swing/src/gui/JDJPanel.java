package gui;

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
	//public final static Color bgColor = new Color(255,190,60);
	
	protected MyJDGui gui;
	
//	public JDJPanel(LayoutManager layout, boolean isDoubleBuffered,GuiFacade gui) {
//		super(layout, isDoubleBuffered);
//		this.gui = gui;
//		//this.setBackground(bgColor);
//		this.setBorder(new EtchedBorder());
//		//this.setOpaque(true);
//		// TODO Auto-generated constructor stub
//	}

	/**
	 * @param layout
	 */
//	public JDJPanel(LayoutManager layout,GuiFacade gui) {
//		super(layout);
//		this.gui = gui;
//		//this.setBackground(bgColor);
//		this.setOpaque(true);
//		// TODO Auto-generated constructor stub
//	}

	/**
	 * @param isDoubleBuffered
//	 */
//	public JDJPanel(boolean isDoubleBuffered,GuiFacade gui) {
//		super(isDoubleBuffered);
//		this.gui = gui;
//		this.setBackground(bgColor);
//		//this.setOpaque(true);
//		// TODO Auto-generated constructor stub
//	}
	
	public static Image getBackGroundImage() {
		return (Image)ImageManager.woodTextureImage.getImage();
	}
	
	public static final int texSizeX = 96;

	public static final int texSizeY = 96;
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		int i = 0;
//		int j = 0;
//		while (i * texSizeX < this.getWidth()) {
//			j = 0;
//			while (j * texSizeY < this.getHeight()) {
//
//				g.drawImage(JDJPanel.getBackGroundImage(), i * texSizeX, j
//						* texSizeY, texSizeX, texSizeY, null);
//				j++;
//			}
//			i++;
//		}
//
//	}
 
	
	/**
	 * 
	 */
	public JDJPanel(MyJDGui gui) {
		super();
		this.gui = gui;
		this.setBackground(bgColor);
		this.setOpaque(false);
		
		// TODO Auto-generated constructor stub
	}

}
