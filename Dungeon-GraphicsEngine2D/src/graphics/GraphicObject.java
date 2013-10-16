/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package graphics;

import util.JDColor;
import graphics.util.JDRectangle;
import dungeon.JDPoint;

public class GraphicObject {

	public JDRectangle getRectangle() {
		return rect;
	}

	public JDImageProxy<?> getImage() {
		return image;
	}

	protected JDRectangle rect;
	protected JDRectangle clickRect;
	protected JDColor c;

	protected boolean flipped = false;
	protected Object clickedObject;

	protected boolean rim = false;
	protected JDImageProxy<?> image;

	public GraphicObject(Object ob, JDRectangle o, JDColor c, JDImageProxy<?> i) {

		this.rect = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}

	public GraphicObject(Object ob, JDRectangle o, JDColor c,
			JDImageProxy<?> i, JDRectangle clickRect) {

		this.rect = o;
		this.c = c;
		this.clickRect = clickRect;
		clickedObject = ob;
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

	public Object getClickedObject() {
		return clickedObject;
	}

	public JDColor getColor() {
		return c;
	}

	public boolean hasPoint(JDPoint p) {
		if (clickRect != null) {
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
	 * @param flipped
	 *            The flipped to set.
	 */
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

}
