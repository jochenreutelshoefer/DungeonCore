package graphics;
import dungeon.JDPoint;
import graphics.util.DrawingRectangle;
import graphics.util.JDRectangle;
import graphics.util.RelativeRectangle;

public class JDImageLocated implements DrawingRectangle {


	private final JDImageProxy<?> image;
	private  RelativeRectangle relativeRectangle;
	private final int sizeX;
	private final int sizeY;
	private  int posY;
	private  int posX;

	public JDImageLocated(JDImageProxy<?> i, int posX, int posY, int sizeX, int sizeY) {
		image = i;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;

	}

	public JDImageLocated(JDImageProxy<?> i, JDRectangle r) {
		image = i;
		this.posX = r.getX();
		this.posY = r.getY();
		this.sizeX = r.getWidth();
		this.sizeY = r.getHeight();

	}

	public JDImageLocated(JDImageProxy<?> i, RelativeRectangle relativeRectangle) {
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
		return posX;
	}

	@Override
	public int getY(int roomOffsetY) {
		if(relativeRectangle != null) {
			return relativeRectangle.getY(roomOffsetY);
		}
		return posY;
	}

	public JDImageProxy<?> getImage() {
		return image;
	}

}
