package de.jdungeon.androidapp.gui.smartcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.LockAction;
import figure.action.StepAction;
import figure.action.result.ActionResult;
import graphics.GraphicObjectRenderer;
import util.JDDimension;

import de.jdungeon.androidapp.gui.ContainerGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class SmartControl extends ContainerGUIElement {

	private final List<GUIElement> allGuiElements = new ArrayList<>();
	private final Collection<GUIElement> positionElements = new ArrayList<>();
	private final Collection<GUIElement> doorElements = new ArrayList<>();
	private final Collection<GUIElement> moveElements = new ArrayList<>();
	private final FigureInfo figure;
	public static final int DOOR_WIDTH = 36;
	private final int doorOuterBorderWidth;
	private final int doorThickness;
	private final JDDimension eastWest;
	private final JDDimension southNorth;
	private final int x02;
	private final int y13;
	private final JDPoint[] doorCoordinates;

	public SmartControl(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, FigureInfo figure) {
		super(position, dimension, screen, game);
		this.figure = figure;

		doorOuterBorderWidth = this.getDimension().getWidth() / 5;

		/*
		some util variables for the door coordinates
		 */
		doorThickness = (int) (DOOR_WIDTH / 2.8);
		eastWest = new JDDimension(doorThickness, DOOR_WIDTH);
		southNorth = new JDDimension(DOOR_WIDTH, doorThickness);
		x02 = this.getDimension().getWidth() / 2 - doorThickness;
		y13 = this.getDimension().getHeight() / 2 - DOOR_WIDTH / 2;
		doorCoordinates = new JDPoint[] {
				new JDPoint(x02, doorOuterBorderWidth),
				new JDPoint(this.getDimension().getWidth() - doorOuterBorderWidth - doorThickness, y13),
				new JDPoint(x02, this.getDimension().getHeight() - doorOuterBorderWidth - doorThickness),
				new JDPoint(doorOuterBorderWidth, y13)
		};

		updatePositionElements();
		updateDoorElements();
		updateMoveElements();
	}

	private void updateMoveElements() {
		moveElements.clear();
		int moveElementSize = 36;
		JDDimension moveElementDimension = new JDDimension(moveElementSize, moveElementSize);
		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();

		if (doors[0] != null && doors[0].isPassable()) {
			moveElements.add(new MoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, 0), moveElementDimension, this, RouteInstruction.Direction.North));
		}
		if (doors[1] != null && doors[1].isPassable()) {
			moveElements.add(new MoveElement(new JDPoint(getDimension().getWidth() - moveElementSize, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.East));
		}
		if (doors[2] != null && doors[2].isPassable()) {
			moveElements.add(new MoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, getDimension()
					.getWidth() - moveElementSize), moveElementDimension, this, RouteInstruction.Direction.South));
		}
		if (doors[3] != null && doors[3].isPassable()) {
			moveElements.add(new MoveElement(new JDPoint(0, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.West));
		}
	}

	private void updateDoorElements() {
		doorElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();
		for (int i = 0; i < 4; i++) {
			DoorInfo door = doors[i];
			if (door != null && door.hasLock()) {
				Action action = new LockAction(door);
				doorElements.add(new DoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, door.isLocked(), this.figure.hasKey(door), action));
			}
		}
	}

	private void updatePositionElements() {
		positionElements.clear();
		JDDimension dimension = this.getDimension();
		int positionAreaSize = (int) (dimension.getWidth() / 1.6);
		int positionAreaOffset = (dimension.getWidth() - positionAreaSize)/2;
		GraphicObjectRenderer renderer = new GraphicObjectRenderer(positionAreaSize);
		for (int i = 0; i < 8; i++) {
			JDPoint positionCoord = renderer.getPositionCoordModified(i);
			int positionSize = 20;
			Action action = new StepAction(i);
			if(figure.checkMovementAction(action).getValue() == ActionResult.VALUE_POSSIBLE) {
				positionElements.add(
						new PositionElement(
								new JDPoint(positionCoord.getX() + positionAreaOffset - positionSize / 2, positionCoord.getY() + positionAreaOffset - positionSize / 2),
								new JDDimension(positionSize, positionSize),
								this,
								action));
			}
			if(figure.getRoomInfo().fightRunning()) {
				FigureInfo otherFigure = this.figure.getRoomInfo().getPositionInRoom(i).getFigure();
				if(otherFigure != null && otherFigure.isHostile(this.figure)) {
					positionElements.add(
							new PositionElement(
									new JDPoint(positionCoord.getX() + positionAreaOffset - positionSize / 2, positionCoord.getY() + positionAreaOffset - positionSize / 2),
									new JDDimension(positionSize, positionSize),
									this,
									new AttackAction(otherFigure.getFighterID())));
				}

			}
		}
	}

	@Override
	protected List<GUIElement> getAllSubElements() {
		return allGuiElements;
	}

	@Override
	public void update(float time) {
		updateDoorElements();
		updatePositionElements();
		updateMoveElements();

		allGuiElements.clear();
		allGuiElements.addAll(doorElements);
		allGuiElements.addAll(positionElements);
		allGuiElements.addAll(moveElements);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	/*
	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		JDPoint pos = this.getPositionOnScreen();
		JDDimension dimension = this.getDimension();
		DrawUtils.drawRectangle(g, Color.BLUE, pos, dimension);

		paintElements(g, doorElements);
		paintElements(g, positionElements);
		paintElements(g, moveElements);

	}

	private void paintElements(Graphics g, Collection<GUIElement> elements) {
		for (GUIElement guiElement : elements) {
			if (guiElement.isVisible()) {
				guiElement.paint(g, null);
			}
		}
	}
	*/
}
