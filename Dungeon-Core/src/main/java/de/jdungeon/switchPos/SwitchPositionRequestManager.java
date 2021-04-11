package de.jdungeon.switchPos;

import de.jdungeon.dungeon.Room;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.fight.FightEndedEvent;
import de.jdungeon.figure.Figure;

import java.util.*;

/**
 * Manages the SwitchPosRequests that are done during the de.jdungeon.game.
 * This is, if a de.jdungeon.figure does proposes a SwitchPositionRequest,
 * it is stored here for the case that it will
 * be accepted by the requested de.jdungeon.figure.
 * If the de.jdungeon.fight ends, all requests for that room will be cleared.
 */
public final class SwitchPositionRequestManager implements EventListener {

    private static SwitchPositionRequestManager instance;

    public static SwitchPositionRequestManager getInstance() {
        if (instance == null) {
            instance = new SwitchPositionRequestManager();
        }
        return instance;
    }

    private SwitchPositionRequestManager() {
        EventManager.getInstance().registerListener(this);
    }

    //we want our data be indexed by room to clear room wise after fights have ended
    private final Map<Room, SwitchPosRequestMap> roomFigureRequestMap = new HashMap<>();

    public Collection<SwitchPosRequest> getSwitchPosRequests(Figure requestedFigure) {
        Room room = requestedFigure.getRoom();
        if (roomFigureRequestMap.containsKey(room)) {
            return roomFigureRequestMap.get(room).getSwitchPosRequests(requestedFigure);
        }
        return Collections.emptySet();
    }

    void removePosSwitchRequest(SwitchPosRequest request) {
        Room room = request.getRequestingFigure().getRoom();
        if (roomFigureRequestMap.containsKey(room)) {
            roomFigureRequestMap.get(room).removePosSwitchRequest(request);
        }
    }

    void addPosSwitchRequest(SwitchPosRequest request) {
        Room room = request.getRequestingFigure().getRoom();
        SwitchPosRequestMap switchPosRequestMap = roomFigureRequestMap.get(room);
        if (switchPosRequestMap == null) {
            switchPosRequestMap = new SwitchPosRequestMap();
            roomFigureRequestMap.put(room, switchPosRequestMap);
        }
        switchPosRequestMap.addPosSwitchRequest(request);
    }

    @Override
    public Collection<Class<? extends Event>> getEvents() {
        return Collections.singletonList(FightEndedEvent.class);
    }

    @Override
    public void notify(Event event) {
        if (event instanceof FightEndedEvent) {
            // we clear out any switch position request after a de.jdungeon.fight has ended
            this.roomFigureRequestMap.remove(((FightEndedEvent) event).getRoom());
        }
    }

    /*
    Some auxiliary data structure with a 'multi-map' from Figure to SwitchPosRequest.
     */
    class SwitchPosRequestMap {
        private final Map<Figure, Set<SwitchPosRequest>> posSwitchRequests = new HashMap<>();

        Collection<SwitchPosRequest> getSwitchPosRequests(Figure requestedFigure) {
            if (posSwitchRequests.containsKey(requestedFigure)) {
                return posSwitchRequests.get(requestedFigure);
            } else {
                return Collections.emptySet();
            }
        }

        void removePosSwitchRequest(SwitchPosRequest request) {
            Figure requestedFigure = request.getRequestedFigure();
            Set<SwitchPosRequest> switchPosRequests = posSwitchRequests.get(requestedFigure);
            if (switchPosRequests != null) {
                switchPosRequests.remove(request);
            }
        }

        void addPosSwitchRequest(SwitchPosRequest request) {
            Figure requestedFigure = request.getRequestedFigure();
            Set<SwitchPosRequest> switchPosRequests = posSwitchRequests.get(requestedFigure);
            if (switchPosRequests == null) {
                switchPosRequests = new HashSet<>();
                posSwitchRequests.put(requestedFigure, switchPosRequests);
            }
            switchPosRequests.add(request);
        }

    }

    public static class SwitchPosRequest {

        private final Figure requestingFigure;
        private final Figure requestedFigure;

        SwitchPosRequest(Figure requestingFigure, Figure requestedFigure) {
            if (requestedFigure.equals(requestingFigure)) {
                throw new IllegalArgumentException("For switching places, figures must not be the same: " + requestedFigure + " - " + requestingFigure);
            }
            this.requestingFigure = requestingFigure;
            this.requestedFigure = requestedFigure;
        }

        Figure getRequestingFigure() {
            return requestingFigure;
        }

        Figure getRequestedFigure() {
            return requestedFigure;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SwitchPosRequest that = (SwitchPosRequest) o;
            return requestingFigure.equals(that.requestingFigure) && requestedFigure.equals(that.requestedFigure);
        }

        @Override
        public int hashCode() {
            return Objects.hash(requestingFigure, requestedFigure);
        }
    }
}
