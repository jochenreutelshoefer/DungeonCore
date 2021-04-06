package location;

import java.util.ArrayList;
import java.util.List;

import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.action.result.ActionResult;
import game.ControlUnit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class RevealMapShrine extends Location {

    private final List<Room> revealedRooms;

    public RevealMapShrine(List<Room> revealedRooms) {
        super();
        this.revealedRooms = revealedRooms;
    }

    public RevealMapShrine(Room revealedRoom) {
        super();
        this.revealedRooms = new ArrayList<>();
        this.revealedRooms.add(revealedRoom);
    }

    @Override
    public void turn(int round) {

    }

    @Override
    public String getStory() {
        return "Zeigt Dir verborgene Bereiche.";
    }

    @Override
    public String toString() {
        return "Karte";
    }

    @Override
    public String getText() {
        return "Karte";
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public int dustCosts() {
        return 0;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        if (doIt) {
            for (Room revealedRoom : revealedRooms) {
                int discoveryStatus = f.getRoomVisibility().getDiscoveryStatus(revealedRoom.getNumber());
                if (discoveryStatus < RoomObservationStatus.VISIBILITY_FIGURES) {
                    f.getRoomVisibility().setVisibilityStatus(revealedRoom.getPoint(), RoomObservationStatus.VISIBILITY_FIGURES);
                    //f.getRoomVisibility().setDiscoveryStatus(revealedRoom.getPoint(), RoomObservationStatus.VISIBILITY_FIGURES);
                } else {
                    // in this case for the user it is helpful to show target room(s)
                    // even if they are already revealed (as reminder)
                    // we 'emulate' this by triggering a corresponding event.
                    ControlUnit control = f.getControl();
                    if (control != null) {
                        control.notifyVisibilityStatusIncrease(revealedRoom.getPoint());
                    }
                }
            }
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }

    @Override
    public boolean usableOnce() {
        return false;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return true;
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}
