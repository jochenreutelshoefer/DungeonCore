package de.jdungeon.app.gui.smartcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import dungeon.ChestInfo;
import dungeon.JDPoint;
import dungeon.Position;
import event.Event;
import event.EventListener;
import event.EventManager;
import figure.hero.HeroInfo;
import graphics.ImageManager;
import gui.Paragraphable;
import item.ItemInfo;
import util.JDDimension;

import de.jdungeon.app.ActionController;
import de.jdungeon.app.gui.ContainerGUIElement;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.activity.ActivityPresenter;
import de.jdungeon.app.gui.activity.ChestItemActivityProvider;
import de.jdungeon.app.gui.activity.SkillActivityProvider;
import de.jdungeon.app.gui.activity.UseItemActivityProvider;
import de.jdungeon.app.gui.itemWheel.ChestItemWheel;
import de.jdungeon.app.gui.itemWheel.ItemWheel;
import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SmartControl extends ContainerGUIElement implements EventListener {

	private final List<GUIElement> guiElements = new ArrayList<>();
	private final HeroInfo figureInfo;
	private final ActionController guiControl;
	private final FocusManager focusManager;

	private ActivityPresenter itemWheelSkills;
	private ActivityPresenter itemWheelChest;
	private ActivityPresenter itemWheelHeroItems;

	private UIFeedback message = null;

	private boolean skillItemWheelShowing = false;
	private boolean chestItemWheelShowing = false;
	private SmartControlRoomPanel smartControl;
	public static final int SMART_CONTROL_SIZE = 290;

	public SmartControl(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, HeroInfo figure, ActionController actionAssembler, FocusManager focusManager) {
		super(position, dimension, game);
		this.figureInfo = figure;
		this.guiControl = actionAssembler;
		this.focusManager = focusManager;

		EventManager.getInstance().registerListener(this);

		initGUIElements();
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		boolean processed = super.handleTouchEvent(touch);
		if (processed) {
			clearMessage();
		}
		return processed;
	}

	@Override
	public void update(float time) {
		super.update(time);

		ChestInfo chest = figureInfo.getRoomInfo().getChest();
		if (this.chestItemWheelShowing && (chest == null || chest.getItemList().isEmpty())) {
			// we need to switch back to skills mode as user has not the respective button in this case
			switchRightItemWheel(null);
			EventManager.getInstance().fireEvent(new ToggleChestViewEvent());
		}
	}

	private void initGUIElements() {
		JDDimension screenSize = screen.getScreenSize();

		Image skillBackgroundImage = (Image) ImageManager.inventory_box_normal.getImage();

		/*
		add additional activities around the smart control room panel
		 */

		/*
		SkillImageManager skillImageManager = new SkillImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));
		JDDimension directionActivityDimension = new JDDimension(directionActivityTilesSize, directionActivityTilesSize);
		JDPoint scoutWestPosition = new JDPoint(smartControlRoomPanelPosition.getX()-directionActivityTilesSize, smartControlRoomPanelPosition.getY()+smartControlSize/2 - directionActivityTilesSize/2);
		JDPoint scoutEastPosition = new JDPoint(smartControlRoomPanelPosition.getX()+smartControlRoomPanelSize.getWidth(), smartControlRoomPanelPosition.getY()+smartControlSize/2 - directionActivityTilesSize/2);
		JDPoint scoutNorthPosition = new JDPoint(smartControlRoomPanelPosition.getX()+smartControlRoomPanelSize.getWidth()/2 - directionActivityTilesSize/2, smartControlRoomPanelPosition.getY() - directionActivityTilesSize);
		JDPoint scoutSouthPosition = new JDPoint(smartControlRoomPanelPosition.getX()+smartControlRoomPanelSize.getWidth()/2 - directionActivityTilesSize/2, smartControlRoomPanelPosition.getY() + smartControlRoomPanelSize.getHeight());
		addScoutButton(scoutWestPosition, directionActivityTilesSize, skillBackgroundImage, skillImageManager, directionActivityDimension, RouteInstruction.Direction.West);
		addScoutButton(scoutEastPosition, directionActivityTilesSize, skillBackgroundImage, skillImageManager, directionActivityDimension, RouteInstruction.Direction.East);
		addScoutButton(scoutNorthPosition, directionActivityTilesSize, skillBackgroundImage, skillImageManager, directionActivityDimension, RouteInstruction.Direction.North);
		addScoutButton(scoutSouthPosition, directionActivityTilesSize, skillBackgroundImage, skillImageManager, directionActivityDimension, RouteInstruction.Direction.South);
		*/

		/*
		 * init hero item wheel
		 */
		int screenWidth = getGame().getScreenWidth();
		int selectedIndexItem = 17;
		int screenWidthBy2 = screenWidth / 2;
		JDDimension itemWheelSize = new JDDimension(screenWidthBy2, screenWidthBy2);
		double wheelCenterY = getGame().getScreenHeight() * 1.8;
		UseItemActivityProvider useItemActivityProvider = new UseItemActivityProvider(figureInfo, game, guiControl, focusManager);
		itemWheelHeroItems = new ItemWheel(
				new JDPoint(0, wheelCenterY),
				itemWheelSize,
				figureInfo,
				screen,
				this.getGame(),
				useItemActivityProvider,
				selectedIndexItem,
				null,
				null,
				"Rucksack",
				0.1f
		);
		this.guiElements.add(itemWheelHeroItems);
		Logger.getLogger(this.getClass().getName()).info("ItemWheel HeroItems Created");

		@SuppressWarnings("SuspiciousNameCombination") JDPoint itemWheelPositionRightSide = new JDPoint(screenWidth - screenWidth / 50, wheelCenterY);
		/*
		 * init skills wheel
		 */
		int selectedIndex = 19;

		SkillActivityProvider skillActivityProvider = new SkillActivityProvider(figureInfo, game, guiControl, focusManager, this);
		itemWheelSkills = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize,
				figureInfo,
				screen,
				this.getGame(),
				skillActivityProvider,
				selectedIndex,
				skillBackgroundImage,
				"Aktivitäten");
		this.guiElements.add(itemWheelSkills);
		Logger.getLogger(this.getClass().getName()).info( "ItemWheel HeroSkills Created");

		/*
		init smart control
		 */
		JDPoint smartControlRoomPanelPosition = new JDPoint(screenSize.getWidth() - SMART_CONTROL_SIZE, screenSize.getHeight() / 2 + 70 - SMART_CONTROL_SIZE / 2);
		JDDimension smartControlRoomPanelSize = new JDDimension(SMART_CONTROL_SIZE, SMART_CONTROL_SIZE);
		smartControl = new SmartControlRoomPanel(
				smartControlRoomPanelPosition,
				smartControlRoomPanelSize, screen, this
				.getGame(), figureInfo, guiControl, this);

		/*
		init chest item wheel
		it shares position with the skill wheel, toggled on/off by the user
		 */
		Image chestBackGroundImage = (Image) GUIImageManager.getImageProxy(GUIImageManager.CHEST_OPEN, game.getFileIO()
				.getImageLoader()).getImage();
		ChestItemActivityProvider chestTakeActivityProvider = new ChestItemActivityProvider(figureInfo, game, this.guiControl);
		JDPoint chestWheelPosition = new JDPoint(smartControl.getPositionOnScreen()
				.getX() + SMART_CONTROL_SIZE / 4, smartControl.getPositionOnScreen().getY() + SMART_CONTROL_SIZE / 2);
		int chestWheelSizeX = screenSize.getWidth() / 6;
		int chestWheelSizeY = screenSize.getWidth() / 6;
		itemWheelChest = new ChestItemWheel(chestWheelPosition,
				new JDDimension(chestWheelSizeX, chestWheelSizeY),
				figureInfo,
				screen,
				this.getGame(),
				chestTakeActivityProvider,
				9,
				null,
				chestBackGroundImage,
				"Truhe",
				16);
		itemWheelChest.setVisible(false);
		this.guiElements.add(itemWheelChest);

		// add smart control as last one
		this.guiElements.add(smartControl);
	}

	@Override
	protected List<GUIElement> getAllSubElements() {
		return guiElements;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	public void focusTakenItem(ItemInfo item) {
		this.itemWheelHeroItems.highlightEntity(item);
		this.itemWheelChest.setHighlightOn(false);
	}

	private void setChestItemWheelVisible() {
		Object firstObject = itemWheelChest.highlightFirst();
		focusManager.setGuiFocusObject((Paragraphable) firstObject);
		itemWheelHeroItems.setHighlightOn(false);
		itemWheelChest.setVisible(true);
		itemWheelSkills.setVisible(false);
		chestItemWheelShowing = true;
		skillItemWheelShowing = false;
	}

	/**
	 * Switch/toggle between skill- and room-items ItemWheel
	 *
	 * @param event kind of the event
	 */
	private void switchRightItemWheel(Event event) {
		if (event == null) {
			setSkillWheelVisible();
		}
		else if (event instanceof ToggleChestViewEvent) {
			if (!chestItemWheelShowing) {
				if (!(figureInfo.getPos().getIndex() == Position.Pos.NW.getValue())) {
					guiControl.getActionAssembler().wannaStepToPosition(Position.Pos.NW);
				}
				setChestItemWheelVisible();
				smartControl.setVisible(false);
			}
			else {
				smartControl.setVisible(true);
				// switching chest item wheel off
				setSkillWheelVisible();
			}
		}
	}

	private void setSkillWheelVisible() {
		itemWheelChest.setVisible(false);
		itemWheelSkills.setVisible(true);
		chestItemWheelShowing = false;
		skillItemWheelShowing = true;
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(ToggleChestViewEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof ToggleChestViewEvent) {
			switchRightItemWheel(event);
		}
	}

	public UIFeedback getMessage() {
		return message;
	}

	private void clearMessage() {
		message = null;
	}

	public void setMessage(UIFeedback message) {
		this.message = message;
	}
}
