/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */




package item;


import figure.attribute.Attribute;
import figure.attribute.ItemModification;
import figure.hero.Character;
import game.DungeonGame;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Weapon;
import item.equipment.weapon.Wolfknife;
import item.paper.BookSpell;
import item.paper.Scroll;
import item.paper.ScrollMagic;

import java.util.LinkedList;

import spell.Bonebreaker;
import spell.Discover;
import spell.Escape;
import spell.Fireball;
import spell.GoldenHit;
import spell.GoldenThrow;
import spell.Heal;
import spell.Isolation;
import spell.KeyLocator;
import spell.Light;
import spell.Raid;
import spell.Repair;
import spell.Search;
import spell.Spell;
import spell.Spy;
import spell.Steal;
import spell.Thunderstorm;


public class ItemPool {
	
	private static String[] spells = { "repair", "heal", "golden_hit", 
				"escape", "thunderstorm", "bonebreaker", "fireball", "spy","key_locator","discover","isolation","escapeRoute", "fir"};
				
	private static int repair = 8;
	private static int heal = 9;
	private static int golden_hit = 4;
	private static int escape = 3;
	private static int thunderstorm = 3;
	private static int bonebreaker = 6;
	private static int fireball = 7;
	private static int spy = 9;
	private static int key_locator = 8;
	private static int discover = 8;
	private static int isolation = 5;
	private static int escapeRoute= 5;
	private static int fir = 8;
	
	private static int [] spell_values = { repair, heal, golden_hit, 
				escape, thunderstorm, bonebreaker, fireball, spy, key_locator, discover, isolation, escapeRoute};
	
	public static Item getRandomItem(int value, double quotient) {
		Item i = null;
		if (quotient < 0.7) {
			i = getCheapItem(value, quotient);
		}
		else if(quotient < 1.0) {
			i = getLowerItem(value, quotient);
		}
		else if(quotient < 1.5) {
			i = getHigherItem(value, quotient);
		}
		else if(quotient > 1.5) {
			i = getGoodItem(value, quotient);
		}
		
		return i;
		
	}
	
	public static Item getCheapItem(int value, double quotient) {
		Item i = null;
		if(value > 20) {
			i = new HealPotion(value);
		}
		else {
			i = new DustItem(value);
		}
		
		
		return i;
	}

	public static Item getLowerItem(int value, double quotient) {
		int heiltrank = 0;
		int staub = 5;
		int magicPaper = 13;
		int randomPotion = 5;
		int weapon = 5;
		int helmet = 2;
		int armor = 2;
		int shield = 2;
		int book = 1;
		int [] p  = {heiltrank,staub,magicPaper,randomPotion,weapon,helmet,armor,shield,book}; 
		////System.out.println("making lower item!"+" "+quotient);
		int sum = summieren(p);
		int k = (int)(Math.random() * sum);
		Item i = null;
		if(k <= summieren(p,0)) {
			////System.out.println("mache Heiltrank - lower");
			i = new HealPotion(value);
		}
		else if(k <= summieren(p,1)) {
			i = new DustItem(value);
		}
		else if(k <= summieren(p,2)) {
			i = getMagicPaper(value, quotient);
		}
		else if(k <=summieren(p,3)) {
			i = getRandomPotion(value);
		}
		else if(k <= summieren(p,4)) {
			
			i = newWeapon(value, false);
		}
		else if(k <= summieren(p,5)) {
			if(value < 10) {
				value = 10;
			}
			i = new Helmet(value,false);
		}
		else if(k <= summieren(p,6)) {
			if(value < 10) {
				value = 10;
			}
			i = new Armor(value,false);
		}
		else if(k <= summieren(p,7)) {
			i = new Shield(value,false);
		}
		else if(k <= summieren(p,8)) {
			i = getBook(value);
		}
		return i;
	}

	/**
	 * Method getBook.
	 * @param value
	 * @return item
	 */
	
	public static Spell spellArray [] = {new Bonebreaker(1),new Fireball(1),new GoldenHit(1)/*,new Convince(1)*/,new Discover(1),
		new Escape(1), new GoldenThrow(1), new Heal(1),new Isolation(1), new KeyLocator(1),new Light(1), new Raid(1),new Repair(1),new Search(1),new Spy(1),new Steal(1), new Thunderstorm(1)};
	
	public static Item getRandomBookSpell() {
		return new BookSpell(spellArray[((int)Math.random()*spellArray.length)]);
	}
	
	
	private static Item getBook(int value) {
		if(value >= 100) {
			if(Math.random() < 0.2) {
				return new BookSpell(new Bonebreaker(1),10);
			}
			else {
				return new BookSpell(new Thunderstorm(1),10);
			}	
		}
		else if(value >= 80) {
			if(Math.random() < 0.3) {
				return new BookSpell(new Fireball(1),10);
			}
			else {
				return new BookSpell(new GoldenHit(1),10);		
			}	
		} 
		else if(value >= 60) {
			if(Math.random() < 0.2) {
				return new BookSpell(new Escape(1),10);
			}
			else {
				return new BookSpell(new Spy(1),10);	
			}	
		} 
		else if(value >= 40) {
			if(Math.random() < 0.2) {
				return new BookSpell(new Repair(1),10);		
			}
			else {
				return new BookSpell(new Heal(1),10);
			}	
		} 
		else return getRandomItem(value, 1+ Math.random());
	}


	/**
	 * Method getRandomPotion.
	 * @param value
	 * @return item
	 */
	private static Item getRandomPotion(int value) {
		return new AttrPotion(Character.getRandomAttr(),value);
	}


	/**
	 * Method getMagicPaper.
	 * @param value
	 * @return item
	 */
	//noch nicht langfristig sinnvoll
	private static Item getMagicPaper(int value, double quotient) {
		Spell s = getRandomSpell();
		Item i = null;
		if( value <= 20) {
			int cost = (20 - value)/2;
			i = new Scroll(s, cost);
		}
		else if( value <= 20) {
			//int cost = 20 - value;
			//s.setLevel(2);
			//i = new scroll(s, cost);
		}
		else if( value <= 100) {
			
			//s.setLevel(value / 10);
			i = new ScrollMagic(s);
		}
		
		return i;
	}


	public static Item getHigherItem(int value, double quotient) {
		int heiltrank = 0;
		int magicPaper = 36;
		int randomPotion = 10;
		int weapon = 16;
		int magicWeapon = 8;
		int helmet = 16;
		int magicHelmet = 8;
		int armor = 16;
		int magicArmor = 8;
		int shield = 16; 
		int magicShield = 8;
		int book = 4;
		int unique = 1;
		int [] p = {heiltrank,magicPaper,randomPotion,weapon,magicWeapon,helmet,magicHelmet,armor,magicArmor,shield,magicShield,book,unique};
		////System.out.println("making higher Item"+" "+quotient);
		int sum = summieren(p);
		int k = (int)(Math.random() * sum);
		Item i = null;
		if(k <= summieren(p,0)) {
			////System.out.println("getting Heiltrank - HigherItem");
			i = new HealPotion(value);
		}
		else if(k <= summieren(p,0)) {
			i = getMagicPaper(value, quotient);
		}
		else if(k <= summieren(p,1)) {
			////System.out.println("getting random Potion - HigherItem");
			i = getRandomPotion(value);
		}
		else if(k <= summieren(p,2)) {
			int worth = (int)(value * 0.9);
			if(worth > 10) {
				i= newWeapon(worth, false);
			}
		}
		else if(k <= summieren(p,3)) {
			
			i = getMagicWeapon(value, quotient);
			//////System.out.println("making magic-weapon! --> exiting itemPool.getHigherItem()");
			//////System.out.println(i.toString());
			//System.exit(0);
		}
		else if(k <= summieren(p,4)) {
			i = new Helmet((int)(value * 0.9),false);
		}
		else if(k <= summieren(p,5)) {
			i = getMagicHelmet(value, quotient);
		}
		else if(k <= summieren(p,6)) {
			i = new Armor((int)(value * 0.9),false);
		}
		else if(k <= summieren(p,7)) {
			i = getMagicArmor(value,quotient);
		}
		else if(k <= summieren(p,8)) {
			i = new Shield((int)(value * 0.9),false);
		}
		else if(k <= summieren(p,9)) {
			i = getMagicShield(value,quotient);
		}
		else if(k <= summieren(p,10)) {
			i = getBook(value);
		}
		
		else if(k <= summieren(p,11)) {
			i = getUnique(value, quotient);
			if ( i == null) {
				////System.out.println("bekomme kein Unique Items!!!");
				i = getLowerItem(value, quotient);
			}
		}
		
		
		return i;
	}
	
	public static DungeonGame game = null;
	public static void setGame(DungeonGame game) {
		ItemPool.game = game;
	}

	/**
	 * Method getUnique.
	 * @param value
	 * @param quotient
	 * @return item
	 */
	public static Item getUnique(int value, double quotient) {
		if(ItemPool.game != null) {
		return ItemPool.game.selectItem(value);
		}
		else {
			////System.out.println("Unique Item");
			return null;	
		}
	}
	
	
	
	public static Item zauberersWeisheit() {
		Helmet weisheit = new Helmet(20,true);
		LinkedList mods = new LinkedList();
		ItemModification mod1 = new ItemModification(Attribute.PSYCHO,1);
		ItemModification mod2 = new ItemModification(Attribute.DUSTREG,0.2);
		ItemModification mod3 = new ItemModification(Attribute.DUST,5);
		
		mods.add(mod1);
		mods.add(mod2);
		mods.add(mod3);
		weisheit.setModifications(mods);
		weisheit.setUnique();
		weisheit.setName("Zauberers Weisheit");
		weisheit.setWorth(80);
		
		return weisheit;
	}
	
	public static Item atlethenhaut() {
		Armor haut = new Armor(20,true);
		LinkedList mods = new LinkedList();
		ItemModification mod1 = new ItemModification(Attribute.DEXTERITY,1);
		ItemModification mod2 = new ItemModification(Attribute.STRENGTH,1);
		ItemModification mod3 = new ItemModification(Attribute.HEALTH,5);
		mods.add(mod1);
		mods.add(mod2);	
		mods.add(mod3);
		haut.setModifications(mods);
		haut.setUnique();
		haut.setName("Athletenhaut");
		haut.setWorth(70);
		
		return haut;
	}
	
	public static Item glasSchild() {
		Shield glas = new Shield(200,true,2);
		
		glas.setUnique();	
		glas.setWorth(40);
		glas.setName("Gläserner Engel");
		
		
		return glas;
	}
	
	public static Item ebertsklinge() {
		Sword ebertsklinge = new Sword(1,15, 25,true, 35);
		ItemModification mod1 = new ItemModification(Attribute.STRENGTH,1);
		ItemModification mod2 = new ItemModification(Attribute.PSYCHO,1);
		ItemModification mod3 = new ItemModification(Attribute.DEXTERITY,1);
		LinkedList mods = new LinkedList();
		mods.add(mod1);
		mods.add(mod2);
		mods.add(mod3);
		ebertsklinge.setModifications(mods);
		ebertsklinge.setUnique();
		ebertsklinge.setName("Ebertsklinge");
		ebertsklinge.setWorth(120);
		
		return ebertsklinge;
	}


	


	/**
	 * Method getMagicShield.
	 * @param value
	 * @param quotient
	 * @return item
	 */
	private static Item getMagicShield(int value, double quotient) {
		return getMagicItem(4,value, quotient);
		
	}
	
	private static Item getMagicItem(int code, int value, double quotient) {
		int item1;
		////System.out.println("MAGISCHER GEGENSTAND!");
		////System.out.println("value: "+value);
		////System.out.println("quotien: "+quotient);
		if(Math.random() < 0.7) {
			item1 = (int) ((value) / (2 * quotient));
			if(item1 < 10) {
				item1 = 10;
			}	
			
		}
		else {
			//gr�sseres Standarditem
			item1 = (int) ((value) / ((quotient * 4) / 3));
			
		}
		////System.out.println("Realteil1: "+item1);
		int rest = item1 % 10;
		item1 -= rest;
		int magic = value - item1;
		if(item1 < 10) {
			item1 = 10;	
			magic = value - 10;
		}
		////System.out.println("Realteil2: "+item1);
		////System.out.println("Magieteil: "+magic);
		
		Item i = null;
		
		LinkedList l = new LinkedList();
		getModifications(magic, l);
		
		if(code == 1) {
			i = Weapon.newWeapon(item1,true);
			
		}
		else if(code == 2) {
			i = new Helmet(item1,true);
		}
		else if(code == 3) {
			i = new Armor(item1,true);
		}
		else if(code == 4) {
			i = new Shield(item1,true);
		}
		////System.out.println("Anzahl der Modificationen bei magischem Gegenstand: "+l.size());
		
		i.setModifications(l);
		if(l.size() == 0) {
			i.setMagic(false);	
		}
	
		i.setWorth(value);
		return i;
		
	}
	
	private static void getModifications(int worth, LinkedList l) {
		
		int health = 10;
		int dust = 10;
		int dustReg = 10;
		int strength = 5;
		int dexterity = 5;
		int psycho = 5;
		int axe = 5;
		int club = 5;
		int lance = 5;
		int sword = 5;
		int wolfknife = 5;
		int nature = 3;
		int creature = 3;
		int undead = 3;
		int scout = 1;
		int threat = 1;
		int [] p = {health,dust,dustReg,strength,dexterity,psycho,axe,club,lance,sword,wolfknife,nature,creature,undead,scout,threat};
		int sum = summieren(p);
		int k = (int)(Math.random() * sum);
		
		if(k <= summieren(p,0)) {
			int rest = buyHealth(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,1)) {
			int rest = buyDust(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,2)) {
			int rest = buyDustReg(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,3)) {
			int rest = buyStrength(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,4)) {
			int rest = buyDexterity(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,5)) {
			int rest = buyPsycho(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,6)) {
			int rest = buyWeapon(Attribute.AXE,worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,7)) {
			int rest = buyWeapon(Attribute.CLUB,worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,8)) {
			int rest = buyWeapon(Attribute.LANCE,worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,9)) {
			int rest = buyWeapon(Attribute.SWORD,worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,10)) {
			int rest = buyWeapon(Attribute.WOLFKNIFE,worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,11)) {
			int rest = buyNature(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,12)) {
			int rest = buyCreature(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		else if(k <= summieren(p,13)) {
			int rest = buyUndead(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}else if(k <= summieren(p,14)) {
			int rest = buyScout(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}else if(k <= summieren(p,15)) {
			int rest = buyThreat(worth,l);
			if(rest > 5) {
				getModifications(rest,l);	
			}
		}
		
		
		
//		if(worth >= 100) {
//			//////System.out.println("Modification �ber 100 gefordert!!!
//
//			getModifications(worth/3,l);
//			getModifications((2*worth)/3,l);
//			
//					}
//		else if(worth >= 50) {
//			modHelp mod1 = new modHelp(getRandomAttr(),2);
//			l.add(mod1);
//			worth -= 40;
//			getModifications(worth, l);
//		}
//		else if(worth >= 20) {
//			modHelp mod1 = new modHelp(getRandomAttr(),1);
//			l.add(mod1);
//			worth -= 20;
//			getModifications(worth, l);
//		}
//		else if(worth >= 15) {
//			modHelp mod1 = new modHelp("DustReg", 0.2);
//			l.add(mod1);
//			worth -= 15;
//			getModifications(worth, l);
//		}
//		else if(worth >= 5) {
//			modHelp mod1 = new modHelp("DustReg", 0.1);
//			l.add(mod1);
//			worth -= 5;
//			getModifications(worth, l);
//		}
//		else {
//			
//		}
			
		
	}


	/**
	 * Method getMagicArmor.
	 * @param value
	 * @param quotient
	 * @return item
	 */
	private static Item getMagicArmor(int value, double quotient) {
		
		return getMagicItem(3,value,quotient);
	}


	/**
	 * Method getMagicHelmet.
	 * @param value
	 * @param quotient
	 * @return item
	 */
	private static Item getMagicHelmet(int value, double quotient) {
		return getMagicItem(2,value,quotient);
	}


	/**
	 * Method getMagicWeapon.
	 * @param value
	 * @param quotient
	 * @return item
	 */
	private static Item getMagicWeapon(int value, double quotient) {
		return getMagicItem(1,value,quotient);
	}
	
	private static int summieren(int [] p) {
		int sum = 0;
		for(int i = 0; i < p.length; i++) {
			sum += p[i];	
		}	
		return sum;
	}
	
	private static int summieren(int [] p, int j) {
		int sum = 0;
		for(int i = 0; i <= j; i++) {
			sum += p[i];	
		}	
		return sum;	
	}


	public static Item getGoodItem(int value, double quotient) {
		int magicPaper = 50;
		int randomPotion = 1;
		int magicWeapon = 20;
		int magicArmor = 20;
		int magicHelmet = 20;
		int magicShield = 20;
		int unique = 1;
		int heiltrank = 2;
		int book = 10;
		int p[] = {magicPaper,randomPotion,magicWeapon,magicArmor,magicHelmet,magicShield,unique,heiltrank,book};
		int sum = summieren(p);
		////System.out.println("making good Item"+" "+quotient);
		int k = (int)(Math.random() * sum);
		Item i = null;
		
		if(k <= summieren(p,0)) {
			i = getMagicPaper(value, quotient);
		}
		else if(k <= summieren(p,1)) {
			i = getRandomPotion(value);
		}
		else if(k <= summieren(p,2)) {
			i = getMagicWeapon(value, quotient);
		}
		else if(k <= summieren(p,3)) {
			i = getMagicArmor(value, quotient);
		}
		else if(k <= summieren(p,4)) {
			i = getMagicHelmet(value, quotient);
		}
		else if(k <= summieren(p,5)) {
			i = getMagicShield(value, quotient);
		}
		else if(k <= summieren(p,6)) {
			i = getUnique(value, quotient);
		}
		else if(k <= summieren(p,7)) {
			////System.out.println("mache Heiltrank - good");
			i = new HealPotion(value);
		}
		else if(k <= summieren(p,8)) {
			i = getBook(value);
			if ( i == null) {
				i = getLowerItem(value, quotient);
			}
		}
		
		return i;
	}
	
	public static Item getGift(int value, double quotient) {
			int magicPaper = 0;
			int randomPotion = 0;
			int magicWeapon = 20;
			int magicArmor = 20;
			int magicHelmet = 20;
			int magicShield = 20;
			int unique = 1;
			int heiltrank = 0;
			int book = 10;
			int p[] = {magicPaper,randomPotion,magicWeapon,magicArmor,magicHelmet,magicShield,unique,heiltrank,book};
			int sum = summieren(p);
			////System.out.println("making good Item"+" "+quotient);
			int k = (int)(Math.random() * sum);
			Item i = null;
		
			if(k <= summieren(p,0)) {
				i = getMagicPaper(value, quotient);
			}
			else if(k <= summieren(p,1)) {
				i = getRandomPotion(value);
			}
			else if(k <= summieren(p,2)) {
				i = getMagicWeapon(value, quotient);
			}
			else if(k <= summieren(p,3)) {
				i = getMagicArmor(value, quotient);
			}
			else if(k <= summieren(p,4)) {
				i = getMagicHelmet(value, quotient);
			}
			else if(k <= summieren(p,5)) {
				i = getMagicShield(value, quotient);
			}
			else if(k <= summieren(p,6)) {
				i = getUnique(value, quotient);
			}
			else if(k <= summieren(p,7)) {
				////System.out.println("mache Heiltrank - good");
				i = new HealPotion(value);
			}
			else if(k <= summieren(p,8)) {
				i = getBook(value);
				if ( i == null) {
					i = getLowerItem(value, quotient);
				}
			}
		
			return i;
		}

//public static String getRandomAttr() {
//		////System.out.println("getRandomAttr");
//		int k = (int) (Math.random() * 100);
//		////System.out.println(Integer.toString(k));
//		String s;
//
//		if (k < 12) {
//			s = ("Psycho");
//		} else if (k < 24) {
//			s = ("Strength");
//		} else if (k < 36) {
//			s = ("Dexterity");
//		} else if (k < 42) {
//			s = ("Axe");
//		} else if (k < 48) {
//			s = ("Club");
//		} else if (k < 54) {
//			s = ("Lance");
//		} else if (k < 60) {
//			s = ("Sword");
//		} else if (k < 66) {
//			s = ("Wolfknife");
//		} else if (k < 73) {
//			s = ("Nature_Knowledge");
//		} else if (k < 80) {
//			s = ("Creature_Knowledge");
//		} else if (k < 87) {
//			s = ("Undead_Knowledge");
//		} else if (k < 95) {
//			s = ("Scout");
//		} else if (k < 100) {
//			s = ("Threat");
//		} else
//			s = ("nichts");
//		////System.out.println(s);
//		return s;
//	}
	
	private static Spell getRandomSpell() {
		int k = (int)(Math.random() * spells.length);
		//hier muss noch die Methode f�r die Wahrscheinlichkeiten der einzelnen Zauberspr�che rein
		Spell s = Spell.getSpell(spells[k]);
		return s;
		
	}
	
	public static Weapon newWeapon(int value, boolean magic) {
		int a = (int) (Math.random() * 100);
		//int value = (int)d;
		if (a <= 15)
			return new Lance(value, false);
		else if (a <= 30)
			return new Wolfknife(value, false);
		else if (a <= 45)
			return new Club(value, false);
		else if (a <= 70)
			return new Axe(value, false);
		else
			return new Sword(value, false);
	}
	
	
	private static int buyHealth(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.4) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		if(value > 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.HEALTH,30));	
		}
		else if(value > 70) {
			value -= 70;
			l.add(new ItemModification(Attribute.HEALTH,20));	
		}
		else if(value > 50) {
			value -= 50;
			l.add(new ItemModification(Attribute.HEALTH,15));	
		}
		else if(value > 40) {
			value -= 30;
			l.add(new ItemModification(Attribute.HEALTH,13));	
		}
		else if(value > 30) {
			value -= 30;
			l.add(new ItemModification(Attribute.HEALTH,11));	
		}
		else if(value >= 26) {
			value -= 26;
			l.add(new ItemModification(Attribute.HEALTH,10));	
		}
		else if(value >= 22) {
			value -= 22;
			l.add(new ItemModification(Attribute.HEALTH,9));	
		}
		else if(value >= 18) {
			value -= 18;
			l.add(new ItemModification(Attribute.HEALTH,8));	
		}
		else if(value >= 15) {
			value -= 15;
			l.add(new ItemModification(Attribute.HEALTH,7));	
		}
		else if(value >= 12) {
			value -= 12;
			l.add(new ItemModification(Attribute.HEALTH,6));	
		}
		else if(value >= 10) {
			value -= 10;
			l.add(new ItemModification(Attribute.HEALTH,5));	
		}
		else if(value >= 8) {
			value -= 8;
			l.add(new ItemModification(Attribute.HEALTH,4));	
		}
		else if(value >= 5) {
			value -= 5;
			l.add(new ItemModification(Attribute.HEALTH,3));	
		}
		else if(value >= 2) {
			value -= 2;
			l.add(new ItemModification(Attribute.HEALTH,2));	
		}
		else if(value >= 1) {
			value -= 1;
			l.add(new ItemModification(Attribute.HEALTH,1));
				
		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyDust(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.4) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		if(value > 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.DUST,30));	
		}
		else if(value > 70) {
			value -= 70;
			l.add(new ItemModification(Attribute.DUST,20));	
		}
		else if(value > 50) {
			value -= 50;
			l.add(new ItemModification(Attribute.DUST,15));	
		}
		else if(value > 40) {
			value -= 30;
			l.add(new ItemModification(Attribute.DUST,13));	
		}
		else if(value > 30) {
			value -= 30;
			l.add(new ItemModification(Attribute.DUST,11));	
		}
		else if(value >= 26) {
			value -= 26;
			l.add(new ItemModification(Attribute.DUST,10));	
		}
		else if(value >= 22) {
			value -= 22;
			l.add(new ItemModification(Attribute.DUST,9));	
		}
		else if(value >= 18) {
			value -= 18;
			l.add(new ItemModification(Attribute.DUST,8));	
		}
		else if(value >= 15) {
			value -= 15;
			l.add(new ItemModification(Attribute.DUST,7));	
		}
		else if(value >= 12) {
			value -= 12;
			l.add(new ItemModification(Attribute.DUST,6));	
		}
		else if(value >= 10) {
			value -= 10;
			l.add(new ItemModification(Attribute.DUST,5));	
		}
		else if(value >= 8) {
			value -= 8;
			l.add(new ItemModification(Attribute.DUST,4));	
		}
		else if(value >= 5) {
			value -= 5;
			l.add(new ItemModification(Attribute.DUST,3));	
		}
		else if(value >= 2) {
			value -= 2;
			l.add(new ItemModification(Attribute.DUST,2));	
		}
		else if(value >= 1) {
			value -= 1;
			l.add(new ItemModification(Attribute.DUST,1));
				
		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyDustReg(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.4) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		if(value >= 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.DUSTREG,0.7));	
		}
		else if(value >= 70) {
			value -= 70;
			l.add(new ItemModification(Attribute.DUSTREG,0.5));	
		}
		else if(value >= 50) {
			value -= 50;
			l.add(new ItemModification(Attribute.DUSTREG,0.4));	
		}
		else if(value >= 35) {
			value -= 35;
			l.add(new ItemModification(Attribute.DUSTREG,0.3));	
		}
		else if(value >= 20) {
			value -= 20;
			l.add(new ItemModification(Attribute.DUSTREG,0.2));	
		}
		else if(value >= 8) {
			value -= 8;
			l.add(new ItemModification(Attribute.DUSTREG,0.1));
				
		}
		rest = value + rest;
		return rest;	
	}
	private static int buyStrength(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.4) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		if(value >= 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.STRENGTH,6));	
		}
		else if(value >= 80) {
			value -= 80;
			l.add(new ItemModification(Attribute.STRENGTH,5));	
		}
		else if(value >= 60) {
			value -= 60;
			l.add(new ItemModification(Attribute.STRENGTH,4));	
		}
		else if(value >= 40) {
			value -= 40;
			l.add(new ItemModification(Attribute.STRENGTH,3));	
		}
		else if(value >= 25) {
			value -= 25;
			l.add(new ItemModification(Attribute.STRENGTH,2));	
		}
		else if(value >= 10) {
			value -= 10;
			l.add(new ItemModification(Attribute.STRENGTH,1));
				
		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyDexterity(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.4) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		if(value >= 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.DEXTERITY,6));	
		}
		else if(value >= 80) {
			value -= 80;
			l.add(new ItemModification(Attribute.DEXTERITY,5));	
		}
		else if(value >= 60) {
			value -= 60;
			l.add(new ItemModification(Attribute.DEXTERITY,4));	
		}
		else if(value >= 40) {
			value -= 40;
			l.add(new ItemModification(Attribute.DEXTERITY,3));	
		}
		else if(value >= 25) {
			value -= 25;
			l.add(new ItemModification(Attribute.DEXTERITY,2));	
		}
		else if(value >= 10) {
			value -= 10;
			l.add(new ItemModification(Attribute.DEXTERITY,1));
				
		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyPsycho(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.4) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		if(value >= 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.PSYCHO,6));	
		}
		else if(value >= 80) {
			value -= 80;
			l.add(new ItemModification(Attribute.PSYCHO,5));	
		}
		else if(value >= 60) {
			value -= 60;
			l.add(new ItemModification(Attribute.PSYCHO,4));	
		}
		else if(value >= 40) {
			value -= 40;
			l.add(new ItemModification(Attribute.PSYCHO,3));	
		}
		else if(value >= 25) {
			value -= 25;
			l.add(new ItemModification(Attribute.PSYCHO,2));	
		}
		else if(value >= 10) {
			value -= 10;
			l.add(new ItemModification(Attribute.PSYCHO,1));
				
		}
		rest = value + rest;
		return rest;	
	}
	
//	private static int buyAxe(int value, LinkedList l) {
//		
//		int rest = 0;
//		//eventuell einen gr��eren Teil �brig lassen
//		if(Math.random() < 0.4) {
//			rest = (int)(0.4+ (Math.random()/5)) * value;
//			value -= rest;
//		}
//		if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp("Axe",6));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Axe",5));	
//		}
//		else if(value >= 60) {
//			value -= 60;
//			l.add(new modHelp("Axe",4));	
//		}
//		else if(value >= 40) {
//			value -= 40;
//			l.add(new modHelp("Axe",3));	
//		}
//		else if(value >= 25) {
//			value -= 25;
//			l.add(new modHelp("Axe",2));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp("Axe",1));
//				
//		}
//		rest = value + rest;
//		return rest;	
//	}
//	
//	private static int buyClub(int value, LinkedList l) {
//		
//		int rest = 0;
//		//eventuell einen gr��eren Teil �brig lassen
//		if(Math.random() < 0.4) {
//			rest = (int)(0.4+ (Math.random()/5)) * value;
//			value -= rest;
//		}
//		if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp("Club",6));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Club",5));	
//		}
//		else if(value >= 60) {
//			value -= 60;
//			l.add(new modHelp("Club",4));	
//		}
//		else if(value >= 40) {
//			value -= 40;
//			l.add(new modHelp("Club",3));	
//		}
//		else if(value >= 25) {
//			value -= 25;
//			l.add(new modHelp("Club",2));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp("Club",1));
//				
//		}
//		rest = value + rest;
//		return rest;	
//	}
//	
//	private static int buyLance(int value, LinkedList l) {
//		
//		int rest = 0;
//		//eventuell einen gr��eren Teil �brig lassen
//		if(Math.random() < 0.4) {
//			rest = (int)(0.4+ (Math.random()/5)) * value;
//			value -= rest;
//		}
//		if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp("Lance",6));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Lance",5));	
//		}
//		else if(value >= 60) {
//			value -= 60;
//			l.add(new modHelp("Lance",4));	
//		}
//		else if(value >= 40) {
//			value -= 40;
//			l.add(new modHelp("Lance",3));	
//		}
//		else if(value >= 25) {
//			value -= 25;
//			l.add(new modHelp("Lance",2));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp("Lance",1));
//				
//		}
//		rest = value + rest;
//		return rest;	
//	}
//	
//	private static int buySword(int value, LinkedList l) {
//		
//		int rest = 0;
//		//eventuell einen gr��eren Teil �brig lassen
//		if(Math.random() < 0.4) {
//			rest = (int)(0.4+ (Math.random()/5)) * value;
//			value -= rest;
//		}
//		if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp("Sword",6));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Sword",5));	
//		}
//		else if(value >= 60) {
//			value -= 60;
//			l.add(new modHelp("Sword",4));	
//		}
//		else if(value >= 40) {
//			value -= 40;
//			l.add(new modHelp("Sword",3));	
//		}
//		else if(value >= 25) {
//			value -= 25;
//			l.add(new modHelp("Sword",2));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp("Sword",1));
//				
//		}
//		rest = value + rest;
//		return rest;	
//	}
//	
//	private static int buyWolfknife(int value, LinkedList l) {
//		
//		int rest = 0;
//		//eventuell einen gr��eren Teil �brig lassen
//		if(Math.random() < 0.4) {
//			rest = (int)(0.4+ (Math.random()/5)) * value;
//			value -= rest;
//		}
//		if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp("Wolfknife",6));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp("Wolfknife",5));	
//		}
//		else if(value >= 60) {
//			value -= 60;
//			l.add(new modHelp("Wolfknife",4));	
//		}
//		else if(value >= 40) {
//			value -= 40;
//			l.add(new modHelp("Wolfknife",3));	
//		}
//		else if(value >= 25) {
//			value -= 25;
//			l.add(new modHelp("Wolfknife",2));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp("Wolfknife",1));
//				
//		}
//		rest = value + rest;
//		return rest;	
//	}
//	
	private static int buyNature(int value, LinkedList l) {
		
		int rest = 0;
		int s = Attribute.NATURE_KNOWLEDGE;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.1) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		
		if(value >= 400) {
			value -= 400;
			l.add(new ItemModification(s,25));	
		}
		else if(value >= 250) {
			value -= 250;
			l.add(new ItemModification(s,20));	
		}
//		else if(value >= 200) {
//			value -= 200;
//			l.add(new modHelp(s,18));	
//		}
		else if(value >= 160) {
			value -= 160;
			l.add(new ItemModification(s,16));	
		}
//		else if(value >= 130) {
//			value -= 130;
//			l.add(new modHelp(s,14));	
//		}
//		else if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp(s,12));	
//		}
//		else if(value >= 80) {
//			value -= 80;
//			l.add(new modHelp(s,10));	
//		}
//		else if(value >= 68) {
//			value -= 68;
//			l.add(new modHelp(s,8));	
//		}
//		else if(value >= 57) {
//			value -= 57;
//			l.add(new modHelp(s,7));	
//		}
//		else if(value >= 46) {
//			value -= 46;
//			l.add(new modHelp(s,6));	
//		}
		else if(value >= 36) {
			value -= 36;
			l.add(new ItemModification(s,5));	
		}
//		else if(value >= 27) {
//			value -= 27;
//			l.add(new modHelp(s,4));	
//		}
//		else if(value >= 18) {
//			value -= 18;
//			l.add(new modHelp(s,3));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp(s,2));	
//		}
//		else if(value >= 5) {
//			value -= 5;
//			l.add(new modHelp(s,1));
//				
//		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyWeapon(int s,int value, LinkedList l) {
		
		int rest = 0;
		//String s = "Nature_Knowledge";
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.1) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		
		if(value >= 400) {
			value -= 400;
			l.add(new ItemModification(s,25));	
		}
		else if(value >= 250) {
			value -= 250;
			l.add(new ItemModification(s,20));	
		}
		else if(value >= 200) {
			value -= 200;
			l.add(new ItemModification(s,18));	
		}
//		else if(value >= 160) {
//			value -= 160;
//			l.add(new modHelp(s,16));	
//		}
//		else if(value >= 130) {
//			value -= 130;
//			l.add(new modHelp(s,14));	
//		}
//		else if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp(s,12));	
//		}
		else if(value >= 80) {
			value -= 80;
			l.add(new ItemModification(s,10));	
		}
		else if(value >= 68) {
			value -= 68;
			l.add(new ItemModification(s,8));	
		}
//		else if(value >= 57) {
//			value -= 57;
//			l.add(new modHelp(s,7));	
//		}
//		else if(value >= 46) {
//			value -= 46;
//			l.add(new modHelp(s,6));	
//		}
		else if(value >= 36) {
			value -= 36;
			l.add(new ItemModification(s,5));	
		}
//		else if(value >= 27) {
//			value -= 27;
//			l.add(new modHelp(s,4));	
//		}
//		else if(value >= 18) {
//			value -= 18;
//			l.add(new modHelp(s,3));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp(s,2));	
//		}
//		else if(value >= 5) {
//			value -= 5;
//			l.add(new modHelp(s,1));
//				
//		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyCreature(int value, LinkedList l) {
		
		int rest = 0;
		int s = Attribute.CREATURE_KNOWLEDGE;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.1) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		
		if(value >= 400) {
			value -= 400;
			l.add(new ItemModification(s,25));	
		}
		else if(value >= 250) {
			value -= 250;
			l.add(new ItemModification(s,20));	
		}
//		else if(value >= 200) {
//			value -= 200;
//			l.add(new modHelp(s,18));	
//		}
		else if(value >= 160) {
			value -= 160;
			l.add(new ItemModification(s,16));	
		}
//		else if(value >= 130) {
//			value -= 130;
//			l.add(new modHelp(s,14));	
//		}
//		else if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp(s,12));	
//		}
		else if(value >= 80) {
			value -= 80;
			l.add(new ItemModification(s,10));	
		}
//		else if(value >= 68) {
//			value -= 68;
//			l.add(new modHelp(s,8));	
//		}
//		else if(value >= 57) {
//			value -= 57;
//			l.add(new modHelp(s,7));	
//		}
//		else if(value >= 46) {
//			value -= 46;
//			l.add(new modHelp(s,6));	
//		}
		else if(value >= 36) {
			value -= 36;
			l.add(new ItemModification(s,5));	
		}
//		else if(value >= 27) {
//			value -= 27;
//			l.add(new modHelp(s,4));	
//		}
//		else if(value >= 18) {
//			value -= 18;
//			l.add(new modHelp(s,3));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp(s,2));	
//		}
//		else if(value >= 5) {
//			value -= 5;
//			l.add(new modHelp(s,1));
//				
//		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyUndead(int value, LinkedList l) {
		
		int rest = 0;
		int s = Attribute.UNDEAD_KNOWLEDGE;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.1) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		
		if(value >= 400) {
			value -= 400;
			l.add(new ItemModification(s,25));	
		}
		else if(value >= 250) {
			value -= 250;
			l.add(new ItemModification(s,20));	
		}
//		else if(value >= 200) {
//			value -= 200;
//			l.add(new modHelp(s,18));	
//		}
		else if(value >= 160) {
			value -= 160;
			l.add(new ItemModification(s,16));	
		}
//		else if(value >= 130) {
//			value -= 130;
//			l.add(new modHelp(s,14));	
//		}
//		else if(value >= 100) {
//			value -= 100;
//			l.add(new modHelp(s,12));	
//		}
		else if(value >= 80) {
			value -= 80;
			l.add(new ItemModification(s,10));	
		}
//		else if(value >= 68) {
//			value -= 68;
//			l.add(new modHelp(s,8));	
//		}
//		else if(value >= 57) {
//			value -= 57;
//			l.add(new modHelp(s,7));	
//		}
//		else if(value >= 46) {
//			value -= 46;
//			l.add(new modHelp(s,6));	
//		}
		else if(value >= 36) {
			value -= 36;
			l.add(new ItemModification(s,5));	
		}
//		else if(value >= 27) {
//			value -= 27;
//			l.add(new modHelp(s,4));	
//		}
//		else if(value >= 18) {
//			value -= 18;
//			l.add(new modHelp(s,3));	
//		}
//		else if(value >= 10) {
//			value -= 10;
//			l.add(new modHelp(s,2));	
//		}
//		else if(value >= 5) {
//			value -= 5;
//			l.add(new modHelp(s,1));
//				
//		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyScout(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.1) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		
		if(value >= 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.SCOUT,3));	
		}
		else if(value >= 50) {
			value -= 50;
			l.add(new ItemModification(Attribute.SCOUT,2));	
		}
		else if(value >= 20) {
			value -= 20;
			l.add(new ItemModification(Attribute.SCOUT,1));
				
		}
		rest = value + rest;
		return rest;	
	}
	
	private static int buyThreat(int value, LinkedList l) {
		
		int rest = 0;
		//eventuell einen gr��eren Teil �brig lassen
		if(Math.random() < 0.1) {
			rest = (int)(0.4+ (Math.random()/5)) * value;
			value -= rest;
		}
		
		if(value >= 100) {
			value -= 100;
			l.add(new ItemModification(Attribute.THREAT,3));	
		}
		else if(value >= 50) {
			value -= 50;
			l.add(new ItemModification(Attribute.THREAT,2));	
		}
		else if(value >= 20) {
			value -= 20;
			l.add(new ItemModification(Attribute.THREAT,1));
				
		}
		rest = value + rest;
		return rest;	
	}

	public static AttrPotion[] getElexirs(int count, int value) {
		AttrPotion[] potions = new AttrPotion[count];
		int a = (value - 10) * 2;
		for (int i = 0; i < count; i++) {
			int s = Character.getRandomAttr();
			potions[i] = new AttrPotion(s, value + ((int) Math.random() * a));
	
		}
		return potions;
	
	}
	
	
	
}

	
