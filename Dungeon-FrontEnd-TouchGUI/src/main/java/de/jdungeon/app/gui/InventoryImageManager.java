package de.jdungeon.app.gui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import de.jdungeon.graphics.ImageManager;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.item.AttrPotion;
//import de.jdungeon.item.Bunch;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.Key;
import de.jdungeon.item.equipment.Armor;
import de.jdungeon.item.equipment.Helmet;
import de.jdungeon.item.equipment.Shield;
import de.jdungeon.item.equipment.weapon.Axe;
import de.jdungeon.item.equipment.weapon.Club;
import de.jdungeon.item.equipment.weapon.Lance;
import de.jdungeon.item.equipment.weapon.Sword;
import de.jdungeon.item.equipment.weapon.Wolfknife;
import de.jdungeon.item.map.AncientMapFragment;
import de.jdungeon.item.paper.Book;
import de.jdungeon.item.paper.InfoScroll;
import de.jdungeon.item.paper.Scroll;
import de.jdungeon.item.quest.MoonRune;
import de.jdungeon.item.quest.Thing;

import de.jdungeon.game.Image;
import de.jdungeon.util.Clazz;

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
		//itemClassImageMap.put(Bunch.class,  guiImageManager.getJDImageProxy(GUIImageManager.BUNCH));
		itemClassImageMap.put(Thing.class,  guiImageManager.getJDImageProxy(GUIImageManager.MANA_BATTERY));
		itemClassImageMap.put(Key.class,  guiImageManager.getJDImageProxy("guiItems/key.gif"));
		itemClassImageMap.put(MoonRune.class,  guiImageManager.getJDImageProxy("guiItems/mondrune.png"));
		itemClassImageMap.put(DustItem.class,  guiImageManager.getJDImageProxy(GUIImageManager.POTION_DUST));
	}

	private final GUIImageManager guiImageManager;

	public InventoryImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
		init();
	}

	public Image getImage(ItemInfo item) {
		if(item == null) return null;
	 	// TODO: make image detection aware of class hierarchy
		// => detect most specific match in class hierarchy
		Set<Class<? extends Item>> classes = itemClassImageMap.keySet();
		for (Class<? extends Item> aClass : classes) {
			if (Clazz.isAssignableFrom(aClass, item.getItemClass())) {
				return (Image)itemClassImageMap.get(aClass).getImage();
			}
		}
		return guiImageManager.getImage(GUIImageManager.NO_IMAGE);
	}

	public JDImageProxy getJDImage(ItemInfo item) {
		if(item == null) return null;
		// TODO: make image detection aware of class hierarchy
		// => detect most specific match in class hierarchy
		Set<Class<? extends Item>> classes = itemClassImageMap.keySet();
		for (Class<? extends Item> aClass : classes) {
			if (Clazz.isAssignableFrom(aClass, item.getItemClass())) {
				return itemClassImageMap.get(aClass);
			}
		}
		return guiImageManager.getJDImage(GUIImageManager.NO_IMAGE);
	}

}
