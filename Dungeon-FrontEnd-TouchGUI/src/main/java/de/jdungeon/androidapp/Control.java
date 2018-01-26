package de.jdungeon.androidapp;

import java.util.ArrayList;
import java.util.Collection;

import audio.AudioEffectsManager;
import control.ActionAssembler;
import control.JDGUIEngine2D;
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
import figure.hero.HeroInfo;
import game.RoomInfoEntity;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;
import shrine.ShrineInfo;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.event.ClickType;
import de.jdungeon.androidapp.event.EndRoundEvent;
import de.jdungeon.androidapp.event.InventoryItemClickedEvent;
import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.DefaultActivity;
public class Control extends ActionAssembler implements EventListener {

	private final FigureInfo figure;

	public Control(FigureInfo figure, JDGUIEngine2D gui) {
		super(gui);
		this.figure = figure;
		EventManager.getInstance().registerListener(this);
	}

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

	public void itemWheelActivityClicked(Activity item,
										 RoomInfoEntity target) {
		if (item == null) {
			return;
		}
		Object o = item.getObject();
		if (o instanceof ItemInfo) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			wannaUseItem((ItemInfo) o, target, false);
		}
	}

	public void scoutingActivity(RoomInfoEntity highlightedEntity) {
		if(highlightedEntity != null) {
			if (highlightedEntity instanceof RoomInfo) {
				int directionToScout = Dir.getDirFromToIfNeighbour(
						figure.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				this.wannaScout(directionToScout);
			}
			else if (highlightedEntity instanceof DoorInfo) {
				int directionToScout = ((DoorInfo) highlightedEntity).getDir(figure.getRoomNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				this.wannaScout(directionToScout);
			}
		}
		else {
			PositionInRoomInfo pos = figure.getPos();
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();

			if (possibleFleeDirection != null) {
				DoorInfo door = figure.getRoomInfo().getDoor(
						possibleFleeDirection);
				if (door != null) {
					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
					this.wannaScout(possibleFleeDirection);
				}
			}
		}
	}

	private void handleDoorInfoClick(DoorInfo doorInfo, boolean doubleClick) {
		doorClicked(doorInfo, doubleClick);

	}

	private void handleChestInfoClick(ChestInfo chestInfo, boolean doubleClick) {
		chestClicked(chestInfo, doubleClick);

	}

	private void handleRoomInfoClick(RoomInfo room, boolean doubleClick) {
		roomClicked(room, doubleClick);

	}

	private void handlePosInfoClick(PositionInRoomInfo pos, boolean doubleClick) {
		positionClicked(pos, doubleClick);
	}

	private void handleShrineInfoClick(ShrineInfo info, boolean doubleClick) {
		shrineClicked(doubleClick);

	}

	private void handleFigureInfoClick(FigureInfo figure, boolean doubleClick) {
		monsterClicked(figure, doubleClick);

	}

	private void handleItemInfoClick(ItemInfo item, boolean doubleClick) {
		itemClicked(item, doubleClick);
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
		wannaSwitchEquipmentItem(itemType, weaponIndex);
	}

	public void inventoryItemLongClicked(int itemType, EquipmentItemInfo info) {
		AudioEffectsManager.playSound(AudioEffectsManager.TAKE_ITEM);
		wannaLayDownItem(info);
	}

	public void endRound() {
		wannaEndRound();
	}


	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(InventoryItemClickedEvent.class);
		events.add(EndRoundEvent.class);
		events.addAll(super.getEvents());
		return events;
	}

	@Override
	public void notify(Event event) {
		super.notify(event);
		if (event instanceof InventoryItemClickedEvent) {
			InventoryItemClickedEvent e = ((InventoryItemClickedEvent) event);
			if (e.getClick() == ClickType.Double) {
				inventoryItemDoubleClicked(e.getType(), e.getItem());
			}
			if (e.getClick() == ClickType.Long) {
				inventoryItemLongClicked(e.getType(), e.getItem());
			}

		}

		if (event instanceof EndRoundEvent) {
			endRound();
		}
	}
}
