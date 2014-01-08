package de.jdungeon.androidapp.gui;

import graphics.ImageManager;
import item.ItemInfo;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Wolfknife;
import de.jdungeon.game.Image;

public class InventoryImageManager {

	public static Image getImage(ItemInfo item) {
		if (item.getItemClass().equals(Sword.class)) {
			return (Image) ImageManager.inventory_sword1.getImage();
		}

		if (item.getItemClass().equals(Axe.class)) {
			return (Image) ImageManager.inventory_axe1.getImage();
		}

		if (item.getItemClass().equals(Lance.class)) {
			return (Image) ImageManager.inventory_lance1.getImage();
		}

		if (item.getItemClass().equals(Club.class)) {
			return (Image) ImageManager.inventory_club1.getImage();
		}

		if (item.getItemClass().equals(Wolfknife.class)) {
			return (Image) ImageManager.inventory_knife1.getImage();
		}

		if (item.getItemClass().equals(Armor.class)) {
			return (Image) ImageManager.inventory_armor1.getImage();
		}

		if (item.getItemClass().equals(Helmet.class)) {
			return (Image) ImageManager.inventory_helmet1.getImage();
		}

		if (item.getItemClass().equals(Shield.class)) {
			return (Image) ImageManager.inventory_shield1.getImage();
		}

		return (Image) ImageManager.deathImage.getImage();
	}

}
