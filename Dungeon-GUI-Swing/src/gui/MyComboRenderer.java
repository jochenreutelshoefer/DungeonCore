/*
 * Created on 19.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyComboRenderer extends JLabel implements ListCellRenderer{
	
	Object item;
	
	  public MyComboRenderer() {
	         setOpaque(true);
	         this.setPreferredSize(new Dimension(100,20));
	     }
	     public Component getListCellRendererComponent(
	         JList list,
	         Object value,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus)
	     {
	    	 item = value;
	    	 if(value != null) {
	    		 setText(value.toString());
	    	 }
	         setBackground(isSelected ? bgActiveColor : bgColor);
	         setForeground(fgColor);
	         return this;
	     }
	     
	     public static Color bgColor = new Color(192,138,19);
	     private static Color fgColor = Color.BLACK;
	     private static Color bgActiveColor = new Color(200,150,22);
	     
//	     public void paint(Graphics g) {
//	    	 int texSizeX = JDJPanel.texSizeX;
//	    	 int texSizeY = JDJPanel.texSizeY;
//	    	 int i = 0;
//	    		int j = 0;
//	    		while (i * texSizeX < this.getWidth()) {
//	    			j = 0;
//	    			while (j * texSizeY < this.getHeight()) {
//
//	    				g.drawImage(JDJPanel.getBackGroundImage(), i * texSizeX, j
//	    						* texSizeY, texSizeX, texSizeY, null);
//	    				j++;
//	    			}
//	    			i++;
//	    		}
//	    		if(item != null) {
//	    		g.drawString(item.toString(), 5, 13);
//	    		}
//	     }
	

}
