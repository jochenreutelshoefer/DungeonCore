package item.map;

import java.util.Collection;
import java.util.Collections;

import dungeon.JDPoint;
import dungeon.RoomEntity;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.action.result.ActionResult;
import game.JDEnv;
import gui.Paragraph;
import item.Item;
import item.interfaces.Usable;
import util.JDColor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 15.08.16.
 */
public class AncientMapFragment extends Item implements Usable {

    public Collection<JDPoint> getRooms() {
        return Collections.unmodifiableCollection(rooms);
    }

    private final Collection<JDPoint> rooms;

    public AncientMapFragment(Collection<JDPoint> rooms) {
        super(rooms.size() * 5, false);
        this.rooms = rooms;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return true;
    }

    @Override
    public boolean usableOnce() {
        return true;
    }

    @Override
    public int dustCosts() {
        return 0;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        if (doIt) {
            DungeonVisibilityMap roomVisibility = f.getRoomVisibility();
            for (JDPoint room : rooms) {
                int discoveryStatus = roomVisibility.getDiscoveryStatus(room);
                if (discoveryStatus < RoomObservationStatus.VISIBILITY_SHRINE) {
                    roomVisibility.setVisibilityStatus(room, RoomObservationStatus.VISIBILITY_SHRINE);
                }
            }
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }

    @Override
    public boolean needsTarget() {
        return false;
    }


    public static String title() {
        return JDEnv.getResourceBundle().getString("map");
    }

    @Override
    public String getText() {
        String s = new String();
        String rooms = makeRoomsString();
        return s;
    }

    private String makeRoomsString() {
        StringBuffer buffy = new StringBuffer();
        for (JDPoint room : rooms) {
            buffy.append("[" + room.getX() + "," + room.getY() + "], ");
        }
        return buffy.toString();
    }

    @Override
    public String toString() {
        return title() + ": " + makeRoomsString();
    }

    @Override
    public Paragraph[] getParagraphs() {
        Paragraph[] p = new Paragraph[2];
        p[0] = new Paragraph(title());
        p[0].setSize(24);
        p[0].setCentered();
        p[0].setColor(JDColor.blue);
        p[0].setBold();

        p[1] = new Paragraph(makeRoomsString());
        p[1].setSize(16);
        p[1].setCentered();
        p[1].setColor(JDColor.black);
        p[1].setBold();

        return p;
    }

}