package de.jdungeon.androidapp.gui.smartcontrol;

import android.util.Log;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import event.EventManager;
import event.WannaMoveEvent;
import util.JDDimension;

import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class MoveElement extends AnimatedSmartControlElement {

	private final GUIElement parent;
	private final RouteInstruction.Direction direction;
	private JDPoint[] triangle;


	public MoveElement(JDPoint position, JDDimension dimension, GUIElement parent, RouteInstruction.Direction direction) {
		super(position, dimension, parent);
		this.parent = parent;
		this.direction = direction;

		JDDimension iconDimension = new JDDimension((int)(dimension.getWidth()*0.8), (int)(dimension.getHeight()*0.8));


		if(direction == RouteInstruction.Direction.West) {
			triangle = getTriangleWest(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleWest(position,  iconDimension, buttonAnimationSizes[i]), parent);
			}
		}
		if(direction == RouteInstruction.Direction.East) {
			triangle = getTriangleEast(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleEast(position,  iconDimension, buttonAnimationSizes[i]), parent);
			}
		}
		if(direction == RouteInstruction.Direction.North) {
			triangle = getTriangleNorth(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleNorth(position, iconDimension, buttonAnimationSizes[i]),parent);
			}
		}
		if(direction == RouteInstruction.Direction.South) {
			triangle = getTriangleSouth(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleSouth(position,  iconDimension, buttonAnimationSizes[i]), parent);
			}
		}
	}

	private JDPoint[] getTriangleSouth(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
		int sizeX = clickAreaDimension.getWidth();
		int sizeY = clickAreaDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(centerX, centerY + ((sizeY /3)* drawScale)); // peak to bottom
		double y = centerY - ((sizeY / 3)*drawScale);
		result[1] = new JDPoint(centerX + ((sizeX/2)*drawScale), y); // upper right
		result[2] = new JDPoint(centerX - ((sizeX/2)*drawScale), y); // upper left
		return result;
	}

	private JDPoint[] getTriangleNorth(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
		int sizeX = clickAreaDimension.getWidth();
		int sizeY = clickAreaDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;


		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(centerX, centerY - ((sizeY /3)* drawScale)); // peak to top
		double y = centerY + ((sizeY / 3) * drawScale);
		result[1] = new JDPoint(centerX + ((sizeX/2)*drawScale), y); // lower right
		result[2] = new JDPoint(centerX - ((sizeX/2)*drawScale), y); // lower left
		return result;
	}

	private JDPoint[] getTriangleEast(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
		int sizeX = clickAreaDimension.getWidth();
		int sizeY = clickAreaDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(centerX + ((sizeX /3)* drawScale), centerY ); // peak to right
		double x = centerX - ((sizeX / 3) * drawScale);
		result[1] = new JDPoint(x, centerY - ((sizeY/2)*drawScale)); // upper
		result[2] = new JDPoint(x, centerY + ((sizeY/2)*drawScale)); // lower
		return result;
	}

	private JDPoint[] getTriangleWest(JDPoint position, JDDimension triangleDimension, double drawScale) {
		int sizeX = triangleDimension.getWidth();
		int sizeY = triangleDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(centerX - ((sizeX /3)* drawScale), centerY ); // peak to left
		double x = centerX + ((sizeX / 3) * drawScale);
		result[1] = new JDPoint(x, centerY - ((sizeY/2)*drawScale)); // upper
		result[2] = new JDPoint(x, centerY + ((sizeY/2)*drawScale)); // lower
		return result;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		super.handleTouchEvent(touch);
		EventManager.getInstance().fireEvent(new WannaMoveEvent(direction));
		return true;
	}




	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		int parentX = parent.getPositionOnScreen().getX();
		int parentY = parent.getPositionOnScreen().getY();
		g.fillTriangle(
				parentX + triangle[0].getX(),
				parentY + triangle[0].getY(),
				parentX + triangle[1].getX(),
				parentY + triangle[1].getY(),
				parentX + triangle[2].getX(),
				parentY + triangle[2].getY(), Colors.WHITE);


	}
}
