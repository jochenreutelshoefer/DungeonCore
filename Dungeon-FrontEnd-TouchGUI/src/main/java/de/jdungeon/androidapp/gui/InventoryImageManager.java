package de.jdungeon.androidapp.gui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import graphics.ImageManager;
import graphics.JDImageProxy;
import item.AttrPotion;
import item.Bunch;
import item.HealPotion;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Wolfknife;
import item.map.AncientMapFragment;
import item.paper.Book;
import item.paper.InfoScroll;
import item.paper.Scroll;
import item.quest.Thing;

import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

public class InventoryImageManager {

	private final Map<Class<? extends Item>, JDImageProxy> itemClassImageMap = new LinkedHashMap<>();

	private void init() {
		itemClassImageMap.put(Sword.class, ImageManager.inventory_sword1);
		itemClassImageMap.put(Axe.class, ImageManager.inventory_axe1);
		itemClassImageMap.put(Lance.class, ImageManager.inventory_lance1);
		itemClassImageMap.put(Club.class, ImageManager.inventory_club1);
		itemClassImageMap.put(Wolfknife.class, ImageManager.inventory_knife1);
		itemClassImageMap.put(Armor.class, ImageManager.inventory_armor1);
		itemClassImageMap.put(Shield.class,  ImageManager.inventory_shield1);
		itemClassImageMap.put(Helmet.class, ImageManager.inventory_helmet1);
		itemClassImageMap.put(HealPotion.class,  guiImageManager.getJDImageProxy(GUIImageManager.POTION));
		itemClassImageMap.put(AttrPotion.class,  guiImageManager.getJDImageProxy(GUIImageManager.POTION_BLUE));
		itemClassImageMap.put(Scroll.class,  guiImageManager.getJDImageProxy(GUIImageManager.SCROLL));
		itemClassImageMap.put(Book.class,  guiImageManager.getJDImageProxy(GUIImageManager.BOOK));
		itemClassImageMap.put(InfoScroll.class,  guiImageManager.getJDImageProxy(GUIImageManager.PARCHMENT));
		itemClassImageMap.put(AncientMapFragment.class,  guiImageManager.getJDImageProxy(GUIImageManager.PARCHMENT));
		itemClassImageMap.put(Bunch.class,  guiImageManager.getJDImageProxy(GUIImageManager.BUNCH));
		itemClassImageMap.put(Thing.class,  guiImageManager.getJDImageProxy(GUIImageManager.MANA_BATTERY));
		itemClassImageMap.put(Key.class,  guiImageManager.getJDImageProxy(GUIImageManager.KEY));
	}

	private final GUIImageManager guiImageManager;

	public InventoryImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
		init();
	}

	public Image getImage(ItemInfo item) {
		// TODO: make image detection aware of class hierarchy
		// => detect most specific match in class hierarchy
		Set<Class<? extends Item>> classes = itemClassImageMap.keySet();
		for (Class<? extends Item> aClass : classes) {
			if (aClass.isAssignableFrom(item.getItemClass())) {
				return (Image)itemClassImageMap.get(aClass).getImage();
			}
		}
		return guiImageManager.getImage(GUIImageManager.NO_IMAGE);
	}

}
