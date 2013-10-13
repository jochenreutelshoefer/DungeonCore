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
import graphics.JDRectangle;

import java.awt.Color;
import java.awt.Rectangle;

import dungeon.JDPoint;


public class GraphicObject {



	public JDRectangle getRectangle() {
		return rect;
	}

	public JDImageProxy getImage() {
		return image;
	}
	protected JDRectangle rect;
	protected JDRectangle clickRect;
	protected Color c;

	protected boolean flipped = false;
	protected Object clickedObject;

	protected boolean rim = false;
	protected JDImageProxy image;

	public GraphicObject(
		Object ob,
		JDRectangle o,
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
			JDImageProxy i) {			
			this(ob, new JDRectangle(o.x, o.y, o.width, o.height), c, i);
		}
	
	public GraphicObject(
			Object ob,
			JDRectangle o,
			Color c,
			JDImageProxy i, JDRectangle clickRect) {
			
			this.rect = o;
			this.c = c;
			this.clickRect = clickRect;
			clickedObject = ob;
			image = i;
		}
	
	public GraphicObject(
			Object ob,
			Rectangle o,
			Color c,
			JDImageProxy i, Rectangle clickRect) {
			
			this(ob, new JDRectangle(o.x, o.y, o.width, o.height), c, i,new JDRectangle(clickRect.x, clickRect.y, clickRect.width, clickRect.height));
		}

	public GraphicObject(Object ob,	JDRectangle o,Color c,boolean rim,JDImageProxy i) {
		this.rim = rim;
		this.rect = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}
	
	public GraphicObject(Object ob,	Rectangle o,Color c,boolean rim,JDImageProxy i) {
		this(ob, new JDRectangle(o.x, o.y, o.width, o.height), c,rim, i);
		
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
	
	

	public boolean hasPoint(JDPoint p) {
		if(clickRect != null) {
			return clickRect.containsPoint(p);
		}
		if (rect != null) {
			return rect.containsPoint(p);
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
