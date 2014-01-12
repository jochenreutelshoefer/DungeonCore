package de.jdungeon.androidapp.gui;

import graphics.ImageManager;
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
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Image;

public class InventoryImageManager {

	public static Image getImage(ItemInfo item, GameScreen screen) {
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
			return GUIImageManager.getImage(GUIImageManager.POTION,
					screen.getGame());
		}
		if (item.getItemClass().isAssignableFrom(Scroll.class)) {
			return GUIImageManager.getImage(GUIImageManager.SCROLL,
					screen.getGame());
		}
		if (item.getItemClass().equals(Book.class)) {
			return GUIImageManager.getImage(GUIImageManager.BOOK,
					screen.getGame());
		}

		if (item.getItemClass().equals(InfoScroll.class)) {
			return GUIImageManager.getImage(GUIImageManager.PARCHMENT,
					screen.getGame());
		}

		if (item.getItemClass().equals(Bunch.class)) {
			return GUIImageManager.getImage(GUIImageManager.KEY,
					screen.getGame());
		}

		return (Image) ImageManager.deathImage.getImage();
	}

}
