package de.jdungeon.androidapp.gui.smartcontrol;

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
public class MoveElement extends SubGUIElement {

	private final GUIElement parent;
	private final RouteInstruction.Direction direction;
	private final JDPoint[] triangle;

	public MoveElement(JDPoint position, JDDimension dimension, GUIElement parent, RouteInstruction.Direction direction) {
		super(position, dimension, parent);
		this.parent = parent;
		this.direction = direction;

		triangle = new JDPoint[3];
		int sizeY = dimension.getHeight();
		int sizeX = dimension.getWidth();
		if(direction == RouteInstruction.Direction.West) {
			triangle[0] = new JDPoint(position.getX() + sizeX/3, position.getY()+ sizeY / 2); // peak to left
			triangle[1] = new JDPoint(position.getX()+ sizeX, position.getY()); // right upper
			triangle[2] = new JDPoint(position.getX()+ sizeX, position.getY()+ sizeY); // right lower
		}
		if(direction == RouteInstruction.Direction.East) {
			triangle[0] = new JDPoint(position.getX()+ sizeX - sizeX/3, position.getY()+ sizeY / 2); // peak to right
			triangle[1] = new JDPoint(position.getX(), position.getY()); // left upper
			triangle[2] = new JDPoint(position.getX(), position.getY()+ sizeY); // left lower
		}
		if(direction == RouteInstruction.Direction.North) {
			triangle[0] = new JDPoint(position.getX()+ sizeX /2, position.getY() + sizeY/3); // peak to top
			triangle[1] = new JDPoint(position.getX()+ sizeX, position.getY()+ sizeY); // lower right
			triangle[2] = new JDPoint(position.getX(), position.getY()+ sizeY); // lower left
		}
		if(direction == RouteInstruction.Direction.South) {
			triangle[0] = new JDPoint(position.getX()+ sizeX /2, position.getY() + sizeY - sizeY/3); // peak downwards
			triangle[1] = new JDPoint(position.getX()+ sizeX, position.getY()); // upper right
			triangle[2] = new JDPoint(position.getX(), position.getY()); // upper left
		}
	}

	@Override
	public void handleTouchEvent(Input.TouchEvent touch) {
		EventManager.getInstance().fireEvent(new WannaMoveEvent(direction));
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
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
