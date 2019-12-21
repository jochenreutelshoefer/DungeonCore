package de.jdungeon.app.gui.smartcontrol;

import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import util.JDDimension;

import de.jdungeon.app.GUIControl;
import de.jdungeon.app.gui.GUIElement;
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
	private final FigureInfo figure;
	private final GUIControl guiControl;
	private JDPoint[] triangle;
	private final int parentX;
	private final int parentY;
	private final int x0;
	private final int y0;
	private final int x1;
	private final int y1;
	private final int x2;
	private final int y2;

	public MoveElement(JDPoint position, JDDimension dimension, GUIElement parent, RouteInstruction.Direction direction, FigureInfo figure, GUIControl guiControl) {
		super(position, dimension, parent);
		this.parent = parent;
		this.direction = direction;
		this.figure = figure;
		this.guiControl = guiControl;

		JDDimension iconDimension = new JDDimension((int) (dimension.getWidth() * 0.8), (int) (dimension.getHeight() * 0.8));
		if (direction == RouteInstruction.Direction.West) {
			triangle = getTriangleWest(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleWest(position, iconDimension, buttonAnimationSizes[i]), parent);
			}
		}
		if (direction == RouteInstruction.Direction.East) {
			triangle = getTriangleEast(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleEast(position, iconDimension, buttonAnimationSizes[i]), parent);
			}
		}
		if (direction == RouteInstruction.Direction.North) {
			triangle = getTriangleNorth(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleNorth(position, iconDimension, buttonAnimationSizes[i]), parent);
			}
		}
		if (direction == RouteInstruction.Direction.South) {
			triangle = getTriangleSouth(position, iconDimension, 1.0);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = new Triangle(getTriangleSouth(position, iconDimension, buttonAnimationSizes[i]), parent);
			}
		}

		parentX = parent.getPositionOnScreen().getX();
		parentY = parent.getPositionOnScreen().getY();
		x0 = parentX + triangle[0].getX();
		y0 = parentY + triangle[0].getY();
		x1 = parentX + triangle[1].getX();
		y1 = parentY + triangle[1].getY();
		x2 = parentX + triangle[2].getX();
		y2 = parentY + triangle[2].getY();
	}

	private JDPoint[] getTriangleSouth(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
		int sizeX = clickAreaDimension.getWidth();
		int sizeY = clickAreaDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result = new JDPoint[3];
		result[0] = new JDPoint(centerX, centerY + ((sizeY / 3) * drawScale)); // peak to bottom
		double y = centerY - ((sizeY / 3) * drawScale);
		result[1] = new JDPoint(centerX + ((sizeX / 2) * drawScale), y); // upper right
		result[2] = new JDPoint(centerX - ((sizeX / 2) * drawScale), y); // upper left
		return result;
	}

	private JDPoint[] getTriangleNorth(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
		int sizeX = clickAreaDimension.getWidth();
		int sizeY = clickAreaDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result = new JDPoint[3];
		result[0] = new JDPoint(centerX, centerY - ((sizeY / 3) * drawScale)); // peak to top
		double y = centerY + ((sizeY / 3) * drawScale);
		result[1] = new JDPoint(centerX + ((sizeX / 2) * drawScale), y); // lower right
		result[2] = new JDPoint(centerX - ((sizeX / 2) * drawScale), y); // lower left
		return result;
	}

	private JDPoint[] getTriangleEast(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
		int sizeX = clickAreaDimension.getWidth();
		int sizeY = clickAreaDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result = new JDPoint[3];
		result[0] = new JDPoint(centerX + ((sizeX / 3) * drawScale), centerY); // peak to right
		double x = centerX - ((sizeX / 3) * drawScale);
		result[1] = new JDPoint(x, centerY - ((sizeY / 2) * drawScale)); // upper
		result[2] = new JDPoint(x, centerY + ((sizeY / 2) * drawScale)); // lower
		return result;
	}

	private JDPoint[] getTriangleWest(JDPoint position, JDDimension triangleDimension, double drawScale) {
		int sizeX = triangleDimension.getWidth();
		int sizeY = triangleDimension.getHeight();

		int centerX = position.getX() + getDimension().getWidth() / 2;
		int centerY = position.getY() + getDimension().getHeight() / 2;

		JDPoint[] result = new JDPoint[3];
		result[0] = new JDPoint(centerX - ((sizeX / 3) * drawScale), centerY); // peak to left
		double x = centerX + ((sizeX / 3) * drawScale);
		result[1] = new JDPoint(x, centerY - ((sizeY / 2) * drawScale)); // upper
		result[2] = new JDPoint(x, centerY + ((sizeY / 2) * drawScale)); // lower
		return result;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		super.handleTouchEvent(touch);
		final Boolean fightRunning = figure.getRoomInfo().fightRunning();
		if (fightRunning != null && fightRunning) {
			guiControl.plugActions(guiControl.getActionAssembler().wannaFlee());
		}
		else {
			guiControl.plugActions(guiControl.getActionAssembler().wannaWalk(direction.getValue()));
		}
		return true;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		g.fillTriangle(
				x0,
				y0,
				x1,
				y1,
				x2,
				y2, Colors.WHITE);

	}
}
