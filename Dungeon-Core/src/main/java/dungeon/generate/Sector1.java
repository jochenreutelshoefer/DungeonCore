/**
 * Implementierung der randomisierten Generierung von Sektor 1
 * Stellt diesen Bereich komplett fertig, Monster, Quest, Items, Schreine, Hallen... 
 *
 */
package dungeon.generate;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.attribute.Attribute;
import figure.monster.Dwarf;
import figure.monster.Monster;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Wolf;
import game.DungeonGame;
import game.JDEnv;
import item.AttrPotion;
import item.DustItem;
import item.HealPotion;
import item.Item;
import item.ItemPool;
import item.Key;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Weapon;
import item.equipment.weapon.Wolfknife;
import item.paper.BookAttr;
import item.paper.InfoScroll;
import item.paper.Scroll;
import item.quest.LuziaAmulett;
import item.quest.LuziasBall;
import item.quest.Thing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import shrine.Angel;
import shrine.Corpse;
import shrine.HealthFountain;
import shrine.Luzia;
import shrine.RepairShrine;
import shrine.Shrine;
import shrine.SorcerLab;
import shrine.Statue;
import spell.Discover;
import spell.Prayer;
import spell.conjuration.FirConjuration;
import ai.AbstractAI;
import ai.ChaserAI;
import dungeon.Chest;
import dungeon.Door;
import dungeon.DoorCountRoomComparator;
import dungeon.Dungeon;
import dungeon.HiddenSpot;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.quest.RoomQuest;
import dungeon.quest.RoomQuest_1x1_1;
import dungeon.quest.RoomQuest_2x2_1;
import dungeon.util.DungeonUtils;
import dungeon.util.RouteInstruction;

public class Sector1 extends Sector {

	private final List<Item> restItems = new LinkedList<Item>();

	private final List<Item> rareItems = new LinkedList<Item>();

	private Room con_room = null;

	public Sector1(Dungeon d, JDPoint startRoom, int number,
			int avMonsterStrength, int mainSize, DungeonGame game, AbstractDungeonFiller df) throws DungeonGenerationFailedException{

		this.df = df;
		this.game = game;
		this.d = d;
		this.number = number;
		this.startRoom = startRoom;

		mainHall = new DefaultHall(d, mainSize, 800, startRoom,
				RouteInstruction.NORTH,  this, "main", 0);

		// CHEATERSCHWERT
		// d.getRoom(startRoom).addItem(new Sword(200,false));
		// d.getRoom(startRoom).addItem(new Shield(300,false));
		// Scroll scroll = new Scroll(new KeyLocator(1),5);
		// d.getRoom(startRoom).addItem(scroll);
		
		mainHall.makeArea(null);
		d.getRoom(startRoom).setStart();
		// ((SectorDungeonFiller1) df).updateView();
		List<Item> itemList = getItemList();
		LinkedList<Chest> chests = getChests(5, itemList);

		DefaultHall normalRaum = makeHall(0, 5, 100, 10, 0, 0, null,
				RouteInstruction.NORTH, "normal", 3);
		if(normalRaum == null) {
			throw new DungeonGenerationFailedException();
		}
		halls.add(normalRaum);

		

		boolean b = normalRaum.plugChest(chests.removeFirst());
		int bigmonster = 5;
		int smallmonster = 8;
		List<Figure> normal = new LinkedList<Figure>();
		for (int i = 0; i < bigmonster; i++) {
			Monster m = df.getBigMonster(1800);
			df.equipMonster(m, 2);
			normal.add(m);
		}
		for (int i = 0; i < smallmonster; i++) {
			Monster m = df.getSmallMonster(1500);
			df.equipMonster(m, 2);
			normal.add(m);
		}
		normalRaum.addMonsterToList(normal);
		normalRaum.setMonster();

		// DARK MASTER !!!!
		// Room mastersRoom = normalRaum.getRandomRoomForShrineNonRQ();
		// dark_master_room = mastersRoom;
		// Dark_master_key key1 = new Dark_master_key(150, false, mastersRoom);
		// Dark_master_key key2 = new Dark_master_key(150, false, mastersRoom);
		//
		// DarkMasterShrine masterShrine = new DarkMasterShrine(mastersRoom,
		// key1, key2);
		// mastersRoom.setShrine(masterShrine, true);
		// normalRaum.getRandomMonster().addItem(key1, null);
		// normalRaum.getRandomMonster().addItem(key2, null);

		DefaultHall wolfRaum = makeHall(Monster.WOLF, 5, 1200, 6, 0, 0, df
				.getNextKey(), 0, "Wolf", 1);
		halls.add(wolfRaum);
		wolfRaum.removeDoors(2);
		Chest wolfChest = chests.removeFirst();
		wolfChest.takeItem(new BookAttr(Attribute.NATURE_KNOWLEDGE, 1));
		b = wolfRaum.plugChest(wolfChest);
		// System.out.println("Chest gesetzt: " + b);
		// ((SectorDungeonFiller1) df).updateView();

		DefaultHall orcRaum = makeHall(Monster.ORC, 5, 1200, 6, 0, 0, df.getNextKey(),
				0, "Ork", 2);
		halls.add(orcRaum);
		orcRaum.removeDoors(2);
		Chest orcChest = chests.removeFirst();
		orcChest.takeItem(new BookAttr(Attribute.CREATURE_KNOWLEDGE, 1));
		b = orcRaum.plugChest(orcChest);
		// System.out.println("Chest gesetzt: " + b);
		// ((SectorDungeonFiller1) df).updateView();

		DefaultHall skeletonRaum = makeHall(Monster.SKELETON, 5, 1200, 6, 0, 0, df
				.getNextKey(), 0, "Skelett", 4);
		halls.add(skeletonRaum);
		skeletonRaum.removeDoors(2);
		Chest skelChest = chests.removeFirst();
		skelChest.takeItem(new BookAttr(Attribute.UNDEAD_KNOWLEDGE, 1));
		b = skeletonRaum.plugChest(skelChest);
		// System.out.println("Chest gesetzt: " + b);
		// ((SectorDungeonFiller1) df).updateView();

		// System.out.println("Versuche RoomQuest zu setzen!");
		// setRoomQuest("2x2_1", mainHall, null);
		Shrine hf = new HealthFountain(30, 2);
		Shrine rs = new RepairShrine(0.3);
		d.addShrine(hf);

		if (setRoomQuest("1x1_1 ", mainHall, rs)) {
			d.addShrine(rs);
		}
		setRoomQuest("1x1_1 ", mainHall, hf);

		b = mainHall.plugChest(chests.removeFirst());
		// //System.out.println("Chest gesetzt: " + b);
		// mainHall.removeDoors(5);
		Monster s = new Skeleton(800, false);
		Monster w = new Wolf(800, false);
		Monster o = new Orc(800, false);
		df.equipMonster(s, 1);
		df.equipMonster(w, 1);
		df.equipMonster(o, 1);
		mainHall.addMonsterToList(s);
		mainHall.addMonsterToList(w);
		mainHall.addMonsterToList(o);

		// DEAD BODY QUEST

		// Schatzraum bestimmen

		int type = (int) (Math.random() * 5);
		Weapon weap = null;
		if (type == 0) {
			weap = new Axe(30, false);
		}
		if (type == 1) {
			weap = new Sword(30, false);

		}
		if (type == 2) {
			weap = new Club(30, false);
		}
		if (type == 3) {
			weap = new Wolfknife(30, false);
		}
		if (type == 4) {
			weap = new Lance(30, false);
		}
		weap.takeRelDamage(0.40);
		Room xroom = getXmasRoom();
		xroom.addItem(weap);
		Room paxTreasure = this.getRandomRimRoom(wolfRaum, orcRaum,
				skeletonRaum, mainHall);
		if(paxTreasure == null) {
			throw new DungeonGenerationFailedException();
		}
		String str = Corpse
				.makeAufzeichnungen(d, startRoom, xroom, paxTreasure);

		List<Item> l = new LinkedList<Item>();
		Scroll disc = new Scroll(new Discover(1), 5);
		l.add(disc);
		Scroll fir = new Scroll(new FirConjuration(1), 5);
		l.add(fir);
		InfoScroll text = new InfoScroll(JDEnv.getResourceBundle().getString(
				"shrine_corpse_notes"), str);
		l.add(text);
		l.add(new AttrPotion((Attribute.DEXTERITY), 25));
		Corpse leiche = new Corpse(l, d.getRoom(startRoom), type);
		d.getRoom(startRoom).setShrine(leiche, true);

		// xmas
		// room xroom = getXmasRoom();
		// weapon weap = weapon.newWeapon(25,false);
		// weap.takeRelDamage(0.40);
		// xroom.addItem(weap);
		// item theIt = null;
		// //int j = 0;
		//		
		// theIt = itemPool.getGift(30 + (int) (Math.random() * 20),1.5);
		// // System.out.println(theIt.toString());
		//		
		// shrine xmas = new xmas(theIt,xroom);
		// xroom.setShrine(xmas,true);

		// VIM
		Wolf vim = Wolf.buildCustomWolf(80, 5, 200, 1, 3, "Vim Wadenbeißer");
		
		
		
		AbstractAI vimAI = new ChaserAI();
		vim.setSpecifiedAI(vimAI);
		RoomQuest rq = mainHall.insertRQXY(1, 1, false, null);
		rq.getEntranceRoom().figureEnters(vim, 0);

		Room rimHome = this.getRandomRimRoom(wolfRaum, orcRaum, skeletonRaum,
				mainHall);
		Key vimKey = new Key(vim.getName());
		vim.takeItem(vimKey);
		
		Scroll disc2 = new Scroll(new Discover(1), 5);
		String str2 = JDEnv.getString("vim_document_part1");
		
		JDPoint location = rimHome.getPoint();
		
		DefaultHall rimHalle = rimHome.getHall();
		DefaultHall vimHall = makeVimHome(rimHome, vimKey);
		
		JDPoint home = vimHall.getStartPoint();
		int dir = DungeonUtils.getNeighbourDirectionFromTo(location, home)
				.getValue();
		String dirString = RouteInstruction.dirToString(dir);
		str2 += " "+location.toString();
		
		str2 += " "+dirString;
		str2 += JDEnv.getString("vim_document_part2");
		InfoScroll textVim = new InfoScroll(JDEnv.getResourceBundle().getString("vim_document_title"), str2);
		vim.takeItem(disc2);
		vim.takeItem(textVim);

		// STATUE
		mainHall.removeDoors(4);
		setStatue(0);
		mainHall.setMonster();

		// SORCLAB
		Room sorcLabRoom = mainHall.getRandomRoomForShrineNonRQ();
		SorcerLab sorc = new SorcerLab(sorcLabRoom);
		sorcLabRoom.setShrine(sorc, true);
		d.addShrine(sorc);
		sorcLabRoom.addItem(new DustItem(14));

		// LUZIA
		Room witchLabRoom = mainHall.getRandomRoomForShrineNonRQ();
		LuziasBall ball = new LuziasBall(100, false);
		Luzia witch = new Luzia(witchLabRoom, ball);
		ball.setLuzia(witch);
		witch.takeItem(ball);
		Thing amulet = new LuziaAmulett(witch);
		game.addTurnableItem(amulet);
		witch.setReqItem(amulet);
		witchLabRoom.setShrine(witch, true);
		d.addShrine(witch);
		Monster m = normalRaum.getRandomMonster();
		// System.out.println("Amulettmonster: "+m.toString());
		m.takeItem(amulet);
		// ball.solved();
		// d.getRoom(new point(18,39)).addItem(ball);
		// m.setMissionIndex(m.MISSION_FLEE);

		// Zwerg-QUEST
		Room dwarfRoom = mainHall.getRandomRoomForShrineNonRQ();
		Dwarf pax = new Dwarf();

		Key dwarfKey = new Key(pax.getName());
		pax.takeItem(dwarfKey);
		pax.takeItem(new Scroll(new Prayer(), 1));
		dwarfRoom.figureEnters(pax, 0);
		Monster infoMonster = normalRaum.getRandomMonster();
		int y = paxTreasure.getNumber().getY();
		String name = leiche.getText();
		String title = JDEnv.getResourceBundle().getString(
				"shrine_corpse_rest_title")
				+ " " + name;
		String textInfo = "##"
				+ JDEnv.getResourceBundle()
						.getString("shrine_corpse_rest_text") + " ##" + " - "
				+ y + " ###";
		InfoScroll info2 = new InfoScroll(title, textInfo);
		infoMonster.takeItem(info2);

		DefaultHall halle = paxTreasure.getHall();
		DefaultHall treasureHall = makeTreasureRoom(paxTreasure, dwarfKey);
		
		
		//ENGEL
		Room angelRoom = normalRaum.getRandomRoomForShrineNonRQ();
		Angel a = new Angel();
		angelRoom.setShrine(a,true);
		angelRoom.addItems(a.getRequestedItems(),null);
		
		

		con_room = normalRaum.getNorthestRoom();
		Key sec2Key = df.getNextKey();
		if (JDEnv.isBeginnerGame()) {
			sec2Key = new Key(JDEnv.getString("difficulty_end"));
		} else {
			restItems.add(sec2Key);
		}
		Door d1 = new Door(con_room, d.getRoomAt(con_room,
				RouteInstruction.direction(RouteInstruction.NORTH)), sec2Key);
		con_room.setDoor(d1, RouteInstruction.NORTH, true);

		// trader_shrine trader = new trader_shrine(70,d);
		// room r1 = d.getRoom(mainHall.startPoint);
		// r1.setShrine(trader);
		// d.addShrine(trader);

		Room spotRoom = mainHall.getRandomRoom();
		HiddenSpot spot = new HiddenSpot(spotRoom, 60, ItemPool.getHigherItem(
				50, 1.5));
		spotRoom.setSpot(spot);

		distributeAllItems();

		Room r1 = mainHall.getRandomRoomForShrineNonRQ();
		Room r2 = normalRaum.getRandomRoom();
		
		List<Room> way = DungeonUtils.findShortestWayFromTo(r1, r2,
				DungeonVisibilityMap.getAllVisMap(this.d));
		

	}

	private Room getRandomRimRoom(DefaultHall wolfRaum, DefaultHall orcRaum,
								  DefaultHall skeletonRaum, DefaultHall mainHall) {
		List<Room> possibleRooms = new LinkedList<Room>();
		possibleRooms.add(wolfRaum.getRimRoom(0));
		possibleRooms.add(orcRaum.getRimRoom(0));
		possibleRooms.add(skeletonRaum.getRimRoom(0));
		possibleRooms.add(mainHall.getRimRoom(0));
		int k = (int) (Math.random() * possibleRooms.size());

		Room res = (possibleRooms.get(k));
		return res;
	}

	private boolean setStatue(int cnt) {

		// Room start = d.getRoom(startRoom);
		// LinkedList neighbours = d.getNeighbourRooms(start);
		// LinkedList possibles = new LinkedList();
		// for (int i = 0; i < neighbours.size(); i++) {
		// LinkedList l = d.getNeighbourRooms((room) neighbours.get(i));
		// possibles.addAll(l);
		// }
		List<Room> possibles = this.mainHall.getRooms();
		List<Room> good = new LinkedList<Room>();
		for (int i = 0; i < possibles.size(); i++) {
			Room r = possibles.get(i);
			if (r.isClaimed() && (r.getRoomQuest() == null)
					&& (!good.contains(r)) && (r.getShrine() == null)) {
				good.add(r);
			}
		}

		Collections.sort(good, new DoorCountRoomComparator());
		printList(good);
		int i = 0;
		boolean bs = false;
		while (i < good.size()) {
			bs = statueHelp((good.get(i)));
			if (bs)
				break;

			i++;
		}
		return bs;

	}

	private Room getXmasRoom() {

		List<Room> possibles = this.mainHall.getRooms();
		List<Room> good = new LinkedList<Room>();
		for (int i = 0; i < possibles.size(); i++) {
			Room r = possibles.get(i);
			if (r.isClaimed() && (r.getRoomQuest() == null)
					&& (!good.contains(r)) && r.getShrine() == null) {
				good.add(r);
			}
		}

		Collections.sort(good, new DoorCountRoomComparator());
		printList(good);

		return (good.get(good.size() - 1));

	}

	protected void printList(List<Room> l) {
		for (int i = 0; i < l.size(); i++) {
			// System.out.println(((room)l.get(i)).getNumber().toString()+"
			// Türen: "+((room)l.get(i)).getDoorCount());
		}
		//System.out.println("-----");
	}

	private boolean statueHelp(Room statueRoom) {
		Statue statue = new Statue();
		// System.out.println("StatueRaum Versuch:
		// "+statueRoom.getNumber().toString()+" Tueren:
		// "+statueRoom.getDoorCount());
		Room neighbour = statueRoom.getAccessibleNeighbour();
		// System.out.println("Nachbarraum: "+neighbour.getNumber().toString());
		int cnt1 = df.validateNetCnt(neighbour, false);

		statueRoom.setShrine(statue, true);
		int cnt2 = df.validateNetCnt(neighbour, false);

		if (cnt1 != cnt2) {
			// System.out.println("NICHT GEEIGNET");
			statueRoom.setShrine(null, false);
			return false;
		}
		d.addShrine(statue);
		// System.out.println("JAWOHL");
		return true;

	}

	private void distributeAllItems() {
		int k = restItems.size();
		for (int i = 0; i < k; i++) {
			Monster m = df.getMonsterIn(1, 0);
			if (m != null) {
				m.takeItem(restItems.remove(0));
			}
		}
		k = rareItems.size();
		for (int i = 0; i < k; i++) {
			Monster m = df.getMonsterIn(1, 0);
			if (m != null) {
				m.takeItem(rareItems.remove(0));
			}
		}
		// System.out.println("sector 1: ");
		// System.out.println(
		// "restItems zu verteilen uebrig: " + restItems.size());
		// System.out.println(
		// "rareItems zu verteilen uebrig: " + rareItems.size());

	}

	@Override
	public Room getConnectionRoom() {
		return con_room;
	}

	private LinkedList<Chest> getChests(int k, List<Item> itemList) {

		LinkedList<Chest> chests = new LinkedList<Chest>();
		for (int i = 0; i < k; i++) {
			LinkedList<Item> items = new LinkedList<Item>();
			Item it1 = itemList.remove((int) (Math.random() * itemList
					.size()));
			Item it2 = itemList.remove((int) (Math.random() * itemList
					.size()));
			items.add(it1);
			items.add(it2);
			Chest c = new Chest(items);
			chests.add(c);
		}
		restItems.addAll(itemList);

		return chests;
	}

	private List<Item> getItemList() {
		List<Item> items = new LinkedList<Item>();
		if (p(70)) {
			items.add(new Helmet(10, false));
		}
		if (p(70)) {
			items.add(new Armor(20, false));
		}
		if (p(70)) {
			items.add(new Shield(20, false));
		}

		int healings = (int) (Math.random() * 6) + 6;
		for (int i = 0; i < healings; i++) {
			items.add(new HealPotion(20));
		}
		int randoms = (int) (Math.random() * 3) + 2;
		for (int i = 0; i < randoms; i++) {
			Item theIt = ItemPool.getGoodItem(10 + (int) (Math.random() * 30),
					0.5 + Math.random());
			if (theIt != null) {
				items.add(theIt);
			}
		}
		int cheaps = (int) (Math.random() * 3) + 5;
		for (int i = 0; i < cheaps; i++) {
			Item theIt = ItemPool.getCheapItem(10 + (int) (Math.random() * 30),
					0.5 + Math.random());
			if (theIt != null) {
				items.add(theIt);
			}
		}

		return items;
	}

	private boolean p(int k) {
		if (Math.random() * 100 > k) {
			return true;
		}
		return false;
	}

	private DefaultHall makeVimHome(Room r1, Key key) {
		Room toMake = null;
		List<Room> neighbours = d.getNeighbourRooms(r1);

		for (int i = 0; i < neighbours.size(); i++) {
			Room n = neighbours.get(i);
			if ((!n.isClaimed())) {
				toMake = n;
				break;
			}
		}

		Chest c = new Chest(ItemPool.getGift(50 + (int) (Math.random() * 15),
				1.5 + Math.random()));
		c.takeItem(ItemPool.getGift(50 + (int) (Math.random() * 15),
				1.5 + Math.random()));
		toMake.setChest(c);

		toMake.addItem(ItemPool.getHigherItem(40 + (int) (Math.random() * 20),
				1.5 + Math.random()));
		toMake.addItem(ItemPool.getGoodItem(40 + (int) (Math.random() * 20),
				1.5 + Math.random()));
		
		int dir = DungeonUtils.getNeighbourDirectionFromTo(r1, toMake)
				.getValue();

		DefaultHall halle1 = new DefaultHall(d, 1, 0,  toMake.getNumber(), dir,  this,
				"Vim", 5);

		boolean ok = halle1.makeArea(null);

		Door hallDoor = new Door(r1, toMake, key);
		hallDoor.setHidden(true);

		hallDoor.setHallDoor(true);

		r1.setDoor(hallDoor,
				DungeonUtils.getNeighbourDirectionFromTo(r1, toMake).getValue(),
				true);

		return halle1;
	}

	private DefaultHall makeTreasureRoom(Room r1, Key key) throws DungeonGenerationFailedException{

		Room toMake = null;
		List<Room> neighbours = d.getNeighbourRooms(r1);

		for (int i = 0; i < neighbours.size(); i++) {
			Room n = neighbours.get(i);
			if ((!n.isClaimed())) {
				toMake = n;
				break;
			}
		}
		
		if(toMake == null) {
			throw new DungeonGenerationFailedException();
		}

		Chest c = new Chest(ItemPool.getGift(50 + (int) (Math.random() * 15),
				1.5 + Math.random()));
		toMake.setChest(c);

		toMake.addItem(ItemPool.getHigherItem(40 + (int) (Math.random() * 20),
				1.5 + Math.random()));
		toMake.addItem(ItemPool.getGoodItem(40 + (int) (Math.random() * 20),
				1.5 + Math.random()));
		c.takeItem(ItemPool.getRandomBookSpell());
		int dir = DungeonUtils.getNeighbourDirectionFromTo(r1, toMake)
				.getValue();
		// System.out.println("Richtung : " + dir);
		// System.out.println(routeInstruction.dirToString(dir));
		DefaultHall halle1 = new DefaultHall(d, 1, 0,  toMake.getNumber(), dir,  this,
				"Zwergenschatz", 5);
		// System.out.println("Versuche halle zu machen: ");
		boolean ok = halle1.makeArea(null);
		if (ok == false) {
			// System.out.println("fehlgeschlagen ok = false");
		}

		Door hallDoor = new Door(r1, toMake, key);
		hallDoor.setHidden(true);

		hallDoor.setHallDoor(true);
		// System.out.println(
		// "Mache Hallentür von: "
		// + r1.toString()
		// + " nach: "
		// + toMake.toString());
		r1.setDoor(hallDoor,
				DungeonUtils.getNeighbourDirectionFromTo(r1, toMake).getValue(),
				true);

		// System.out.println("Jetzt halle: " + name);

		return halle1;
	}

	private DefaultHall doHall(Room r1, int monsterType, int monsterCnt,
							   int monsterValue, int hallSize, int subHalls, int roomQuests,
							   Key hallKey, int direction, String name, int floorIndex) {
		if (r1 == null) {
			// System.out.println("kein Rim Room zu finden!");
			return null;
		}
		Room toMake = null;
		List<Room> neighbours = d.getNeighbourRooms(r1);
		if (direction == 0) {

			for (int i = 0; i < neighbours.size(); i++) {
				Room n = neighbours.get(i);
				if ((!n.isClaimed())) {
					toMake = n;
					break;
				}
			}
		} else {
			// System.out.println("Richtung: " + direction);
			// System.out.println("Gefundener RimRoom: " + r1.toString());
			toMake = d.getRoomAt(r1, RouteInstruction.direction(direction));
		}
		int dir = DungeonUtils.getNeighbourDirectionFromTo(r1, toMake)
				.getValue();
		// System.out.println("Richtung : " + dir);
		// System.out.println(routeInstruction.dirToString(dir));
		DefaultHall halle1 = new DefaultHall(d, hallSize, monsterValue, toMake
				.getNumber(), dir, this, name, floorIndex);
		// System.out.println("Versuche halle zu machen: ");
		boolean ok = halle1.makeArea(null);
		if (ok == false) {
			return null;
		}
		return halle1;
	}

	private DefaultHall makeHall(int monsterType, int monsterCnt, int monsterValue,
								 int hallSize, int subHalls, int roomQuests, Key hallKey,
								 int direction, String name, int floorIndex) {
		Room r1 = mainHall.getRimRoom(direction);

		DefaultHall h1 = doHall(r1, monsterType, monsterCnt, monsterValue, hallSize,
				subHalls, roomQuests, hallKey, direction, name, floorIndex);

		boolean ok = true;
		if (h1 == null) {
			ok = false;
		}

		int loop = 0;
		while (!ok) {
			loop++;
			r1 = mainHall.getRimRoom(direction);
			h1 = doHall(r1, monsterType, monsterCnt, monsterValue, hallSize,
					subHalls, roomQuests, hallKey, direction, name, floorIndex);

			ok = true;
			if (h1 == null) {
				ok = false;
			}
			if (loop > 100) {
				// System.out.println("Halle zu erstellen unm�glich!");
				return null;
			}
		}
		Room toMake = d.getRoom(h1.getStartPoint());
		Door hallDoor;
		if (hallKey != null) {
			hallDoor = new Door(r1, toMake, hallKey);
			Monster m = Monster.createMonster(monsterType, monsterValue, game);
			df.equipMonster(m, 2);
			m.takeItem(hallKey);
			mainHall.addMonsterToList(m);
		} else {
			hallDoor = new Door(r1, toMake);
		}
		hallDoor.setHallDoor(true);
		// System.out.println(
		// "Mache Hallent�r von: "
		// + r1.toString()
		// + " nach: "
		// + toMake.toString());
		r1.setDoor(hallDoor,
				DungeonUtils.getNeighbourDirectionFromTo(r1, toMake).getValue(),
				true);

		// System.out.println("Jetzt halle: " + name);

		if (monsterType != 0) {
			LinkedList<Figure> monsters = new LinkedList<Figure>();
			for (int i = 0; i < monsterCnt; i++) {
				Monster m = Monster.createMonster(monsterType, monsterValue,
						game);
				df.equipMonster(m, 2);
				monsters.add(m);
			}

			h1.setMonster(monsters);
		}
		return h1;
	}

	public boolean setRoomQuest(String roomQuestType, DefaultHall theHall, Shrine s) {
		int cnt = 0;
		Room rq_room = mainHall.getRandomRoom();
		// //System.out.println("rq_room ist null: "+(rq_room == null));
		JDPoint rq_point = rq_room.getNumber();
		RoomQuest rq = null;
		if (roomQuestType.equals("2x2_1")) {
			rq = new RoomQuest_2x2_1(rq_point, df, restItems);
		}
		if (roomQuestType.equals("1x1_1 ")) {
			rq = new RoomQuest_1x1_1(rq_point, df, false, true, s, restItems);
		}
		if (rq == null) {
			return false;
		}
		boolean rq_ok = false;

		if (rq.isPossible()) {
			rq_ok = true;
			// System.out.println("RoomQuest erfolgreich gesetzt!");
			// System.out.println(rq.getClass());
			// System.out.println("Punkt: " + rq_point.toString());

			if (s != null) {
				s.setLocation(rq_room);
				// System.out.println(
				// "loc ist null: " + (s.getLocation() == null));
			}
		} else {
			// System.out.println("RoomQuest.setUp() fehlgeschlagen!");
		}

		while (!rq_ok) {
			cnt++;
			rq_room = mainHall.getRandomRoom();
			// //System.out.println("rq_room ist null: "+(rq_room == null));
			rq_point = rq_room.getNumber();
			if (roomQuestType.equals("2x2_1")) {
				rq = new RoomQuest_2x2_1(rq_point, df, restItems);
			}
			if (roomQuestType.equals("1x1_1 ")) {
				rq = new RoomQuest_1x1_1(rq_point, df, false, true, s,
						restItems);
			}

			if (rq.isPossible()) {
				rq_ok = true;
				// System.out.println("RoomQuest erfolgreich gesetzt!");
				// System.out.println(rq.getClass());
				// System.out.println("Punkt: " + rq_point.toString());

				if (s != null) {
					s.setLocation(rq_room);
					// System.out.println(
					// "loc ist null: " + (s.getLocation() == null));
				}
			}

			if (cnt > 500) {
				break;
			}
		}
		// //System.out.println("Anzahl versuche: " + cnt);

		return rq_ok;
	}

	// class MyStatueRoomComparator implements Comparator{
	//		
	// public int compare(Object o1, Object o2) {
	// room r1 = ((room)o1);
	// int value1 = (r1.getDoorCount());
	// room r2 = ((room)o2);
	// int value2 = (r2.getDoorCount());
	// if(value1 < value2) {
	// return 1;
	// }
	// else {
	// return -1;
	// }
	//			
	//		
	// }
	//		
	// }
}