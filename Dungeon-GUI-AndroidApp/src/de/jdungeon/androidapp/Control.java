package de.jdungeon.androidapp;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;
import game.JDGUI;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;

import java.util.List;

import shrine.ShrineInfo;
import control.ActionAssembler;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;

public class Control {

	private final JDungeonApp app;
	private final JDGUI gui;
	private final ActionAssembler actionAssembler;

	public ActionAssembler getActionAssembler() {
		return actionAssembler;
	}

	public Control(JDungeonApp game, JDGUI gui) {
		this.app = game;
		this.gui = gui;
		actionAssembler = new ActionAssembler();
		actionAssembler.setGui(gui);

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

	public void inventoryItemClicked(ItemInfo item) {
		actionAssembler.wannaUseItem(item, null, false);
	}

	public void itemWheelActivityClicked(ItemWheelActivity item) {
		if (item == null) {
			return;
		}
		Object o = item.getObject();
		if (o instanceof ItemInfo) {
			inventoryItemClicked((ItemInfo) o);
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
		actionAssembler.shrineClicked(false);

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
		actionAssembler.wannaSwitchEquipmentItem(itemType, weaponIndex);
	}

	public void inventoryItemLongClicked(int itemType, EquipmentItemInfo info) {
		actionAssembler.wannaLayDownItem(info);
	}

	public void endRound() {
		actionAssembler.wannaEndRound();
	}

	public InfoEntity getUniqueTargetEntity(
			Class<? extends InfoEntity> targetClass) {
		if (targetClass.equals(FigureInfo.class)) {
			List<FigureInfo> figureInfos = gui.getFigure().getRoomInfo()
					.getFigureInfos();
			if (figureInfos.size() == 2) {
				// remove player
				figureInfos.remove(gui.getFigure());
				// enemy remains
				return figureInfos.get(0);
			}
		}

		return null;

	}
}
