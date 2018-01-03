package graphics;
import dungeon.JDPoint;
import graphics.util.DrawingRectangle;
import graphics.util.JDRectangle;
import graphics.util.RelativeRectangle;

import de.jdungeon.game.Image;

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
		Image image = (Image) this.image.getImage();
		if(image.getHeight() == 128 && image.getWidth() == 128) {
			// some of the figure animation images have size 128 instead of 96
			return (sizeX * 128) / 96;
		}
		return sizeX;
	}

	@Override
	public int getHeight() {
		Image image = (Image) this.image.getImage();
		if(image.getHeight() == 128 && image.getWidth() == 128) {
			// some of the figure animation images have size 128 instead of 96
			return (sizeY * 128) / 96;
		}
		return sizeY;
	}

	@Override
	public int getX(int roomOffsetX) {

		int xValue;
		if(relativeRectangle != null) {
			xValue = relativeRectangle.getX(roomOffsetX);
		} else {
			xValue = posX;
		}
		Image resourceImage = (Image) this.image.getImage();
		if(resourceImage.getHeight() == 128 && resourceImage.getWidth() == 128) {
			// some of the figure animation images have size 128 instead of 96
			int width = getWidth();
			return (int) (xValue - (((((float)(width * 128 - width * 96))/96)/2)));
		} else {
			return xValue;
		}
	}

	@Override
	public int getY(int roomOffsetY) {

		int yValue;
		if(relativeRectangle != null) {
			yValue = relativeRectangle.getY(roomOffsetY);
		} else {
			yValue = posY;
		}

		Image resourceImage = (Image) this.image.getImage();
		if(resourceImage.getHeight() == 128 && resourceImage.getWidth() == 128) {
			// some of the figure animation images have size 128 instead of 96
			int height = getHeight();
			return (int) (yValue - (((((float)(height * 128 - height * 96))/96)/2)));
		} else {
			return yValue;
		}
	}

	public JDImageProxy<?> getImage() {
		return image;
	}

}
