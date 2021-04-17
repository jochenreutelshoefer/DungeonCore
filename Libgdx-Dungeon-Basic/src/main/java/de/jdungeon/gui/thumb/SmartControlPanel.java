package de.jdungeon.gui.thumb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jdungeon.audio.AudioEffectsManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.WorldChangedEvent;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.graphics.GraphicObjectRenderer;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.location.LocationInfo;
import de.jdungeon.skill.FleeSkill;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.gui.LibgdxDrawUtils;
import de.jdungeon.gui.LibgdxTakeItemActivityProvider;
import de.jdungeon.gui.activity.DirectionScoutActivity;
import de.jdungeon.gui.LibgdxContainerGUIElement;
import de.jdungeon.gui.LibgdxGUIElement;
import de.jdungeon.world.PlayerController;
import org.graalvm.compiler.nodes.virtual.EnsureVirtualizedNode;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class SmartControlPanel extends LibgdxContainerGUIElement implements EventListener {


	private final List<LibgdxGUIElement> allGuiElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> positionElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> doorElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> moveElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> scoutElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> chestElements = new ArrayList<>();
	private final Collection<LibgdxGUIElement> shrineElements = new ArrayList<>();
	private final LibgdxGUIElement outerFrame;
	private final LibgdxGUIElement innerFrame;
	private final FigureInfo figure;
	private final PlayerController playerController;
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
	private final JDDimension moveElementDimension;
	private final int positionAreaSize;
	private final int positionAreaOffset;
	private final LibgdxSubGUIElementAnimated chestGUIELement;
	private final LibgdxPositionElement[] freeStepPositionElements = new LibgdxPositionElement[8];
	private final LibgdxPositionElement[] dotPositionElements = new LibgdxPositionElement[8];
	private final JDPoint[] positionElementPositions = new JDPoint[8];
	private final JDDimension positionDimension;
	private final LibgdxSubGUIElementAnimated shrineElement;

	private final LibgdxFloorItemPresenter floorItemPresenter;

	private static final JDPoint[] positionCoord = new JDPoint[8];
	private final JDPoint[] positionCoordModified = new JDPoint[8];
	private final InventoryImageManager inventoryImageManager;

	public SmartControlPanel(JDPoint position, JDDimension dimension, GUIImageManager guiImageManager, FigureInfo figure, PlayerController playerController) {
		super(position, dimension);
		this.figure = figure;
		this.playerController = playerController;
		positionAreaSize = (int) (dimension.getWidth() / 2.2);
		positionAreaOffset = (dimension.getWidth() - positionAreaSize) / 2;

		skillImageManager = new SkillImageManager(guiImageManager);
		inventoryImageManager = new InventoryImageManager(guiImageManager);


		int screenWidth = Gdx.app.getGraphics().getWidth();
		int MOVE_ELEMENT_SIZE = screenWidth / 21;
		int DOOR_WIDTH = (int) (MOVE_ELEMENT_SIZE * 0.9);


		/*
		some de.jdungeon.util variables for the door coordinates
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

		for (int i = 0; i < positionCoord.length; i++) {
			positionCoord[i] = GraphicObjectRenderer.getPositionCoordinates(Position.Pos.fromValue(i), positionAreaSize);
			positionCoordModified[i] = new JDPoint(positionCoord[i].getX() + positionAreaSize / 20, positionCoord[i].getY());
		}

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
		int positionElementSize = (int) (MOVE_ELEMENT_SIZE * 0.8);
		int correctionX = MOVE_ELEMENT_SIZE/22;		 // TODO: why in hell do we need this to fit correctly ???
		int correctionY = -1 * MOVE_ELEMENT_SIZE/16; // TODO: why in hell do we need this to fit correctly ???
		positionDimension = new JDDimension(positionElementSize, positionElementSize);
		for (int i = 0; i < freeStepPositionElements.length; i++) {
			JDPoint positionCoord = positionCoordModified[i];
			positionElementPositions[i] = new JDPoint(correctionX + positionCoord.getX() + positionAreaOffset - positionElementSize / 2, correctionY + positionCoord.getY() + positionAreaOffset - positionElementSize / 2);
			freeStepPositionElements[i] = new LibgdxPositionElement(positionElementPositions[i], positionDimension, this, null, Color.WHITE, null, playerController.getActionAssembler(), i);
			dotPositionElements[i] = new LibgdxPositionElement(
					new JDPoint(positionCoord.getX() + positionAreaOffset, positionCoord.getY() + positionAreaOffset),
					new JDDimension(correctionX, correctionX), this, null, Color.GRAY, null, playerController.getActionAssembler()
			);
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
		int shrineElementSize = (int) (MOVE_ELEMENT_SIZE * 0.75);
		final JDDimension shrineDimension = new JDDimension(shrineElementSize, shrineElementSize);
		final JDPoint posRelativeShrine = new JDPoint(getDimension().getWidth() * 19 / 28, getDimension()
				.getHeight() * 9 / 48);
		shrineElement = new LibgdxSubGUIElementAnimated(posRelativeShrine, shrineDimension, this, "guiItems/temple.png") {
			@Override
			public boolean handleClickEvent(int x, int y) {
				super.handleClickEvent(x, y);
				// todo: refactor using Activities
				SmartControlPanel.this.playerController.plugActions(SmartControlPanel.this.playerController.getActionAssembler().getActionAssemblerHelper(). wannaUseShrine(null, false));
				SmartControlPanel.this.playerController.getGameScreen().getFocusManager().setWorldFocusObject(SmartControlPanel.this.figure.getRoomInfo().getLocation());
				return true;
			}
		};

		// chest button
		int takeElementSizeX = (int) (MOVE_ELEMENT_SIZE * 0.75);
		int takeElementSizeY = takeElementSizeX * 3 / 5;
		final JDDimension chestDimension = new JDDimension(takeElementSizeX, takeElementSizeY);
		final JDPoint posRelative = new JDPoint(getDimension().getWidth() / 5, getDimension().getHeight() / 5);
		String chestImage = "guiItems/Treasure-valuable-wealth-asset_3-512.png";
		chestGUIELement = new LibgdxSubGUIElementAnimated(posRelative, chestDimension, this, chestImage) {

			@Override
			public boolean handleClickEvent(int x, int y) {
				super.handleClickEvent(x, y);
				AudioEffectsManager.playSound(AudioEffectsManager.CHEST_OPEN);
				// todo: refactor using Activities
				SmartControlPanel.this.playerController.plugActions(SmartControlPanel.this.playerController.getActionAssembler().getActionAssemblerHelper().chestClicked(null, false));
				return true;
			}
		};

		floorItemPresenter = new LibgdxFloorItemPresenter(
				this.getPositionOnScreen(),
				this.getDimension(),
				this,
				inventoryImageManager,
				new LibgdxTakeItemActivityProvider(figure, inventoryImageManager, this.playerController),
				null,
				screenWidth / 17);

		EventManager.getInstance().unregisterInstances(this.getClass()); // required to get rid of old instances (GC)
		EventManager.getInstance().registerListener(this);
		updateAllElementsIfNecessary();
	}

	private LibgdxActivityControlElement createScoutElementNorth() {
		DirectionScoutActivity scoutActivity = new DirectionScoutActivity(this.playerController, RouteInstruction.Direction.North);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveNorth.getPositionOnScreen().getX();
		int y = moveNorth.getPositionOnScreen().getY() + moveNorth.getDimension().getHeight() + doorThickness;
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveNorth.getDimension(), moveNorth.getParent(), scoutActivity, skillImage);
	}

	private LibgdxActivityControlElement createScoutElementSouth() {
		DirectionScoutActivity scoutActivity = new DirectionScoutActivity(this.playerController, RouteInstruction.Direction.South);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveSouth.getPositionOnScreen().getX();
		int y = moveSouth.getPositionOnScreen().getY() - moveSouth.getDimension().getHeight() - doorThickness;
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveSouth.getDimension(), moveSouth.getParent(), scoutActivity, skillImage);
	}

	private LibgdxActivityControlElement createScoutElementWest() {
		DirectionScoutActivity scoutActivity = new DirectionScoutActivity(this.playerController, RouteInstruction.Direction.West);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveWest.getPositionOnScreen().getX() + moveWest.getDimension().getWidth() + doorThickness;
		int y = moveWest.getPositionOnScreen().getY();
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveWest.getDimension(), moveWest.getParent(), scoutActivity, skillImage);
	}

	private LibgdxActivityControlElement createScoutElementEast() {
		DirectionScoutActivity scoutActivity = new DirectionScoutActivity(this.playerController, RouteInstruction.Direction.East);
		String skillImage = skillImageManager.getImage(scoutActivity.getObject());
		int x = moveEast.getPositionOnScreen().getX() - moveEast.getDimension().getWidth() - doorThickness;
		int y = moveEast.getPositionOnScreen().getY();
		return new LibgdxActivityControlElement(new JDPoint(x, y), moveEast.getDimension(), moveEast.getParent(), scoutActivity, skillImage);
	}

	public void animateEnemyBlobs() {
		for (LibgdxGUIElement positionElement : positionElements) {
			if (positionElement instanceof LibgdxPositionElement) {
				final RoomInfoEntity clickableObject = ((LibgdxPositionElement) positionElement).getClickableObject();
				if (clickableObject instanceof FigureInfo) {
					if (((FigureInfo) clickableObject).isHostile(this.figure)) {
						((LibgdxPositionElement) positionElement).startAnimation();
					}
				}
			}
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

	// we always have to skip one render cycle because the player didn't yet get
	// his new APs for the next round (and UI should show Options WITH AP)
	boolean skipOne = false;

	private void updateAllElementsIfNecessary() {
		if (worldHasChanged && ! skipOne && ! this.figure.isDead()) {
			updatePositionElements();
			updateDoorElements();
			updateMoveElements();
			updateChestElement();
			updateShrineElement();
			worldHasChanged = false;
		}
		if(skipOne) {
			// we have skipped one...
			skipOne = false;
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
		return figure.checkAction(figure.getSkill(FleeSkill.class).newActionFor(figure).get()).getSituation() == ActionResult.Situation.possible;
	}

	private LibgdxMoveElement createMoveWest(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(0, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.West, figure, playerController.getActionAssembler());
	}

	private LibgdxMoveElement createMoveNorth(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, 0), moveElementDimension, this, RouteInstruction.Direction.North, figure, playerController.getActionAssembler());
	}

	private LibgdxMoveElement createMoveEast(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() - moveElementSize, getDimension()
				.getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.East, figure, playerController.getActionAssembler());
	}

	private LibgdxMoveElement createMoveSouth(int moveElementSize, JDDimension moveElementDimension) {
		return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, getDimension()
				.getWidth() - moveElementSize), moveElementDimension, this, RouteInstruction.Direction.South, figure, playerController.getActionAssembler());
	}

	private void updateDoorElements() {
		doorElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		DoorInfo[] doors = roomInfo.getDoors();
		for (int i = 0; i < 4; i++) {
			DoorInfo door = doors[i];
			if (door != null && door.hasLock()) {
				LibgdxDoorElement doorElement = new LibgdxDoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, door
						.isLocked(), this.figure.hasKey(door), door, playerController.getActionAssembler());
				doorElements.add(doorElement);
			}
		}
	}

	private void updateShrineElement() {
		shrineElements.clear();
		RoomInfo roomInfo = figure.getRoomInfo();
		LocationInfo shrine = roomInfo.getLocation();

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

		RoomInfo roomInfo = this.figure.getRoomInfo();
		if (roomInfo == null) {
			// we have a concurrency problem here when finished the de.jdungeon.level via exit
			return;
		}
		for (int i = 0; i < 8; i++) {
			FigureInfo otherFigure = roomInfo.getPositionInRoom(i).getFigure();
			Action action = new StepAction(this.figure, i);
			// TODO: cache this
			if (figure.checkAction(action).getSituation() == ActionResult.Situation.possible) {
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
									otherFigure, Color.RED, otherFigure, playerController.getActionAssembler()));
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
									otherFigure, color, otherFigure, playerController.getActionAssembler()));
				}
			}
			else {
				if(Position.getMinDistanceFromTo(figure.getPositionInRoomIndex(), i) == 1) {
					ActionResult actionResult = figure.checkAction(action);
					Gdx.app.log(this.getClass().getName(), "Cannot step to neighbour position: "+i+ " action possible: "+ actionResult);
				}
				positionElements.add(dotPositionElements[i]);
			}
		}
	}

	@Override
	protected List<LibgdxGUIElement> getAllSubElements() {
		return allGuiElements;
	}

	@Override
	public void update(float time) {
		//if(!this.playerController.isDungeonTransactionLocked()) {
			updateAllElementsIfNecessary();
		//}

		// TODO: do we need to clear and reinsert all elements for every update ??
		allGuiElements.clear();
		//allGuiElements.add(outerFrame);
		allGuiElements.add(innerFrame);
		allGuiElements.addAll(doorElements);
		allGuiElements.addAll(positionElements);
		allGuiElements.addAll(moveElements);
		allGuiElements.addAll(scoutElements);
		//allGuiElements.addAll(takeItemElements);
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
			this.skipOne = true;
		}
	}
}