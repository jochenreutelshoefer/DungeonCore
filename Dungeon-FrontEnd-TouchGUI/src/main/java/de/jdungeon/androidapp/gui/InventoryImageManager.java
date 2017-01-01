package de.jdungeon.androidapp.gui;

import graphics.ImageManager;
import item.AttrPotion;
import item.Bunch;
import item.HealPotion;
import item.ItemInfo;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Wolfknife;
import item.paper.Book;
import item.paper.InfoScroll;
import item.paper.Scroll;

import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

public class InventoryImageManager {

	private final GUIImageManager guiImageManager;

	public InventoryImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
	}

	public Image getImage(ItemInfo item) {
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

		if (item.getItemClass().equals(HealPotion.class)) {
			return guiImageManager.getImage(GUIImageManager.POTION);
		}
		if (item.getItemClass().equals(AttrPotion.class)) {
			return guiImageManager.getImage(GUIImageManager.POTION_BLUE);
		}
		if (Scroll.class.isAssignableFrom(item.getItemClass())) {
			return guiImageManager.getImage(GUIImageManager.SCROLL);
		}
		if (item.getItemClass().equals(Book.class)) {
			return guiImageManager.getImage(GUIImageManager.BOOK);
		}

		if (item.getItemClass().equals(InfoScroll.class)) {
			return guiImageManager.getImage(GUIImageManager.PARCHMENT);
		}

		if (item.getItemClass().equals(Bunch.class)) {
			return guiImageManager.getImage(GUIImageManager.BUNCH);
		}

		return guiImageManager.getImage(GUIImageManager.NO_IMAGE);
	}

}
