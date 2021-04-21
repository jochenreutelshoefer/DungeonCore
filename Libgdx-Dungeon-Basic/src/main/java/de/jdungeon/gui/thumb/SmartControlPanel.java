package de.jdungeon.gui.thumb;

import java.util.*;

import de.jdungeon.asset.Assets;
import de.jdungeon.audio.AudioEffectsManager;
import com.badlogic.gdx.Gdx;
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

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class SmartControlPanel extends LibgdxContainerGUIElement implements EventListener {


    private final List<LibgdxGUIElement> allGuiElements = new ArrayList<>();
    private final Collection<LibgdxGUIElement> doorElements = new ArrayList<>();
    private final Collection<LibgdxGUIElement> moveElements = new ArrayList<>();
    private final Collection<LibgdxGUIElement> scoutElements = new ArrayList<>();
    private final Collection<LibgdxGUIElement> chestElements = new ArrayList<>();
    private final Collection<LibgdxGUIElement> shrineElements = new ArrayList<>();
    private final LibgdxGUIElement innerFrame;
    private final FigureInfo figure;
    private final PlayerController playerController;
    private final int doorThickness;
    private final JDDimension eastWest;
    private final JDDimension southNorth;
    private final JDPoint[] doorCoordinates;
    private final SkillImageManager skillImageManager;
    private boolean worldHasChanged = true;
    private boolean visible = true;

    private final LibgdxMoveElement moveWest;
    private final LibgdxMoveElement moveEast;
    private final LibgdxMoveElement moveNorth;
    private final LibgdxMoveElement moveSouth;
    private final LibgdxSubGUIElementAnimated chestGUIElement;
    private final SCPositionElement[] scPositionElements = new SCPositionElement[8];
    private final LibgdxSubGUIElementAnimated shrineElement;

    private final LibgdxFloorItemPresenter floorItemPresenter;

    private static final JDPoint[] positionCoord = new JDPoint[8];

    public SmartControlPanel(JDPoint position, JDDimension dimension, GUIImageManager guiImageManager, FigureInfo figure, PlayerController playerController) {
        super(position, dimension);
        this.figure = figure;
        this.playerController = playerController;

        skillImageManager = new SkillImageManager(guiImageManager);
        InventoryImageManager inventoryImageManager = new InventoryImageManager(guiImageManager);


        int screenWidth = Gdx.app.getGraphics().getWidth();
        int MOVE_ELEMENT_SIZE = (int) (((float) dimension.getWidth()) / 7.0);
        int DOOR_WIDTH = (int) (MOVE_ELEMENT_SIZE * 0.9);


		/*
		some util variables for the door coordinates
		 */
        doorThickness = (int) (DOOR_WIDTH / 2.8);
        eastWest = new JDDimension(doorThickness, DOOR_WIDTH);
        southNorth = new JDDimension(DOOR_WIDTH, doorThickness);
        int x02 = this.getDimension().getWidth() / 2 - DOOR_WIDTH / 2;
        int y13 = this.getDimension().getHeight() / 2 - DOOR_WIDTH / 2;
        doorCoordinates = new JDPoint[]{
                new JDPoint(x02, MOVE_ELEMENT_SIZE),
                new JDPoint(this.getDimension().getWidth() - MOVE_ELEMENT_SIZE - doorThickness, y13),
                new JDPoint(x02, this.getDimension().getHeight() - MOVE_ELEMENT_SIZE - doorThickness),
                new JDPoint(MOVE_ELEMENT_SIZE, y13)
        };
        // create all door elements
        for (int i = 0; i < 4; i++) {
                LibgdxDoorElement doorElement = new LibgdxDoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, RouteInstruction.Direction.fromInteger(i+1), playerController.getActionAssembler());
                doorElements.add(doorElement);
        }

        int positionAreaSize = (int) (dimension.getWidth() / 2.2);
        int positionAreaOffset = (dimension.getWidth() - positionAreaSize) / 2;
        JDPoint[] positionCoordModified = new JDPoint[8];
        for (int i = 0; i < positionCoord.length; i++) {
            positionCoord[i] = GraphicObjectRenderer.getPositionCoordinates(Position.Pos.fromValue(i), positionAreaSize);
            positionCoordModified[i] = new JDPoint(positionCoord[i].getX() + positionAreaSize / 20, positionCoord[i].getY());
        }

        // prepare step position elements
        int positionElementSize = (int) (MOVE_ELEMENT_SIZE * 0.8);
        int correctionX = MOVE_ELEMENT_SIZE / 22;         // TODO: why in hell do we need this to fit correctly ???
        int correctionY = -1 * MOVE_ELEMENT_SIZE / 16; // TODO: why in hell do we need this to fit correctly ???
        JDDimension positionDimension = new JDDimension(positionElementSize, positionElementSize);
        JDPoint[] positionElementPositions = new JDPoint[8];
        for (int i = 0; i < positionElementPositions.length; i++) {
            JDPoint currentPosCoordModified = positionCoordModified[i];
            positionElementPositions[i] = new JDPoint(correctionX + currentPosCoordModified.getX() + positionAreaOffset - positionElementSize / 2, correctionY + currentPosCoordModified.getY() + positionAreaOffset - positionElementSize / 2);
            scPositionElements[i] = new SCPositionElement(positionElementPositions[i], positionDimension, this, playerController.getActionAssembler(), i);
            this.allGuiElements.add(scPositionElements[i]);
        }

        // draw border outerFrame
        /*
        JDPoint positionOuterFrame = new JDPoint(0, this.getDimension().getHeight() * 0.03);
        JDDimension dimensionOuterFrame = new JDDimension(this.getDimension().getWidth(), (int) (this.getDimension()
                .getHeight() * 0.94));
        outerFrame = createRectGUIElement(positionOuterFrame, dimensionOuterFrame);

         */

        int innerFrameNorthBoundY = doorCoordinates[0].getY();
        int innerFrameEastBoundX = doorCoordinates[1].getX();
        int innerFrameSouthBoundY = doorCoordinates[2].getY();
        int innerFrameWestBoundX = doorCoordinates[3].getX();
        innerFrame = createRectGUIElement(new JDPoint(innerFrameWestBoundX, innerFrameNorthBoundY), new JDDimension(innerFrameEastBoundX - innerFrameWestBoundX + doorThickness, innerFrameSouthBoundY - innerFrameNorthBoundY + doorThickness));


        JDDimension moveElementDimension = new JDDimension(MOVE_ELEMENT_SIZE, MOVE_ELEMENT_SIZE);
        moveNorth = createMoveNorth(moveElementDimension);
        moveEast = createMoveEast(moveElementDimension);
        moveWest = createMoveWest(moveElementDimension);
        moveSouth = createMoveSouth(moveElementDimension);

        LibgdxActivityControlElement scoutNorth = createScoutElementNorth();
        LibgdxActivityControlElement scoutSouth = createScoutElementSouth();
        LibgdxActivityControlElement scoutWest = createScoutElementWest();
        LibgdxActivityControlElement scoutEast = createScoutElementEast();
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
                SmartControlPanel.this.playerController.plugActions(SmartControlPanel.this.playerController.getActionAssembler().getActionAssemblerHelper().wannaUseShrine(null, false));
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
        chestGUIElement = new LibgdxSubGUIElementAnimated(posRelative, chestDimension, this, chestImage) {

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
        updateAllElementsIfNecessary(0, 0);
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
        for (LibgdxGUIElement positionElement : scPositionElements) {
            if (positionElement instanceof LibgdxPositionElement) {
                final RoomInfoEntity clickableObject = ((LibgdxPositionElement) positionElement).getClickableObject();
                if (clickableObject instanceof FigureInfo) {
                    if (((FigureInfo) clickableObject).isHostile(this.figure)) {
                       // TODO: animate if attack was clicked, but the target is not unique because of multiple enemgies!!
                        // ((SCPositionElement) positionElement).startAnimation();
                    }
                }
            }
        }
    }

    private LibgdxSubGUIElement createRectGUIElement(JDPoint position, JDDimension dimension) {
        return new LibgdxSubGUIElement(position, dimension, this) {

            private int height;
            private int width;
            private JDPoint absolutePosition;

            @Override
            public boolean isVisible() {
                return true;
            }

            @Override
            public void update(float time, int round) {
                absolutePosition = new JDPoint(parent.getPositionOnScreen().getX() + this.getPositionOnScreen()
                        .getX(),
                        parent.getPositionOnScreen().getY() + this.getPositionOnScreen().getY());
                width = this.getDimension().getWidth();
                height = this.getDimension().getHeight();
            }

            @Override
            public void paint(ShapeRenderer renderer) {
                //LibgdxDrawUtils.drawRectangle(renderer, com.badlogic.gdx.graphics.Color.WHITE, absolutePosition, this.getDimension());
            }

            @Override
            public void paint(SpriteBatch batch, float deltaTime) {
                batch.draw(Assets.instance.getAtlasRegion(GUIImageManager.SC_RECT, Assets.instance.getGuiAtlas()), absolutePosition.getX(), absolutePosition.getY(), width, height);
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

    private void updateAllElementsIfNecessary(float deltaTime, int round) {
        if (worldHasChanged /*&& ! skipOne*/ && !this.figure.isDead()) {
            innerFrame.update(deltaTime, round);
            updatePositionElements(deltaTime, round);
            updateDoorElements(deltaTime, round);
            updateMoveElements();
            updateChestElement();
            updateShrineElement();
            worldHasChanged = false;
        }
        if (skipOne) {
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
        } else {

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

    private LibgdxMoveElement createMoveWest(JDDimension moveElementDimension) {
        int moveElementSize = moveElementDimension.getWidth();
        return new LibgdxMoveElement(new JDPoint(0, getDimension().getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.West, figure, playerController.getActionAssembler());
    }

    private LibgdxMoveElement createMoveNorth(JDDimension moveElementDimension) {
        int moveElementSize = moveElementDimension.getWidth();
        return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, 0), moveElementDimension, this, RouteInstruction.Direction.North, figure, playerController.getActionAssembler());
    }

    private LibgdxMoveElement createMoveEast(JDDimension moveElementDimension) {
        int moveElementSize = moveElementDimension.getWidth();
        return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() - moveElementSize, getDimension()
                .getHeight() / 2 - moveElementSize / 2), moveElementDimension, this, RouteInstruction.Direction.East, figure, playerController.getActionAssembler());
    }

    private LibgdxMoveElement createMoveSouth(JDDimension moveElementDimension) {
        int moveElementSize = moveElementDimension.getWidth();
        return new LibgdxMoveElement(new JDPoint(getDimension().getWidth() / 2 - moveElementSize / 2, getDimension()
                .getWidth() - moveElementSize), moveElementDimension, this, RouteInstruction.Direction.South, figure, playerController.getActionAssembler());
    }

    private void updateDoorElements(float deltaTime, int round) {
        for (LibgdxGUIElement doorElement : doorElements) {
            doorElement.update(deltaTime, round);
        }
        /*
        doorElements.clear();
        RoomInfo roomInfo = figure.getRoomInfo();
        DoorInfo[] doors = roomInfo.getDoors();
        for (int i = 0; i < 4; i++) {
            DoorInfo door = doors[i];
            if (door != null && door.hasLock()) {
                LibgdxDoorElement doorElement = new LibgdxDoorElement(doorCoordinates[i], (i == 0 || i == 2) ? southNorth : eastWest, this, RouteInstruction.Direction.fromInteger(i+1), playerController.getActionAssembler());
                doorElements.add(doorElement);
            }
        }

         */
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
            chestElements.add(chestGUIElement);
        }
    }

    private void updatePositionElements(float deltaTime, int round) {

        RoomInfo roomInfo = this.figure.getRoomInfo();
        if (roomInfo == null) {
            // we have a concurrency problem here when finished the de.jdungeon.level via exit
            return;
        }
        for (int i = 0; i < 8; i++) {
            this.scPositionElements[i].update(deltaTime, round);
        }
    }

    @Override
    protected List<LibgdxGUIElement> getAllSubElements() {
        return allGuiElements;
    }

    @Override
    public void update(float deltaTime, int round) {
        //if(!this.playerController.isDungeonTransactionLocked()) {
        updateAllElementsIfNecessary(deltaTime, round);
        //}

        // TODO: do we need to clear and reinsert all elements for every update ??
        allGuiElements.clear();
        //allGuiElements.add(outerFrame);
        allGuiElements.add(innerFrame);
        allGuiElements.addAll(doorElements);
        //allGuiElements.addAll(positionElements);
        allGuiElements.addAll(Arrays.asList(scPositionElements));
        allGuiElements.addAll(moveElements);
        allGuiElements.addAll(scoutElements);
        //allGuiElements.addAll(takeItemElements);
        allGuiElements.addAll(chestElements);
        allGuiElements.addAll(shrineElements);
        allGuiElements.add(floorItemPresenter);
        floorItemPresenter.update(deltaTime, round);
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