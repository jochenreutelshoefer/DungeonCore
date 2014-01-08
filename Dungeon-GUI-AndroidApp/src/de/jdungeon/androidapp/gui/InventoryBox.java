package de.jdungeon.androidapp.gui;

import item.equipment.EquipmentItemInfo;
import util.JDDimension;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.JDPoint;

public class InventoryBox extends AbstractGUIElement {

	private EquipmentItemInfo item;

	public EquipmentItemInfo getItem() {
		return item;
	}

	private final Image background;
	private final Image foregroundEmpty;

	public void setItem(EquipmentItemInfo item) {
		this.item = item;
	}

	public InventoryBox(JDPoint position, JDDimension dim, Image background,
			Image foregroundEmpty, GUIElement parent) {
		super(position, dim, parent);
		this.background = background;
		this.foregroundEmpty = foregroundEmpty;
	}

	@Override
	public boolean isVisible() {
		return true;
	}


	@Override
	public void paint(Graphics g, JDPoint parentPosition) {


		g.drawScaledImage(background, parentPosition.getX() + position.getX(),
				parentPosition.getY() + position.getY(),
				this.dimension.getWidth(), this.dimension.getHeight(), 0, 0,
				background.getWidth(), background.getHeight());
		if (item != null) {
			Image itemImage = InventoryImageManager.getImage(item);
			g.drawScaledImage(itemImage,
					parentPosition.getX() + position.getX() + 8,
					parentPosition.getY() + position.getY() + 8,
					this.dimension.getWidth() - 16,
					this.dimension.getHeight() - 16, 0, 0,
					itemImage.getWidth(), itemImage.getHeight());

		} else {
			g.drawScaledImage(foregroundEmpty,
					parentPosition.getX() + position.getX() + 10,
					parentPosition.getY() + position.getY() + 10,
					this.dimension.getWidth() - 20,
					this.dimension.getHeight() - 20, 0, 0,
					foregroundEmpty.getWidth(), foregroundEmpty.getHeight());
		}

	}

}
