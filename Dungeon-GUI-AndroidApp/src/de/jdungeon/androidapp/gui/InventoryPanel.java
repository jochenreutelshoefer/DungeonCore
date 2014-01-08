package de.jdungeon.androidapp.gui;

import item.equipment.EquipmentItemInfo;
import util.JDDimension;
import android.view.MotionEvent;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.ScrollMotion;
import dungeon.JDPoint;
import figure.action.EquipmentChangeAction;
import figure.hero.HeroInfo;
import graphics.ImageManager;
import graphics.JDImageProxy;

public class InventoryPanel extends SlidingGUIElement {

	private static final int helmetX = 90;
	private static final int helmetY = 20;

	private static final int armorX = 90;
	private static final int armorY = 90;

	private static final int weaponX = 20;
	private static final int weaponY = 160;

	private static final int shieldX = 160;
	private static final int shieldY = 160;

	private static final int boxGrid = 63;
	private static final int boxSize = 60;

	private final HeroInfo hero;

	private final InventoryBox helmet1;
	private final InventoryBox helmet2;
	private final InventoryBox helmet3;

	private final InventoryBox armor1;
	private final InventoryBox armor2;
	private final InventoryBox armor3;

	private final InventoryBox weapon1;
	private final InventoryBox weapon2;
	private final InventoryBox weapon3;

	private final InventoryBox shield1;
	private final InventoryBox shield2;
	private final InventoryBox shield3;

	private final InventoryBox[] boxes;
	private final InventoryBox[] helmets;
	private final InventoryBox[] armors;
	private final InventoryBox[] weapons;
	private final InventoryBox[] shields;

	private static final JDDimension size = new JDDimension(300, 360);

	private static final int yPosition = 0;

	public InventoryPanel(HeroInfo hero, GameScreen screen) {
		super(new JDPoint(0, yPosition), size, new JDPoint(
				(size.getWidth() * -1) + 20, yPosition), screen);
		this.hero = hero;

		helmet1 = new InventoryBox(new JDPoint(helmetX, helmetY),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_select.getImage(),
				(Image) ImageManager.inventory_empty_helmet.getImage(), this);
		helmet2 = new InventoryBox(new JDPoint(helmetX + boxGrid, helmetY),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_helmet.getImage(), this);
		helmet3 = new InventoryBox(new JDPoint(helmetX + boxGrid + boxGrid,
				helmetY), new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_helmet.getImage(), this);

		armor1 = new InventoryBox(new JDPoint(armorX, armorY), new JDDimension(
				boxSize, boxSize),
				(Image) ImageManager.inventory_box_select.getImage(),
				(Image) ImageManager.inventory_empty_armor.getImage(), this);
		armor2 = new InventoryBox(new JDPoint(armorX + boxGrid, armorY),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_armor.getImage(), this);
		armor3 = new InventoryBox(new JDPoint(armorX + boxGrid + boxGrid,
				armorY), new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_armor.getImage(), this);

		weapon1 = new InventoryBox(new JDPoint(weaponX, weaponY),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_select.getImage(),
				(Image) ImageManager.inventory_empty_weapon.getImage(), this);
		weapon2 = new InventoryBox(new JDPoint(weaponX, weaponY + boxGrid),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_weapon.getImage(), this);
		weapon3 = new InventoryBox(new JDPoint(weaponX, weaponY + boxGrid
				+ boxGrid), new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_weapon.getImage(), this);

		shield1 = new InventoryBox(new JDPoint(shieldX, shieldY),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_select.getImage(),
				(Image) ImageManager.inventory_empty_shield.getImage(), this);
		shield2 = new InventoryBox(new JDPoint(shieldX, shieldY + boxGrid),
				new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_shield.getImage(), this);
		shield3 = new InventoryBox(new JDPoint(shieldX, shieldY + boxGrid
				+ boxGrid), new JDDimension(boxSize, boxSize),
				(Image) ImageManager.inventory_box_normal.getImage(),
				(Image) ImageManager.inventory_empty_shield.getImage(), this);

		InventoryBox[] boxes = { helmet1, helmet2, helmet3, armor1, armor2,
				armor3, shield1, shield2, shield3, weapon1, weapon2, weapon3 };
		this.boxes = boxes;

		InventoryBox[] helmets = { helmet1, helmet2, helmet3 };
		this.helmets = helmets;

		InventoryBox[] shields = { shield1, shield2, shield3 };
		this.shields = shields;

		InventoryBox[] weapons = { weapon1, weapon2, weapon3 };
		this.weapons = weapons;

		InventoryBox[] armors = { armor1, armor2, armor3 };
		this.armors = armors;

		this.slideOut();

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	private void slideOut() {
		this.slideStep = SLIDE_OUT_STEPS;
	}

	private void slideIn() {
		this.slideStep = -1;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {

		/*
		 * if it is out of screen a touch on the visible border will open it
		 */
		if (getCurrentX() == targetPos.getX()) {
			this.slideStep = -1;
			return;
		}

		JDPoint coordinates = new JDPoint(touch.x, touch.y);
		for (InventoryBox box : boxes) {
			if (box.hasPoint(coordinates)) {
				boxClicked(box, false);
			}
		}
	}

	@Override
	public void handleDoubleTapEvent(MotionEvent touch) {
		float rawX = touch.getRawX();
		float rawY = touch.getRawY();

		JDPoint coordinates = this.getScreen().normalizeRawCoordinates(touch);

		/*
		 * if an icon is clicked, we trigger an inventory action
		 */
		boolean isOnIcon = false;
		for (InventoryBox box : boxes) {
			if (box.hasPoint(coordinates)) {
				boxClicked(box, true);
				isOnIcon = true;
			}
		}

		/*
		 * if no icon is clicked we toggle inventory panel
		 */
		if (!isOnIcon) {

			if (getCurrentX() == targetPos.getX()) {
				slideIn();
			} else {
				slideOut();
			}
		}
	}

	private void boxClicked(InventoryBox clickedBox, boolean changeAction) {
		int type = -1;
		EquipmentItemInfo item = null;
		if (clickedBox == armor1) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR;
			item = armor2.getItem();
		}
		if (clickedBox == armor2) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR;
			item = armor2.getItem();
		}

		if (clickedBox == armor3) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR;
			item = armor3.getItem();
		}

		if (clickedBox == helmet1) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_HELMET;
			item = helmet1.getItem();
		}
		if (clickedBox == helmet2) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_HELMET;
			item = helmet2.getItem();
		}
		if (clickedBox == helmet3) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_HELMET;
			item = helmet3.getItem();
		}

		if (clickedBox == shield1) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD;
			item = shield1.getItem();
		}
		if (clickedBox == shield2) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD;
			item = shield2.getItem();
		}
		if (clickedBox == shield3) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD;
			item = shield3.getItem();
		}

		if (clickedBox == weapon1) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON;
			item = weapon1.getItem();
		}
		if (clickedBox == weapon2) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON;
			item = weapon2.getItem();
		}
		if (clickedBox == weapon3) {
			type = EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON;
			item = weapon3.getItem();
		}

		if (item != null) {
			this.getScreen().setInfoEntity(item);

		if (type != -1 && changeAction) {
			this.getScreen().getControl()
					.inventoryItemDoubleClicked(type, item);
		}
		}

	}

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		if (scrolling.getMovement().getX() > 0) {
			slideOut();
		}
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		int x = this.getCurrentX();

		GUIUtils.drawBackground(g, x, position.getY(), dimension);

		GUIUtils.drawDoubleBorder(g, x, position.getY(), dimension, 20);

		JDImageProxy<?> background = ImageManager.inventory_figure_background;
		Image image = (Image) background.getImage();
		g.drawScaledImage(image, x + 70, position.getY() + 40, 110, 288, 0, 0,
				image.getWidth(), image.getHeight());

		for (InventoryBox inventoryBox : boxes) {
			inventoryBox.paint(g, new JDPoint(x, position.getY()));
		}

	}

	private void setInventoryItems(int currentIndex, InventoryBox[] boxes,
			int type) {
		/*
		 * first box in array set with current active items
		 */
		boxes[0].setItem(hero.getEquipmentItemInfo(currentIndex, type));

		/*
		 * remaining two array fields are set with remaining items
		 */
		int boxIndex = 1;
		for (int i = 0; i < 3; i++) {
			if (i != currentIndex) {
				boxes[boxIndex].setItem(hero.getEquipmentItemInfo(i, type));
				boxIndex++;
			}
		}
	}

	@Override
	public void update(float time) {
		super.update(time);

		/*
		 * update box items
		 */
		setInventoryItems(hero.getHelmetIndex(), helmets,
				EquipmentChangeAction.EQUIPMENT_TYPE_HELMET);

		setInventoryItems(hero.getArmorIndex(), armors,
				EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR);

		setInventoryItems(hero.getShieldIndex(), shields,
				EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD);

		setInventoryItems(hero.getWeaponIndex(), weapons,
				EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON);

	}

}
