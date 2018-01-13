/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import dungeon.ItemInfoOwner;
import dungeon.util.RouteInstruction;
import figure.action.Action;
import figure.action.result.ActionResult;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.memory.FigureMemory;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import game.DungeonGame;
import game.InfoEntity;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import log.Log;
import spell.AbstractSpell;
import spell.SpellInfo;
import ai.DefaultMonsterIntelligence;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.util.DungeonUtils;

/**
 * Abstrakte Klasse - Soll fuer eine Steuerung die Informationen einer Figur
 * liefern.
 */
public abstract class FigureInfo extends InfoEntity implements ItemInfoOwner {

	private final Figure f;
	public FigureInfo(Figure f, DungeonVisibilityMap stats) {
		super(stats);
		this.f = f;
	}

	public static List<FigureInfo> makeInfos(List<Figure> figures, Figure perceiver) {
		List<FigureInfo> result = new ArrayList<FigureInfo>();
		for (Figure figure : figures) {
			// todo : find better solution using appropriate design pattern
			if(figure instanceof Hero) {
				result.add(new HeroInfo((Hero)figure, perceiver.getRoomVisibility()));
			}
			if(figure instanceof Monster) {
				result.add(new MonsterInfo((Monster)figure, perceiver.getRoomVisibility()));
			}
		}
		return result;
	}
	
	public Class<? extends Figure> getFigureClass() {
		return f.getClass();
	}
	
	public abstract int getMight();
	
	public List<JDPoint> getShortestWayFromTo(JDPoint p1, JDPoint p2) {
		
		List<Room> wayFromTo = DungeonUtils.findShortestWayFromTo(
				this.getRoomInfo(p1), this.getRoomInfo(p2));
		List<JDPoint> result = new ArrayList<JDPoint>();
		for (Room room : wayFromTo) {
			result.add(room.getPoint());
		}
		return result;
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
	

	public RouteInstruction.Direction getLookDirection() {
		if(f.getLocation() == null) {
			// should not happen
			return RouteInstruction.Direction.South;
		}
		int visStat = map.getVisibilityStatus(f.getLocation());

		if(visStat >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return f.getLookDirection();
		}
		// todo: handle this situation reasonable
		Log.warning("No look direction found for: "+this);
		return RouteInstruction.Direction.fromInteger((int) (Math.random()* 4 +1));

	}
	
	

	
	public boolean isHostile(FigureInfo fig) {
		if(fig.equals(this)) {
			// hopefully not called, but you never know..
			return false;
		}
		return f.getControl().isHostileTo(fig);
	}
	
	public void controlLeaves() {
		f.setControl(null);
		DungeonGame.getInstance().controlLeft(f);
	}
	
	public int getFleeDistance() {
		return  DefaultMonsterIntelligence.getMinFleeDist(this);
	}
	
	
	public PositionInRoomInfo getPos() {
		int vis = map.getVisibilityStatus(f.getLocation());
		if(vis >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return new PositionInRoomInfo(f.getPos(), map);
		}else {
			return null;
		}
	}
	
	public int getPositionInRoomIndex() {
		// elvis has left the building
		if(f.getLocation() == null) return -1;

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
		if(map.getDungeon().equals(f.actualDungeon)) {
			if(map.getVisibilityStatus(f.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
				return f.isDead();
			}
		}
		return null;
	}
	
	public Boolean hasKey(DoorInfo  d) {
		if(f.equals(map.getFigure())) {
		return f.hasKey(d.getLock().getKey().getType());
		}
		
		return null;
	}
	
	public static FigureInfo makeFigureInfo(Figure f, DungeonVisibilityMap map) {
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
		return new JDPoint(f.getRoom().getNumber().getX(), f.getRoom()
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
		return f.getRoom().getDungeon().getSize();
	}
	
	public RoomInfo getRoomInfo(int x, int y) {
		return f.getRoom().getDungeon().getRoomInfoNr(x, y, map);
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
		return DungeonGame.getInstance().getRound();
	}

	/**
	 * @return Identifikationsnummer der Figure
	 */
	public int getFighterID() {
		return f.getFighterID();
	}

	public ActionResult checkAction(Action a) {
		return f.checkAction(a);
	}

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

	@Override
	public ItemInfo[] getItemArray() {
		return getFigureItems();
	}

	@Override
	public List<ItemInfo> getItems() {
		return getFigureItemList();
	}

	public List<ItemInfo> getFigureItemList() {
		return Arrays.asList(getFigureItems());
	}

	public String getName() {
		return f.getName();
	}




	public List<SpellInfo> getSpells() {
		if (map.getFigure().equals(f)) {
			List<SpellInfo> res = new LinkedList<SpellInfo>();
			List l = f.getSpellbook().getSpells();
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				AbstractSpell element = (AbstractSpell) iter.next();
				res.add(new SpellInfo(element, map));
			}
			return res;
		}
		return null;
	}
}