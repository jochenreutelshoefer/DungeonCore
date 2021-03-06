/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.ItemInfoOwner;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.Path;
import de.jdungeon.dungeon.util.DungeonUtils;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.memory.FigureMemory;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.gui.Paragraphable;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.log.Log;
import de.jdungeon.skill.Skill;
import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.spell.SpellInfo;

/**
 * Abstrakte Klasse - Soll fuer eine Steuerung die Informationen einer Figur
 * liefern.
 */
public abstract class FigureInfo extends RoomInfoEntity implements ItemInfoOwner {

    private final Figure f;

    public FigureInfo(Figure f, DungeonVisibilityMap stats) {
        super(stats);
        this.f = f;
    }

    public static List<FigureInfo> makeInfos(List<Figure> figures, Figure perceiver) {
        List<FigureInfo> result = new ArrayList<FigureInfo>();
        for (Figure figure : figures) {
            // todo : find better solution using appropriate design pattern
            if (figure instanceof Hero) {
                result.add(new HeroInfo((Hero) figure, perceiver.getViwMap()));
            }
            if (figure instanceof Monster) {
                result.add(new MonsterInfo((Monster) figure, perceiver.getViwMap()));
            }
        }
        return result;
    }

    @Override
    public Collection<PositionInRoomInfo> getInteractionPositions() {
        int positionInRoomIndex = this.getPositionInRoomIndex();
        int prev = Position.getNextIndex(positionInRoomIndex, true);
        int next = Position.getNextIndex(positionInRoomIndex, false);
        Collection<PositionInRoomInfo> result = new ArrayList<>();
        result.add(this.getRoomInfo().getPositionInRoom(prev));
        result.add(this.getRoomInfo().getPositionInRoom(next));
        return result;
    }


    public APAgility getAgility() {
        if (map.getVisibilityStatus(f.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
            return f.getAgility();
        }
        return null;
    }

    public double getAttributeValue(Attribute.Type s) {
        if (map.getVisibilityStatus(f.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
            Attribute attribute = f.getAttribute(s);
            if (attribute != null) {
                return attribute.getValue();
            }
        }
        return -1;
    }

    public double getAttributeBasic(Attribute.Type s) {

        if (map.getVisibilityStatus(f.getRoomNumber()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
            Attribute attribute = f.getAttribute(s);
            if (attribute != null) {
                return attribute.getBasic();
            }
        }
        return -1;
    }

    public Class<? extends Figure> getFigureClass() {
        return f.getClass();
    }

    public Path getShortestWayFromTo(JDPoint p1, JDPoint p2) {
        return DungeonUtils.findShortestPath(p1, p2, this.map, false);
    }

    public abstract List<ItemInfo> getAllItems();

    @Override
    public FigureMemory getMemoryObject(FigureInfo info) {
        return f.getMemoryObject(info);
    }

    public RouteInstruction.Direction getLookDirection() {
        if (f.getRoomNumber() == null) {
            // should not happen
            return RouteInstruction.Direction.South;
        }
        int visStat = map.getVisibilityStatus(f.getRoomNumber());

        if (visStat >= RoomObservationStatus.VISIBILITY_FIGURES) {
            return f.getLookDirection();
        }
        // todo: handle this situation reasonable
        Log.warning("No look direction found for: " + this);
        return RouteInstruction.Direction.fromInteger((int) (Math.random() * 4 + 1));
    }

    public boolean isHostile(FigureInfo fig) {
        if (fig.equals(this)) {
            // hopefully not called, but you never know..
            return false;
        }
        return f.getControl().isHostileTo(fig);
    }

    public PositionInRoomInfo getPos() {
        int vis = map.getVisibilityStatus(f.getRoomNumber());
        if (vis >= RoomObservationStatus.VISIBILITY_FIGURES) {
            return new PositionInRoomInfo(f.getPos(), map);
        } else {
            return null;
        }
    }


    public int getPositionInRoomIndex() {

        int vis = map.getVisibilityStatus(f.getRoomNumber());

        if (vis >= RoomObservationStatus.VISIBILITY_FIGURES) {
            return f.getPositionInRoom();
        } else {
            return -1;
        }
    }

    public Boolean isDead() {
        // in principle one should verify access via visibility state here also..
        return f.isDead();
    }

    public Boolean hasKey(DoorInfo d) {
        if (f.equals(map.getFigure())) {
            return f.hasKey(d.getLock().getKeyPhrase());
        }

        return null;
    }

    public FigurePresentation getFigurePresentation() {
        return this.f.getFigurePresentation();
    }

    public static FigureInfo makeFigureInfo(Figure f, DungeonVisibilityMap map) {
        if (map == null) {
            System.out.println("FigureInfo mit VisMap null!");
            return null;
        }
        if (f instanceof Hero) {
            return new HeroInfo((Hero) f, map);
        }
        if (f instanceof Monster) {
            return MonsterInfo.makeMonsterInfo((Monster) f, map);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof FigureInfo) {
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
        Room room = f.getRoom();
        if (room == null) return null;
        return new JDPoint(room.getNumber().getX(), room
                .getNumber().getY());
    }

    /**
     * Liefert ein Array von WrappedItem aller Gegenstaende, die in diesem Raum
     * auf dem Boden liegen.
     *
     * @return Gegenstaende im Raum
     */
    public ItemInfo[] getRoomItems() {
        if (f.getRoom() != null) {
            return f.getRoom().getItemInfos(map);
        }
        return null;
    }

    /**
     * Liefert ein Integer-Array, dass die Tueren des Raumes kodiert. array[0]
     * -> Norden; array[1] -> Osten; array[2] -> Sueden; array[3] -> Westen;
     * <p>
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
        return ((Paragraphable) f).getParagraphs();
    }

    public boolean hasItem(ItemInfo it) {
        ItemInfo[] items = getFigureItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(it)) {
                return true;
            }
        }
        return false;
    }

    public RoomInfo getRoomInfo() {
        return RoomInfo.makeRoomInfo(f.getRoom(), map);
    }

    public String getStatus() {
        int vis = map.getVisibilityStatus(f.getRoomNumber());
        if (vis >= RoomObservationStatus.VISIBILITY_FIGURES) {
            return f.getStatus();
        } else {
            return "";
        }
    }


    public <T extends Skill> T getSkill(Class<T> clazz) {
        return this.f.getSkill(clazz);
    }

    public Collection<Skill> getSkills() {
        return f.getSkillSet().getSkills();
    }

    public RoomInfo getRoomInfo(int x, int y) {
        if (f.getRoom() == null || f.getDungeon() == null) return null;
        return f.getRoom().getDungeon().getRoomInfoNr(x, y, map);
    }

    public RoomInfo getRoomInfo(JDPoint p) {
        return getRoomInfo(p.getX(), p.getY());
    }

    public HealthLevel getHealthLevel() {
        return f.getHealthLevel();
    }

    public int getActionPoints() {
        return f.getActionPoints();
    }

    public int getFigureID() {
        return f.getFigureID();
    }

    public ActionResult checkAction(Action a) {
        return f.checkAction(a);
    }

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
            for (Iterator iter = l.iterator(); iter.hasNext(); ) {
                AbstractSpell element = (AbstractSpell) iter.next();
                res.add(new SpellInfo(element, map));
            }
            return res;
        }
        return null;
    }
}