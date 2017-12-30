package de.jdungeon.androidapp;

import audio.AudioEffectsManager;
import event.Event;
import event.EventListener;
import event.EventManager;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;
import gui.Paragraphable;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import shrine.ShrineInfo;
import control.ActionAssembler;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.event.ClickType;
import de.jdungeon.androidapp.event.EndRoundEvent;
import de.jdungeon.androidapp.event.InventoryItemClickedEvent;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import de.jdungeon.game.Audio;

import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;

public class Control implements EventListener {

	private final ActionAssembler actionAssembler;
	private final FigureInfo figure;

	public ActionAssembler getActionAssembler() {
		return actionAssembler;
	}

	public Control(FigureInfo figure, ActionAssembler actionAssembler) {
		this.figure = figure;
		this.actionAssembler = actionAssembler;
		EventManager.getInstance().registerListener(this);
	}

	public void objectClicked(Object clickedObject, boolean doubleClick) {
		if (clickedObject instanceof ItemInfo) {
			handleItemInfoClick(((ItemInfo) clickedObject), doubleClick);
		} else if (clickedObject instanceof FigureInfo) {
			handleFigureInfoClick(((FigureInfo) clickedObject), doubleClick);
		} else if (clickedObject instanceof ShrineInfo) {
			handleShrineInfoClick(((ShrineInfo) clickedObject), doubleClick);
		} else if (clickedObject instanceof PositionInRoomInfo) {
			handlePosInfoClick(((PositionInRoomInfo) clickedObject),
					doubleClick);
		} else if (clickedObject instanceof RoomInfo) {
			handleRoomInfoClick(((RoomInfo) clickedObject), doubleClick);
		} else if (clickedObject instanceof ChestInfo) {
			handleChestInfoClick(((ChestInfo) clickedObject), doubleClick);
		} else if (clickedObject instanceof DoorInfo) {
			handleDoorInfoClick(((DoorInfo) clickedObject), doubleClick);
		}

	}

	public void inventoryItemClicked(ItemInfo item, Paragraphable target) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		actionAssembler.wannaUseItem(item, target, false);
	}

	public void itemWheelActivityClicked(ItemWheelActivity item,
			Paragraphable target) {
		if (item == null) {
			return;
		}
		Object o = item.getObject();
		if (o instanceof ItemInfo) {
			inventoryItemClicked((ItemInfo) o, target);
		}
	}

	private void handleDoorInfoClick(DoorInfo doorInfo, boolean doubleClick) {
		actionAssembler.doorClicked(doorInfo, doubleClick);

	}

	private void handleChestInfoClick(ChestInfo chestInfo, boolean doubleClick) {
		actionAssembler.chestClicked(chestInfo, doubleClick);

	}

	private void handleRoomInfoClick(RoomInfo room, boolean doubleClick) {
		actionAssembler.roomClicked(room, doubleClick);

	}

	private void handlePosInfoClick(PositionInRoomInfo pos, boolean doubleClick) {
		actionAssembler.positionClicked(pos, doubleClick);
	}

	private void handleShrineInfoClick(ShrineInfo info, boolean doubleClick) {
		actionAssembler.shrineClicked(doubleClick);

	}

	private void handleFigureInfoClick(FigureInfo figure, boolean doubleClick) {
		actionAssembler.monsterClicked(figure, doubleClick);

	}

	private void handleItemInfoClick(ItemInfo item, boolean doubleClick) {
		actionAssembler.itemClicked(item, doubleClick);
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
		actionAssembler.wannaSwitchEquipmentItem(itemType, weaponIndex);
	}

	public void inventoryItemLongClicked(int itemType, EquipmentItemInfo info) {
		AudioEffectsManager.playSound(AudioEffectsManager.TAKE_ITEM);
		actionAssembler.wannaLayDownItem(info);
	}

	public void endRound() {
		actionAssembler.wannaEndRound();
	}

	public InfoEntity getUniqueTargetEntity(
			Class<? extends InfoEntity> targetClass) {
		if (targetClass.equals(FigureInfo.class)) {
			List<FigureInfo> figureInfos = figure.getRoomInfo()
					.getFigureInfos();
			if (figureInfos.size() == 2) {
				// remove player
				figureInfos.remove(figure);
				// enemy remains
				return figureInfos.get(0);
			}
		}

		return null;

	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(InventoryItemClickedEvent.class);
		events.add(EndRoundEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if(event instanceof InventoryItemClickedEvent) {
			InventoryItemClickedEvent e = ((InventoryItemClickedEvent)event);
			if(e.getClick() == ClickType.Double) {
				inventoryItemDoubleClicked(e.getType(), e.getItem());
			}
			if(e.getClick() == ClickType.Long) {
				inventoryItemLongClicked(e.getType(), e.getItem());
			}

		}

		if(event instanceof EndRoundEvent) {
			endRound();
		}
	}
}
