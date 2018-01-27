package de.jdungeon.androidapp.gui.smartcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import audio.AudioEffectsManager;
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
import figure.action.FleeAction;
import figure.action.LockAction;
import figure.action.StepAction;
import figure.action.result.ActionResult;
import graphics.GraphicObjectRenderer;
import item.ItemInfo;
import shrine.ShrineInfo;
import util.JDDimension;

import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.GUIControl;
import de.jdungeon.androidapp.gui.ContainerGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.androidapp.gui.SubGUIElementAnimated;
import de.jdungeon.androidapp.gui.activity.TakeItemButtonClickedEvent;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class SmartControlRoomPanel extends ContainerGUIElement implements EventListener {

	private final List<GUIElement> allGuiElements = new ArrayList<>();
	private final Collection<GUIElement> positionElements = new ArrayList<>();
	private final Collection<GUIElement> doorElements = new ArrayList<>();
	private final Collection<GUIElement> moveElements = new ArrayList<>();
	private final Collection<GUIElement> takeItemElements = new ArrayList<>();
	private final Collection<GUIElement> chestElements = new ArrayList<>();
	private final Collection<GUIElement> shrineElements = new ArrayList<>();
	private final GUIElement outerFrame;
	private final FigureInfo figure;
	private final GUIControl guiControl;
	public static final int DOOR_WIDTH = 36;
	private final int doorOuterBorderWidth;
	private final int doorThickness;
	private final JDDimension eastWest;
	private final JDDimension southNorth;
	private final int x02;
	private final int y13;
	private final JDPoint[] doorCoordinates;
	private boolean worldHasChanged = true;

	private final MoveElement moveWest;
	private final MoveElement moveEast;
	private final MoveElement moveNorth;
	private final MoveElement moveSouth;
	public static final int MOVE_ELEMENT_SIZE = 40;
	private final JDDimension moveElementDimension;
	private final int positionAreaSize;
	private final int positionAreaOffset;
	private final GraphicObjectRenderer renderer;
	private final SubGUIElementAnimated chestGUIELement;
	private final SubGUIElementAnimated takeGUIElement;
	private final PositionElement[] freeStepPositionElements = new PositionElement[8];
	private final PositionElement[] dotPositionElements = new PositionElement[8];
	private final JDPoint[] positionElementPositions = new JDPoint[8];
	private final JDDimension positionDimension;
	private final SubGUIElementAnimated shrineElement;
	private final GUIElement innerFrame;

	public SmartControlRoomPanel(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, FigureInfo figure, GUIControl actionAssembler) {
		super(position, dimension, screen, game);
		this.figure = figure;
		this.guiControl = actionAssembler;
		positionAreaSize = (int) (dimension.getWidth() / 1.6);
		positionAreaOffset = (dimension.getWidth() - positionAreaSize) / 2;
		renderer = new GraphicObjectRenderer(positionAreaSize);
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

		// draw border outerFrame
		JDPoint positionOuterFrame = new JDPoint(0, this.getDimension().getHeight() * 0.03);
		JDDimension dimensionOuterFrame = new JDDimension(this.getDimension().getWidth(), (int) (this.getDimension()
				.getHeight() * 0.94));
		outerFrame = createRectGUIElement(positionOuterFrame, dimensionOuterFrame);

		int innerFrameNorthBoundY = doorCoordinates[0].getY();
		int innerFrameEastBoundX = doorCoordinates[1].getX();
		int innerFrameSouthBoundY = doorCoordinates[2].getY();
		int innerFrameWestBoundX = doorCoordinates[3].getX();
		innerFrame = createRectGUIElement(new JDPoint(innerFrameWestBoundX, innerFrameNorthBoundY), new JDDimension(innerFrameEastBoundX - innerFrameWestBoundX + doorThickness, innerFrameSouthBoundY - innerFrameNorthBoundY + doorThickness));

		// prepare step position elements
		int positionElementSize = 32;
		int correctionX = 3;
		int correctionY = -3;
		positionDimension = new JDDimension(positionElementSize, positionElementSize);
		for (int i = 0; i < freeStepPositionElements.length; i++) {
			JDPoint positionCoord = renderer.getPositionCoordModified(i);
			positionElementPositions[i] = new JDPoint(correctionX + positionCoord.getX() + positionAreaOffset - positionElementSize / 2, correctionY + positionCoord
					.getY() + positionAreaOffset - positionElementSize / 2);
			freeStepPositionElements[i] = new PositionElement(
					positionElementPositions[i],
					positionDimension,
					this, null, Colors.WHITE, null, actionAssembler, i);
			dotPositionElements[i] = new PositionElement(
					new JDPoint(positionCoord.getX() + positionAreaOffset - 1, positionCoord.getY() + positionAreaOffset - 1),
					new JDDimension(3, 3), this, null, Colors.GRAY, null, actionAssembler);
		}

		moveElementDimension = new JDDimension(MOVE_ELEMENT_SIZE, MOVE_ELEMENT_SIZE);
		moveNorth = createMoveNorth(MOVE_ELEMENT_SIZE, moveElementDimension);
		moveEast = createMoveEast(MOVE_ELEMENT_SIZE, moveElementDimension);
		moveWest = createMoveWest(MOVE_ELEMENT_SIZE, moveElementDimension);
		moveSouth = createMoveSouth(MOVE_ELEMENT_SIZE, moveElementDimension);

		// shrine button
		int shrineElementSize = 30;
		final JDDimension shrineDimension = new JDDimension(shrineElementSize, shrineElementSize);
		final JDPoint posRelativeShrine = new JDPoint(getDimension().getWidth() * 19 / 28, getDimension()
				.getHeight() * 9 / 48);
		Image shrineImage = (Image) GUIImageManager.getImageProxy("guiItems/temple.png", game.getFileIO()
				.getImageLoader()).getImage();
		shrineElement = new SubGUIElementAnimated(posRelativeShrine, shrineDimension, this, shrineImage) {
			@Override
			public boolean handleTouchEvent(Input.TouchEvent touch) {
				super.handleTouchEvent(touch);
				EventManager.getInstance().fireEvent(new ShrineButtonClickedEvent());
				return true;
			}
		};

		// chest button
		int takeElementSizeX = 30;
		int takeElementSizeY = 18;
		final JDDimension chestDimension = new JDDimension(takeElementSizeX, takeElementSizeY);
		final JDPoint posRelative = new JDPoint(getDimension().getWidth() / 5, getDimension().getHeight() / 5);
		Image chestImage = (Image) GUIImageManager.getImageProxy("guiItems/Treasure-valuable-wealth-asset_3-512.png", game
				.getFileIO()
				.getImageLoader()).getImage();
		chestGUIELement = new SubGUIElementAnimated(posRelative, chestDimension, this, chestImage) {

			@Override
			public boolean handleTouchEvent(Input.TouchEvent touch) {
				super.handleTouchEvent(touch);
				AudioEffectsManager.playSound(AudioEffectsManager.CHEST_OPEN);
				EventManager.getInstance().fireEvent(new ChestItemButtonClickedEvent());
				guiControl.plugActions(guiControl.getActionAssembler().chestClicked(null, false));
				return true;
			}
		};

		// take from floor  button
		int takeElementSize = 28;
		final JDDimension takeDimension = new JDDimension(takeElementSize, takeElementSize);
		final JDPoint posRelativeTake = new JDPoint(getDimension().getWidth() / 2 - takeElementSize / 2, getDimension().getHeight() / 2 - takeElementSize / 2);
		takeGUIElement = new SubGUIElementAnimated(posRelativeTake, takeDimension, this, null) {
			@Override
			public boolean handleTouchEvent(Input.TouchEvent touch) {
				super.handleTouchEvent(touch);
				EventManager.getInstance().fireEvent(new TakeItemButtonClickedEvent());
				return true;
			}
		};

		EventManager.getInstance().registerListener(this);
		updateAllElementsIfNecessary();
	}

	private SubGUIElement createRectGUIElement(JDPoint position, JDDimension dimension) {
		return new SubGUIElement(
				position,
				dimension
				, this) {
			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public void paint(Graphics g, JDPoint viewportPosition) {
				JDPoint absolutePosition = new JDPoint(parent.getPositionOnScreen().getX() + this.getPositionOnScreen()
						.getX(),
						parent.getPositionOnScreen().getY() + this.getPositionOnScreen().getY());
				Color borderColor = Colors.WHITE;
				DrawUtils.drawRectangle(g, borderColor, absolutePosition, this.getDimension());
			}
		};
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

		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();

		Boolean fightRunning = figure.getRoomInfo().fightRunning();
		if (fightRunning != null && fightRunning) {
			int positionInRoomIndex = figure.getPositionInRoomIndex();
			if (positionInRoomIndex == 1) {
				if (checkFleeAction()) {
					moveElements.add(moveNorth);
				}
			}
			if (positionInRoomIndex == 3) {
				if (checkFleeAction()) {
					moveElements.add(moveEast);
				}
			}
			if (positionInRoomIndex == 5) {
				if (checkFleeAction()) {
					moveElements.add(moveSouth);
				}
			}
			if (positionInRoomIndex == 7) {
				if (checkFleeAction()) {
					moveElements.add(moveWest);
				}
			}
		}
		else {

			if (doors[0] != null && doors[0].isPassable()) {
				moveElements.add(moveNorth);
			}
			if (doors[1] != null && doors[1].isPassable()) {
				moveElements.add(moveEast);
			}
			if (doors[2] != null && doors[2].isPassable()) {
				moveElements.add(moveSouth);
			}
			if (doors[3] != null && doors[3].isPassable()) {
				moveElements.add(moveWest);
			}
		}
	}

	private boolean checkFleeAction() {
		return figure.checkAction(new FleeAction(false)).getValue() == ActionResult.VALUE_POSSIBLE;
	}

	private MoveElement createMoveWest(int moveElementSize, JDDimension moveElementDimension) {
		return new MoveElement(new JDPoint(0, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.West, figure, guiControl);
	}

	private MoveElement createMoveNorth(int moveElementSize, JDDimension moveElementDimension) {
		return new MoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, 0), moveElementDimension, this, RouteInstruction.Direction.North, figure, guiControl);
	}

	private MoveElement createMoveEast(int moveElementSize, JDDimension moveElementDimension) {
		return new MoveElement(new JDPoint(getDimension().getWidth() - moveElementSize, getDimension()
				.getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.East, figure, guiControl);
	}

	private MoveElement createMoveSouth(int moveElementSize, JDDimension moveElementDimension) {
		return new MoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, getDimension()
				.getWidth() - moveElementSize), moveElementDimension, this, RouteInstruction.Direction.South, figure, guiControl);
	}

	private void updateDoorElements() {
		doorElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();
		for (int i = 0; i < 4; i++) {
			DoorInfo door = doors[i];
			if (door != null && door.hasLock()) {
				Action action = new LockAction(door);
				DoorElement doorElement = new DoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, door
						.isLocked(), this.figure.hasKey(door), action, door, guiControl);
				doorElements.add(doorElement);
			}
		}
	}

	private void updateTakeElement() {
		takeItemElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		List<ItemInfo> items = roomInfo.getItems();
		if (items != null && !items.isEmpty()) {
			takeItemElements.add(takeGUIElement);
		}
	}

	private void updateShrineElement() {
		shrineElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		ShrineInfo shrine = roomInfo.getShrine();

		if (shrine != null) {

			shrineElements.add(shrineElement);
		}
	}

	private void updateChestElement() {
		chestElements.clear();
		ChestInfo chest = figure.getRoomInfo().getChest();
		if (chest == null) {
			return;
		}
		List<ItemInfo> items = figure.getRoomInfo().getChest().getItemList();

		if (!items.isEmpty()) {
			chestElements.add(chestGUIELement);
		}
	}

	private void updatePositionElements() {
		positionElements.clear();

		for (int i = 0; i < 8; i++) {
			FigureInfo otherFigure = this.figure.getRoomInfo().getPositionInRoom(i).getFigure();
			JDPoint positionCoord = renderer.getPositionCoordModified(i);
			Action action = new StepAction(i);
			// TODO: cache this
			if (figure.checkAction(action).getValue() == ActionResult.VALUE_POSSIBLE) {
				positionElements.add(freeStepPositionElements[i]);
			}
			else if (otherFigure != null) {
				// show other figures
				if (otherFigure.isHostile(this.figure)) {
					positionElements.add(
							new PositionElement(
									positionElementPositions[i],
									positionDimension,
									this,
									otherFigure, Colors.RED, otherFigure, guiControl));
				}
				else {
					de.jdungeon.game.Color color = Colors.GREEN;
					if (otherFigure.equals(figure)) {
						color = Colors.YELLOW;
					}
					positionElements.add(
							new PositionElement(
									positionElementPositions[i],
									positionDimension,
									this,
									null, color, otherFigure, guiControl));
				}
			}
			else {
				positionElements.add(
						dotPositionElements[i]);
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
		//allGuiElements.add(outerFrame);
		allGuiElements.add(innerFrame);
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
