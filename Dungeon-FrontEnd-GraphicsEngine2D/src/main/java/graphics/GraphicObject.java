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



	protected DrawingRectangle rect;
	protected DrawingRectangle clickRect;
	protected JDColor c;

	protected Object clickedObject;

	protected boolean rim = false;
	protected JDImageProxy<?> image;

	public GraphicObject(Object ob, DrawingRectangle rectangle, JDColor c, JDImageProxy<?> i) {
		this.rect = rectangle;
		this.c = c;
		clickedObject = ob;
		image = i;
	}

	public GraphicObject(Object object, DrawingRectangle rectangle, JDColor color,
			JDImageProxy<?> i, DrawingRectangle clickRect) {

		this.rect = rectangle;
		this.c = color;
		this.clickRect = clickRect;
		clickedObject = object;
		image = i;
	}

	public GraphicObject(Object ob, DrawingRectangle o, JDColor c, boolean rim,
			JDImageProxy<?> i) {
		this.rim = rim;
		this.rect = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GraphicObject that = (GraphicObject) o;

		if (!rect.equals(that.rect)) return false;
		if (clickRect != null ? !clickRect.equals(that.clickRect) : that.clickRect != null) return false;
		if (!clickedObject.equals(that.clickedObject)) return false;
		return image.equals(that.image);
	}

	@Override
	public int hashCode() {
		int result = rect.hashCode();
		result = 31 * result + (clickRect != null ? clickRect.hashCode() : 0);
		result = 31 * result + clickedObject.hashCode();
		result = 31 * result + image.hashCode();
		return result;
	}

	public Object getClickableObject() {
		return clickedObject;
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

	@Override
	public String toString() {
		if (clickedObject == null) {
			return image.toString();
		} else {
			return this.getClass().getName()+" " +clickedObject.toString();
		}
	}


}
