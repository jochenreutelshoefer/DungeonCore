package graphics;

import figure.monster.Monster;
import figure.monster.MonsterInfo;
import item.AttrPotion;
import item.DustItem;
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
import item.paper.Book;
import item.paper.InfoScroll;
import item.paper.Scroll;
import item.quest.DarkMasterKey;
import item.quest.Feather;
import item.quest.Incense;
import item.quest.LuziasBall;
import item.quest.Rune;
import item.quest.Thing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import animation.AnimationSet;
import animation.AnimationUtils;
import dungeon.JDPoint;

public class GraphicObjectRenderer {
	


	public static GraphicObject[] drawItems(int xcoord, int ycoord,
			ItemInfo[] itemArray, int roomSize) {
		if (itemArray == null) {
			return new GraphicObject[0];
		}
		Point p2[] = getItemPoints(xcoord, ycoord, roomSize);

		GraphicObject[] itemObs = new GraphicObject[4];

		// Schleife bis 4
		int i = 0;
		while (i < itemArray.length && i < 4) {
			Point q = p2[i];

			if (itemArray[i] != null) {

				if (AttrPotion.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					if (((itemArray[i]).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
						itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))), Color.yellow,ImageManager.potion_redImage);
					} else {
						itemObs[i] = new GraphicObject(
								itemArray[i], new Rectangle(q, new Dimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))), Color.yellow,ImageManager.potion_blueImage);
					}

				} else if (itemArray[i].getItemClass().equals(DustItem.class)) {
					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
									new Dimension(10 * (roomSize / 100),
											8 * (roomSize / 100))),Color.yellow,ImageManager.dustImage);
				} else if (itemArray[i].getItemClass().equals(Sword.class)) {
					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow,ImageManager.swordImage);
				} else if (itemArray[i].getItemClass().equals(Axe.class)) {
					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow,ImageManager.axeImage);
				} else if (itemArray[i].getItemClass().equals(Club.class)) {
					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow,ImageManager.clubImage);
				} else if (itemArray[i].getItemClass().equals(Lance.class)) {
					itemObs[i] = new GraphicObject( itemArray[i], new Rectangle(q, new Dimension(
									50 * (roomSize / 100),
									40 * (roomSize / 100))), Color.yellow,ImageManager.lanceImage);
				} else if (itemArray[i].getItemClass().equals(Wolfknife.class)) {
					itemObs[i] = new GraphicObject(	itemArray[i], new Rectangle(q, new Dimension(
									20 * (roomSize / 100),
									15 * (roomSize / 100))), Color.yellow,ImageManager.wolfknifeImage);
				} else if (itemArray[i].getItemClass().equals(Armor.class)) {
					int sizeX = 30 * (roomSize / 100);
					int sizeY = 20 * (roomSize / 100);

					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.armorImage);
				} else if (itemArray[i].getItemClass().equals(Shield.class)) {
					int sizeX = 20 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow, ImageManager.shieldImage);
				} else if (itemArray[i].getItemClass().equals(Helmet.class)) {
					int sizeX = 24 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow, ImageManager.helmetImage);
				} else if (Scroll.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.scrollImage);
				} else if (itemArray[i].getItemClass().equals(InfoScroll.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(
									q, new Dimension(sizeX, sizeY)),
							Color.yellow,ImageManager.documentImage);
				} else if (itemArray[i].getItemClass().equals(Feather.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(	itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.featherImage);
				} else if (itemArray[i].getItemClass().equals(Incense.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(	 itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,ImageManager.potion_greenImage);
				} else if (itemArray[i].getItemClass().equals(Key.class)) {
					int sizeX = 16 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new GraphicObject(	itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.keyImage);

				} else if (itemArray[i].getItemClass().equals(Rune.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy im = null;
					if ((itemArray[i]).toString().indexOf('J') != -1) {
						im = ImageManager.rune_yellowImage;
					} else if ((itemArray[i]).toString().indexOf('A') != -1) {
						im = ImageManager.rune_greenImage;
					} else if ((itemArray[i]).toString().indexOf('V') != -1) {
						im = ImageManager.rune_redImage;
					}

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else if (itemArray[i].getItemClass().equals(
						DarkMasterKey.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy im = ImageManager.cristall_redImage;

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else if (itemArray[i].getClass().equals(LuziasBall.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy im = ImageManager.kugelImage;

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else if (itemArray[i].getClass().equals(Book.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy im = ImageManager.bookImage;

					itemObs[i] = new GraphicObject( itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);

				} else if (Thing.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy im = null;

					im = ImageManager.amulettImage;

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else {

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(
							q, new Dimension(8, 5)), Color.yellow, null);
				}
			} else {
				itemObs[i] = null;
			}
			i++;
		}
		return itemObs;
	}
	
	private static Point[] getItemPoints(int xcoord, int ycoord, int roomSize) {
		Point[] points = new Point[4];

		points[0] = new Point(xcoord + RoomSize.by2(roomSize) -  RoomSize.by8(roomSize),
				ycoord + RoomSize.by3(roomSize));
		points[1] = new Point(xcoord + RoomSize.by2(roomSize), ycoord + RoomSize.by2(roomSize)
				- RoomSize.by16(roomSize));
		points[2] = new Point(xcoord + RoomSize.by3(roomSize), ycoord
				+ (3 * RoomSize.by5(roomSize)));
		points[3] = new Point(xcoord + (int) (3 * RoomSize.by5(roomSize)), ycoord
				+ (3 * RoomSize.by5(roomSize)));

		return points;
	}
	
	public static GraphicObject drawAMonster(MonsterInfo m, Point p, int roomSize) {
		JDImageAWT ob = null;
		int mClass = m.getMonsterClass();
		int sizeX = (int) (getMonsterSize(m, roomSize).width);
		int sizeY = (int) (getMonsterSize(m, roomSize).height);
		JDRectangle rect = new JDRectangle(new JDPoint(p.x - (sizeX / 2), p.y
				- (sizeY / 2)), sizeX, sizeY);
		int dir = m.getLookDir();
		if (mClass == Monster.WOLF) {

			JDImageProxy wolf = ImageManager.wolfImage[dir - 1];
			if (m.getLevel() == 2) {
				wolf = ImageManager.wolfImage[dir - 1];
			}
			ob = new JDImageAWT(wolf, rect);
		} else if (mClass == Monster.ORC) {
			JDImageProxy orc = ImageManager.orcImage[dir - 1];

			if (m.getLevel() == 2) {
				orc = ImageManager.orcImage[dir - 1];
			}
			ob = new JDImageAWT(orc, rect);
		} else if (mClass == Monster.SKELETON) {
			JDImageProxy skel = ImageManager.skelImage[dir - 1];

			if (m.getLevel() == 2) {
				skel = ImageManager.skelImage[dir - 1];
			}
			ob = new JDImageAWT(skel, rect);
		} else if (mClass == Monster.GHUL) {
			JDImageProxy ghul = ImageManager.ghulImage[dir - 1];

			if (m.getLevel() == 2) {
				ghul = ImageManager.ghulImage[dir - 1];
			}
			ob = new JDImageAWT(ghul, rect);
		} else if (mClass == Monster.OGRE) {
			JDImageProxy ogre = ImageManager.ogreImage[dir - 1];

			if (m.getLevel() == 2) {
				ogre = ImageManager.ogreImage[dir - 1];
			}
			ob = new JDImageAWT(ogre, rect);
		} else if (mClass == Monster.BEAR) {
			JDImageProxy bear = ImageManager.bearImage[dir - 1];

			if (m.getLevel() == 2) {
				bear = ImageManager.bearImage[dir - 1];
			}
			ob = new JDImageAWT(bear, rect);

		} else if (mClass == Monster.DARKMASTER) {
			ob = new JDImageAWT(ImageManager.darkMasterImage, rect);

		} else if (mClass == Monster.DWARF) {
			ob = new JDImageAWT(ImageManager.dark_dwarfImage, rect);

		} else if (mClass == Monster.FIR) {
			ob = new JDImageAWT(ImageManager.finImage, rect);

		} else {
			ob = new JDImageAWT(ImageManager.engelImage, rect);
		}

		if (m.isDead() != null && m.isDead().booleanValue()) {
			// System.out.println("will totes Monster malen!");
			AnimationSet set = AnimationUtils.getFigure_tipping_over(m);
			if (set != null && set.getLength() > 0) {
				JDImageProxy i = set.getImagesNr(set.getLength() - 1);
				ob = new JDImageAWT(i, rect);
			}
		}

		int mouseSize = RoomSize.by5(roomSize);
		return new JDGraphicObject(ob, m, rect, Color.white, new JDRectangle(p.x - mouseSize / 2, p.y - mouseSize / 2,
				mouseSize, mouseSize));
	}
	
	public static Dimension getMonsterSize(MonsterInfo m, int roomSize) {
		int mClass = m.getMonsterClass();
		if (mClass == Monster.WOLF) {
			return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));
		}
		if (mClass == Monster.ORC) {
			return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));
		}
		if (mClass == Monster.SKELETON) {
			return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));
		}
		if (mClass == Monster.GHUL) {
			return new Dimension(RoomSize.by2(roomSize), RoomSize.by2(roomSize));
		}
		if (mClass == Monster.OGRE) {
			return new Dimension(RoomSize.by2(roomSize), RoomSize.by2(roomSize));
		}
		if (mClass == Monster.BEAR) {
			return new Dimension((int) (roomSize / 2.2), (int) (roomSize / 2.2));
		}

		return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));

	}

	
}
