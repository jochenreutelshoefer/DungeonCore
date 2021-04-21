package de.jdungeon.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jdungeon.ai.AbstractAI;
import de.jdungeon.ai.DefaultMonsterIntelligence;
import de.jdungeon.dungeon.util.InfoUnitUnwrapper;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.PlayerDiedEvent;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.Turnable;
import de.jdungeon.location.Location;
import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.spell.TimedSpellInstance;
import de.jdungeon.switchPos.SwitchPositionRequestManager;

public class Dungeon implements Turnable, EventListener {

    private boolean transactionLocked = false;

    private final Room[][] theDungeon;

    private final JDPoint[][] points;

    private final JDPoint size;

    // TODO: remove this field
    @Deprecated
    private JDPoint heroPosition;

    private final List<Location> shrines = new ArrayList<>();

    private boolean gameOver = false;

    private final InfoUnitUnwrapper unwrapper;

    public Map<Integer, Figure> getFigureIndex() {
        return Collections.unmodifiableMap(figureIndex);
    }

    Map<Integer, Figure> figureIndex = new HashMap<>();

    DungeonVisibilityMap allVisMap;

    public SwitchPositionRequestManager getSwitchPositionRequestManager() {
        return switchPositionRequestManager;
    }

    SwitchPositionRequestManager switchPositionRequestManager =  new SwitchPositionRequestManager();

    public Dungeon(int sizeX, int sizeY) {
        EventManager.getInstance().registerListener(this);
        unwrapper = new InfoUnitUnwrapper(this);
        theDungeon = new Room[sizeX][sizeY];
        size = new JDPoint(sizeX, sizeY);
        points = new JDPoint[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                points[i][j] = new JDPoint(i, j);
                theDungeon[i][j] = new Room(i, j, this);
            }
        }
        allVisMap = DungeonVisibilityMap.getAllVisMap(this);
    }

    /**
     * Giving a VisMap that sees everything.
     *
     * @return all-seeing vismap
     */
    public DungeonVisibilityMap getAllVisMap() {
        return allVisMap;
    }

    public void insertFigure(Figure figure) {
        figureIndex.put(figure.getFigureID(), figure);
    }

    public void prepare() {
        for (int i = 0; i < this.getSize().getX(); i++) {
            for (int j = 0; j < this.getSize().getY(); j++) {
                List<Figure> roomFigures = theDungeon[i][j].getRoomFigures();
                for (Figure roomFigure : roomFigures) {
                    figureIndex.put(roomFigure.getFigureID(), roomFigure);
                }
            }
        }

        createVisibilityMaps();
        setMonsterControls();
    }

    private void createVisibilityMaps() {
        Set<Integer> s = figureIndex.keySet();
        for (Iterator<Integer> iter = s.iterator(); iter.hasNext(); ) {
            Integer element = iter.next();
            Figure f = figureIndex.get(element);
            f.createVisibilityMap(this);
        }
    }

    private void setMonsterControls() {
        Set<Integer> s = figureIndex.keySet();
        for (Iterator<Integer> iter = s.iterator(); iter.hasNext(); ) {
            Integer element = iter.next();
            Figure f = figureIndex.get(element);
            if (f instanceof Monster) {
                MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(f,
                        f.getViwMap());
                AbstractAI ai = new DefaultMonsterIntelligence();
                ai.setFigure(info);
                ControlUnit control = new FigureControl(info, ai);
                if (f.getControl() == null) {
                    f.setControl(control);
                }
            }
        }
    }

    public Collection<Room> getAllRooms() {
        Set<Room> result = new HashSet<Room>();
        for (int i = 0; i < getRooms().length; i++) {
            result.addAll(Arrays.asList(getRooms()[i]));
        }
        return result;
    }

    public Collection<Room> getWallRooms() {
        Set<Room> result = new HashSet<Room>();
        Collection<Room> allRooms = getAllRooms();
        for (Room room : allRooms) {
            if (room.isWall()) {
                result.add(room);
            }
        }
        return result;
    }

    public Dungeon(int x, int y, int xh, int yh) {
        this(x, y);
        heroPosition = new JDPoint(xh, yh);
    }

    public JDPoint getPoint(int x, int y) {
        if (x >= 0 && y >= 0 && x < points.length && y < points[0].length) {
            return points[x][y];
        }

        return null;
    }


    @Override
    public void turn(int round, DungeonWorldUpdater mode) {

        // currently working for GameLoopMode.DistinctWorldLoopThread only
        shrinesTurn(round, mode);
        roomsTurn(round, mode);
        spellsTurn(round, mode);
    }

    public boolean turnRenderLoop(int round, DungeonWorldUpdater worldUpdater) {
        GameLoopMode mode = GameLoopMode.RenderThreadWorldUpdate;
        //todo: check that each round is triggered only once (for shrines and spells also!)
        shrinesTurn(round, worldUpdater);
        boolean roundCompleted = roomsTurn(round, worldUpdater);
        spellsTurn(round, worldUpdater);
        if (roundCompleted) {
            return true;
        } else {
            return false;
        }
    }

    private void spellsTurn(int round, DungeonWorldUpdater mode) {
        List<TimedSpellInstance> spells = AbstractSpell.timedSpells;
        for (int i = 0; i < spells.size(); i++) {
            ((Turnable) spells.get(i)).turn(round, mode);
        }
    }

    public void addShrine(Location s) {
        if (s != null) {
            shrines.add(s);
        }
    }

    public void removeShrine(Location s) {
        if (s != null) {
            shrines.remove(s);
        }
    }

    public JDPoint getRandomPoint() {
        int x = (int) (Math.random() * size.getX());
        int y = (int) (Math.random() * size.getY());
        return getPoint(x, y);
    }

    private void shrinesTurn(int round, DungeonWorldUpdater worldUpdater) {
        for (Location shrine : shrines) {
            shrine.turn(round, worldUpdater);
        }
    }

    public JDPoint getSize() {
        return size;
    }

    public Room getRoom(int x, int y) {
        return getRoom(new JDPoint(x, y));
    }

    public Room getRoom(JDPoint p) {
        if (p == null) {
            return null;
        }
        if (p.getY() < 0 || p.getY() >= this.getSize().getY()
                || p.getX() < 0 || p.getX() >= this.getSize().getX()) {
            // invalid coordinates for this dungeon
            return null;
        }
        return theDungeon[p.getX()][p.getY()];
    }

    public Room[][] getRooms() {
        return theDungeon;
    }

    public List<Room> getNeighbourRooms(Room r) {
        List<Room> l = new LinkedList<Room>();
        JDPoint p = r.getPoint();
        int x = p.getX();
        int y = p.getY();
        Room neighbours[] = new Room[4];
        neighbours[0] = getRoomNr(x - 1, y);
        neighbours[1] = getRoomNr(x + 1, y);
        neighbours[2] = getRoomNr(x, y - 1);
        neighbours[3] = getRoomNr(x, y + 1);
        for (int i = 0; i < 4; i++) {
            if (neighbours[i] != null) {
                l.add(neighbours[i]);
            }
        }
        return l;
    }

    public Room getRoomNr(int x, int y) {
        if ((x >= 0) && (x < theDungeon.length) && (y >= 0)
                && (y < theDungeon[0].length)) {
            return theDungeon[x][y];
            // }
        } else {
            return null;
        }
    }

    public RoomInfo getRoomInfoNr(int x, int y, DungeonVisibilityMap status) {
        return RoomInfo.makeRoomInfo(getRoomNr(x, y), status);
    }

    public Room getRoomAt(Room r, RouteInstruction.Direction dir) {
        if (r == null) {
            return null;
        }
        if (dir == RouteInstruction.Direction.North) {
            JDPoint p = r.getNumber();
            return getRoomNr(p.getX(), p.getY() - 1);
        } else if (dir == RouteInstruction.Direction.East) {
            JDPoint p = r.getNumber();
            return getRoomNr(p.getX() + 1, p.getY());
        } else if (dir == RouteInstruction.Direction.West) {
            JDPoint p = r.getNumber();
            return getRoomNr(p.getX() - 1, p.getY());
        } else if (dir == RouteInstruction.Direction.South) {
            JDPoint p = r.getNumber();
            return getRoomNr(p.getX(), p.getY() + 1);
        } else {
            return null;
        }
    }

    @Deprecated
    public JDPoint getHeroPosition() {
        return heroPosition;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    private boolean roomsTurn(int round, DungeonWorldUpdater worldUpdater) {
        for (int i = 0; i < theDungeon.length; i++) {
            for (int j = 0; j < theDungeon[0].length; j++) {
                if (this.gameOver) {
                    return true;
                }
                boolean roomFiguresAllCompletedRound = theDungeon[i][j].turn(round, worldUpdater);

                if (worldUpdater.getGameLoopMode() == GameLoopMode.RenderThreadWorldUpdate) {
                    // if a figure in some is current idle, we break and try again on next render loop call
                    if (!roomFiguresAllCompletedRound) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public Collection<Class<? extends Event>> getEvents() {
        return Collections.singletonList(PlayerDiedEvent.class);
    }

    @Override
    public void notify(Event event) {
        if (event instanceof PlayerDiedEvent) {
            this.gameOver = true;
        }
    }

    public InfoUnitUnwrapper getUnwrapper() {
        return unwrapper;
    }

    public void removeFigureFromIndex(Figure figure) {
        figureIndex.remove(figure.getFigureID());
    }


    public void destroy() {
        // clean all figures from the static figure index (TODO: refactor - remove figure index!)
        Collection<Figure> figures = collectAllFigures();
        for (Figure figure : figures) {
            figureIndex.remove(figure.getFigureID());
        }
    }

    public Collection<Figure> collectAllFigures() {
        List<Figure> result = new ArrayList<>();
        for (int i = 0; i < theDungeon.length; i++) {
            for (int j = 0; j < theDungeon[0].length; j++) {
                result.addAll(theDungeon[i][j].getRoomFigures());
            }
        }
        return result;
    }

    public boolean isTransactionLocked() {
        return this.transactionLocked;
    }

    public void setTransactionLock() {
        //Log.info("setting transaction lock");
        this.transactionLocked = true;
    }

    public void releaseTransactionLock() {
        //Log.info("releasing transaction lock");
        this.transactionLocked = false;
    }
}


