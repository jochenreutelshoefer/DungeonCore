/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package graphics;

import graphics.util.DrawingRectangle;
import graphics.util.JDRectangle;
import util.JDColor;
import dungeon.JDPoint;

public class GraphicObject {

	public DrawingRectangle getRectangle() {
		return rect;
	}

	public JDImageProxy<?> getImage() {
		return image;
	}

	@Override
	public String toString() {
		if (clickedObject == null) {
			return image.toString();
		} else {
			return clickedObject.toString();
		}
	}

	protected DrawingRectangle rect;
	protected DrawingRectangle clickRect;
	protected JDColor c;

	protected boolean flipped = false;
	protected Object clickedObject;

	protected boolean rim = false;
	protected JDImageProxy<?> image;

	public GraphicObject(Object ob, JDRectangle rectangle, JDColor c, JDImageProxy<?> i) {

		this.rect = rectangle;
		this.c = c;
		clickedObject = ob;
		image = i;
	}

	public GraphicObject(Object object, JDRectangle rectangle, JDColor color,
			JDImageProxy<?> i, JDRectangle clickRect) {

		this.rect = rectangle;
		this.c = color;
		this.clickRect = clickRect;
		clickedObject = object;
		image = i;
	}

	public GraphicObject(Object ob, JDRectangle o, JDColor c, boolean rim,
			JDImageProxy<?> i) {
		this.rim = rim;
		this.rect = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}

	public boolean getRim() {
		return rim;
	}

	public Object getClickableObject() {
		return clickedObject;
	}

	public JDColor getColor() {
		return c;
	}

	public boolean hasPoint(JDPoint p, int roomOffsetX, int roomOffsetY) {
		if (clickRect != null) {
			return clickRect.containsPoint(p, roomOffsetX, roomOffsetY);
		}
		if (rect != null) {
			return rect.containsPoint(p, roomOffsetX, roomOffsetY);
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
	 * @param flipped
	 *            The flipped to set.
	 */
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

}
