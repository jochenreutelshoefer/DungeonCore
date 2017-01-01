package animation;

import dungeon.JDPoint;
import dungeon.Position;
import graphics.GraphicObjectRenderer;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import org.apache.log4j.Logger;

import de.jdungeon.game.Image;

public class AnimationFrame {

	private JDImageProxy<?> image;
	private final double currentProgress;
	private final Position.Pos from;
	private final Position.Pos to;
	private String text;
	private JDPoint textCoordinatesOffset;

	public JDImageProxy<?> getImage() {
		return image;
	}

	public void setImage(JDImageProxy<?> image) {
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public JDPoint getTextCoordinatesOffset() {
		return textCoordinatesOffset;
	}

	public void setTextCoordinatesOffset(JDPoint textCoordinatesOffset) {
		this.textCoordinatesOffset = textCoordinatesOffset;
	}

	public AnimationFrame(JDImageProxy<?> image, double currentProgress, Position.Pos from, Position.Pos to) {
		super();
		if(to == null) {
			Logger.getLogger(this.getClass()).error("Creating AnimationFrame for unknown position! "+((Image)image.getImage()).toString());
		}
		this.image = image;
		this.currentProgress = currentProgress;
		this.from = from;
		this.to = to;
	}

	public AnimationFrame(JDImageProxy<?> image, String text,
			JDPoint textCoordinatesOffset, double currentProgress, Position.Pos from, Position.Pos to) {
		super();
		this.image = image;
		this.text = text;
		this.textCoordinatesOffset = textCoordinatesOffset;
		this.currentProgress = currentProgress;
		this.from = from;
		this.to = to;
	}

	public JDImageLocated getLocatedImage(int roomOffsetX, int roomOffsetY, int figureSizeX, int figureSizeY, int roomSize) {
		if (to == null) {
			// animation is out of our visibility, hence we cannot render/locate anything
			return null;
		}
		JDPoint positionFromOffset = GraphicObjectRenderer.getPositionCoordinates(from, roomSize);
		JDPoint positionToOffset = GraphicObjectRenderer.getPositionCoordinates(to, roomSize);
		int posSize = GraphicObjectRenderer.getPosSize(roomSize);
		assert positionToOffset != null;
		int coordinateX = roomOffsetX+positionToOffset.getX();
		int coordinateY = roomOffsetY+positionToOffset.getY();
		if(from != to && positionFromOffset != null) {
			int diffX = (int) ((positionToOffset.getX() - positionFromOffset.getX()) * currentProgress);
			int diffY = (int) ((positionToOffset.getY() - positionFromOffset.getY()) * currentProgress);
			coordinateX = roomOffsetX+positionFromOffset.getX()+diffX;
			coordinateY = roomOffsetY+positionFromOffset.getY()+diffY ;
		}
		return new JDImageLocated(getImage(), coordinateX + posSize/2 - figureSizeX/2 , coordinateY /*+ posSize/2*/ - figureSizeY/2 , figureSizeX, figureSizeY);
	}
}
