package de.jdungeon.gui.thumb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import audio.AudioEffectsManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
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
import game.RoomInfoEntity;
import graphics.GraphicObjectRenderer;
import item.ItemInfo;
import shrine.ShrineInfo;
import util.JDDimension;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.app.gui.activity.SkillActivityProvider;
import de.jdungeon.app.gui.activity.TakeItemActivityProvider;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.app.gui.smartcontrol.ShrineButtonClickedEvent;
import de.jdungeon.app.gui.smartcontrol.ToggleChestViewEvent;
import de.jdungeon.game.Game;
import de.jdungeon.gui.LibgdxDrawUtils;
import de.jdungeon.ui.LibgdxContainerGUIElement;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class SmartControlPanel extends LibgdxContainerGUIElement implements EventListener {

	public static final int SMART_CONTROL_SIZE = 290;

	private final List<LibgdxGUIElement> allGuiElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> positionElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> doorElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> moveElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> scoutElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> takeItemElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> chestElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> shrineElements = new ArrayList<>();
	private final LibgdxGUIElement outerFrame;
	private final FigureInfo figure;
	private final ActionAssembler guiControl;
	public static final int DOOR_WIDTH = 36;
	private final int doorOuterBorderWidth;
	private final int doorThickness;
	private final JDDimension eastWest;
	private final JDDimension southNorth;
	private final int x02;
	private final int y13;
	private final JDPoint[] doorCoordinates;
	private final LibgdxActivityControlElement scoutNorth;
	private final LibgdxActivityControlElement scoutSouth;
	private final LibgdxActivityControlElement scoutWest;
	private final LibgdxActivityControlElement scoutEast;
	private final SkillImageManager skillImageManager;
	private boolean worldHasChanged = true;
	private boolean visible = true;

	private final LibgdxMoveElement moveWest;
	private final LibgdxMoveElement moveEast;
	private final LibgdxMoveElement moveNorth;
	private final LibgdxMoveElement moveSouth;
	public static final int MOVE_ELEMENT_SIZE = 40;
	private final JDDimension moveElementDimension;
	private final int positionAreaSize;
	private final int positionAreaOffset;
	private final GraphicObjectRenderer renderer;
	private final LibgdxSubGUIElementAnimated chestGUIELement;
	private final LibgdxPositionElement[] freeStepPositionElements = new LibgdxPositionElement[8];
	private final LibgdxPositionElement[] dotPositionElements = new LibgdxPositionElement[8];
	private final JDPoint[] positionElementPositions = new JDPoint[8];
	private final JDDimension positionDimension;
	private final LibgdxSubGUIElementAnimated shrineElement;
	private final LibgdxGUIElement innerFrame;
	private final LibgdxFloorItemPresenter floorItemPresenter;

	public SmartControlPanel(JDPoint position, JDDimension dimension, Game game, FigureInfo figure, ActionAssembler actionAssembler) {
		super(position, dimension, game);
		this.figure = figure;
		this.guiControl = actionAssembler;
		positionAreaSize = (int) (dimension.getWidth() / 2.2);
		positionAreaOffset = (dimension.getWidth() - positionAreaSize) / 2;
		renderer = new GraphicObjectRenderer(positionAreaSize);

		skillImageManager = new SkillImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));


		/*
		some util variables for the door coordinates
		 */
		doorThickness = (int) (DOOR_WIDTH / 2.8);
		doorOuterBorderWidth = MOVE_ELEMENT_SIZE;
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
			freeStepPositionElements[i] = new LibgdxPositionElement(
					positionElementPositions[i],
					positionDimension,
					this, null, Color.WHITE, null, actionAssembler, i);
			dotPositionElements[i] = new LibgdxPositionElement(
					new JDPoint(positionCoord.getX() + positionAreaOffset - 1, positionCoord.getY() + positionAreaOffset - 1),
					new JDDimension(3, 3), this, null, Color.GRAY, null, actionAssembler);
		}

		moveElementDimension = new JDDimension(MOVE_ELEMENT_SIZE, MOVE_ELEMENT_SIZE);
		moveNorth = createMoveNorth(MOVE_ELEMENT_SIZE, moveElementDimension);
		moveEast = createMoveEast(MOVE_ELEMENT_SIZE, moveElementDimension);
		moveWest = createMoveWest(MOVE_ELEMENT_SIZE, moveElementDimension);
		moveSouth = createMoveSouth(MOVE_ELEMENT_SIZE, moveElementDimension);

		scoutNorth = createScoutElementNorth();
		scoutSouth = createScoutElementSouth();
		scoutWest = createScoutElementWest();
		scoutEast = createScoutElementEast();
		scoutElements.add(scoutNorth);
		scoutElements.add(scoutSouth);
		scoutElements.add(scoutWest);
		scoutElements.add(scoutEast);

		// shrine button
		int shrineElementSize = 30;
		final JDDimension shrineDimension = new JDDimension(shrineElementSize, shrineElementSize);
		final JDPoint posRelativeShrine = new JDPoint(getDimension().getWidth() * 19 / 28, getDimension()
				.getHeight() * 9 / 48);
		shrineElement = new LibgdxSubGUIElementAnimated(posRelativeShrine, shrineDimension, this, "guiItems/temple.png") {
			@Override
			public boolean handleClickEvent(int x, int y) {
				super.handleClickEvent(x, y);
				EventManager.getInstance().fireEvent(new ShrineButtonClickedEvent());
				return true;
			}
		};

		// chest button
		int takeElementSizeX = 30;
		int takeElementSizeY = 18;
		final JDDimension chestDimension = new JDDimension(takeElementSizeX, takeElementSizeY);
		final JDPoint posRelative = new JDPoint(getDimension().getWidth() / 5, getDimension().getHeight() / 5);
		String chestImage = "guiItems/Treasure-valuable-wealth-asset_3-512.png";
		chestGUIELement = new LibgdxSubGUIElementAnimated(posRelative, chestDimension, this, chestImage) {

			@Override
			public boolean handleClickEvent(int x, int y) {
				super.handleClickEvent(x, y);
				AudioEffectsManager.playSound(AudioEffectsManager.CHEST_OPEN);
				EventManager.getInstance().fireEvent(new ToggleChestViewEvent());
				guiControl.plugActions(guiControl.getActionAssembler().chestClicked(null, false));
				return true;
			}
		};

		floorItemPresenter = new LibgdxFloorItemPresenter(this.getPositionOnScreen(), this.getDimension(), this, game, new TakeItemActivityProvider(figure, game, guiControl), null, 50);

		EventManager.getInstance().registerListener(this);
		updateAllElementsIfNecessary();
	}

	private LibgdxActivityControlElement createScoutElementNorth() {
		ScoutActivity scoutActivity = new ScoutActivity(RouteInstruction.Direction.North);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveNorth.getPositionOnScreen().getX();
		int y = moveNorth.getPositionOnScreen().getY() + moveNorth.getDimension().getHeight() + doorThickness;
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveNorth.getDimension(), moveNorth.getParent(), scoutActivity, skillImage);
	}

	private LibgdxActivityControlElement createScoutElementSouth() {
		ScoutActivity scoutActivity = new ScoutActivity(RouteInstruction.Direction.South);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveSouth.getPositionOnScreen().getX();
		int y = moveSouth.getPositionOnScreen().getY() - moveSouth.getDimension().getHeight() - doorThickness;
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveSouth.getDimension(), moveSouth.getParent(), scoutActivity, skillImage);
	}

	private LibgdxActivityControlElement createScoutElementWest() {
		ScoutActivity scoutActivity = new ScoutActivity(RouteInstruction.Direction.West);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveWest.getPositionOnScreen().getX() + moveWest.getDimension().getWidth() + doorThickness;
		int y = moveWest.getPositionOnScreen().getY();
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveWest.getDimension(), moveWest.getParent(), scoutActivity, skillImage);
	}

	private LibgdxActivityControlElement createScoutElementEast() {
		ScoutActivity scoutActivity = new ScoutActivity(RouteInstruction.Direction.East);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveEast.getPositionOnScreen().getX() - moveEast.getDimension().getWidth() - doorThickness;
		int y = moveEast.getPositionOnScreen().getY();
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveEast.getDimension(), moveEast.getParent(), scoutActivity, skillImage);
	}

	class ScoutActivity extends AbstractExecutableActivity {

		private final RouteInstruction.Direction direction;

		public ScoutActivity(RouteInstruction.Direction direction) {
			this.direction = direction;
		}

		@Override
		public Object getObject() {
			return SkillActivityProvider.SCOUT;
		}

		@Override
		public RoomInfoEntity getTarget() {
			// TODO
			return null;
		}

		@Override
		public void execute() {
			guiControl.scoutingActivity(figure.getRoomInfo().getDoor(direction));
		}

		@Override
		public boolean isCurrentlyPossible() {
			final RoomInfo roomInfo = figure.getRoomInfo();
			if (roomInfo == null) {
				return false;
			}
			Boolean fightRunning = roomInfo.fightRunning();
			DoorInfo door = roomInfo
					.getDoor(direction);
			if (door == null) return false;
			PositionInRoomInfo scoutPosition = door
					.getPositionAtDoor(roomInfo, false);
			return fightRunning != null && !fightRunning && (!scoutPosition.isOccupied() || figure.equals(scoutPosition.getFigure()));
		}
	}

	private LibgdxSubGUIElement createRectGUIElement(JDPoint position, JDDimension dimension) {
		return new LibgdxSubGUIElement(position, dimension, this) {
			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public void paint(ShapeRenderer renderer) {
				JDPoint absolutePosition = new JDPoint(parent.getPositionOnScreen().getX() + this.getPositionOnScreen()
						.getX(),
						parent.getPositionOnScreen().getY() + this.getPositionOnScreen().getY());
				LibgdxDrawUtils.drawRectangle(renderer, com.badlogic.gdx.graphics.Color.WHITE, absolutePosition, this.getDimension());
			}

			@Override
			public void paint(SpriteBatch batch) {

			}

			@Override
			public boolean handleClickEvent(int screenX, int screenY) {
				return false;
			}
		};
	}

	private void updateAllElementsIfNecessary() {
		if (worldHasChanged) {
			updatePositionElements();
			updateDoorElements();
			updateMoveElements();
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

	private LibgdxMoveElement createMoveWest(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(0, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.West, figure, guiControl);
	}

	private LibgdxMoveElement createMoveNorth(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, 0), moveElementDimension, this, RouteInstruction.Direction.North, figure, guiControl);
	}

	private LibgdxMoveElement createMoveEast(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() - moveElementSize, getDimension()
				.getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.East, figure, guiControl);
	}

	private LibgdxMoveElement createMoveSouth(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, getDimension()
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
				LibgdxDoorElement doorElement = new LibgdxDoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, door
						.isLocked(), this.figure.hasKey(door), action, door, guiControl);
				doorElements.add(doorElement);
			}
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
							new LibgdxPositionElement(
									positionElementPositions[i],
									positionDimension,
									this,
									otherFigure, Color.RED, otherFigure, guiControl));
				}
				else {
					Color color = Color.GREEN;
					if (otherFigure.equals(figure)) {
						color = Color.YELLOW;
					}
					positionElements.add(
							new LibgdxPositionElement(
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
	protected List<LibgdxGUIElement> getAllSubElements() {
		return allGuiElements;
	}

	@Override
	public void update(float time) {
		updateAllElementsIfNecessary();
		// TODO: do we need to clear and reinsert all elements for every update ??
		allGuiElements.clear();
		//allGuiElements.add(outerFrame);
		allGuiElements.add(innerFrame);
		allGuiElements.addAll(doorElements);
		allGuiElements.addAll(positionElements);
		allGuiElements.addAll(moveElements);
		allGuiElements.addAll(scoutElements);
		allGuiElements.addAll(takeItemElements);
		allGuiElements.addAll(chestElements);
		allGuiElements.addAll(shrineElements);
		allGuiElements.add(floorItemPresenter);
		floorItemPresenter.update(time);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean value) {
		visible = value;
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
}