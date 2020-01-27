package de.jdungeon.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import audio.AudioEffectsManager;
import ai.ActionAssemblerHelper;
import dungeon.ChestInfo;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import event.Event;
import event.EventListener;
import event.EventManager;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.ScoutAction;
import figure.hero.HeroInfo;
import game.JDGUI;
import game.RoomInfoEntity;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;
import shrine.ShrineInfo;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.ClickType;
import de.jdungeon.app.event.EndRoundEvent;
import de.jdungeon.app.event.InventoryItemClickedEvent;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.smartcontrol.ShrineButtonClickedEvent;

/**
 * The Action controller takes User Interface interactions, such as clicked objects,
 * and translates it to Actions or sequences of Action that can be performed by the game character.
 *
 */

public class ActionAssembler implements EventListener {

	private final FigureInfo figure;
	private final JDGUI gui;
	private Action lastAction;
	private int repeatActionCounter;

	private final ActionAssemblerHelper actionAssembler;

	public ActionAssembler(FigureInfo figure, JDGUI gui) {
		this.figure = figure;
		this.gui = gui;
		actionAssembler = new ActionAssemblerHelper(figure);
		EventManager.getInstance().registerListener(this);
	}

	public void triggerPlannedActions() {

		// we repeat scout actions until scout was successful
		if (lastAction instanceof ScoutAction) {
			int direction = ((ScoutAction) lastAction).getDirection();
			RoomInfo scoutedRoom = figure.getRoomInfo().getNeighbourRoom(direction);
			if ((scoutedRoom != null) && scoutedRoom.getVisibilityStatus() < RoomObservationStatus.VISIBILITY_FIGURES) {
				if (!figure.getRoomInfo().fightRunning() && repeatActionCounter < 10) {
					plugAction(lastAction);
				}
			}
		}
	}

	/**
	 * Thread-blackboard put method (called by UI-Thread)
	 *
	 * The gui thread plugs an action to be performed by the UI-controlled figure.
	 *
	 * Adds a particular Action to the action sequence to be executed
	 *
	 * @param action action to be executed
	 */
	public void plugAction(Action action) {
		plugActions(Collections.singletonList(action));
	}

	/**
	 * Thread-blackboard put method (called by UI-Thread)
	 *
	 * The gui thread plugs a sequence of actions to be performed by the UI-controlled figure.
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
			if (lastAction == a) {
				repeatActionCounter++;
			}
			else {
				repeatActionCounter = 0;
			}
			lastAction = a;
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
		else if (clickedObject instanceof ShrineInfo) {
			handleShrineInfoClick(((ShrineInfo) clickedObject), doubleClick);
		}
		else if (clickedObject instanceof PositionInRoomInfo) {
			handlePosInfoClick(((PositionInRoomInfo) clickedObject),
					doubleClick);
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

	public ActionAssemblerHelper getActionAssembler() {
		return actionAssembler;
	}

	public void itemWheelActivityClicked(Activity item,
										 RoomInfoEntity target) {
		if (item == null) {
			return;
		}
		Object o = item.getObject();
		if (o instanceof ItemInfo) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			List<Action> actions = actionAssembler.wannaUseItem((ItemInfo) o, target, false);
			plugActions(actions);
		}
	}

	public void scoutingActivity(RoomInfoEntity highlightedEntity) {
		if (highlightedEntity != null) {
			if (highlightedEntity instanceof RoomInfo) {
				int directionToScout = Dir.getDirFromToIfNeighbour(
						figure.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				List<Action> actions = actionAssembler.wannaScout(directionToScout);
				plugActions(actions);
			}
			else if (highlightedEntity instanceof DoorInfo) {
				int directionToScout = ((DoorInfo) highlightedEntity).getDir(figure.getRoomNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				List<Action> actions = actionAssembler.wannaScout(directionToScout);
				plugActions(actions);
			}
		}
		else {
			PositionInRoomInfo pos = figure.getPos();
			if(pos == null) {
				// hero dead, game over but gui still active
				return;
			}
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();

			if (possibleFleeDirection != null) {
				DoorInfo door = figure.getRoomInfo().getDoor(
						possibleFleeDirection);
				if (door != null) {
					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
					List<Action> actions = actionAssembler.wannaScout(possibleFleeDirection);
					plugActions(actions);
				}
			}
		}
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

	private void handleShrineInfoClick(ShrineInfo info, boolean doubleClick) {
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
		AudioEffectsManager.playSound(AudioEffectsManager.TAKE_ITEM);
		List<Action> actions = actionAssembler.wannaSwitchEquipmentItem(itemType, weaponIndex);
		plugActions(actions);
	}

	public void inventoryItemLongClicked(int itemType, EquipmentItemInfo info) {
		AudioEffectsManager.playSound(AudioEffectsManager.TAKE_ITEM);
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
			List<Action> actions = this.getActionAssembler().wannaUseShrine(this.figure.getRoomInfo(), false);
			plugActions(actions);
		}

		if (event instanceof EndRoundEvent) {
			endRound();
		}
	}
}
