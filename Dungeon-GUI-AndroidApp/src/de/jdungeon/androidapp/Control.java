package de.jdungeon.androidapp;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.JDGUI;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;
import shrine.ShrineInfo;
import control.ActionAssembler;
import de.jdungeon.androidapp.gui.ItemWheelActivity;
import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;

public class Control {

	private final JDungeonApp app;
	private final JDGUI gui;
	private final ActionAssembler actionAssembler;

	public Control(JDungeonApp game, JDGUI gui) {
		this.app = game;
		this.gui = gui;
		actionAssembler = new ActionAssembler();
		actionAssembler.setGui(gui);

	}

	public void objectClicked(Object clickedObject) {
		if (clickedObject instanceof ItemInfo) {
			handleItemInfoClick(((ItemInfo) clickedObject));
		} else if (clickedObject instanceof FigureInfo) {
			handleFigureInfoClick(((FigureInfo) clickedObject));
		} else if (clickedObject instanceof ShrineInfo) {
			handleShrineInfoClick(((ShrineInfo) clickedObject));
		} else if (clickedObject instanceof PositionInRoomInfo) {
			handlePosInfoClick(((PositionInRoomInfo) clickedObject));
		} else if (clickedObject instanceof RoomInfo) {
			handleRoomInfoClick(((RoomInfo) clickedObject));
		} else if (clickedObject instanceof ChestInfo) {
			handleChestInfoClick(((ChestInfo) clickedObject));
		} else if (clickedObject instanceof DoorInfo) {
			handleDoorInfoClick(((DoorInfo) clickedObject));
		}

	}

	public void inventoryItemClicked(ItemInfo item) {
		actionAssembler.wannaUseItem(item, null, false);
	}

	public void itemWheelActivityClicked(ItemWheelActivity item) {
		Object o = item.getObject();
		if (o instanceof ItemInfo) {
			inventoryItemClicked((ItemInfo) o);
		}
	}

	private void handleDoorInfoClick(DoorInfo doorInfo) {
		actionAssembler.doorClicked(doorInfo, false);

	}

	private void handleChestInfoClick(ChestInfo chestInfo) {
		actionAssembler.chestClicked(chestInfo, false);

	}

	private void handleRoomInfoClick(RoomInfo room) {
		actionAssembler.roomClicked(room, false);

	}

	private void handlePosInfoClick(PositionInRoomInfo pos) {
		actionAssembler.positionClicked(pos, false);

	}

	private void handleShrineInfoClick(ShrineInfo info) {
		actionAssembler.shrineClicked(false);

	}

	private void handleFigureInfoClick(FigureInfo figure) {
		actionAssembler.monsterClicked(figure, false);

	}

	private void handleItemInfoClick(ItemInfo item) {
		actionAssembler.itemClicked(item, false);

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
		actionAssembler.wannaSwitchEquipmentItem(itemType, weaponIndex);
	}

	public void inventoryItemLongClicked(int itemType, EquipmentItemInfo info) {
		actionAssembler.wannaLayDownItem(info);
	}

	public void endRound() {
		actionAssembler.wannaEndRound();
	}
}
