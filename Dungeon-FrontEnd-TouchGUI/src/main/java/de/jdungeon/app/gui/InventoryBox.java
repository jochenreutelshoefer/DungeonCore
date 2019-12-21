package de.jdungeon.app.gui;

import item.equipment.EquipmentItemInfo;
import util.JDDimension;

import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

import dungeon.JDPoint;

public class InventoryBox extends AbstractGUIElement {

	private EquipmentItemInfo item;
	private Image itemImage;
	private final int width_16;
	private final int height_16;
	private final int width_20;
	private final int height_20;
	private final int posX8;
	private final int posY8;
	private final int posX10;
	private final int posY10;


	private final Image background;
	private final Image foregroundEmpty;
	private final InventoryImageManager inventoryImageManager;

	public void setItem(EquipmentItemInfo item) {
		this.item = item;
		itemImage = inventoryImageManager.getImage(item);
	}

	public InventoryBox(JDPoint position, JDDimension dim, Image background,
			Image foregroundEmpty, GUIElement parent, InventoryImageManager inventoryImageManager) {
		super(position, dim, parent);
		this.background = background;
		this.foregroundEmpty = foregroundEmpty;
		this.inventoryImageManager = inventoryImageManager;



		width_16 = this.dimension.getWidth() - 16;
		height_16 = this.dimension.getHeight() - 16;
		width_20 = this.dimension.getWidth() - 20;
		height_20 = this.dimension.getHeight() - 20;
		posX8 = position.getX() + 8;
		posY8 = position.getY() + 8;
		posX10 = position.getX() + 10;
		posY10 = position.getY() + 10;
	}

	@Override
	public boolean needsRepaint() {
		return true;
	}

	public EquipmentItemInfo getItem() {
		return item;
	}


	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		return false;
	}

	@Override
	public void paint(Graphics g, JDPoint parentPosition) {
		g.drawScaledImage(background, parentPosition.getX() + position.getX(),
				parentPosition.getY() + position.getY(),
				this.dimension.getWidth(), this.dimension.getHeight(), 0, 0,
				background.getWidth(), background.getHeight());
		if (item != null) {
			g.drawScaledImage(itemImage,
					parentPosition.getX() + posX8,
					parentPosition.getY() + posY8,
					width_16,
					height_16, 0, 0,
					itemImage.getWidth(), itemImage.getHeight());

		} else {
			g.drawScaledImage(foregroundEmpty,
					parentPosition.getX() + posX10,
					parentPosition.getY() + posY10,
					width_20,
					height_20, 0, 0,
					foregroundEmpty.getWidth(), foregroundEmpty.getHeight());
		}
	}

}
