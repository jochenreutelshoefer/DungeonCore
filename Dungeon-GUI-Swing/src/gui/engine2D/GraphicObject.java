/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.engine2D;

import graphics.JDImageProxy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import dungeon.Door;

public class GraphicObject {



	public Rectangle getRectangle() {
		return rect;
	}

	public JDImageProxy getImage() {
		return image;
	}
	protected Rectangle rect;
	protected Rectangle clickRect;
	protected Color c;

	protected boolean flipped = false;
	protected Object clickedObject;

	protected boolean rim = false;
	protected JDImageProxy image;

	public GraphicObject(
		Object ob,
		Rectangle o,
		Color c,
		JDImageProxy i) {
		
		this.rect = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}
	
	public GraphicObject(
			Object ob,
			Rectangle o,
			Color c,
			JDImageProxy i,Rectangle clickRect) {
			
			this.rect = o;
			this.c = c;
			this.clickRect = clickRect;
			clickedObject = ob;
			image = i;
		}

	public GraphicObject(Object ob,	Rectangle o,Color c,boolean rim,JDImageProxy i) {
		this.rim = rim;
		this.rect = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}

	public boolean getRim() {
		return rim;
	}

	
	public Object getClickedObject() {
		return clickedObject;
	}



	
	
	public Color getColor() {
		return c;
	}
	
	

	public boolean hasPoint(Point p) {
		if(clickRect != null) {
			return clickRect.contains(p);
		}
		if (rect != null) {
			return rect.contains(p);
		} else {
			return false;
		}
	}
	/**
	 * @return Returns the flipped.
	 */
	public boolean isFlipped() {
		return flipped;
	}
	/**
	 * @param flipped The flipped to set.
	 */
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

}
