/**
 * Implementierung der randomisierten Generierung von Sektor 2
 * Stellt diesen Bereich komplett fertig, Monster, Quest, Items, Schreine, Hallen... 
 *
 */
package dungeon.generate;

import figure.monster.Monster;
import game.DungeonGame;
import item.HealPotion;
import item.Item;
import item.ItemPool;
import item.Key;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.quest.Rune;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import shrine.Brood;
import shrine.HealthFountain;
import shrine.QuestShrine;
import shrine.RepairShrine;
import shrine.RuneFinder;
import shrine.RuneShrine;
import shrine.Shrine;
import dungeon.Chest;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.quest.RoomQuest;
import dungeon.quest.RoomQuest_XxY;
import dungeon.quest.RoomQuest_trader_1x2;
import dungeon.util.DungeonUtils;
import dungeon.util.RouteInstruction;


public class Sector2 extends Sector {

	
	private final List<Item> restItems = new LinkedList<Item>();
	private final List<Item> rareItems = new LinkedList<Item>();
	private final List<Shrine> shrinesInRQ = new LinkedList<Shrine>();
	private final List<Shrine> shrinesNonRQ = new LinkedList<Shrine>();
	private final Room con_room = null;

	public Sector2(
		Dungeon d,
		JDPoint startRoom,
		int number,
		int avMonsterStrength,
		int mainSize,
		DungeonGame game,
		AbstractDungeonFiller df) {

		this.df = df;
		this.game = game;
		this.d = d;
		this.number = number;
		this.startRoom = startRoom;
		
		makeRareItems();

		mainHall =
			new Hall(
				d,
				mainSize,
				500,
				0,
				startRoom,
				RouteInstruction.NORTH,
				3,
				this,
				"main2",0);
		boolean ok = mainHall.makeArea(null);
		if (ok) {
			//System.out.println("main2 ok!");
		}
		else {
			mainHall =
				new Hall(
					d,
					mainSize,
					500,
					0,
					startRoom,
					RouteInstruction.NORTH,
					3,
					this,
					"main2",0);
			ok = mainHall.makeArea(null);
			if (!ok) {
				//System.out.println("main2 hat nicht geschafft!");
			}
		}
		d.getRoom(startRoom).setStart();
		halls.add(mainHall);

		//andere Halle
		
//		roomQuest_XxY rqxyOK =
//			otherHall.insertRQXY(
//				3 + (int) (Math.random() * 0),
//				3 + (int) (Math.random() * 0),
//				false);
//		//System.out.println("rqxyOK: " + rqxyOK);
//		if (rqxyOK == null) {
//			//System.out.println("abbruch!");
//			System.exit(0);
//		}
//		rqxyOK.removeDoors(8);
//		rqxyOK.plugMonster(2500,1.5,roomQuest.TYPE_UNDEAD);
//		
//		
//		
//		roomQuest_XxY rqxyOK2 =
//			otherHall.insertRQXY(
//				1 + (int) (Math.random() * 0),
//				3 + (int) (Math.random() * 0),
//				false);
//		//System.out.println("rqxyOK2: " + rqxyOK2);
//		if (rqxyOK2 == null) {
//			//System.out.println("abbruch!");
//			System.exit(0);
//		}
//		rqxyOK2.removeDoors(8);
//		rqxyOK2.plugMonster(2500,1.5,roomQuest.TYPE_SMALL);
//		
//		
//		roomQuest_XxY rqxyOK3 =
//			otherHall.insertRQXY(
//				4 + (int) (Math.random() * 0),
//				2 + (int) (Math.random() * 0),
//				false);
//		//System.out.println("rqxyOK2: " + rqxyOK3);
//		if (rqxyOK3 == null) {
//			//System.out.println("abbruch!");
//			System.exit(0);
//		}
//		rqxyOK3.removeDoors(8);
//		rqxyOK3.plugMonster(2500,1.5,roomQuest.TYPE_WOLF);


		
		Shrine hf = new HealthFountain(50, 4);
		Shrine rs = new RepairShrine(0.5);
		d.addShrine(hf);
		

		boolean b3x3 = setRoomQuest("3x3_1 ", mainHall, null);
		//System.out.println("3x3 :" + b3x3);
		setRoomQuest("1x1_1 ", mainHall, hf);
		if(setRoomQuest("1x1_1 ", mainHall, rs)) {
			d.addShrine(rs);
			
		}
		setTraderQuest();

		int smallMonsters = 10;
		int bigMonsters = 7;
		LinkedList viecher = new LinkedList();
		for (int i = 0; i < smallMonsters; i++) {
			Monster m = df.getSmallMonster(1700);
			df.equipMonster(m, 2);
			viecher.add(m);
		}
		for (int i = 0; i < bigMonsters; i++) {
			Monster m = df.getBigMonster(2100);
			df.equipMonster(m, 2);
			viecher.add(m);
		}
		mainHall.addMonsterToList(viecher);

		//room natureRoom = mainHall.getRandomRoomForShrine();
		//natureRoom.addMonster(new bear(1800, game));
		Brood natureBrood = new Brood(Brood.BROOD_NATURE);
		//natureRoom.setShrine(natureBrood, true);
		d.addShrine(natureBrood);
		shrinesNonRQ.add(natureBrood);

		//room creatureRoom = mainHall.getRandomRoomForShrine();
		//creatureRoom.addMonster(new ogre(1800, game));
		Brood creatureBrood = new Brood(Brood.BROOD_CREATURE);
	//	creatureRoom.setShrine(creatureBrood, true);
		d.addShrine(creatureBrood);
		shrinesNonRQ.add(creatureBrood);

		//room undeadRoom = mainHall.getRandomRoomForShrine();
		//undeadRoom.addMonster(new ghul(1800, game));
		Brood undeadBrood = new Brood(Brood.BROOD_UNDEAD);
		//undeadRoom.setShrine(undeadBrood, true);
		d.addShrine(undeadBrood);
		shrinesNonRQ.add(undeadBrood);

		//room runeShrine1Room = mainHall.getRandomRoomForShrine();
		//room runeShrine2Room = mainHall.getRandomRoomForShrine();
		//room runeShrine3Room = mainHall.getRandomRoomForShrine();
		//room runeShrine4Room = mainHall.getRandomRoomForShrine();

		Rune rune1 = df.getRunes()[0];
		RuneShrine rs1 = new RuneShrine(1, rune1.getChar());
		Rune rune2 = df.getRunes()[1];
		RuneShrine rs2 = new RuneShrine(2, rune2.getChar());
		Rune rune3 = df.getRunes()[2];
		RuneShrine rs3 = new RuneShrine(3, rune3.getChar());
		Rune rune4 = df.getRunes()[3];
		RuneShrine rs4 = new RuneShrine(4, rune4.getChar());

		//runeShrine1Room.setShrine(rs1, true);
		//runeShrine2Room.setShrine(rs2, true);
		//runeShrine3Room.setShrine(rs3, true);
		//runeShrine4Room.setShrine(rs4, true);
		shrinesInRQ.add(rs1);
		shrinesInRQ.add(rs2);
		shrinesInRQ.add(rs3);
		shrinesInRQ.add(rs4);

////		room runeFinderShrine1Room = mainHall.getRandomRoomForShrine();
////		room runeFinderShrine2Room = mainHall.getRandomRoomForShrine();
////		room runeFinderShrine3Room = mainHall.getRandomRoomForShrine();
////		room runeFinderShrine4Room = mainHall.getRandomRoomForShrine();

		Shrine rfs1 = new RuneFinder(rune1);

		Shrine rfs2 = new RuneFinder(rune2);

		Shrine rfs3 = new RuneFinder(rune3);

		Shrine rfs4 = new RuneFinder(rune4);

		d.addShrine(rfs1);
		d.addShrine(rfs2);
		d.addShrine(rfs3);
		d.addShrine(rfs4);

//		runeFinderShrine1Room.setShrine(rfs1, true);
//		runeFinderShrine2Room.setShrine(rfs2, true);
//		runeFinderShrine3Room.setShrine(rfs3, true);
//		runeFinderShrine4Room.setShrine(rfs4, true);

		shrinesInRQ.add(rfs1);
		shrinesInRQ.add(rfs2);
		shrinesInRQ.add(rfs3);
		shrinesInRQ.add(rfs4);

		QuestShrine qs = new QuestShrine(200);
		rareItems.add(qs.getRequestedItem());
		
		QuestShrine qs2 = new QuestShrine(200);
		rareItems.add(qs2.getRequestedItem());
		
		shrinesInRQ.add(qs);
		shrinesInRQ.add(qs2);
		
		Shrine hf2 = new HealthFountain(50, 4);
		Shrine reps2 = new RepairShrine(0.5);
		d.addShrine(hf2);
		d.addShrine(reps2);
		
		Shrine hf3 = new HealthFountain(50, 4);
		Shrine reps3 = new RepairShrine(0.5);
		d.addShrine(hf3);
		d.addShrine(reps3);
		
		shrinesNonRQ.add(hf2);
		shrinesNonRQ.add(hf3);
		shrinesNonRQ.add(reps2);
		shrinesNonRQ.add(reps3);
		
		
		//
		//
		mainHall.setMonster();
		//
		//
		
		Key h1 = df.getNextKey();
		Hall otherHall = makeHall(0, 0, 3000, 70, 0, 0, h1, 0, "other",6);
		List<Key> keys = fillHallWithRQ(otherHall);
		rareItems.addAll(keys);
		mainHall.getRandomMonster().takeItem(h1);
		halls.add(otherHall);
		
//		key h2 = df.getNextKey();
//		hall otherHall2 = makeHall(0, 0, 3000,70, 0, 0, h2, 0, "other2");
//		LinkedList keys2 = fillHallWithRQ(otherHall2);
//		rareItems.addAll(keys2);
//		mainHall.getRandomMonster().addItem(h2,null);
//		halls.add(otherHall2);
		
		
		distributeAllShrines();
		distributeAllItems();

	}
	
	private void distributeAllShrines() {
		//System.out.println("DISTRIBUTE SHRINES!");
		if(shrinesInRQ.size() > 0) {
			shrinesNonRQ.addAll(shrinesInRQ);	
		}	
		//System.out.println("Anzahl: "+shrinesNonRQ.size());
		int size = shrinesNonRQ.size();
		for(int i = 0; i < size; i++) {
			
			//System.out.println();
			int k = (int)(Math.random() * halls.size());
			Hall h = halls.get(k);	
			Room r = h.getRandomRoomForShrineNonRQ();
			Shrine s = shrinesNonRQ.remove(0);
			//System.out.println(i+s.getClass().getName());
			r.setShrine(s,true);
			//shrinesNonRQ.remove(s);
			setGuard(r,3000);
		}
	}
	
	private void setGuard(Room r, int value) {
		Monster m = df.getBigMonster(3000,game);
		r.figureEnters(m,0);
	}
	

	private boolean setTraderQuest() {
		int cnt = 0;
		Room rq_room = mainHall.getRandomRoom();
		JDPoint rq_point = rq_room.getNumber();
		RoomQuest rq = new RoomQuest_trader_1x2(rq_point, df, rareItems);
		boolean rq_ok = false;
		if (rq.isValid()) {
			if (rq.setUp()) {
				rq_ok = true;
				////System.out.println("RoomQuest erfolgreich gesetzt!");
				////System.out.println(rq.getClass());
				////System.out.println("Punkt: " + rq_point.toString());
			} else {
				////System.out.println("RoomQuest.setUp() fehlgeschlagen!");
			}
		}
		while (!rq_ok) {
			cnt++;
			rq_room = mainHall.getRandomRoom();
			rq_point = rq_room.getNumber();
			rq = new RoomQuest_trader_1x2(rq_point, df, rareItems);

			if (rq.isValid()) {
				if (rq.setUp()) {
					rq_ok = true;
					////System.out.println("RoomQuest erfolgreich gesetzt!");
					//System.out.println(rq.getClass());
					////System.out.println("Punkt: " + rq_point.toString());

				}
			}
			if (cnt > 500) {
				break;
			}
		}
		////System.out.println("Anzahl versuche: " + cnt);
		return rq_ok;
	}

	private void distributeAllItems() {
		int k = restItems.size();
		for (int i = 0; i < k; i++) {
			Monster m = df.getMonsterIn(2, 0);
			if (m != null) {
				m.takeItem(restItems.remove(0));
			} else {
				//System.out.println("kein Monster fuer Gegenstand gefunden!");
			}
		}
		int l = rareItems.size();
		//for (int i = 0; i < l; i++) {
		int secCnt = 0;
		while(rareItems.size() > 0 && secCnt < 10000){
			secCnt++;
			Monster m = df.getMonsterIn(2, 0);
			if (m != null) {
				Item it = rareItems.get(0);
				if (it instanceof Rune) {
					//System.out.println(
					//	"Gebe: " + it.toString() + " an: " + m.toString());
				}
				
				if(m.getRoom().getRoomQuest() == null) {
//					if(it instanceof Key) {
//						System.out.println("Gebe :"+((Key)it).toString()+" an :"+m.toString()+"  - Raum: "+m.getLocation().toString()+" RQ: "+m.getRoom().getRoomQuest());
//					}
					m.takeItem(it);
					rareItems.remove(it);
				}
				
			} else {
				//System.out.println("kein Monster fuer Gegenstand gefunden!");
			}
		}
		//System.out.println(
		//	"restItems zu verteilen uebrig: " + restItems.size());
		//System.out.println(
		//	"rareItems zu verteilen uebrig: " + restItems.size());
	}

	/**
	 * 
	 * @uml.property name="con_room"
	 */
	@Override
	public Room getConnectionRoom() {
		return con_room;
	}

	private List<Chest> getChests(int k, List<Item> itemList) {

		List<Chest> chests = new LinkedList<Chest>();
		for (int i = 0; i < k; i++) {
			List<Item> items = new ArrayList<Item>();
			Item it1 =
				itemList.remove((int) (Math.random() * itemList.size()));
			Item it2 =
				itemList.remove((int) (Math.random() * itemList.size()));
			items.add(it1);
			items.add(it2);
			Chest c = new Chest(items);
			chests.add(c);
		}
		restItems.addAll(itemList);

		return chests;
	}

	private void makeRareItems() {
		for (int i = 0; i < df.getRunes().length; i++) {
			////System.out.println("adding: " + df.runen[i].toString());
			rareItems.add(df.getRunes()[i]);
		}
		rareItems.add(ItemPool.getUnique(80, 0));
		for (int i = 0; i < 6; i++) {
			Item it = ItemPool.getHigherItem(60, 1 + Math.random());
			if (it != null) {
				////System.out.println("generiert: "+it.toString());

				rareItems.add(it);
			} else {
				////System.out.println("null");	
			}
		}

	}

	private LinkedList getItemList() {
		LinkedList items = new LinkedList();
		if (p(70)) {
			items.add(new Helmet(40, false));
		}
		if (p(70)) {
			items.add(new Armor(50, false));
		}
		if (p(70)) {
			items.add(new Shield(50, false));
		}

		int healings = (int) (Math.random() * 6) + 6;
		for (int i = 0; i < healings; i++) {
			items.add(new HealPotion(30));
		}
		int randoms = (int) (Math.random() * 3) + 2;
		for (int i = 0; i < randoms; i++) {
			items.add(
				ItemPool.getGoodItem(
					10 + (int) (Math.random() * 30),
					0.5 + Math.random()));
		}
		int cheaps = (int) (Math.random() * 3) + 5;
		for (int i = 0; i < cheaps; i++) {
			items.add(
				ItemPool.getCheapItem(
					(int) (Math.random() * 30),
					0.5 + Math.random()));
		}

		return items;
	}

	private boolean p(int k) {
		if (Math.random() * 100 > k) {
			return true;
		}
		return false;
	}

	private Hall makeHall(
		int monsterType,
		int monsterCnt,
		int monsterValue,
		int hallSize,
		int subHalls,
		int roomQuests,
		Key hallKey,
		int direction,
		String name, int floorIndex) {
		Room r1 = mainHall.getRimRoom(direction);

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
			//System.out.println("Richtung: " + direction);
			//System.out.println("Gefundener RimRoom: " + r1.toString());
			toMake = d.getRoomAt(r1, RouteInstruction.direction(direction));
		}
		int dir = DungeonUtils.getNeighbourDirectionFromTo(r1, toMake)
				.getValue();
		//System.out.println("Richtung : " + dir);
		//System.out.println(routeInstruction.dirToString(dir));
		Hall halle1 =
			new Hall(
				d,
				hallSize,
				monsterValue,
				subHalls,
				toMake.getNumber(),
				dir,
				roomQuests,
				this,
				name,1);
		boolean ok = halle1.makeArea(null);
		int loop = 0;
		while (!ok) {
			loop++;
			r1 = mainHall.getRimRoom(direction);
			toMake = null;
			neighbours = d.getNeighbourRooms(r1);
			//if (direction == 0) {
			for (int i = 0; i < neighbours.size(); i++) {
				Room n = neighbours.get(i);
				if ((!n.isClaimed())) {
					toMake = n;
					break;
				}
			}
			//} else {
			//	//System.out.println("Gefundener RimRoom: " + r1.toString());
			//	toMake = d.getRoomAt(r1, direction);
			//}
			dir = DungeonUtils.getNeighbourDirectionFromTo(r1, toMake)
					.getValue();
			halle1 =
				new Hall(
					d,
					hallSize,
					monsterValue,
					subHalls,
					toMake.getNumber(),
					dir,
					roomQuests,
					this,
					name,2);
			ok = halle1.makeArea(null);
			if (loop > 1000) {
				//System.out.println("Halle zu erstellen unm�glich!");
				return null;
			}
		}
		Door hallDoor;
		if (hallKey != null) {
			hallDoor = new Door(r1, toMake, hallKey);
			Monster m = Monster.createMonster(1+(int)(Math.random()*6), monsterValue, game);
			m.takeItem(hallKey);
			mainHall.addMonsterToList(m);
		} else {
			hallDoor = new Door(r1, toMake);
		}
		hallDoor.setHallDoor(true);
		//System.out.println(
		//	"Mache Hallent�r von: "
		//		+ r1.toString()
		//		+ " nach: "
		//		+ toMake.toString());
		r1.setDoor(
			hallDoor,
				DungeonUtils.getNeighbourDirectionFromTo(r1, toMake).getValue(),
			true);

		//System.out.println("Jetzt halle: " + name);

		if (monsterType != 0) {
			LinkedList monsters = new LinkedList();
			for (int i = 0; i < monsterCnt; i++) {
				monsters.add(
					Monster.createMonster(monsterType, monsterValue, game));
			}

			halle1.setMonster(monsters);
		}
		return halle1;
	}

	public boolean setRoomQuest(String roomQuestType, Hall theHall, Shrine s) {
		int cnt = 0;
		Room rq_room = mainHall.getRandomRoom();
		JDPoint rq_point = rq_room.getNumber();
		RoomQuest rq =
			RoomQuest.newRoomQuest(
				roomQuestType,
				rq_point,
				rareItems,
				rareItems,
				s,
				df);

		if (rq == null) {
			//System.out.println("rq ist null ");
			return false;
		}
		boolean rq_ok = false;

		if (rq.isPossible()) {
			rq_ok = true;
			//System.out.println("RoomQuest erfolgreich gesetzt!");
			//System.out.println(rq.getClass());
			//System.out.println("Punkt: " + rq_point.toString());
			if (s != null) {
				s.setLocation(rq_room);
				//System.out.println(
				//	"loc ist null: " + (s.getLocation() == null));
			}
		} else {
			//System.out.println("RoomQuest.setUp() fehlgeschlagen!");
		}

		while (!rq_ok) {
			cnt++;
			rq_room = mainHall.getRandomRoom();
			rq_point = rq_room.getNumber();
			rq =
				RoomQuest.newRoomQuest(
					roomQuestType,
					rq_point,
					rareItems,
					rareItems,
					s,
					df);

			if (rq.isPossible()) {
				rq_ok = true;
				//System.out.println("RoomQuest erfolgreich gesetzt!");
				//System.out.println(rq.getClass());
				//System.out.println("Punkt: " + rq_point.toString());
				if (s != null) {
					s.setLocation(rq_room);
					//System.out.println(
					//	"loc ist null: " + (s.getLocation() == null));
				}

			}

			if (cnt > 100) {
				break;
			}
		}
		//System.out.println("Anzahl versuche: " + cnt);
		return rq_ok;
	}
	
	private List<Key> fillHallWithRQ(Hall halle) {
		//System.out.println();
		//System.out.println("fillHallWithRQ(): "+halle.getName());
		List<Key> keyList = new LinkedList<Key>();
		int k = halle.getSize();
		int questCnt = k / 20;
		//int questCnt = 1;
		//System.out.println("Anzahl gewuenschte RQs: "+questCnt);
		int [] sizes = { 15,12,9,8,6,4,3,2,1};
		int aktualSize = -1;
		RoomQuest rq = null;
		int questsMade = 0;
		while(questsMade < questCnt) {
			//int rounds = 0;
			int sizer = 0;
			aktualSize = -1;
			while(rq == null) {
				//rounds++;
				aktualSize++;
				if(aktualSize > 8) {
					//System.out.println("es konnten nicht genug rqs gesetzt werden!");
					break;
				}
				 sizer = aktualSize;
				if(Math.random() < 0.5) {
					aktualSize--;
					sizer = (int)(Math.random() * 6);	
				}
				//System.out.println("Versuche Groe�e: "+sizes[sizer]);
				rq = trySetRoomQuest(sizes[sizer],halle);
				
				
			}
			if(rq != null) {
				//rq.removeDoors();
				rq.plugMonster(2500,1.5,questsMade+6);
				Key ke = ((RoomQuest_XxY)rq).getKey();
				if(ke != null) {
					keyList.add(ke);
				}
				questsMade++;
				//System.out.println("Erfolgreich! - eins fertig");
				rq = null;
			}
			else if(rq == null) {
				break;	
			}
			//System.out.println("Wieder ein Quest fertig! "+questsMade);
		}
		
		return keyList;
		
	}

	private RoomQuest trySetRoomQuest(int size, Hall halle) {
		//System.out.println("trySetRoomQuest("+size+")");
		boolean foundSize = false;
		int sizes1 [] = {3,2,1};
		int aktualSize1 = 0;
		int otherSize = 0;
		while(!foundSize) {
			if(aktualSize1 == 3) {
				//System.out.println("kein RQ fuer Groesse machbar!");
				System.exit(0);
			}
			otherSize = size /	sizes1[aktualSize1];	
			if(otherSize * sizes1[aktualSize1] == size) {
				foundSize = true;
				break;
			}
			aktualSize1++;
		}
		int x;
		int y;
		
		if(Math.random() < 0.5) {
			x = otherSize;
			y = sizes1[aktualSize1];	
		}
		else {
			y = otherSize;
			x = sizes1[aktualSize1];	
		}
		//System.out.println("Mahse: "+x+" - "+y); 
		Shrine sh = getRandomRQShrine();
		RoomQuest_XxY rqxyOK =
			halle.insertRQXY(
				x,
				y,
				true,sh);
		if(rqxyOK == null) {
			//System.out.println("Passt nicht rein, ist null!");	
			
			 rqxyOK =
			halle.insertRQXY(
				y,
				x,
				true,sh);
			if(rqxyOK != null)  {
				shrinesInRQ.remove(sh);
					
			}
		}
		else {
			shrinesInRQ.remove(sh);
			////System.out.println("passt!");	
		}	
		return rqxyOK;
		
	}
	
	private Shrine getRandomRQShrine() {
		if(shrinesInRQ.size() > 0) {
			int k = (int)(Math.random() * shrinesInRQ.size());
			return shrinesInRQ.get(k);			
		}	
		else {
			return null;	
		}
	}

}
