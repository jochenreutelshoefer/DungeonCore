/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import figure.action.Action;
import figure.action.result.ActionResult;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.memory.FigureMemory;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import game.InfoEntity;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import spell.Spell;
import spell.SpellInfo;
import ai.DefaultMonsterIntelligence;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;

/**
 * Abstrakte Klasse - Soll fuer eine Steuerung die Informationen einer Figur
 * liefern.
 */
public abstract class FigureInfo extends InfoEntity {

	Figure f;
	public FigureInfo(Figure f, DungeonVisibilityMap stats) {
		super(stats);
		this.f = f;
	}
	
	public Class<? extends Figure> getFigureClass() {
		return f.getClass();
	}
	
	public abstract int getMight();
	
	public List getShortestWayFromTo(JDPoint p1, JDPoint p2) {
		return f.getGame().getDungeon().findShortestWayFromTo(p1,p2,map);
	}
	

//	/**
//	 * Liefert die Information �ber das Vorhandensein eines
//	 * Schreins/Oertlichkeit
//	 * 
//	 * Kodierung: SHRINE_NOSHRINE = 0; SHRINE_CORPSE= 1; SHRINE_DARK_MASTER = 2;
//	 * SHRINE_HEALTH_FOUNTAIN = 3; SHRINE_INFO = 4; SHRINE_LUZIA = 5;
//	 * SHRINE_QUEST = 6; SHRINE_REPAIR = 7; SHRINE_RUNEFINDER = 8; SHRINE_RUNE =
//	 * 9; SHRINE_SORCER_LAB = 10; SHRINE_STATUE = 11; SHRINE_TRADER = 12;
//	 * SHRINE_XMAS = 13; SHRINE_BROOD = 14;
//	 * 
//	 * 
//	 * @return Index des Schreintyps
//	 */
//
//
//	public int getShrineIndex() {
//		if (f.getRoom().getShrine() != null) {
//			return f.getRoom().getShrine().getShrineIndex();
//		} else {
//			return Shrine.SHRINE_NOSHRINE;
//		}
//	}
	
	public abstract List<ItemInfo> getAllItems();
	
	@Override
	public FigureMemory getMemoryObject(FigureInfo info) {
		return f.getMemoryObject(info);
	}
	
	
	
	
	public int getLookDir() {
		int visStat = map.getVisibilityStatus(f.getLocation());
		
		if(visStat >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return f.getLookDir();
		}
		else {
			int bla = 6;
		}
		return -1;
	}
	
	public boolean isHostile(FigureInfo fig) {
		return f.getControl().isHostileTo(fig);
	}
	
	public void controlLeaves() {
		f.setControl(null);
		f.getGame().controlLeft(f);
	}
	
	public int getFleeDistance() {
		return  DefaultMonsterIntelligence.getMinFleeDist(this);
	}
	
	
	public PositionInRoomInfo getPos() {
		int vis = map.getVisibilityStatus(f.getLocation());
		if(vis >= RoomObservationStatus.VISIBILITY_FIGURES) {
			Position p = f.getPos();
			
			//System.out.println("passt! "+ p.toString());
			
			return new PositionInRoomInfo(f.getPos(),map);
		}else {
			//System.out.println("scheisse wars!");
			return null;
		}
	}
	
	public int getPositionInRoomIndex() {
		int vis = map.getVisibilityStatus(f.getLocation());
		
		if(vis >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return f.getPositionInRoom();
		}
		else {
			//System.out.println("verschweige PosIndex!");
			return -1;
		}
	}
	public Boolean isDead() {
		if(map.getVisibilityStatus(f.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return new Boolean(f.isDead());
		}
		return null;
	}
	
	public Boolean hasKey(DoorInfo  d) {
		if(f.equals(map.getFigure())) {
		return new Boolean(f.hasKey(d.getLock()));
		}
		
		return null;
	}
	
	public static FigureInfo makeFigureInfo(Figure f,DungeonVisibilityMap map) {
		if(map == null) {
			System.out.println("FigureInfo mit VisMap null!");
			return null;
		}
		if(f instanceof Hero) {
			return new HeroInfo((Hero)f,map);
		}
		if(f instanceof Monster) {
			return MonsterInfo.makeMonsterInfo((Monster)f,map);
		}
		return null;
	}
	
	public int getVisStatus(RoomInfo r) {
		return f.getRoomObservationStatus(r.getLocation()).getVisibilityStatus();
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof FigureInfo) {
			if (((FigureInfo) o).f.equals(this.f)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return f.hashCode();
	}

	public String getShortStatus() {
		return f.getShortStatus();
	}
	
	@Override
	public String toString() {
		return f.toString();
	}

	/**
	 * Liefert die Koordinate des Raumes indem sich das Monster momentan
	 * befindet
	 * 
	 * @return Koordinate des Raumes
	 */
	public JDPoint getRoomNumber() {
		return JDPoint.getPoint(f.getRoom().getNumber().getX(), f.getRoom()
				.getNumber().getY());
	}

	/**
	 * Liefert ein Array von WrappedItem aller Gegenstaende, die in diesem Raum
	 * auf dem Boden liegen.
	 * 
	 * @return Gegenstaende im Raum
	 */
	public ItemInfo[] getRoomItems() {
		if(f.getRoom() != null) {
		return f.getRoom().getItemInfos(map);
		}
		return null;
	}

	/**
	 * Liefert ein Integer-Array, dass die Tueren des Raumes kodiert. array[0]
	 * -> Norden; array[1] -> Osten; array[2] -> Sueden; array[3] -> Westen;
	 * 
	 * 0 -> Keine Tuere; 1 -> normale Tuere; 2 -> Tuere mit Schloss offen; 3 ->
	 * Tuere mit Schloss verschlossen;
	 * 
	 * @return Kodierung der Tueren des Raumes
	 */
	public int[] getRoomDoors() {
		return f.getRoom().makeDoorInfo();
	}

	@Override
	public Paragraph[] getParagraphs() {
		return ((Paragraphable)f).getParagraphs();
	}
	
	public boolean hasItem(ItemInfo it) {
		ItemInfo[] items = getFigureItems();
		for (int i = 0; i < items.length; i++) {
			if(items[i].equals(it)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isThief(){
		return f.isThief();
	}
	/**
	 * Liefert ein Array von MonsterInfos mit allen Monstern, die sich in dem
	 * selben Raum befinden (einschlie�lich this)
	 * 
	 * @return Die Monster des Raumes
	 */
	public FigureInfo[] getMonsterInRoom() {
		return f.getRoom().getMonsterInfoArray(map);
	}
	
	public int getLevel() {
		return f.getLevel();
	}
	
//	public DungeonVisibilityMap getVisMap() {
//		return f.getRoomVisibility();
//	}
//	public RoomObservationStatus getObservationStatusObject(JDPoint p) {
//		return f.getRoomObservationStatus(p);
//	}
//	
	public RoomInfo getRoomInfo(){
		return RoomInfo.makeRoomInfo(f.getRoom(), map);
	}
	
	public SpellInfo getLastSpell() {
		if(f.getLastSpell() != null) {
			return new SpellInfo(f.getLastSpell(),map);
		}
		return null;
	}
	
	
	
	public JDPoint getDungeonSize() {
		return f.getGame().getDungeon().getSize();
	}
	
	public RoomInfo getRoomInfo(int x, int y) {
		if(this.map.getDiscoveryStatus(x, y) < RoomObservationStatus.VISIBILITY_FOUND) {
			return null;
		}
		return f.getGame().getDungeon().getRoomInfoNr(x, y, map);
	}
	public RoomInfo getRoomInfo(JDPoint p) {
		return getRoomInfo(p.getX(),p.getY());
	}
	
//	public RoomInfo getMemory(int i, int j) {
//		if(this instanceof HeroInfo) {
//			return ((Hero)f).getMemory(i, j);
//		}
//		return null;
//	}
	

	//	public int getFighterIndex() {
	//		return m.getFighterID();
	//	}

	/**
	 * Liefert die Anzahl der Monster in dem Raum (einschlie�lich this)
	 * 
	 * @return Anzahl Monster
	 */
	public int getMonsterCntInRoom() {
		return f.getRoom().getRoomFigures().size();
	}

	/**
	 * Liefert den Gesundheitszustand des Monsters
	 * 
	 * Kodierung: > 50% --> HEALTHY = 4; > 30% -->WEAK = 3; > 15% -->INJURED =
	 * 2; > 8% --> WOUNDED = 1; < 8% --> CRITICAL = 0;
	 * 
	 * 
	 * @return Gesundheitszustand
	 */
	public int getHealthLevel() {
		return f.getHealthLevel();
	}

	/**
	 * Liefert die Anzahl Kampfaktionspunkte die das Monster in der aktuellen
	 * Kampfrunde noch zur Verf�gung hat.
	 * 
	 * @return Anzahl Kampfaktionspunkte
	 */
	public int getFightAP() {
		return f.getFightAP();
	}

	//	public int getFighterIndex() {
	//		return m.getFighterID();
	//	}

	/**
	 * Liefert die Anzahl Aktionspunkte, die das Monster in dieser Spielrunde
	 * noch zur Verf�gung hat.
	 * 
	 * @return Anzahl Aktionspunkte
	 */
	public int getActionPoints() {
		return f.getActionPoints();
	}

	/**
	 * Liefert die Spielrunde zur�ck
	 * 
	 * @return Spielrunde
	 */
	public int getGameRound() {
		return f.getGame().getRound();
	}

	/**
	 * @return Identifikationsnummer der Figure
	 */
	public int getFighterID() {
		return f.getFighterID();
	}

	public ActionResult checkMovementAction(Action a) {
		return f.checkAction(a);
	}

//	public ActionResult checkFightAction(Action a) {
//		return f.checkFightAction(a);
//	}

	/**
	 * Liefert ein Array von WrappedItem aller Gegenstaende, die das Monster mit
	 * sich fuehrt.
	 * 
	 * 
	 * @return Gegenstaende der Figure
	 */
	public ItemInfo[] getFigureItems() {
		return f.getItemInfos(map);
	}

	public List<ItemInfo> getFigureItemList() {
		List<ItemInfo> l = new LinkedList<ItemInfo>();
		ItemInfo o[] = getFigureItems();
		for (int i = 0; i < o.length; i++) {
			l.add(o[i]);
		}
		return l;
	}

	public String getName() {
		return f.getName();
	}




	public List<SpellInfo> getSpells() {
		if (map.getFigure().equals(f)) {
			List<SpellInfo> res = new LinkedList<SpellInfo>();
			List l = f.getSpellbook().getSpells();
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				Spell element = (Spell) iter.next();
				res.add(new SpellInfo(element, map));
			}
			return res;
		}
		return null;
	}
}