package de.jdungeon.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jdungeon.ai.ActionAssemblerHelper;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.user.PlayerControllerI;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.equipment.EquipmentItemInfo;
import de.jdungeon.location.LocationInfo;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.ClickType;
import de.jdungeon.app.event.EndRoundEvent;
import de.jdungeon.app.event.InventoryItemClickedEvent;
import de.jdungeon.app.gui.smartcontrol.ShrineButtonClickedEvent;

/**
 * The Action controller takes User Interface interactions, such as clicked objects,
 * and translates it to Actions or sequences of Action that can be performed by the de.jdungeon.game character.
 *
 */

public class ActionAssembler implements EventListener {

	private final FigureInfo figure;

	private final PlayerControllerI gui;
	private final ActionAssemblerHelper actionAssembler;

	public ActionAssembler(FigureInfo figure, PlayerControllerI gui) {
		this.figure = figure;
		this.gui = gui;
		actionAssembler = new ActionAssemblerHelper(figure);
		EventManager.getInstance().registerListener(this);
	}

	public FigureInfo getFigure() {
		return figure;
	}


	/**
	 * Thread-blackboard put method (called by UI-Thread)
	 *
	 * The de.jdungeon.gui thread plugs a sequence of actions to be performed by the UI-controlled de.jdungeon.figure.
	 *
	 * Adds a sequences of Actions to the characters' action sequence to be executed
	 *
	 * @param l actions to be executed
	 */
	public void plugActions(List<Action> l) {
		List<Action>  actions = new ArrayList<>(l);
		final Boolean fight = figure.getRoomInfo().fightRunning();
		if ((fight == null || !fight) && figure.getActionPoints() < 1 &&  (actions.isEmpty() || !(actions.get(0) instanceof EndRoundAction))) {
			actions.add(new EndRoundAction());
		}
		for (Action a : actions) {
			// plugging action into the actual thread blackboard data
			gui.plugAction(a);
		}

	}

	/**
	 * Handles a clicked objects and transforms it to an executable action or a sequence of actions
	 *
	 * @param clickedObject object that has been clicked
	 * @param doubleClick true if it was a double click, false otherwise
	 */
	public void objectClicked(Object clickedObject, boolean doubleClick) {
		if (clickedObject instanceof ItemInfo) {
			handleItemInfoClick(((ItemInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof FigureInfo) {
			handleFigureInfoClick(((FigureInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof LocationInfo) {
			handleShrineInfoClick(((LocationInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof PositionInRoomInfo) {
			handlePosInfoClick(((PositionInRoomInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof RoomInfo) {
			handleRoomInfoClick(((RoomInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof ChestInfo) {
			handleChestInfoClick(((ChestInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof DoorInfo) {
			handleDoorInfoClick(((DoorInfo) clickedObject), doubleClick);
		}

	}

	public ActionAssemblerHelper getActionAssemblerHelper() {
		return actionAssembler;
	}

	private void handleDoorInfoClick(DoorInfo doorInfo, boolean doubleClick) {
		List<Action> actions = actionAssembler.doorClicked(doorInfo, doubleClick);
		plugActions(actions);
	}

	private void handleChestInfoClick(ChestInfo chestInfo, boolean doubleClick) {
		List<Action> actions = actionAssembler.chestClicked(chestInfo, doubleClick);
		plugActions(actions);
	}

	private void handleRoomInfoClick(RoomInfo room, boolean doubleClick) {
		List<Action> actions = actionAssembler.roomClicked(room, doubleClick);
		plugActions(actions);
	}

	private void handlePosInfoClick(PositionInRoomInfo pos, boolean doubleClick) {
		List<Action> actions = actionAssembler.positionClicked(pos, doubleClick);
		plugActions(actions);
	}

	private void handleShrineInfoClick(LocationInfo info, boolean doubleClick) {
		List<Action> actions = actionAssembler.shrineClicked(doubleClick);
		plugActions(actions);
	}

	private void handleFigureInfoClick(FigureInfo figure, boolean doubleClick) {
		List<Action> actions = actionAssembler.monsterClicked(figure, doubleClick);
		plugActions(actions);
	}

	private void handleItemInfoClick(ItemInfo item, boolean doubleClick) {
		List<Action> actions = actionAssembler.itemClicked(item, doubleClick);
		plugActions(actions);
	}

	public void inventoryItemDoubleClicked(int itemType, EquipmentItemInfo info) {
		HeroInfo hero = ((HeroInfo) info.getOwner());
		int weaponIndex = -1;
		for (int i = 0; i < 3; i++) {
			EquipmentItemInfo itemInfo = hero.getEquipmentItemInfo(i, itemType);
			if (info.equals(itemInfo)) {
				weaponIndex = i;
				break;
			}
		}
		List<Action> actions = actionAssembler.wannaSwitchEquipmentItem(itemType, weaponIndex);
		plugActions(actions);
	}

	public void inventoryItemLongClicked(int itemType, EquipmentItemInfo info) {
		List<Action> actions = actionAssembler.wannaLayDownItem(info);
		plugActions(actions);
	}

	public void endRound() {
		List<Action> actions = actionAssembler.wannaEndRound();
		plugActions(actions);
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(InventoryItemClickedEvent.class);
		events.add(EndRoundEvent.class);
		events.add(ShrineButtonClickedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof InventoryItemClickedEvent) {
			InventoryItemClickedEvent e = ((InventoryItemClickedEvent) event);
			if (e.getClick() == ClickType.Double) {
				inventoryItemDoubleClicked(e.getType(), e.getItem());
			}
			if (e.getClick() == ClickType.Long) {
				inventoryItemLongClicked(e.getType(), e.getItem());
			}

		}
		if (event instanceof ShrineButtonClickedEvent) {
			List<Action> actions = this.getActionAssemblerHelper().wannaUseShrine(this.figure.getRoomInfo(), false);
			plugActions(actions);
		}

		if (event instanceof EndRoundEvent) {
			endRound();
		}
	}

	public void wannaFlee() {
		plugActions(getActionAssemblerHelper().wannaFlee());
	}

	public void wannaWalk(int directionValue) {
		plugActions(getActionAssemblerHelper().wannaWalk(directionValue));
	}

	public List<Action> wannaScout(int directionValue) {
		return getActionAssemblerHelper().wannaScout(directionValue);
	}

	public void wannaUseChest() {
		plugActions(getActionAssemblerHelper().wannaUseChest(false));
	}

	public void wannaUseLocation() {
		plugActions(getActionAssemblerHelper().wannaUseShrine(figure.getRoomInfo(), false));
	}

	public void wannaUseItem(ItemInfo item) {
		if(item != null) {
			plugActions(getActionAssemblerHelper().wannaUseItem(item, null, false));
		}
	}


	public void wannaTakeItem() {
		List<ItemInfo> items = figure.getRoomInfo().getItems();
		if(items == null || items.isEmpty()) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);

		} else {
			plugActions(getActionAssemblerHelper().wannaTakeItem(items.get(0)));
		}
	}

	public void wannaLockUnlockDoor() {
		List<Action> actions = getActionAssemblerHelper().wannaLockDoor();
		if(actions != null && !actions.isEmpty()) {
			plugActions(actions);
		} else {
			// TODO: generate hint for the de.jdungeon.user that in case of multiple doors he should step towards one to make the action unambigous
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
		}
	}


}
