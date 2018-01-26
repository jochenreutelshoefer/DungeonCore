package de.jdungeon.androidapp.gui.smartcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dungeon.ChestInfo;
import dungeon.Dir;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import event.ActionEvent;
import event.Event;
import event.EventListener;
import event.EventManager;
import figure.action.StepAction;
import figure.hero.HeroInfo;
import game.RoomInfoEntity;
import graphics.ImageManager;
import gui.Paragraphable;
import item.ItemInfo;
import util.JDDimension;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.gui.ContainerGUIElement;
import de.jdungeon.androidapp.gui.FocusManager;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.activity.AbstractExecutableActivity;
import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.ActivityPresenter;
import de.jdungeon.androidapp.gui.activity.ActivityProvider;
import de.jdungeon.androidapp.gui.activity.ChestItemActivityProvider;
import de.jdungeon.androidapp.gui.activity.SimpleActivityProvider;
import de.jdungeon.androidapp.gui.activity.SingleActivityPresenter;
import de.jdungeon.androidapp.gui.activity.SkillActivityProvider;
import de.jdungeon.androidapp.gui.activity.TakeItemActivityProvider;
import de.jdungeon.androidapp.gui.activity.TakeItemButtonClickedEvent;
import de.jdungeon.androidapp.gui.activity.UseItemActivityProvider;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheel;
import de.jdungeon.androidapp.gui.skillselection.SkillImageManager;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SmartControl extends ContainerGUIElement implements EventListener {

	private final List<GUIElement> guiElements = new ArrayList<>();
	private final HeroInfo figureInfo;
	private final Control actionAssembler;
	private final FocusManager focusManager;

	private ActivityPresenter itemWheelRoomItems;
	private ActivityPresenter itemWheelSkills;
	private ActivityPresenter itemWheelChest;
	private ActivityPresenter itemWheelHeroItems;

	private boolean roomItemWheelShowing = false;
	private boolean skillItemWheelShowing = false;
	private boolean chestItemWheelShowing = false;



	public SmartControl(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, HeroInfo figure, Control actionAssembler, FocusManager focusManager) {
		super(position, dimension, screen, game);
		this.figureInfo = figure;
		this.actionAssembler = actionAssembler;
		this.focusManager = focusManager;

		EventManager.getInstance().registerListener(this);

		initGUIElements();
	}

	@Override
	public void update(float time) {
		super.update(time);
		ChestInfo chest = figureInfo.getRoomInfo().getChest();
		if ((this.roomItemWheelShowing && figureInfo.getRoomInfo().getItems().isEmpty())
				||
				(this.chestItemWheelShowing && (chest == null || chest.getItemList().isEmpty()))) {
			// we need to switch back to skills mode as user has not the respective button in this case
			switchRightItemWheel(null);
		}


	}

	private void initGUIElements() {
		/*
		init smart control
		 */
		int smartControlSize = 220;
		int directionActivityTilesSize = 35;
		JDDimension screenSize = screen.getScreenSize();
		JDPoint smartControlRoomPanelPosition = new JDPoint(screenSize.getWidth() - smartControlSize - 2 * directionActivityTilesSize, screenSize.getHeight() / 2 + 70 - smartControlSize / 2);
		JDDimension smartControlRoomPanelSize = new JDDimension(smartControlSize, smartControlSize);
		SmartControlRoomPanel smartControl = new SmartControlRoomPanel(
				smartControlRoomPanelPosition,
				smartControlRoomPanelSize, screen, this
				.getGame(), figureInfo, actionAssembler);
		this.guiElements.add(smartControl);

		Image skillBackgroundImage = (Image) ImageManager.inventory_box_normal.getImage();

		/*
		add additional activities around the smart control room panel
		 */
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

		/*
		 * init hero item wheel
		 */
		int screenWidth = getGame().getScreenWidth();
		int selectedIndexItem = 17;
		int screenWidthBy2 = screenWidth / 2;
		JDDimension itemWheelSize = new JDDimension(screenWidthBy2, screenWidthBy2);
		double wheelCenterY = getGame().getScreenHeight() * 1.8;
		UseItemActivityProvider useItemActivityProvider = new UseItemActivityProvider(figureInfo, game, actionAssembler, focusManager);
		itemWheelHeroItems = new ItemWheel(new JDPoint(0, wheelCenterY),
				itemWheelSize, figureInfo, screen, this.getGame(),
				useItemActivityProvider,
				selectedIndexItem, null, null, "Rucksack");
		this.guiElements.add(itemWheelHeroItems);

		@SuppressWarnings("SuspiciousNameCombination") JDPoint itemWheelPositionRightSide = new JDPoint(screenWidth - screenWidth / 50, wheelCenterY);
		/*
		 * init skills wheel
		 */
		int selectedIndex = 19;

		SkillActivityProvider skillActivityProvider = new SkillActivityProvider(figureInfo, game, actionAssembler, focusManager);
		itemWheelSkills = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize, figureInfo, screen, this.getGame(),
				skillActivityProvider,
				selectedIndex,
				skillBackgroundImage, null, "Aktivitäten");
		this.guiElements.add(itemWheelSkills);

		/*
		init room item wheel
		it shares position with the skill wheel, toggled on/off by the user
		 */
		Image floorBackGroundImage = (Image) GUIImageManager.getImageProxy(GUIImageManager.FLOOR_BG, game.getFileIO()
				.getImageLoader()).getImage();

		TakeItemActivityProvider takeActivityProvider = new TakeItemActivityProvider(figureInfo, game);
		itemWheelRoomItems = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize, figureInfo, screen, this.getGame(),
				takeActivityProvider,
				selectedIndex,
				null, floorBackGroundImage, "Boden");
		itemWheelRoomItems.setVisible(false);
		this.guiElements.add(itemWheelRoomItems);


		/*
		init chest item wheel
		it shares position with the skill wheel, toggled on/off by the user
		 */
		Image chestBackGroundImage = (Image) GUIImageManager.getImageProxy(GUIImageManager.CHEST_OPEN, game.getFileIO()
				.getImageLoader()).getImage();
		ChestItemActivityProvider chestTakeActivityProvider = new ChestItemActivityProvider(figureInfo, game);
		itemWheelChest = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize, figureInfo, screen, this.getGame(),
				chestTakeActivityProvider,
				selectedIndex,
				null, chestBackGroundImage, "Truhe");
		itemWheelChest.setVisible(false);
		this.guiElements.add(itemWheelChest);


	}

	private void addScoutButton(JDPoint tilePosition, int directionActivityTilesSize, Image skillBackgroundImage, SkillImageManager skillImageManager, JDDimension directionActivityDimension, final RouteInstruction.Direction dir) {
			Activity scoutActivity = new AbstractExecutableActivity() {
			@Override
			public void execute() {
				actionAssembler.scoutingActivity(getTarget());
			}

			@Override
			public boolean isCurrentlyPossible() {
				RoomInfo room = figureInfo.getRoomInfo();
				PositionInRoomInfo pos = room.getPositionInRoom(7);
				Boolean noFight = !(room.fightRunning() != null && room.fightRunning().booleanValue());
				return noFight && room.getDoor(dir) != null && (!pos.isOccupied() || pos
						.getFigure().equals(figureInfo));
			}

			@Override
			public Object getObject() {
				return SkillActivityProvider.SCOUT;
			}

			@Override
			public RoomInfoEntity getTarget() {
				return figureInfo.getRoomInfo().getDoor(dir);
			}
		};
		ActivityProvider simpleProvider = new SimpleActivityProvider(scoutActivity, skillImageManager.getImage(SkillActivityProvider.SCOUT));
		ActivityPresenter scoutWestPresenter = new SingleActivityPresenter(tilePosition, directionActivityDimension, screen, game, simpleProvider, skillBackgroundImage, directionActivityTilesSize);
		this.guiElements.add(scoutWestPresenter);
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		return super.hasPoint(p)
				|| (itemWheelChest.isVisible() && itemWheelChest.hasPoint(p))
				|| (itemWheelHeroItems.isVisible() && itemWheelHeroItems.hasPoint(p))
				|| (itemWheelRoomItems.isVisible() && itemWheelRoomItems.hasPoint(p))
				|| (itemWheelSkills.isVisible() && itemWheelSkills.hasPoint(p));
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
		this.itemWheelRoomItems.setHighlightOn(false);
	}

	private void setChestItemWheelVisible() {
		Object firstObject = itemWheelChest.highlightFirst();
		focusManager.setGuiFocusObject((Paragraphable)firstObject);
		itemWheelHeroItems.setHighlightOn(false);

		itemWheelChest.setVisible(true);
		itemWheelRoomItems.setVisible(false);
		itemWheelSkills.setVisible(false);
		roomItemWheelShowing = false;
		chestItemWheelShowing = true;
		skillItemWheelShowing = false;
	}

	private void setRoomItemsWheelVisible() {
		Object firstObject = itemWheelRoomItems.highlightFirst();
		focusManager.setGuiFocusObject((Paragraphable)firstObject);
		itemWheelHeroItems.setHighlightOn(false);

		itemWheelChest.setVisible(false);
		itemWheelRoomItems.setVisible(true);
		itemWheelSkills.setVisible(false);
		roomItemWheelShowing = true;
		chestItemWheelShowing = false;
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
		else if (event instanceof TakeItemButtonClickedEvent) {
			if (!roomItemWheelShowing) {
				setRoomItemsWheelVisible();
			}
			else {
				// switching room item wheel off
				setSkillWheelVisible();
			}
		}
		else if (event instanceof ChestItemButtonClickedEvent) {
			if (!chestItemWheelShowing) {
				if (!(figureInfo.getPos().getIndex() == Position.Pos.NW.getValue())) {
					EventManager.getInstance().fireEvent(new ActionEvent(new StepAction(Position.Pos.NW.getValue())));
				}
				setChestItemWheelVisible();
			}
			else {
				// switching chest item wheel off
				setSkillWheelVisible();
			}
		}
	}

	private void setSkillWheelVisible() {
		itemWheelChest.setVisible(false);
		itemWheelRoomItems.setVisible(false);
		itemWheelSkills.setVisible(true);
		roomItemWheelShowing = false;
		chestItemWheelShowing = false;
		skillItemWheelShowing = true;
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(TakeItemButtonClickedEvent.class);
		events.add(ChestItemButtonClickedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof TakeItemButtonClickedEvent || event instanceof ChestItemButtonClickedEvent) {
			switchRightItemWheel(event);
		}
	}
}
