package de.jdungeon.androidapp.gui.smartcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.WorldChangedEvent;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.FleeAction;
import figure.action.LockAction;
import figure.action.StepAction;
import figure.action.result.ActionResult;
import graphics.GraphicObjectRenderer;
import item.ItemInfo;
import shrine.ShrineInfo;
import util.JDDimension;

import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.gui.ContainerGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.androidapp.gui.SubGUIElementRelative;
import de.jdungeon.androidapp.gui.itemWheel.ChestItemButtonClickedEvent;
import de.jdungeon.androidapp.gui.itemWheel.TakeItemButtonClickedEvent;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class SmartControl extends ContainerGUIElement implements EventListener {

	private final List<GUIElement> allGuiElements = new ArrayList<>();
	private final Collection<GUIElement> positionElements = new ArrayList<>();
	private final Collection<GUIElement> doorElements = new ArrayList<>();
	private final Collection<GUIElement> moveElements = new ArrayList<>();
	private final Collection<GUIElement> takeItemElements = new ArrayList<>();
	private final Collection<GUIElement> chestElements = new ArrayList<>();
	private final Collection<GUIElement> shrineElements = new ArrayList<>();
	private final GUIElement frame;
	private final FigureInfo figure;
	public static final int DOOR_WIDTH = 36;
	private final int doorOuterBorderWidth;
	private final int doorThickness;
	private final JDDimension eastWest;
	private final JDDimension southNorth;
	private final int x02;
	private final int y13;
	private final JDPoint[] doorCoordinates;
	private boolean worldHasChanged = true;

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
		x02 = this.getDimension().getWidth() / 2 - DOOR_WIDTH / 2;
		y13 = this.getDimension().getHeight() / 2 - DOOR_WIDTH / 2;
		doorCoordinates = new JDPoint[] {
				new JDPoint(x02, doorOuterBorderWidth),
				new JDPoint(this.getDimension().getWidth() - doorOuterBorderWidth - doorThickness, y13),
				new JDPoint(x02, this.getDimension().getHeight() - doorOuterBorderWidth - doorThickness),
				new JDPoint(doorOuterBorderWidth, y13)
		};

		// draw border frame
		frame = new SubGUIElement(
				new JDPoint(0, this.getDimension().getHeight() * 0.03),
				new JDDimension(this.getDimension().getWidth(), (int)(this.getDimension().getHeight() * 0.94))
				, this) {
			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public void paint(Graphics g, JDPoint viewportPosition) {
				JDPoint absolutePosition = new JDPoint(parent.getPositionOnScreen().getX() + this.getPositionOnScreen()
						.getX(), parent.getPositionOnScreen()
						.getY() + this.getPositionOnScreen()
						.getY());
				Color borderColor = Colors.WHITE;
				DrawUtils.drawRectangle(g, borderColor, absolutePosition, this.getDimension());
			}
		};

		EventManager.getInstance().registerListener(this);
		updateAllElementsIfNecessary();
	}

	private void updateAllElementsIfNecessary() {
		if (worldHasChanged) {
			updatePositionElements();
			updateDoorElements();
			updateMoveElements();
			updateTakeElement();
			updateChestElement();
			updateShrineElement();
			worldHasChanged = false;
		}
	}

	private void updateMoveElements() {
		moveElements.clear();
		int moveElementSize = 40;
		JDDimension moveElementDimension = new JDDimension(moveElementSize, moveElementSize);
		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();

		if (figure.getRoomInfo().fightRunning()) {
			int positionInRoomIndex = figure.getPositionInRoomIndex();
			if (positionInRoomIndex == 1) {
				if (checkFleeAction()) {
					addDoorNorth(moveElementSize, moveElementDimension);
				}
			}
			if (positionInRoomIndex == 3) {
				if (checkFleeAction()) {
					addDoorEast(moveElementSize, moveElementDimension);
				}
			}
			if (positionInRoomIndex == 5) {
				if (checkFleeAction()) {
					addDoorSouth(moveElementSize, moveElementDimension);
				}
			}
			if (positionInRoomIndex == 7) {
				if (checkFleeAction()) {
					addDoorWest(moveElementSize, moveElementDimension);
				}
			}
		}
		else {

			if (doors[0] != null && doors[0].isPassable()) {
				addDoorNorth(moveElementSize, moveElementDimension);
			}
			if (doors[1] != null && doors[1].isPassable()) {
				addDoorEast(moveElementSize, moveElementDimension);
			}
			if (doors[2] != null && doors[2].isPassable()) {
				addDoorSouth(moveElementSize, moveElementDimension);
			}
			if (doors[3] != null && doors[3].isPassable()) {
				addDoorWest(moveElementSize, moveElementDimension);
			}
		}
	}

	private boolean checkFleeAction() {
		return figure.checkAction(new FleeAction(false)).getValue() == ActionResult.VALUE_POSSIBLE;
	}

	private void addDoorWest(int moveElementSize, JDDimension moveElementDimension) {
		moveElements.add(new MoveElement(new JDPoint(0, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.West));
	}

	private void addDoorNorth(int moveElementSize, JDDimension moveElementDimension) {
		moveElements.add(new MoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, 0), moveElementDimension, this, RouteInstruction.Direction.North));
	}

	private void addDoorEast(int moveElementSize, JDDimension moveElementDimension) {
		moveElements.add(new MoveElement(new JDPoint(getDimension().getWidth() - moveElementSize, getDimension()
				.getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.East));
	}

	private void addDoorSouth(int moveElementSize, JDDimension moveElementDimension) {
		moveElements.add(new MoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, getDimension()
				.getWidth() - moveElementSize), moveElementDimension, this, RouteInstruction.Direction.South));
	}

	private void updateDoorElements() {
		doorElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();
		for (int i = 0; i < 4; i++) {
			DoorInfo door = doors[i];
			if (door != null && door.hasLock()) {
				Action action = new LockAction(door);
				doorElements.add(new DoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, door
						.isLocked(), this.figure.hasKey(door), action, door));
			}
		}
	}

	private void updateTakeElement() {
		takeItemElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		List<ItemInfo> items = roomInfo.getItems();
		int takeElementSize = 25;
		if (!items.isEmpty()) {
			final JDDimension dimension = new JDDimension(takeElementSize, takeElementSize);
			final JDPoint posRelative = new JDPoint(getDimension().getWidth() / 2 - takeElementSize / 2, getDimension().getHeight() / 2 - takeElementSize / 2);
			takeItemElements.add(new SubGUIElementRelative(posRelative, dimension, this) {
				@Override
				public boolean handleTouchEvent(Input.TouchEvent touch) {
					super.handleTouchEvent(touch);
					EventManager.getInstance().fireEvent(new TakeItemButtonClickedEvent());
					return true;
				}
			});
		}
	}

	private void updateShrineElement() {
		shrineElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		ShrineInfo shrine = roomInfo.getShrine();
		int shrineElementSize = 30;
		if (shrine != null) {
			final JDDimension dimension = new JDDimension(shrineElementSize, shrineElementSize);
			final JDPoint posRelative = new JDPoint(getDimension().getWidth() * 5 / 7, getDimension()
					.getHeight() / 4 - shrineElementSize / 2);
			shrineElements.add(new SubGUIElementRelative(posRelative, dimension, this) {
				@Override
				public boolean handleTouchEvent(Input.TouchEvent touch) {
					super.handleTouchEvent(touch);
					EventManager.getInstance().fireEvent(new ShrineButtonClickedEvent());
					return true;
				}
			});
		}
	}

	private void updateChestElement() {
		chestElements.clear();
		ChestInfo chest = figure.getRoomInfo().getChest();
		if (chest == null) {
			return;
		}
		List<ItemInfo> items = figure.getRoomInfo().getChest().getItemList();
		int takeElementSizeX = 30;
		int takeElementSizeY = 20;
		if (!items.isEmpty()) {
			final JDDimension dimension = new JDDimension(takeElementSizeX, takeElementSizeY);
			final JDPoint posRelative = new JDPoint(getDimension().getWidth() / 5, getDimension().getHeight() / 5);
			chestElements.add(new SubGUIElementRelative(posRelative, dimension, this) {

				@Override
				public boolean handleTouchEvent(Input.TouchEvent touch) {
					super.handleTouchEvent(touch);
					EventManager.getInstance().fireEvent(new ChestItemButtonClickedEvent());
					return true;
				}
			});
		}
	}

	private void updatePositionElements() {
		positionElements.clear();
		JDDimension dimension = this.getDimension();
		int positionAreaSize = (int) (dimension.getWidth() / 1.6);
		int positionAreaOffset = (dimension.getWidth() - positionAreaSize) / 2;
		GraphicObjectRenderer renderer = new GraphicObjectRenderer(positionAreaSize);
		int correctionX = 3;
		int correctionY = -3;
		for (int i = 0; i < 8; i++) {
			JDPoint positionCoord = renderer.getPositionCoordModified(i);
			int positionSize = 26;
			Action action = new StepAction(i);
			FigureInfo otherFigure = this.figure.getRoomInfo().getPositionInRoom(i).getFigure();
			JDPoint position = new JDPoint(correctionX + positionCoord.getX() + positionAreaOffset - positionSize / 2, correctionY + positionCoord
					.getY() + positionAreaOffset - positionSize / 2);
			JDDimension positionDimension = new JDDimension(positionSize, positionSize);
			// TODO: cache this
			if (figure.checkAction(action).getValue() == ActionResult.VALUE_POSSIBLE) {
				positionElements.add(
						new PositionElement(
								position,
								positionDimension,
								this,
								action, Colors.WHITE, null));
			}
			else if (otherFigure != null) {
				// show other figures
				if (otherFigure.isHostile(this.figure)) {
					positionElements.add(
							new PositionElement(
									position,
									positionDimension,
									this,
									new AttackAction(otherFigure.getFighterID()), Colors.RED, otherFigure));
				}
				else {
					de.jdungeon.game.Color color = Colors.GREEN;
					if (otherFigure.equals(figure)) {
						color = Colors.YELLOW;
					}
					positionElements.add(
							new PositionElement(
									position,
									positionDimension,
									this,
									null, color, otherFigure));
				}
			}
			else {
				positionElements.add(
						new PositionElement(
								new JDPoint(positionCoord.getX() + positionAreaOffset - 1, positionCoord
										.getY() + positionAreaOffset - 1),
								new JDDimension(3, 3),
								this,
								null, Colors.GRAY, null));
			}
		}
	}

	@Override
	protected List<GUIElement> getAllSubElements() {
		return allGuiElements;
	}

	@Override
	public void update(float time) {
		updateAllElementsIfNecessary();
		allGuiElements.clear();
		allGuiElements.add(frame);
		allGuiElements.addAll(doorElements);
		allGuiElements.addAll(positionElements);
		allGuiElements.addAll(moveElements);
		allGuiElements.addAll(takeItemElements);
		allGuiElements.addAll(chestElements);
		allGuiElements.addAll(shrineElements);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Set<Class<? extends Event>> result = new HashSet<>();
		result.add(WorldChangedEvent.class);
		return result;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof WorldChangedEvent) {
			this.worldHasChanged = true;
		}
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
