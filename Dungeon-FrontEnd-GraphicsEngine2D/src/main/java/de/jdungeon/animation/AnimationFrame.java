package de.jdungeon.animation;

import com.badlogic.gdx.utils.Pool;
import de.jdungeon.game.GameEnv;
import de.jdungeon.game.Logger;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.graphics.GraphicObjectRenderer;
import de.jdungeon.graphics.JDImageLocated;
import de.jdungeon.graphics.JDImageProxy;

import de.jdungeon.game.Image;

public class AnimationFrame implements Pool.Poolable {


	private JDImageProxy<?> image;
	private double currentProgress;
	private Position.Pos from;
	private Position.Pos to;
	private String text;
	private JDPoint textCoordinatesOffset = new JDPoint(0, 0);

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

	/**
	 * Constructor for pooling
	 */
	public AnimationFrame() {
		super();
	}

	public void setCurrentProgress(double currentProgress) {
		this.currentProgress = currentProgress;
	}

	public void setFrom(Position.Pos from) {
		this.from = from;
	}

	public void setTo(Position.Pos to) {
		this.to = to;
	}

	public void setTextCoordinatesOffset(JDPoint textCoordinatesOffset) {
		this.textCoordinatesOffset = textCoordinatesOffset;
	}

	public void setTextCoordinatesOffsetX(int textCoordinatesOffsetX) {
		this.textCoordinatesOffset.setX(textCoordinatesOffsetX);
	}


	public void setTextCoordinatesOffsetY(int textCoordinatesOffsetY) {
		this.textCoordinatesOffset.setY(textCoordinatesOffsetY);
	}


	public AnimationFrame(JDImageProxy<?> image, double currentProgress, Position.Pos from, Position.Pos to) {
		super();
		if(to == null) {
			Logger logger = GameEnv.getInstance().getGame().getLogger();
			logger.error(this.getClass().getSimpleName(),"Creating AnimationFrame for unknown position! "+((Image)image.getImage()).toString());
		}
		this.image = image;
		this.currentProgress = currentProgress;
		this.from = from;
		this.to = to;
	}

	public AnimationFrame(JDImageProxy<?> image, String text,
			int textCoordinatesOffsetX, int textCoordinatesOffsetY, double currentProgress, Position.Pos from, Position.Pos to) {
		super();
		this.image = image;
		this.text = text;
		this.textCoordinatesOffset.setX(textCoordinatesOffsetX);
		this.textCoordinatesOffset.setY(textCoordinatesOffsetY);
		this.currentProgress = currentProgress;
		this.from = from;
		this.to = to;
	}

	public JDImageLocated getLocatedImage(int roomOffsetX, int roomOffsetY, int figureSizeX, int figureSizeY) {
		if (to == null) {
			// de.jdungeon.animation is out of our visibility, hence we cannot render/locate anything
			return null;
		}
		if(from == null) {
			from = to;
		}
		JDPoint positionFromOffset = GraphicObjectRenderer.getPositionCoordinates(from);
		JDPoint positionToOffset = GraphicObjectRenderer.getPositionCoordinates(to);
		int posSize = GraphicObjectRenderer.getPosSize();
		assert positionToOffset != null;
		int coordinateX = roomOffsetX + positionToOffset.getX();
		int coordinateY = roomOffsetY + positionToOffset.getY();
		if(from != to && positionFromOffset != null) {
			int diffX = (int) ((positionToOffset.getX() - positionFromOffset.getX()) * currentProgress);
			int diffY = (int) ((positionToOffset.getY() - positionFromOffset.getY()) * currentProgress);
			coordinateX = roomOffsetX+positionFromOffset.getX()+diffX;
			coordinateY = roomOffsetY+positionFromOffset.getY()+diffY ;
		}
		return new JDImageLocated(getImage(), coordinateX + posSize/2 - figureSizeX/2 , coordinateY - figureSizeY/2 , figureSizeX, figureSizeY);
	}

	@Override
	public void reset() {
		image = null;
		currentProgress = 0;
		from = null;
		to = null;
		text = null;
		textCoordinatesOffset = null;
	}
}
