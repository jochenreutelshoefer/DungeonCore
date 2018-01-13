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
public class MoveElement extends SubGUIElement {

	private final GUIElement parent;
	private final RouteInstruction.Direction direction;
	private JDPoint[] triangle;

	private long buttonAnimationStart;
	private final int buttonAnimationStage = 0;
	private static final int buttonAnimationStepTime = 500;

	//public static float[] buttonAnimationSizes = new float[] {1.2f, 1.4f, 1.6f, 2.0f, 3.0f, 4.0f};
	public static float[] buttonAnimationSizes = new float[] {2f, 3, 4f, 5};
	private final JDPoint[][] animationShapes;

	public MoveElement(JDPoint position, JDDimension dimension, GUIElement parent, RouteInstruction.Direction direction) {
		super(position, dimension, parent);
		this.parent = parent;
		this.direction = direction;

		int sizeY = dimension.getHeight();
		int sizeX = dimension.getWidth();
		animationShapes = new JDPoint[buttonAnimationSizes.length][3];
		if(direction == RouteInstruction.Direction.West) {
			triangle = getTriangleWest(position, sizeX, sizeY);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = getTriangleWest(position, (int) (sizeX * buttonAnimationSizes[i]), (int) (sizeY * buttonAnimationSizes[i]));
			}
		}
		if(direction == RouteInstruction.Direction.East) {
			triangle = getTriangleEast(position, sizeX, sizeY);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = getTriangleEast(position, (int) (sizeX * buttonAnimationSizes[i]), (int) (sizeY * buttonAnimationSizes[i]));
			}
		}
		if(direction == RouteInstruction.Direction.North) {
			triangle = getTriangleNorth(position, sizeX, sizeY);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = getTriangleNorth(position, (int) (sizeX * buttonAnimationSizes[i]), (int) (sizeY * buttonAnimationSizes[i]));
			}
		}
		if(direction == RouteInstruction.Direction.South) {
			triangle = getTriangleSouth(position, sizeX, sizeY);
			for (int i = 0; i < buttonAnimationSizes.length; i++) {
				animationShapes[i] = getTriangleSouth(position, (int) (sizeX * buttonAnimationSizes[i]), (int) (sizeY * buttonAnimationSizes[i]));
			}
		}
	}

	public static JDPoint[] getTriangleSouth(JDPoint parentPosition, int sizeX, int sizeY) {
		int sizeBy10 = sizeX/10;
		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(parentPosition.getX() + sizeX /2, parentPosition.getY() + sizeY - sizeY/3); // peak downwards
		result[1] = new JDPoint(parentPosition.getX() + sizeX, parentPosition.getY()+  sizeBy10); // upper right
		result[2] = new JDPoint(parentPosition.getX(), parentPosition.getY() +  sizeBy10); // upper left
		return normalize(result);
	}

	public static JDPoint[] getTriangleNorth(JDPoint parentPosition, int sizeX, int sizeY) {
		int sizeBy10 = sizeX/10;
		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(parentPosition.getX() + sizeX /2, parentPosition.getY() + sizeY/3); // peak to top
		result[1] = new JDPoint(parentPosition.getX() + sizeX, parentPosition.getY()+ sizeY -   sizeBy10); // lower right
		result[2] = new JDPoint(parentPosition.getX(), parentPosition.getY()+ sizeY - sizeBy10); // lower left
		return normalize(result);
	}


	public static JDPoint[] getTriangleEast(JDPoint parentPosition, int sizeX, int sizeY) {
		int sizeBy10 = sizeX/10;
		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(parentPosition.getX() + sizeX - sizeX/3, parentPosition.getY()+ sizeY / 2); // peak to right
		result[1] = new JDPoint(parentPosition.getX() + sizeBy10, parentPosition.getY()); // left upper
		result[2] = new JDPoint(parentPosition.getX() + sizeBy10, parentPosition.getY()+ sizeY); // left lower
		return normalize(result);
	}

	public static JDPoint[] getTriangleWest(JDPoint parentPosition, int sizeX, int sizeY) {
		int sizeBy10 = sizeX/10;
		JDPoint[] result =  new JDPoint[3];
		result[0] = new JDPoint(parentPosition.getX() + sizeX/3, parentPosition.getY()+ sizeY / 2); // peak to left
		result[1] = new JDPoint(parentPosition.getX() + sizeX - sizeBy10, parentPosition.getY()); // right upper
		result[2] = new JDPoint(parentPosition.getX() + sizeX - sizeBy10, parentPosition.getY()+ sizeY); // right lower
		return normalize(result);
	}

	private static JDPoint[] normalize(JDPoint[] points) {
		int maxX = 0;
		int maxY = 0;
		int minY = Integer.MAX_VALUE;
		int minX = Integer.MAX_VALUE;
		for (JDPoint point : points) {
			int x = point.getX();
			if(x < minX) {
				minX = x;
			}
			if(x > maxX) {
				maxX = x;
			}
			int y = point.getY();
			if(y < minY) {
				minY = y;
			}
			if(y > maxY) {
				maxY = y;
			}
		}

		int dimensionXHalf = (maxX - minX)/2;
		int dimensionYHalf = (maxY - minY)/2;


		JDPoint [] pointsNormalized = new JDPoint[points.length];
		for (int i  = 0; i < points.length; i++) {
			pointsNormalized[i] = new JDPoint(points[i].getX() - dimensionXHalf, points[i].getY() - dimensionYHalf);
		}

		return pointsNormalized;
	}


	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		EventManager.getInstance().fireEvent(new WannaMoveEvent(direction));
		startButtonAnimation();
		return true;
	}



	private void startButtonAnimation() {
		buttonAnimationStart = System.currentTimeMillis();
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

		long elapsedTime = System.currentTimeMillis() - buttonAnimationStart;
		if(elapsedTime < buttonAnimationSizes.length * buttonAnimationStepTime) {
			int stage = (int)(elapsedTime / buttonAnimationStepTime);
			Log.w("aniButton", ""+stage);
			g.drawTriangle(parentX + animationShapes[stage][0].getX(),
					parentY + animationShapes[stage][0].getY(),
					parentX + animationShapes[stage][1].getX(),
					parentY + animationShapes[stage][1].getY(),
					parentX + animationShapes[stage][2].getX(),
					parentY + animationShapes[stage][2].getY(), Colors.WHITE);
		}

	}
}
