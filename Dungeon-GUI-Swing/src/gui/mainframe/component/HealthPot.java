package gui.mainframe.component;
import gui.MyJDGui;
import gui.JDJPanel;

import java.awt.*;

import javax.swing.*;

public class HealthPot extends JComponent {

	int healthValue;
	int healthMax;
	int sizeX;
	int sizeY;
	Color c;
	Image deathImage;
	Image potImage;
	
	MyJDGui gui;
	
	public HealthPot(int val, int max, int sizeX, int sizeY, Color c,MyJDGui gui) {
		this.c = c;
		healthValue = val;
		healthMax = max;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.gui = gui;
		//this.potImage = pot;
		this.setOpaque(false);
		
		
	}

	
	public HealthPot() {
		this.setOpaque(false);
	}
	
	private static final int texSizeX = 96;

	private static final int texSizeY = 96;
	
	public HealthPot(int sizeX, int sizeY, Color c, Image deathImage,MyJDGui gui,Image pot) {
		this.c = c;
		this.gui = gui;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		healthValue = 1;
		healthMax = 1;
		this.deathImage = deathImage;
		this.potImage = pot;
		this.setPreferredSize(new Dimension(120,120));
		this.setOpaque(false);
	}
	
	public HealthPot(int sizeX, int sizeY, Color c,MyJDGui gui, Image pot) {
			this.c = c;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			healthValue = 1;
			healthMax = 1;
			this.gui = gui;
			this.potImage = pot;
			this.setPreferredSize(new Dimension(120,180));
			this.setOpaque(false);
		
		}
	
	public void setVal (int v, int m) {
		healthValue = v;
		healthMax = m;
	}
	
	Image offscreenImage; 
	
	public void paint(Graphics g) {
		
		if(healthValue < 0 && deathImage != null) {
			//System.out.println("Male Totenkopf");
			g.drawImage(deathImage,0,0,sizeX,sizeY,null);
			g.setColor(Color.black);		
					g.drawRect(0,0, sizeX, sizeY);
					g.drawRect(1,1, sizeX-2,sizeY-2);
		}
		else {
			
			

			

		double percent = (((double) healthValue )/ healthMax) * sizeY;
		int p = (int) percent;
		
		g.setColor(c);
		g.fillOval(1,4, sizeX-4,sizeY-10);
		
		
		if(p < sizeY) {
		offscreenImage = gui.getMainFrame().createImage(sizeX, sizeY-p);
	Graphics g2 = offscreenImage.getGraphics();
	int i = 0;
	int j = 0;
	while (i * texSizeX < this.getWidth()) {
		j = 0;
		while (j * texSizeY < this.getHeight()) {

			g2.drawImage(JDJPanel.getBackGroundImage(), i * texSizeX, j
					* texSizeY, texSizeX, texSizeY, null);
			j++;
		}
		i++;
	}
		
		
		
		
		
		
		g.drawImage(offscreenImage, 0, 0,null);
		
		}
		
//		g.setColor(JDJPanel.bgColor);
//		g.fillRect(0, 0, sizeX, sizeY-p);
	//	g.setColor(Color.white);
	//	g.fillOval(0,0,sizeX,sizeY-p);
	//	g.setColor(Color.black);		
		//g.drawOval(0,0, sizeX, sizeY);
		//g.drawOval(1,1, sizeX-2,sizeY-2);
		g.setColor(Color.black);
		String s = Integer.toString(healthValue)+"/"+Integer.toString(healthMax);
		g.setFont(new Font("Arial", 0, 20));
		g.drawString(s, 28 ,73);
		
		if(potImage != null) {
			g.drawImage(potImage,0, 0, sizeX, sizeY,null);
		}
		}
		
	}
}
		
