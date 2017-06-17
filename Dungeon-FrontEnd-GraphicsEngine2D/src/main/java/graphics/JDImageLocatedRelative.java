package graphics;

import dungeon.JDPoint;
import graphics.util.DrawingRectangle;
import graphics.util.JDRectangle;
import graphics.util.RelativeRectangle;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.06.17.
 */
public class JDImageLocatedRelative extends JDImageLocated implements DrawingRectangle {

	private final JDImageProxy<?> image;
	private RelativeRectangle relativeRectangle;
	private final int sizeX;
	private final int sizeY;
	private  int posY;
	private  int posX;

	public JDImageLocatedRelative(JDImageProxy<?> i, int posX, int posY, int sizeX, int sizeY) {
		super(i, 0,0,0,0);  // only temporary
		image = i;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;

	}

	public JDImageLocatedRelative(JDImageProxy<?> i, RelativeRectangle relativeRectangle) {
		super(i, relativeRectangle);
		image = i;

		this.relativeRectangle = relativeRectangle;
		this.sizeX = relativeRectangle.getWidth();
		this.sizeY = relativeRectangle.getHeight();
	}


	@Override
	public boolean containsPoint(JDPoint p, int roomOffsetX, int roomOffsetY) {
		// TODO
		return false;
	}

	@Override
	public int getWidth() {
		return sizeX;
	}

	@Override
	public int getHeight() {
		return sizeY;
	}

	@Override
	public int getX(int roomOffsetX) {
		if(relativeRectangle != null) {
			return relativeRectangle.getX(roomOffsetX);
		}
		return roomOffsetX + posX;
	}

	@Override
	public int getY(int roomOffsetY) {
		if(relativeRectangle != null) {
			return relativeRectangle.getY(roomOffsetY);
		}
		return roomOffsetY + posY;
	}

	@Override
	public JDImageProxy<?> getImage() {
		return image;
	}

}
